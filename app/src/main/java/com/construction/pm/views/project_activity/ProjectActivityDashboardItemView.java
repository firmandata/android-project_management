package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;

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

        ViewUtil.setBackgroundSelectableItem(mContext, mTotalTask);

        if (projectActivityDashboardModel.getStatusTask() != null) {
            if (projectActivityDashboardModel.getStatusTask() == StatusTaskEnum.IN_PROGRESS) {
                mProjectActivityDashboardItemView.setBackgroundResource(R.drawable.border_project_activity_dashboard_item_primary);
                mTotalTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimary));
                mStatusTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimaryPrimaryBackground));
            } else if (projectActivityDashboardModel.getStatusTask() == StatusTaskEnum.COMING_DUE) {
                mProjectActivityDashboardItemView.setBackgroundResource(R.drawable.border_project_activity_dashboard_item_info);
                mTotalTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextInfo));
                mStatusTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimaryInfoBackground));
            } else if (projectActivityDashboardModel.getStatusTask() == StatusTaskEnum.SHOULD_HAVE_STARTED) {
                mProjectActivityDashboardItemView.setBackgroundResource(R.drawable.border_project_activity_dashboard_item_warning);
                mTotalTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextWarning));
                mStatusTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimaryWarningBackground));
            } else if (projectActivityDashboardModel.getStatusTask() == StatusTaskEnum.LATE) {
                mProjectActivityDashboardItemView.setBackgroundResource(R.drawable.border_project_activity_dashboard_item_danger);
                mTotalTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextDanger));
                mStatusTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimaryDangerBackground));
            } else if (projectActivityDashboardModel.getStatusTask() == StatusTaskEnum.COMPLETED) {
                mProjectActivityDashboardItemView.setBackgroundResource(R.drawable.border_project_activity_dashboard_item_success);
                mTotalTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextSuccess));
                mStatusTask.setTextColor(mContext.getResources().getColor(R.color.baseColorTextPrimarySuccessBackground));
            }
        }
    }
}
