package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.PhotoCameraActivity;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentFormView;

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
        // -- Redirect to PhotoCameraActivity --
        Intent intent = new Intent(this.getContext(), PhotoCameraActivity.class);
        startActivity(intent);
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
}