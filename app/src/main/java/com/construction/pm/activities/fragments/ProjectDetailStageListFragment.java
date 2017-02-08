package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.project.ProjectDetailStageListView;

public class ProjectDetailStageListFragment extends Fragment {
    protected ProjectDetailStageListView mProjectDetailStageListView;

    public static ProjectDetailStageListFragment newInstance() {
        return new ProjectDetailStageListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectDetailStageListView --
        mProjectDetailStageListView = ProjectDetailStageListView.buildProjectDetailStageListView(getContext(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectDetailStageListView to fragment --
        return mProjectDetailStageListView.getView();
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
