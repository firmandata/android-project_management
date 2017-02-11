package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.views.project.ProjectStageListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectStageListFragment extends Fragment implements ProjectStageListView.ProjectStageListListener {

    public static final String PARAM_PROJECT_STAGE_MODELS = "ProjectStageModels";

    protected ProjectStageListView mProjectStageListView;

    public static ProjectStageListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectStageListFragment newInstance(final ProjectStageModel[] projectStageModels) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectStageModels != null) {
            try {
                org.json.JSONArray projectStageModelsJsonArray = new org.json.JSONArray();
                for (ProjectStageModel projectStageModel : projectStageModels) {
                    projectStageModelsJsonArray.put(projectStageModel.build());
                }
                String projectStageModelsJson = projectStageModelsJsonArray.toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_MODELS, projectStageModelsJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectStageListFragment --
        ProjectStageListFragment projectStageListFragment = new ProjectStageListFragment();
        projectStageListFragment.setArguments(bundle);
        return projectStageListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectStageModel[] projectStageModels = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectStageModels parameter --
            String projectStageModelsJson = bundle.getString(PARAM_PROJECT_STAGE_MODELS);
            if (projectStageModelsJson != null) {
                try {
                    List<ProjectStageModel> projectStageModelList = new ArrayList<ProjectStageModel>();
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectStageModelsJson);
                    for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                        ProjectStageModel projectStageModel = ProjectStageModel.build(jsonArray.getJSONObject(jsonArrayIndex));
                        projectStageModelList.add(projectStageModel);
                    }
                    if (projectStageModelList.size() > 0) {
                        projectStageModels = new ProjectStageModel[projectStageModelList.size()];
                        projectStageModelList.toArray(projectStageModels);
                    }
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectStageListView --
        mProjectStageListView = ProjectStageListView.buildProjectStageListView(getContext(), null);
        mProjectStageListView.setProjectStageListListener(this);
        if (projectStageModels != null)
            mProjectStageListView.setProjectStageModels(projectStageModels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectStageListView to fragment --
        return mProjectStageListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectStageListRequest() {
        mProjectStageListView.stopRefreshAnimation();
    }

    @Override
    public void onProjectStageItemClick(ProjectStageModel projectStageModel) {

    }

    public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
        if (mProjectStageListView != null)
            mProjectStageListView.setProjectStageModels(projectStageModels);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
