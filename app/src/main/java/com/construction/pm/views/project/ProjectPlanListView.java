package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectPlanListView {
    protected Context mContext;

    protected RelativeLayout mProjectPlanListView;

    public ProjectPlanListView(final Context context) {
        mContext = context;
    }

    public ProjectPlanListView(final Context context, final RelativeLayout projectPlanListView) {
        this(context);

        initializeView(projectPlanListView);
    }

    public static ProjectPlanListView buildProjectPlanListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectPlanListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectPlanListView buildProjectPlanListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectPlanListView(context, R.layout.project_plan_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectPlanListView) {
        mProjectPlanListView = projectPlanListView;
    }

    public RelativeLayout getView() {
        return mProjectPlanListView;
    }
}
