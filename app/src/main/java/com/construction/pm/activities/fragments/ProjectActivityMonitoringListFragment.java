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

import com.construction.pm.asynctask.InspectorProjectActivityMonitoringListAsyncTask;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringListAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringListAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringListFragment extends Fragment implements
        ProjectActivityMonitoringListView.ProjectActivityMonitoringListListener,
        ImageRequestListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringListView mProjectActivityMonitoringListView;

    protected ProjectActivityMonitoringListFragmentListener mProjectActivityMonitoringListFragmentListener;

    public static ProjectActivityMonitoringListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel) {
        return newInstance(projectActivityModel, null);
    }

    public static ProjectActivityMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel, final ProjectActivityMonitoringListFragmentListener projectActivityMonitoringListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                String projectActivityModelJson = projectActivityModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityMonitoringListFragment --
        ProjectActivityMonitoringListFragment projectActivityMonitoringListFragment = new ProjectActivityMonitoringListFragment();
        projectActivityMonitoringListFragment.setArguments(bundle);
        projectActivityMonitoringListFragment.setProjectActivityMonitoringListFragmentListener(projectActivityMonitoringListFragmentListener);
        return projectActivityMonitoringListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityModel projectActivityModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityModel = ProjectActivityModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityMonitoringListView --
        mProjectActivityMonitoringListView = ProjectActivityMonitoringListView.buildProjectActivityMonitoringListView(getContext(), null);
        mProjectActivityMonitoringListView.setProjectActivityModel(projectActivityModel);
        mProjectActivityMonitoringListView.setProjectActivityMonitoringListListener(this);
        mProjectActivityMonitoringListView.setImageRequestListener(this);

        // -- Load ProjectActivityMonitoringList --
        onProjectActivityMonitoringListRequest(projectActivityModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityMonitoringList to fragment --
        return mProjectActivityMonitoringListView.getView();
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
    public void onProjectActivityMonitoringListRequest(ProjectActivityModel projectActivityModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare InspectorProjectActivityMonitoringListAsyncTask --
        InspectorProjectActivityMonitoringListAsyncTask inspectorProjectActivityMonitoringListAsyncTask = new InspectorProjectActivityMonitoringListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityMonitoringListAsyncTaskResult inspectorProjectActivityMonitoringListAsyncTask) {
                mAsyncTaskList.remove(this);

                if (inspectorProjectActivityMonitoringListAsyncTask != null) {
                    onProjectActivityMonitoringListRequestSuccess(inspectorProjectActivityMonitoringListAsyncTask.getProjectActivityMonitoringModels());
                    if (inspectorProjectActivityMonitoringListAsyncTask.getMessage() != null)
                        onProjectActivityMonitoringListRequestMessage(inspectorProjectActivityMonitoringListAsyncTask.getMessage());
                } else {
                    onProjectActivityMonitoringListRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityMonitoringListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do InspectorProjectActivityMonitoringListAsyncTask --
        inspectorProjectActivityMonitoringListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new InspectorProjectActivityMonitoringListAsyncTaskParam(getContext(), settingUserModel, projectActivityModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mProjectActivityMonitoringListFragmentListener != null)
            mProjectActivityMonitoringListFragmentListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
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

    protected void onProjectActivityMonitoringListRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityMonitoringListRequestSuccess(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
        mProjectActivityMonitoringListView.setProjectActivityMonitoringModels(projectActivityMonitoringModels);
    }

    protected void onProjectActivityMonitoringListRequestMessage(final String message) {

    }

    public void reloadProjectActivityMonitoringListRequest(final ProjectActivityModel projectActivityModel) {
        // -- Load ProjectActivityMonitoringList --
        onProjectActivityMonitoringListRequest(projectActivityModel);
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
        if (mProjectActivityMonitoringListView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringListView.getFilePhotoItemView(fileId);
            if (filePhotoItemView != null)
                filePhotoItemView.startProgress();
        }
    }

    @Override
    public void onNetworkFileCacheProgress(Integer fileId, String progress) {

    }

    @Override
    public void onNetworkFileCacheFinish(FileModel fileModel) {
        if (fileModel != null) {
            if (mProjectActivityMonitoringListView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringListView.getFilePhotoItemView(fileModel.getFileId());
                if (filePhotoItemView != null) {
                    File file = fileModel.getFile(getContext());
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                }
            }
        }
    }

    @Override
    public void onNetworkFileDownloadStart(Integer fileId) {

    }

    @Override
    public void onNetworkFileDownloadProgress(Integer fileId, String progress) {
        if (mProjectActivityMonitoringListView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringListView.getFilePhotoItemView(fileId);
            if (filePhotoItemView != null)
                filePhotoItemView.setProgressText(progress);
        }
    }

    @Override
    public void onNetworkFileDownloadFinish(FileModel fileModel, FileModel fileModelCache) {
        FileModel existFileModel = null;
        if (fileModel != null)
            existFileModel = fileModel;
        if (fileModelCache != null)
            existFileModel = fileModelCache;

        if (existFileModel != null) {
            if (mProjectActivityMonitoringListView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringListView.getFilePhotoItemView(existFileModel.getFileId());
                if (filePhotoItemView != null) {
                    if (fileModel != null) {
                        File file = fileModel.getFile(getContext());
                        if (file != null)
                            filePhotoItemView.setFilePhotoThumbnail(file);
                    }
                    filePhotoItemView.stopProgress();
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

    public void addProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringListView.addProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    public void setProjectActivityMonitoringListFragmentListener(final ProjectActivityMonitoringListFragmentListener projectActivityMonitoringListFragmentListener) {
        mProjectActivityMonitoringListFragmentListener = projectActivityMonitoringListFragmentListener;
    }

    public interface ProjectActivityMonitoringListFragmentListener {
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
