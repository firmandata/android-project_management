package com.construction.pm.activities.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.FileViewActivity;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentDetailFragment extends Fragment implements
        ImageRequestListener,
        ImageRequestClickListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL = "PROJECT_STAGE_ASSIGN_COMMENT_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageAssignCommentDetailView mProjectStageAssignCommentDetailView;

    public static ProjectStageAssignCommentDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectStageAssignCommentDetailFragment newInstance(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectStageAssignCommentModel != null) {
            try {
                String projectStageAssignCommentModelJson = projectStageAssignCommentModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectStageAssignCommentDetailFragment --
        ProjectStageAssignCommentDetailFragment projectStageAssignCommentDetailFragment = new ProjectStageAssignCommentDetailFragment();
        projectStageAssignCommentDetailFragment.setArguments(bundle);
        return projectStageAssignCommentDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectStageAssignCommentModel projectStageAssignCommentModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectStageAssignCommentModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectStageAssignCommentModel = ProjectStageAssignCommentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectStageAssignCommentDetailView --
        mProjectStageAssignCommentDetailView = ProjectStageAssignCommentDetailView.buildProjectStageAssignCommentDetailView(getContext(), null);
        mProjectStageAssignCommentDetailView.setImageRequestListener(this);
        mProjectStageAssignCommentDetailView.setImageRequestClickListener(this);
        mProjectStageAssignCommentDetailView.setProjectStageAssignCommentModel(projectStageAssignCommentModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectStageAssignCommentDetail to fragment --
        return mProjectStageAssignCommentDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        bindNetworkFileService();
    }

    @Override
    public void onPause() {
        unbindNetworkFileService();

        super.onPause();
    }

    @Override
    public void onImageRequest(final FilePhotoItemView filePhotoItemView, final Integer fileId) {
        if (fileId == null)
            return;

        if (mNetworkFileServiceMessengerSender != null) {
            NetworkFileMessageHandler.requestFile(mNetworkFileServiceMessengerSender, fileId);
        } else {
            // -- Pooling until (execute when NetworkFileService bond) --
            mNetworkFileServicePendingRequestFileIdList.add(fileId);
        }
    }

    @Override
    public void onImageRequestClick(Integer fileId) {
        // -- Redirect to FileViewActivity --
        Intent intent = new Intent(getContext(), FileViewActivity.class);
        intent.putExtra(FileViewActivity.INTENT_PARAM_FILE_ID, fileId);
        startActivity(intent);
    }

    // --------------------------------
    // -- NetworkFileService Handler --
    // --------------------------------

    protected ServiceConnection mNetworkFileServiceConnection;
    protected List<Integer> mNetworkFileServicePendingRequestFileIdList = new ArrayList<Integer>();
    protected Messenger mNetworkFileServiceMessengerSender;
    protected Messenger mNetworkFileServiceMessengerReceiver;

    protected void bindNetworkFileService() {
        // -- Prepare message receiver --
        NetworkFileMessageHandler notificationMessageHandler = new NetworkFileMessageHandler(getContext());
        notificationMessageHandler.setNetworkFileMessageHandlerProgressListener(this);
        mNetworkFileServiceMessengerReceiver = new Messenger(notificationMessageHandler);

        // -- Prepare NetworkFileServiceConnection --
        mNetworkFileServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mNetworkFileServiceMessengerSender = new Messenger(iBinder);
                NetworkFileMessageHandler.sendRegister(mNetworkFileServiceMessengerSender, mNetworkFileServiceMessengerReceiver);

                for (Integer fileId : mNetworkFileServicePendingRequestFileIdList) {
                    onImageRequest(null, fileId);
                }
                mNetworkFileServicePendingRequestFileIdList.clear();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mNetworkFileServiceMessengerSender = null;
            }
        };

        // -- Bind NetworkFileService --
        Intent intent = new Intent(getContext(), NetworkFileService.class);
        getActivity().bindService(intent, mNetworkFileServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onNetworkFileStart(Integer fileId) {
        if (mProjectStageAssignCommentDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentDetailView.getFilePhotoItemTabView(fileId);
            if (filePhotoItemView != null)
                filePhotoItemView.startProgress();
            if (filePhotoItemTabView != null)
                filePhotoItemTabView.startProgress();
        }
    }

    @Override
    public void onNetworkFileCacheProgress(Integer fileId, String progress) {

    }

    @Override
    public void onNetworkFileCacheFinish(FileModel fileModel) {
        if (fileModel != null) {
            File file = fileModel.getFile(getContext());
            if (mProjectStageAssignCommentDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentDetailView.getFilePhotoItemView(fileModel.getFileId());
                FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentDetailView.getFilePhotoItemTabView(fileModel.getFileId());
                if (file != null) {
                    if (filePhotoItemView != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    if (filePhotoItemTabView != null)
                        filePhotoItemTabView.setFilePhotoThumbnail(file);
                }
            }
        }
    }

    @Override
    public void onNetworkFileDownloadStart(Integer fileId) {

    }

    @Override
    public void onNetworkFileDownloadProgress(Integer fileId, String progress) {
        if (mProjectStageAssignCommentDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentDetailView.getFilePhotoItemTabView(fileId);
            if (filePhotoItemView != null)
                filePhotoItemView.setProgressText(progress);
            if (filePhotoItemTabView != null)
                filePhotoItemTabView.setProgressText(progress);
        }
    }

    @Override
    public void onNetworkFileDownloadFinish(FileModel fileModel, FileModel fileModelCache) {
        Integer fileId = null;
        File file = null;
        if (fileModel != null) {
            fileId = fileModel.getFileId();
            file = fileModel.getFile(getContext());
        }
        if (fileModelCache != null)
            fileId = fileModelCache.getFileId();

        if (fileId != null) {
            if (mProjectStageAssignCommentDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentDetailView.getFilePhotoItemView(fileId);
                if (filePhotoItemView != null) {
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    filePhotoItemView.stopProgress();
                }

                FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentDetailView.getFilePhotoItemTabView(fileId);
                if (filePhotoItemTabView != null) {
                    if (file != null)
                        filePhotoItemTabView.setFilePhotoThumbnail(file);
                    filePhotoItemTabView.stopProgress();
                }
            }
        }
    }

    protected void unbindNetworkFileService() {
        if (mNetworkFileServiceMessengerSender != null)
            NetworkFileMessageHandler.sendUnregister(mNetworkFileServiceMessengerSender, mNetworkFileServiceMessengerReceiver);
        if (mNetworkFileServiceConnection != null)
            getActivity().unbindService(mNetworkFileServiceConnection);
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
