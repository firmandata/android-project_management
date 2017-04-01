package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringFormView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringFormFragment extends Fragment implements
        ProjectActivityMonitoringFormView.ProjectActivityMonitoringFormListener,
        ImageRequestDuplicateListener {
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
        mProjectActivityMonitoringFormView.setImageRequestDuplicateListener(this);
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
    public void onImageRequestDuplicate(final ImageView imageView, final ImageView duplicateImageView, final Integer fileId) {
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
                        if (fileModel.getFileData() != null) {
                            ImageUtil.setImageThumbnailView(getContext(), imageView, fileModel.getFileData());
                            if (duplicateImageView != null)
                                ImageUtil.setImageThumbnailView(getContext(), duplicateImageView, fileModel.getFileData());
                        }
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
                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        if (fileModel.getFileData() != null) {
                            ImageUtil.setImageThumbnailView(getContext(), imageView, fileModel.getFileData());
                            if (duplicateImageView != null)
                                ImageUtil.setImageThumbnailView(getContext(), duplicateImageView, fileModel.getFileData());
                        }
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId));
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
