package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.CameraActivity;
import com.construction.pm.activities.GalleryActivity;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentFormView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentFormFragment extends Fragment implements ProjectStageAssignCommentFormView.ProjectStageAssignCommentFormListener {
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

    public WebApiParam.WebApiParamFile getPhotoId() {
        return mProjectStageAssignCommentFormView.getPhotoId();
    }
}