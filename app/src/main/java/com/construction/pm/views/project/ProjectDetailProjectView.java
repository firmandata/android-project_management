package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ProjectDetailProjectView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailProjectView;

    public ProjectDetailProjectView(final Context context) {
        mContext = context;
    }

    public ProjectDetailProjectView(final Context context, final RelativeLayout projectDetailProjectView) {
        this(context);

        initializeView(projectDetailProjectView);
    }

    public static ProjectDetailProjectView buildProjectDetailProjectView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailProjectView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailProjectView buildProjectDetailProjectView(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailProjectView(context, R.layout.project_detail_project_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectDetailProjectView) {
        mProjectDetailProjectView = projectDetailProjectView;
    }

    public RelativeLayout getView() {
        return mProjectDetailProjectView;
    }
}
