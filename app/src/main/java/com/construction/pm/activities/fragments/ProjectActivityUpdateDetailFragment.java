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
import com.construction.pm.asynctask.InspectorProjectActivityMonitoringDetailAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringDetailAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringDetailAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityUpdateDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateDetailFragment extends Fragment implements
        ProjectActivityUpdateDetailView.ProjectActivityUpdateDetailListener,
        ImageRequestListener,
        ImageRequestClickListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateDetailView mProjectActivityUpdateDetailView;

    public static ProjectActivityUpdateDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityUpdateDetailFragment newInstance(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityUpdateModel != null) {
            try {
                String projectActivityUpdateModelJson = projectActivityUpdateModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityUpdateDetailFragment --
        ProjectActivityUpdateDetailFragment projectActivityUpdateDetailFragment = new ProjectActivityUpdateDetailFragment();
        projectActivityUpdateDetailFragment.setArguments(bundle);
        return projectActivityUpdateDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityUpdateModel projectActivityUpdateModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityUpdateModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityUpdateDetailView --
        mProjectActivityUpdateDetailView = ProjectActivityUpdateDetailView.buildProjectActivityUpdateDetailView(getContext(), null);
        mProjectActivityUpdateDetailView.setProjectActivityUpdateDetailListener(this);
        mProjectActivityUpdateDetailView.setImageRequestListener(this);
        mProjectActivityUpdateDetailView.setImageRequestClickListener(this);
        mProjectActivityUpdateDetailView.setProjectActivityUpdateModel(projectActivityUpdateModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityUpdateDetail to fragment --
        return mProjectActivityUpdateDetailView.getView();
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
    public void onProjectActivityMonitoringDetailRequest(Integer projectActivityMonitoringId, Integer projectActivityId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare InspectorProjectActivityMonitoringDetailAsyncTask --
        InspectorProjectActivityMonitoringDetailAsyncTask inspectorProjectActivityMonitoringDetailAsyncTask = new InspectorProjectActivityMonitoringDetailAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityMonitoringDetailAsyncTaskResult inspectorProjectActivityMonitoringDetailAsyncTask) {
                mAsyncTaskList.remove(this);

                if (inspectorProjectActivityMonitoringDetailAsyncTask != null) {
                    onProjectActivityMonitoringDetailRequestSuccess(inspectorProjectActivityMonitoringDetailAsyncTask.getProjectActivityMonitoringModel());
                    if (inspectorProjectActivityMonitoringDetailAsyncTask.getMessage() != null)
                        onProjectActivityMonitoringDetailRequestMessage(inspectorProjectActivityMonitoringDetailAsyncTask.getMessage());
                } else {
                    onProjectActivityMonitoringDetailRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityMonitoringDetailRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do InspectorProjectActivityMonitoringDetailAsyncTask --
        inspectorProjectActivityMonitoringDetailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new InspectorProjectActivityMonitoringDetailAsyncTaskParam(getContext(), settingUserModel, projectActivityMonitoringId, projectActivityId, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectActivityMonitoringDetailRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityMonitoringDetailRequestSuccess(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityUpdateDetailView.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    protected void onProjectActivityMonitoringDetailRequestMessage(final String message) {

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
        if (mProjectActivityUpdateDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityUpdateDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityUpdateDetailView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityUpdateDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityUpdateDetailView.getFilePhotoItemView(fileModel.getFileId());
                FilePhotoItemView filePhotoItemTabView = mProjectActivityUpdateDetailView.getFilePhotoItemTabView(fileModel.getFileId());
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
        if (mProjectActivityUpdateDetailView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityUpdateDetailView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityUpdateDetailView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityUpdateDetailView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityUpdateDetailView.getFilePhotoItemView(fileId);
                if (filePhotoItemView != null) {
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    filePhotoItemView.stopProgress();
                }

                FilePhotoItemView filePhotoItemTabView = mProjectActivityUpdateDetailView.getFilePhotoItemTabView(fileId);
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
