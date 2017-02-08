package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectDetailContractView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailContractView;

    public ProjectDetailContractView(final Context context) {
        mContext = context;
    }

    public ProjectDetailContractView(final Context context, final RelativeLayout projectDetailContractView) {
        this(context);

        initializeView(projectDetailContractView);
    }

    public static ProjectDetailContractView buildProjectDetailContractView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailContractView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailContractView buildProjectDetailContractView(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailContractView(context, R.layout.project_detail_contract_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectDetailContractView) {
        mProjectDetailContractView = projectDetailContractView;
    }

    public RelativeLayout getView() {
        return mProjectDetailContractView;
    }
}
