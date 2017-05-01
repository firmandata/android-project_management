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
import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringDetailFragment extends Fragment implements
        ImageRequestListener,
        ImageRequestClickListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringDetailView mProjectActivityMonitoringDetailView;

    public static ProjectActivityMonitoringDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityMonitoringDetailFragment newInstance(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityMonitoringModel != null) {
            try {
                String projectActivityMonitoringModelJson = projectActivityMonitoringModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityMonitoringDetailFragment --
        ProjectActivityMonitoringDetailFragment projectActivityMonitoringDetailFragment = new ProjectActivityMonitoringDetailFragment();
        projectActivityMonitoringDetailFragment.setArguments(bundle);
        return projectActivityMonitoringDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityMonitoringModel projectActivityMonitoringModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityMonitoringModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityMonitoringDetailView --
        mProjectActivityMonitoringDetailView = ProjectActivityMonitoringDetailView.buildProjectActivityMonitoringDetailView(getContext(), null);
        mProjectActivityMonitoringDetailView.setImageRequestListener(this);
        mProjectActivityMonitoringDetailView.setImageRequestClickListener(this);
        mProjectActivityMonitoringDetailView.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityMonitoringDetail to fragment --
        return mProjectActivityMonitoringDetailView.getView();
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
        if (mProjectActivityMonitoringDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringDetailView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityMonitoringDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringDetailView.getFilePhotoItemView(fileModel.getFileId());
                FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringDetailView.getFilePhotoItemTabView(fileModel.getFileId());
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
        if (mProjectActivityMonitoringDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringDetailView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityMonitoringDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringDetailView.getFilePhotoItemView(fileId);
                if (filePhotoItemView != null) {
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    filePhotoItemView.stopProgress();
                }

                FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringDetailView.getFilePhotoItemTabView(fileId);
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
