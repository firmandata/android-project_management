package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.ProjectPlanActivity;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.views.project.ProjectPlanListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectPlanListFragment extends Fragment implements ProjectPlanListView.ProjectPlanListListener {

    public static final String PARAM_PROJECT_PLAN_MODELS = "ProjectPlanModels";

    protected ProjectPlanListView mProjectPlanListView;

    public static ProjectPlanListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectPlanListFragment newInstance(final ProjectPlanModel[] projectPlanModels) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectPlanModels != null) {
            try {
                org.json.JSONArray projectPlanModelsJsonArray = new org.json.JSONArray();
                for (ProjectPlanModel projectPlanModel : projectPlanModels) {
                    projectPlanModelsJsonArray.put(projectPlanModel.build());
                }
                String projectStageModelsJson = projectPlanModelsJsonArray.toString(0);
                bundle.putString(PARAM_PROJECT_PLAN_MODELS, projectStageModelsJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectPlanListFragment --
        ProjectPlanListFragment projectPlanListFragment = new ProjectPlanListFragment();
        projectPlanListFragment.setArguments(bundle);
        return projectPlanListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectPlanModel[] projectPlanModels = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectPlanModels parameter --
            String projectPlanModelsJson = bundle.getString(PARAM_PROJECT_PLAN_MODELS);
            if (projectPlanModelsJson != null) {
                try {
                    List<ProjectPlanModel> projectPlanModelList = new ArrayList<ProjectPlanModel>();
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectPlanModelsJson);
                    for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                        ProjectPlanModel projectPlanModel = ProjectPlanModel.build(jsonArray.getJSONObject(jsonArrayIndex));
                        projectPlanModelList.add(projectPlanModel);
                    }
                    if (projectPlanModelList.size() > 0) {
                        projectPlanModels = new ProjectPlanModel[projectPlanModelList.size()];
                        projectPlanModelList.toArray(projectPlanModels);
                    }
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectPlanListView --
        mProjectPlanListView = ProjectPlanListView.buildProjectPlanListView(getContext(), null);
        mProjectPlanListView.setProjectPlanListListener(this);
        if (projectPlanModels != null)
            mProjectPlanListView.setProjectPlanModels(projectPlanModels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectPlanListView to fragment --
        return mProjectPlanListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectPlanListRequest() {
        mProjectPlanListView.stopRefreshAnimation();
    }

    @Override
    public void onProjectPlanItemClick(ProjectPlanModel projectPlanModel) {
        // -- Redirect to ProjectPlanActivity --
        Intent intent = new Intent(this.getContext(), ProjectPlanActivity.class);

        try {
            org.json.JSONObject projectPlanModelJsonObject = projectPlanModel.build();
            String projectPlanModelJson = projectPlanModelJsonObject.toString(0);

            intent.putExtra(ProjectPlanActivity.INTENT_PARAM_PROJECT_PLAN_MODEL, projectPlanModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivity(intent);
    }

    public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
        if (mProjectPlanListView != null)
            mProjectPlanListView.setProjectPlanModels(projectPlanModels);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
