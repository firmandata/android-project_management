package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.project.ProjectDetailContractView;

public class ProjectDetailContractFragment extends Fragment {
    protected ProjectDetailContractView mProjectDetailContractView;

    public static ProjectDetailContractFragment newInstance() {
        return new ProjectDetailContractFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectDetailContractView --
        mProjectDetailContractView = ProjectDetailContractView.buildProjectDetailContractView(getContext(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectDetailContractView to fragment --
        return mProjectDetailContractView.getView();
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
