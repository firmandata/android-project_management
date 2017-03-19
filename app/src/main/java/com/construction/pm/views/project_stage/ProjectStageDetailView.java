package com.construction.pm.views.project_stage;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectStageDetailView {

    protected Context mContext;

    protected RelativeLayout mProjectStageDetailView;

    protected AppCompatTextView mStageDate;
    protected AppCompatTextView mStageCode;
    protected AppCompatTextView mStageFromCode;
    protected AppCompatTextView mStageNextCode;
    protected AppCompatTextView mStageNextPlanDate;
    protected AppCompatTextView mStageNextSubject;
    protected AppCompatTextView mStageNextLocation;
    protected AppCompatTextView mStageNextMessage;

    protected ProjectStageModel mProjectStageModel;

    public ProjectStageDetailView(final Context context) {
        mContext = context;
    }

    public ProjectStageDetailView(final Context context, final RelativeLayout projectStageDetailView) {
        this(context);

        initializeView(projectStageDetailView);
    }

    public static ProjectStageDetailView buildProjectStageDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageDetailView buildProjectStageDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageDetailView(context, R.layout.project_stage_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageDetailView) {
        mProjectStageDetailView = projectStageDetailView;

        mStageDate = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageDate);
        mStageCode = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageCode);
        mStageFromCode = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageFromCode);
        mStageNextCode = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageNextCode);
        mStageNextPlanDate = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageNextPlanDate);
        mStageNextSubject = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageNextSubject);
        mStageNextLocation = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageNextLocation);
        mStageNextMessage = (AppCompatTextView) mProjectStageDetailView.findViewById(R.id.stageNextMessage);
    }

    public RelativeLayout getView() {
        return mProjectStageDetailView;
    }

    public void setProjectStageModel(final ProjectStageModel projectStageModel) {
        if (projectStageModel == null)
            return;

        mProjectStageModel = projectStageModel;

        mStageDate.setText(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageDate()));
        mStageCode.setText(projectStageModel.getStageCode());
        mStageFromCode.setText(projectStageModel.getStageFromCode());
        mStageNextCode.setText(projectStageModel.getStageNextCode());
        mStageNextPlanDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectStageModel.getStageNextPlanDate()));
        mStageNextSubject.setText(projectStageModel.getStageNextSubject());
        mStageNextLocation.setText(projectStageModel.getStageNextLocation());
        mStageNextMessage.setText(projectStageModel.getStageNextMessage());
    }

    public ProjectStageModel getProjectStageModel() {
        return mProjectStageModel;
    }
}
