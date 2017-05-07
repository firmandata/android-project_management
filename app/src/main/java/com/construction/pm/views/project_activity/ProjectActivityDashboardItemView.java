package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.utils.StringUtil;

public class ProjectActivityDashboardItemView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityDashboardItemView;
    protected AppCompatTextView mStatusTask;
    protected AppCompatTextView mTotalTask;

    public ProjectActivityDashboardItemView(final Context context) {
        mContext = context;
    }

    public ProjectActivityDashboardItemView(final Context context, final RelativeLayout projectActivityDashboardItemView) {
        this(context);

        initializeView(projectActivityDashboardItemView);
    }

    public static ProjectActivityDashboardItemView buildProjectActivityDashboardItemView(final Context context, final int layoutId, final ViewGroup viewGroup, boolean attachToRoot) {
        return new ProjectActivityDashboardItemView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup, attachToRoot));
    }

    public static ProjectActivityDashboardItemView buildProjectActivityDashboardItemView(final Context context, final ViewGroup viewGroup, boolean attachToRoot) {
        return buildProjectActivityDashboardItemView(context, R.layout.project_activity_dashboard_item_view, viewGroup, attachToRoot);
    }

    public static ProjectActivityDashboardItemView buildProjectActivityDashboardItemView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityDashboardItemView(context, viewGroup, true);
    }

    protected void initializeView(final RelativeLayout projectActivityDashboardItemView) {
        mProjectActivityDashboardItemView = projectActivityDashboardItemView;

        mStatusTask = (AppCompatTextView) mProjectActivityDashboardItemView.findViewById(R.id.statusTask);
        mTotalTask = (AppCompatTextView) mProjectActivityDashboardItemView.findViewById(R.id.totalTask);
    }

    public RelativeLayout getView() {
        return mProjectActivityDashboardItemView;
    }

    public void setProjectActivityDashboardModel(final ProjectActivityDashboardModel projectActivityDashboardModel) {
        if (projectActivityDashboardModel.getStatusTask() != null)
            mStatusTask.setText(projectActivityDashboardModel.getStatusTask().getValue());
        if (projectActivityDashboardModel.getTotalTask() != null)
            mTotalTask.setText(StringUtil.numberFormat(projectActivityDashboardModel.getTotalTask()));
    }
}
