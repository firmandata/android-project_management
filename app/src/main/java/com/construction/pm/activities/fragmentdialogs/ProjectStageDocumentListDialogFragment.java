package com.construction.pm.activities.fragmentdialogs;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.construction.pm.R;
import com.construction.pm.asynctask.FileInfoGetAsyncTask;
import com.construction.pm.asynctask.param.FileInfoGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.utils.FileUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.adapter.ProjectStageDocumentListAdapter;
import com.construction.pm.views.file.FileDocumentItemView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageDocumentListDialogFragment extends DialogFragment implements
        ProjectStageDocumentListAdapter.ProjectStageDocumentListAdapterListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_STAGE_DOCUMENT_MODEL = "PROJECT_STAGE_DOCUMENT_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageDocumentModel mProjectStageDocumentModel;
    protected ProjectStageDocumentListAdapter mProjectStageDocumentListAdapter;

    protected ProjectStageDocumentListListener mProjectStageDocumentListListener;

    public static ProjectStageDocumentListDialogFragment newInstance(ProjectStageDocumentModel projectStageDocumentModel, final ProjectStageDocumentListListener projectStageDocumentListListener) {
        final Bundle bundle = new Bundle();
        if (projectStageDocumentModel != null) {
            try {
                bundle.putString(PARAM_PROJECT_STAGE_DOCUMENT_MODEL, projectStageDocumentModel.build().toString(0));
            } catch (org.json.JSONException ex) {
            }
        }

        final ProjectStageDocumentListDialogFragment projectStageDocumentListDialogFragment = new ProjectStageDocumentListDialogFragment();
        projectStageDocumentListDialogFragment.setArguments(bundle);
        projectStageDocumentListDialogFragment.setProjectStageDocumentListListener(projectStageDocumentListListener);
        return projectStageDocumentListDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectStageDocumentModel parameter --
            String projectStageDocumentModelJson = bundle.getString(PARAM_PROJECT_STAGE_DOCUMENT_MODEL);
            if (projectStageDocumentModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageDocumentModelJson);
                    mProjectStageDocumentModel = ProjectStageDocumentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {

                }
            }
        }

        mProjectStageDocumentListAdapter = new ProjectStageDocumentListAdapter(mProjectStageDocumentModel);
        mProjectStageDocumentListAdapter.setProjectStageDocumentListAdapterListener(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setAdapter(mProjectStageDocumentListAdapter, null);

        return alertDialogBuilder.create();
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
    public void onProjectStageDocumentListAdapterRequestFileInfo(final int position, Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileInfoGetAsyncTask --
        FileInfoGetAsyncTask fileInfoGetAsyncTask = new FileInfoGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileGetAsyncTaskResult) {
                mAsyncTaskList.remove(this);

                if (fileGetAsyncTaskResult != null) {
                    mProjectStageDocumentListAdapter.setItem(position, fileGetAsyncTaskResult.getFileModel());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {

                    }
                }
            }
        };

        // -- Do FileInfoGetAsyncTask --
        fileInfoGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new FileInfoGetAsyncTaskParam(getContext(), settingUserModel, fileId));
    }

    @Override
    public void onProjectStageDocumentListAdapterClick(FileModel fileModel) {
        if (fileModel != null) {
            if (mNetworkFileServiceMessengerSender != null)
                NetworkFileMessageHandler.requestFile(mNetworkFileServiceMessengerSender, fileModel.getFileId());

            if (mProjectStageDocumentListListener != null)
                mProjectStageDocumentListListener.onProjectStageDocumentItemClick(fileModel);
        }
    }

    // --------------------------------
    // -- NetworkFileService Handler --
    // --------------------------------

    protected ServiceConnection mNetworkFileServiceConnection;
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
        if (mProjectStageDocumentListAdapter != null) {
            FileDocumentItemView fileDocumentItemView = mProjectStageDocumentListAdapter.getFileDocumentItemView(fileId);
            if (fileDocumentItemView != null)
                fileDocumentItemView.startProgress();
        }
    }

    @Override
    public void onNetworkFileCacheProgress(Integer fileId, String progress) {

    }

    @Override
    public void onNetworkFileCacheFinish(FileModel fileModel) {

    }

    @Override
    public void onNetworkFileDownloadStart(Integer fileId) {

    }

    @Override
    public void onNetworkFileDownloadProgress(Integer fileId, String progress) {
        if (mProjectStageDocumentListAdapter != null) {
            FileDocumentItemView fileDocumentItemView = mProjectStageDocumentListAdapter.getFileDocumentItemView(fileId);
            if (fileDocumentItemView != null)
                fileDocumentItemView.setProgressText(progress);
        }
    }

    @Override
    public void onNetworkFileDownloadFinish(FileModel fileModel, FileModel fileModelCache) {
        FileModel fileModelExist = null;
        if (fileModel != null)
            fileModelExist = fileModel;
        else if (fileModelCache != null)
            fileModelExist = fileModelCache;

        if (fileModelExist != null) {
            if (mProjectStageDocumentListAdapter != null) {
                FileDocumentItemView fileDocumentItemView = mProjectStageDocumentListAdapter.getFileDocumentItemView(fileModelExist.getFileId());
                if (fileDocumentItemView != null)
                    fileDocumentItemView.stopProgress();
            }

            File file = fileModelExist.getFile(getContext());
            if (file != null)
                FileUtil.openFile(getContext(), file);
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

    public void setProjectStageDocumentListListener(final ProjectStageDocumentListListener projectStageDocumentListListener) {
        mProjectStageDocumentListListener = projectStageDocumentListListener;
    }

    public interface ProjectStageDocumentListListener {
        void onProjectStageDocumentItemClick(FileModel fileModel);
    }
}
