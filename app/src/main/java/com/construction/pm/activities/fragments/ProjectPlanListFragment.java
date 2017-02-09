package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.views.project.ProjectPlanListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectPlanListFragment extends Fragment {

    public static final String PARAM_PROJECT_PLAN_MODELS = "ProjectPlanModels";

    protected ProjectPlanListView mProjectPlanListView;

    protected ProjectPlanModel[] mProjectPlanModels;

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

        // -- Prepare ProjectPlanListView --
        mProjectPlanListView = ProjectPlanListView.buildProjectPlanListView(getContext(), null);

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
                        mProjectPlanModels = new ProjectPlanModel[projectPlanModelList.size()];
                        projectPlanModelList.toArray(mProjectPlanModels);
                    }
                } catch (org.json.JSONException ex) {
                }
            }
        }
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

    public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
        mProjectPlanModels = projectPlanModels;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
