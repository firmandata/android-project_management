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
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.views.adapter.SpinnerActivityStatusAdapter;

public class ProjectActivityUpdateFormView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityUpdateFormView;

    protected DatePickerView mActualStartDate;
    protected DatePickerView mActualEndDate;
    protected AppCompatSpinner mActivityStatus;
    protected SpinnerActivityStatusAdapter mSpinnerActivityStatusAdapter;
    protected SeekBar mPercentComplete;
    protected AppCompatEditText mComment;

    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;

    public ProjectActivityUpdateFormView(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateFormView(final Context context, final RelativeLayout projectActivityUpdateFormView) {
        this(context);

        initializeView(projectActivityUpdateFormView);
    }

    public static ProjectActivityUpdateFormView buildProjectActivityUpdateFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateFormView buildProjectActivityUpdateFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateFormView(context, R.layout.project_activity_update_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectActivityUpdateFormView = projectActivityDetailView;

        mActualStartDate = (DatePickerView) mProjectActivityUpdateFormView.findViewById(R.id.actualStartDate);
        mActualEndDate = (DatePickerView) mProjectActivityUpdateFormView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatSpinner) mProjectActivityUpdateFormView.findViewById(R.id.activityStatus);
        mPercentComplete = (SeekBar) mProjectActivityUpdateFormView.findViewById(R.id.percentComplete);
        mComment = (AppCompatEditText) mProjectActivityUpdateFormView.findViewById(R.id.comment);

        mSpinnerActivityStatusAdapter = new SpinnerActivityStatusAdapter(mContext);
        mActivityStatus.setAdapter(mSpinnerActivityStatusAdapter);
    }

    public RelativeLayout getView() {
        return mProjectActivityUpdateFormView;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mActualStartDate.setDate(projectActivityMonitoringModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityMonitoringModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityMonitoringModel.getActivityStatus())));
        if (projectActivityMonitoringModel.getPercentComplete() != null)
            mPercentComplete.setProgress(projectActivityMonitoringModel.getPercentComplete().intValue());
        mComment.setText(projectActivityMonitoringModel.getComment());

        mProjectActivityUpdateModel = new ProjectActivityUpdateModel();
        mProjectActivityUpdateModel.setProjectActivityMonitoringId(projectActivityMonitoringModel.getProjectActivityMonitoringId());
    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (projectActivityUpdateModel == null)
            return;

        mProjectActivityUpdateModel = projectActivityUpdateModel.duplicate();

        mActualStartDate.setDate(projectActivityUpdateModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityUpdateModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityUpdateModel.getActivityStatus())));
        if (projectActivityUpdateModel.getPercentComplete() != null)
            mPercentComplete.setProgress(projectActivityUpdateModel.getPercentComplete().intValue());
        mComment.setText(projectActivityUpdateModel.getComment());
    }

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        if (mProjectActivityUpdateModel == null)
            mProjectActivityUpdateModel = new ProjectActivityUpdateModel();

        mProjectActivityUpdateModel.setActualStartDate(mActualStartDate.getCalendar());
        mProjectActivityUpdateModel.setActualEndDate(mActualEndDate.getCalendar());
        if (mActivityStatus.getSelectedItem() != null)
            mProjectActivityUpdateModel.setActivityStatus(((ActivityStatusEnum) mActivityStatus.getSelectedItem()).getValue());
        else
            mProjectActivityUpdateModel.setActivityStatus(null);
        mProjectActivityUpdateModel.setPercentComplete(Double.valueOf(String.valueOf(mPercentComplete.getProgress())));
        mProjectActivityUpdateModel.setComment(mComment.getText().toString());

        return mProjectActivityUpdateModel;
    }
}
