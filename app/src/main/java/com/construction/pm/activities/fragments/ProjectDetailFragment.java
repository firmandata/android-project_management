package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.ProjectActivity;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.views.project.ProjectDetailView;

public class ProjectDetailFragment extends Fragment {

    public static final String PARAM_PROJECT_MODEL = "ProjectModel";

    protected ProjectDetailView mProjectDetailView;

    public static ProjectDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectDetailFragment newInstance(final ProjectModel projectModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectModel != null) {
            try {
                org.json.JSONObject projectModelJsonObject = projectModel.build();
                String projectModelJson = projectModelJsonObject.toString(0);
                bundle.putString(PARAM_PROJECT_MODEL, projectModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectDetailFragment --
        ProjectDetailFragment projectDetailFragment = new ProjectDetailFragment();
        projectDetailFragment.setArguments(bundle);
        return projectDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectModel projectModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectModel parameter --
            String projectModelJson = bundle.getString(PARAM_PROJECT_MODEL);
            if (projectModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectModelJson);
                    projectModel = ProjectModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectDetailView --
        mProjectDetailView = ProjectDetailView.buildProjectDetailView(getContext(), null);
        if (projectModel != null)
            mProjectDetailView.setProjectModel(projectModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectDetailView to fragment --
        return mProjectDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setProjectModel(final ProjectModel projectModel) {
        if (mProjectDetailView != null)
            mProjectDetailView.setProjectModel(projectModel);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
