package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectActivityDetailView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityDetailView;

    protected AppCompatTextView mTaskName;
    protected AppCompatTextView mPlanStartDate;
    protected AppCompatTextView mPlanEndDate;
    protected AppCompatTextView mActualStartDate;
    protected AppCompatTextView mActualEndDate;
    protected AppCompatTextView mActivityStatus;
    protected AppCompatTextView mPercentComplete;
    protected AppCompatTextView mStatusTask;

    public ProjectActivityDetailView(final Context context) {
        mContext = context;
    }

    public ProjectActivityDetailView(final Context context, final RelativeLayout projectActivityDetailView) {
        this(context);

        initializeView(projectActivityDetailView);
    }

    public static ProjectActivityDetailView buildProjectActivityDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityDetailView buildProjectActivityDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityDetailView(context, R.layout.project_activity_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectActivityDetailView = projectActivityDetailView;

        mTaskName = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.taskName);
        mPlanStartDate = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.planStartDate);
        mPlanEndDate = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.planEndDate);
        mActualStartDate = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.actualStartDate);
        mActualEndDate = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.activityStatus);
        mPercentComplete = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.percentComplete);
        mStatusTask = (AppCompatTextView) mProjectActivityDetailView.findViewById(R.id.statusTask);
    }

    public RelativeLayout getView() {
        return mProjectActivityDetailView;
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        if (projectActivityModel == null)
            return;

        mTaskName.setText(projectActivityModel.getTaskName());
        mPlanStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getPlanStartDate()));
        mPlanEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getPlanEndDate()));
        mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getActualStartDate()));
        mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getActualEndDate()));
        mActivityStatus.setText(projectActivityModel.getActivityStatus());
        mPercentComplete.setText(StringUtil.numberFormat(projectActivityModel.getPercentComplete()));
        if (projectActivityModel.getStatusTask() != null)
            mStatusTask.setText(projectActivityModel.getStatusTask().getValue());
    }
}
