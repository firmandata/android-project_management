package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectStageListView {
    protected Context mContext;

    protected RelativeLayout mProjectStageListView;

    public ProjectStageListView(final Context context) {
        mContext = context;
    }

    public ProjectStageListView(final Context context, final RelativeLayout projectStageListView) {
        this(context);

        initializeView(projectStageListView);
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageListView(context, R.layout.project_stage_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageListView) {
        mProjectStageListView = projectStageListView;
    }

    public RelativeLayout getView() {
        return mProjectStageListView;
    }
}
