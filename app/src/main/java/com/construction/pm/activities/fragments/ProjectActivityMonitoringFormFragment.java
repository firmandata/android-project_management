package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringFormView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringFormFragment extends Fragment {
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
}
