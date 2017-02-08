package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.project.ProjectPlanListView;

public class ProjectPlanListFragment extends Fragment {
    protected ProjectPlanListView mProjectPlanListView;

    public static ProjectPlanListFragment newInstance() {
        return new ProjectPlanListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectPlanListView --
        mProjectPlanListView = ProjectPlanListView.buildProjectPlanListView(getContext(), null);
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
    public void onDetach() {
        super.onDetach();
    }
}
