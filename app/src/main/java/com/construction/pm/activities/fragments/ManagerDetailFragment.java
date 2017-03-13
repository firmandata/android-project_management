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
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.views.manager.ManagerDetailView;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailFragment extends Fragment implements ManagerDetailView.ManagerDetailViewListener {

    public static final String PARAM_PROJECT_ACTIVITY_MODEL = "ProjectActivityModel";

    protected List<AsyncTask> mAsyncTaskList;

    protected ManagerDetailView mManagerDetailView;

    protected ManagerDetailFragmentListener mManagerDetailFragmentListener;

    public static ManagerDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ManagerDetailFragment newInstance(final ProjectActivityModel projectActivityModel) {
        return newInstance(projectActivityModel, null);
    }

    public static ManagerDetailFragment newInstance(final ProjectActivityModel projectActivityModel, final ManagerDetailFragmentListener managerDetailFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                org.json.JSONObject projectActivityModelJsonObject = projectActivityModel.build();
                String projectActivityModelJson = projectActivityModelJsonObject.toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ManagerDetailFragment --
        ManagerDetailFragment managerDetailFragment = new ManagerDetailFragment();
        managerDetailFragment.setArguments(bundle);
        managerDetailFragment.setManagerDetailFragmentListener(managerDetailFragmentListener);
        return managerDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityModel projectActivityModel = null;

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
        }

        // -- Prepare ProjectActivityMonitoringListView --
        mManagerDetailView = ManagerDetailView.buildManagerDetailView(getContext(), null);
        mManagerDetailView.setManagerDetailViewListener(this);
        mManagerDetailView.loadLayoutToFragment(this, projectActivityModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ManagerDetailView to fragment --
        return mManagerDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onManagerDetailRequest(ProjectActivityModel projectActivityModel) {
        mManagerDetailView.setLayoutData(projectActivityModel);
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (mManagerDetailFragmentListener != null)
            mManagerDetailFragmentListener.onProjectActivityUpdateListItemClick(projectActivityUpdateModel);
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mManagerDetailFragmentListener != null)
            mManagerDetailFragmentListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
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

    public void setManagerDetailFragmentListener(final ManagerDetailFragmentListener managerDetailFragmentListener) {
        mManagerDetailFragmentListener = managerDetailFragmentListener;
    }

    public interface ManagerDetailFragmentListener {
        void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel);
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
