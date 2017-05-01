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

import com.construction.pm.activities.CameraActivity;
import com.construction.pm.activities.GalleryActivity;
import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringFormView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringFormFragment extends Fragment implements
        ProjectActivityMonitoringFormView.ProjectActivityMonitoringFormListener,
        ImageRequestListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";
    public static final String PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringFormView mProjectActivityMonitoringFormView;

    public static ProjectActivityMonitoringFormFragment newInstance() {
        return newInstance(null, null);
    }

    public static ProjectActivityMonitoringFormFragment newInstance(final ProjectActivityModel projectActivityModel, final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                String projectActivityModelJson = projectActivityModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }
        if (projectActivityMonitoringModel != null) {
            try {
                String projectActivityMonitoringModelJson = projectActivityMonitoringModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityMonitoringFormFragment --
        ProjectActivityMonitoringFormFragment projectActivityMonitoringFormFragment = new ProjectActivityMonitoringFormFragment();
        projectActivityMonitoringFormFragment.setArguments(bundle);
        return projectActivityMonitoringFormFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityModel projectActivityModel = null;
        ProjectActivityMonitoringModel projectActivityMonitoringModel = null;

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

            // -- Get ProjectActivityMonitoringModel parameter --
            String projectActivityMonitoringModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (projectActivityMonitoringModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityMonitoringModelJson);
                    projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityMonitoringFormView --
        mProjectActivityMonitoringFormView = ProjectActivityMonitoringFormView.buildProjectActivityMonitoringFormView(getContext(), null);
        mProjectActivityMonitoringFormView.setProjectActivityMonitoringFormListener(this);
        mProjectActivityMonitoringFormView.setImageRequestListener(this);
        mProjectActivityMonitoringFormView.setProjectActivityModel(projectActivityModel);
        mProjectActivityMonitoringFormView.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityMonitoringFormView to fragment --
        return mProjectActivityMonitoringFormView.getView();
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
    public void onRequestCamera() {
        // -- Redirect to CameraActivity --
        Intent intent = new Intent(this.getContext(), CameraActivity.class);
        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_CAMERA_ACTIVITY);
    }

    @Override
    public void onRequestGallery() {
        // -- Redirect to GalleryActivity --
        Intent intent = new Intent(this.getContext(), GalleryActivity.class);
        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_GALLERY_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_CAMERA_ACTIVITY || requestCode == ConstantUtil.INTENT_REQUEST_GALLERY_ACTIVITY) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_CAMERA_ACTIVITY_RESULT_FILE || resultCode == ConstantUtil.INTENT_REQUEST_GALLERY_ACTIVITY_RESULT_FILE) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_FILE_PATH)) {
                        String filePath = bundle.getString(ConstantUtil.INTENT_RESULT_FILE_PATH);
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists())
                                mProjectActivityMonitoringFormView.setPhotoId(file);
                        }
                    }
                }
            }
        }
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
        if (mProjectActivityMonitoringFormView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringFormView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringFormView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityMonitoringFormView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringFormView.getFilePhotoItemView(fileModel.getFileId());
                FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringFormView.getFilePhotoItemTabView(fileModel.getFileId());
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
        if (mProjectActivityMonitoringFormView != null) {
            FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringFormView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringFormView.getFilePhotoItemTabView(fileId);
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
            if (mProjectActivityMonitoringFormView != null) {
                FilePhotoItemView filePhotoItemView = mProjectActivityMonitoringFormView.getFilePhotoItemView(fileId);
                if (filePhotoItemView != null) {
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    filePhotoItemView.stopProgress();
                }

                FilePhotoItemView filePhotoItemTabView = mProjectActivityMonitoringFormView.getFilePhotoItemTabView(fileId);
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

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        return mProjectActivityMonitoringFormView.getProjectActivityMonitoringModel();
    }

    public WebApiParam.WebApiParamFile getPhoto(final int position) {
        return mProjectActivityMonitoringFormView.getPhoto(position);
    }
}
