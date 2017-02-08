package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectDetailStageListView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailStageListView;

    public ProjectDetailStageListView(final Context context) {
        mContext = context;
    }

    public ProjectDetailStageListView(final Context context, final RelativeLayout projectDetailStageListView) {
        this(context);

        initializeView(projectDetailStageListView);
    }

    public static ProjectDetailStageListView buildProjectDetailStageListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailStageListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailStageListView buildProjectDetailStageListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailStageListView(context, R.layout.project_detail_stage_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectDetailStageListView) {
        mProjectDetailStageListView = projectDetailStageListView;
    }

    public RelativeLayout getView() {
        return mProjectDetailStageListView;
    }
}
