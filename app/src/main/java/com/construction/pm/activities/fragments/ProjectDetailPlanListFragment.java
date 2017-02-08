package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.project.ProjectDetailPlanListView;

public class ProjectDetailPlanListFragment extends Fragment {
    protected ProjectDetailPlanListView mProjectDetailPlanListView;

    public static ProjectDetailPlanListFragment newInstance() {
        return new ProjectDetailPlanListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectDetailPlanListView --
        mProjectDetailPlanListView = ProjectDetailPlanListView.buildProjectDetailPlanListView(getContext(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectDetailPlanListView to fragment --
        return mProjectDetailPlanListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
