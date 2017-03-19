package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.DatePickerView;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.views.adapter.SpinnerActivityStatusAdapter;

public class ProjectActivityMonitoringFormView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityMonitoringFormView;

    protected DatePickerView mActualStartDate;
    protected DatePickerView mActualEndDate;
    protected AppCompatSpinner mActivityStatus;
    protected SpinnerActivityStatusAdapter mSpinnerActivityStatusAdapter;
    protected SeekBar mPercentComplete;
    protected AppCompatEditText mComment;

    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;

    public ProjectActivityMonitoringFormView(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringFormView(final Context context, final RelativeLayout projectActivityMonitoringFormView) {
        this(context);

        initializeView(projectActivityMonitoringFormView);
    }

    public static ProjectActivityMonitoringFormView buildProjectActivityMonitoringFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringFormView buildProjectActivityMonitoringFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringFormView(context, R.layout.project_activity_monitoring_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectActivityMonitoringFormView = projectActivityDetailView;

        mActualStartDate = (DatePickerView) mProjectActivityMonitoringFormView.findViewById(R.id.actualStartDate);
        mActualEndDate = (DatePickerView) mProjectActivityMonitoringFormView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatSpinner) mProjectActivityMonitoringFormView.findViewById(R.id.activityStatus);
        mPercentComplete = (SeekBar) mProjectActivityMonitoringFormView.findViewById(R.id.percentComplete);
        mComment = (AppCompatEditText) mProjectActivityMonitoringFormView.findViewById(R.id.comment);

        mSpinnerActivityStatusAdapter = new SpinnerActivityStatusAdapter(mContext);
        mActivityStatus.setAdapter(mSpinnerActivityStatusAdapter);
    }

    public RelativeLayout getView() {
        return mProjectActivityMonitoringFormView;
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        if (projectActivityModel == null)
            return;

        mProjectActivityMonitoringModel = new ProjectActivityMonitoringModel();
        mProjectActivityMonitoringModel.setProjectActivityId(projectActivityModel.getProjectActivityId());
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mProjectActivityMonitoringModel = projectActivityMonitoringModel.duplicate();

        mActualStartDate.setDate(projectActivityMonitoringModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityMonitoringModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityMonitoringModel.getActivityStatus())));
        if (projectActivityMonitoringModel.getPercentComplete() != null)
            mPercentComplete.setProgress(projectActivityMonitoringModel.getPercentComplete().intValue());
        mComment.setText(projectActivityMonitoringModel.getComment());
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        if (mProjectActivityMonitoringModel == null)
            mProjectActivityMonitoringModel = new ProjectActivityMonitoringModel();

        mProjectActivityMonitoringModel.setActualStartDate(mActualStartDate.getCalendar());
        mProjectActivityMonitoringModel.setActualEndDate(mActualEndDate.getCalendar());
        if (mActivityStatus.getSelectedItem() != null)
            mProjectActivityMonitoringModel.setActivityStatus(((ActivityStatusEnum) mActivityStatus.getSelectedItem()).getValue());
        else
            mProjectActivityMonitoringModel.setActivityStatus(null);
        mProjectActivityMonitoringModel.setPercentComplete(Double.valueOf(String.valueOf(mPercentComplete.getProgress())));
        mProjectActivityMonitoringModel.setComment(mComment.getText().toString());

        return mProjectActivityMonitoringModel;
    }
}
