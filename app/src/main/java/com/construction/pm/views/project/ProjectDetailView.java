package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailView;

    public ProjectDetailView(final Context context) {
        mContext = context;
    }

    public ProjectDetailView(final Context context, final RelativeLayout projectDetailView) {
        this(context);

        initializeView(projectDetailView);
    }

    public static ProjectDetailView buildProjectDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailView buildProjectDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailView(context, R.layout.project_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectDetailView) {
        mProjectDetailView = projectDetailView;
    }

    public RelativeLayout getView() {
        return mProjectDetailView;
    }
}
