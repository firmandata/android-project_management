package com.construction.pm.views.project_plan;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectPlanDetailView {

    protected Context mContext;

    protected RelativeLayout mProjectPlanDetailView;

    protected AppCompatTextView mSequenceNo;
    protected AppCompatTextView mTaskName;
    protected AppCompatTextView mTaskWeightPercentage;
    protected AppCompatTextView mPlanStartDate;
    protected AppCompatTextView mPlanEndDate;
    protected AppCompatTextView mRealizationStartDate;
    protected AppCompatTextView mRealizationEndDate;
    protected AppCompatTextView mRealizationStatus;
    protected AppCompatTextView mPercentComplete;

    public ProjectPlanDetailView(final Context context) {
        mContext = context;
    }

    public ProjectPlanDetailView(final Context context, final RelativeLayout projectPlanDetailView) {
        this(context);

        initializeView(projectPlanDetailView);
    }

    public static ProjectPlanDetailView buildProjectPlanDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectPlanDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectPlanDetailView buildProjectPlanDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectPlanDetailView(context, R.layout.project_plan_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectPlanDetailView) {
        mProjectPlanDetailView = projectPlanDetailView;

        mSequenceNo = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.sequenceNo);
        mTaskName = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.taskName);
        mTaskWeightPercentage = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.taskWeightPercentage);
        mPlanStartDate = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.planStartDate);
        mPlanEndDate = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.planEndDate);
        mRealizationStartDate = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.realizationStartDate);
        mRealizationEndDate = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.realizationEndDate);
        mRealizationStatus = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.realizationStatus);
        mPercentComplete = (AppCompatTextView) mProjectPlanDetailView.findViewById(R.id.percentComplete);
    }

    public RelativeLayout getView() {
        return mProjectPlanDetailView;
    }

    public void setProjectPlanModel(final ProjectPlanModel projectPlanModel) {
        if (projectPlanModel == null)
            return;

        mSequenceNo.setText(StringUtil.numberFormat(projectPlanModel.getSequenceNo()));
        mTaskName.setText(projectPlanModel.getTaskName());
        mTaskWeightPercentage.setText(StringUtil.numberFormat(projectPlanModel.getTaskWeightPercentage()));
        mPlanStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanStartDate()));
        mPlanEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanEndDate()));
        mRealizationStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationStartDate()));
        mRealizationEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationEndDate()));
        mRealizationStatus.setText(projectPlanModel.getRealizationStatus());
        mPercentComplete.setText(StringUtil.numberFormat(projectPlanModel.getPercentComplete()));
    }
}
