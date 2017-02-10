package com.construction.pm.views.project;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectDetailView;
    protected AppCompatTextView mTvContractNo;
    protected AppCompatTextView mTvProjectName;
    protected AppCompatTextView mTvProjectStartDate;
    protected AppCompatTextView mTvProjectEndDate;
    protected AppCompatTextView mTvProjectDescription;
    protected AppCompatTextView mTvProjectLocation;
    protected AppCompatTextView mTvStageCode;
    protected AppCompatTextView mTvProjectStatus;

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

        mTvContractNo = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvContractNo);
        mTvProjectName = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectName);
        mTvProjectStartDate = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectStartDate);
        mTvProjectEndDate = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectEndDate);
        mTvProjectDescription = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectDescription);
        mTvProjectLocation = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectLocation);
        mTvStageCode = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvStageCode);
        mTvProjectStatus = (AppCompatTextView) mProjectDetailView.findViewById(R.id.tvProjectStatus);
    }

    public RelativeLayout getView() {
        return mProjectDetailView;
    }

    public void setProjectModel(final ProjectModel projectModel) {
        if (projectModel == null)
            return;

        mTvContractNo.setText(projectModel.getContractNo());
        mTvProjectName.setText(projectModel.getProjectName());
        mTvProjectStartDate.setText(DateTimeUtil.ToDateDisplayString(projectModel.getProjectStartDate()));
        mTvProjectEndDate.setText(DateTimeUtil.ToDateDisplayString(projectModel.getProjectEndDate()));
        mTvProjectDescription.setText(projectModel.getProjectDescription());
        mTvProjectLocation.setText(projectModel.getProjectLocation());
        mTvStageCode.setText(projectModel.getStageCode());
        mTvProjectStatus.setText(projectModel.getProjectStatus());
    }
}
