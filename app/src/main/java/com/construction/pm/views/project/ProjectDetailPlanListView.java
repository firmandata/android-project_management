package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectDetailPlanListView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailPlanListView;

    public ProjectDetailPlanListView(final Context context) {
        mContext = context;
    }

    public ProjectDetailPlanListView(final Context context, final RelativeLayout projectDetailPlanListView) {
        this(context);

        initializeView(projectDetailPlanListView);
    }

    public static ProjectDetailPlanListView buildProjectDetailPlanListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailPlanListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailPlanListView buildProjectDetailPlanListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailPlanListView(context, R.layout.project_detail_plan_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectDetailPlanListView) {
        mProjectDetailPlanListView = projectDetailPlanListView;
    }

    public RelativeLayout getView() {
        return mProjectDetailPlanListView;
    }
}
