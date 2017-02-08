package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.project.ProjectDetailProjectView;

public class ProjectDetailProjectFragment extends Fragment {
    protected ProjectDetailProjectView mProjectDetailProjectView;

    public static ProjectDetailProjectFragment newInstance() {
        return new ProjectDetailProjectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectDetailProjectView --
        mProjectDetailProjectView = ProjectDetailProjectView.buildProjectDetailProjectView(getContext(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectDetailProjectView to fragment --
        return mProjectDetailProjectView.getView();
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
