package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.file.FilePhotoView;
import com.construction.pm.views.file.TouchImageView;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePhotoViewFragment extends Fragment implements ImageRequestListener {
    public static final String PARAM_FILE_ID = "FileId";

    protected List<AsyncTask> mAsyncTaskList;

    protected FilePhotoView mFilePhotoView;

    public static FilePhotoViewFragment newInstance() {
        return newInstance(null);
    }

    public static FilePhotoViewFragment newInstance(final Integer fileId) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt(PARAM_FILE_ID, fileId);

        // -- Create FilePhotoViewFragment --
        FilePhotoViewFragment filePhotoViewFragment = new FilePhotoViewFragment();
        filePhotoViewFragment.setArguments(bundle);
        return filePhotoViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        Integer fileId = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get FileId parameter --
            fileId = bundle.getInt(PARAM_FILE_ID);
        }

        // -- Prepare FilePhotoView --
        mFilePhotoView = FilePhotoView.buildFilePhotoView(getContext(), null);
        mFilePhotoView.setImageRequestListener(this);
        if (fileId != null)
            mFilePhotoView.setFileId(fileId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load FilePhotoView to fragment --
        return mFilePhotoView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setFileId(final Integer fileId) {
        if (mFilePhotoView != null)
            mFilePhotoView.setFileId(fileId);
    }

    @Override
    public void onImageRequest(final ImageView imageView, final Integer fileId) {
        final TouchImageView touchImageView = (TouchImageView) imageView;

        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        final SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileGetNetworkAsyncTask --
        final FileGetNetworkAsyncTask fileGetNetworkAsyncTask = new FileGetNetworkAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                mAsyncTaskList.remove(this);

                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        File file = fileModel.getFile(getContext());
                        if (file != null)
                            ImageUtil.setImageView(getContext(), touchImageView, file);
                    }
                }
            }
        };

        // -- Prepare FileGetCacheAsyncTask --
        FileGetCacheAsyncTask fileGetCacheAsyncTask = new FileGetCacheAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                FileModel fileModel = null;
                if (fileRequestAsyncTaskResult != null) {
                    fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        File file = fileModel.getFile(getContext());
                        if (file != null)
                            ImageUtil.setImageView(getContext(), touchImageView, file);
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, fileModel));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, null));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
