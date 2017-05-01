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
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.services.NetworkFileMessageHandler;
import com.construction.pm.services.NetworkFileService;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentFormView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentFormFragment extends Fragment implements
        ProjectStageAssignCommentFormView.ProjectStageAssignCommentFormListener,
        ImageRequestListener,
        NetworkFileMessageHandler.NetworkFileMessageHandlerProgressListener {

    public static final String PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL = "PROJECT_STAGE_ASSIGNMENT_MODEL";
    public static final String PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL = "PROJECT_STAGE_ASSIGN_COMMENT_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageAssignCommentFormView mProjectStageAssignCommentFormView;

    public static ProjectStageAssignCommentFormFragment newInstance() {
        return newInstance(null, null);
    }

    public static ProjectStageAssignCommentFormFragment newInstance(final ProjectStageAssignmentModel projectStageAssignmentModel, final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectStageAssignmentModel != null) {
            try {
                String projectStageAssignmentModelJson = projectStageAssignmentModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL, projectStageAssignmentModelJson);
            } catch (org.json.JSONException ex) {
            }
        }
        if (projectStageAssignCommentModel != null) {
            try {
                String projectStageAssignCommentModelJson = projectStageAssignCommentModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectStageAssignCommentFormFragment --
        ProjectStageAssignCommentFormFragment projectStageAssignCommentFormFragment = new ProjectStageAssignCommentFormFragment();
        projectStageAssignCommentFormFragment.setArguments(bundle);
        return projectStageAssignCommentFormFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectStageAssignmentModel projectStageAssignmentModel = null;
        ProjectStageAssignCommentModel projectStageAssignCommentModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectStageAssignmentModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectStageAssignmentModel = ProjectStageAssignmentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }

            // -- Get ProjectStageAssignCommentModel parameter --
            String projectStageAssignCommentModelJson = bundle.getString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL);
            if (projectStageAssignCommentModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageAssignCommentModelJson);
                    projectStageAssignCommentModel = ProjectStageAssignCommentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectStageAssignCommentFormView --
        mProjectStageAssignCommentFormView = ProjectStageAssignCommentFormView.buildProjectStageAssignCommentFormView(getContext(), null);
        mProjectStageAssignCommentFormView.setProjectStageAssignCommentFormListener(this);
        mProjectStageAssignCommentFormView.setImageRequestListener(this);
        mProjectStageAssignCommentFormView.setProjectStageAssignmentModel(projectStageAssignmentModel);
        mProjectStageAssignCommentFormView.setProjectStageAssignCommentModel(projectStageAssignCommentModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectStageAssignCommentFormView to fragment --
        return mProjectStageAssignCommentFormView.getView();
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
                                mProjectStageAssignCommentFormView.setPhotoId(file);
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
        if (mProjectStageAssignCommentFormView != null) {
            FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentFormView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentFormView.getFilePhotoItemTabView(fileId);
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
            if (mProjectStageAssignCommentFormView != null) {
                FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentFormView.getFilePhotoItemView(fileModel.getFileId());
                FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentFormView.getFilePhotoItemTabView(fileModel.getFileId());
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
        if (mProjectStageAssignCommentFormView != null) {
            FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentFormView.getFilePhotoItemView(fileId);
            FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentFormView.getFilePhotoItemTabView(fileId);
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
            if (mProjectStageAssignCommentFormView != null) {
                FilePhotoItemView filePhotoItemView = mProjectStageAssignCommentFormView.getFilePhotoItemView(fileId);
                if (filePhotoItemView != null) {
                    if (file != null)
                        filePhotoItemView.setFilePhotoThumbnail(file);
                    filePhotoItemView.stopProgress();
                }

                FilePhotoItemView filePhotoItemTabView = mProjectStageAssignCommentFormView.getFilePhotoItemTabView(fileId);
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

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        return mProjectStageAssignCommentFormView.getProjectStageAssignCommentModel();
    }

    public WebApiParam.WebApiParamFile getPhoto(final int position) {
        return mProjectStageAssignCommentFormView.getPhoto(position);
    }
}