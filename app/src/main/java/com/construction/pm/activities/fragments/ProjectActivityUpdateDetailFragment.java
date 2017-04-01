package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.views.project_activity.ProjectActivityUpdateDetailView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateDetailFragment extends Fragment {

    public static final String PARAM_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateDetailView mProjectActivityUpdateDetailView;

    public static ProjectActivityUpdateDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityUpdateDetailFragment newInstance(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityUpdateModel != null) {
            try {
                String projectActivityUpdateModelJson = projectActivityUpdateModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityUpdateDetailFragment --
        ProjectActivityUpdateDetailFragment projectActivityUpdateDetailFragment = new ProjectActivityUpdateDetailFragment();
        projectActivityUpdateDetailFragment.setArguments(bundle);
        return projectActivityUpdateDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityUpdateModel projectActivityUpdateModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityUpdateModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityUpdateDetailView --
        mProjectActivityUpdateDetailView = ProjectActivityUpdateDetailView.buildProjectActivityUpdateDetailView(getContext(), null);
        mProjectActivityUpdateDetailView.setProjectActivityUpdateModel(projectActivityUpdateModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityUpdateDetail to fragment --
        return mProjectActivityUpdateDetailView.getView();
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
}
