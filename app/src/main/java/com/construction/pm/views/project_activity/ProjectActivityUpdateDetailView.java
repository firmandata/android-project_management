package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectActivityUpdateDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityUpdateDetailView;

    protected AppCompatTextView mUpdateDate;
    protected AppCompatTextView mActualStartDate;
    protected AppCompatTextView mActualEndDate;
    protected AppCompatTextView mActivityStatus;
    protected AppCompatTextView mPercentComplete;
    protected AppCompatTextView mComment;

    public ProjectActivityUpdateDetailView(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateDetailView(final Context context, final RelativeLayout projectActivityUpdateDetailView) {
        this(context);

        initializeView(projectActivityUpdateDetailView);
    }

    public static ProjectActivityUpdateDetailView buildProjectActivityUpdateDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateDetailView buildProjectActivityUpdateDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateDetailView(context, R.layout.project_activity_update_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityUpdateDetailView) {
        mProjectActivityUpdateDetailView = projectActivityUpdateDetailView;

        mUpdateDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateDate);
        mActualStartDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.actualStartDate);
        mActualEndDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.activityStatus);
        mPercentComplete = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.percentComplete);
        mComment = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.comment);
    }

    public RelativeLayout getView() {
        return mProjectActivityUpdateDetailView;
    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (projectActivityUpdateModel == null)
            return;

        mUpdateDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityUpdateModel.getUpdateDate()));
        mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualStartDate()));
        mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualEndDate()));
        mActivityStatus.setText(projectActivityUpdateModel.getActivityStatus());
        mPercentComplete.setText(StringUtil.numberFormat(projectActivityUpdateModel.getPercentComplete()));
        mComment.setText(projectActivityUpdateModel.getComment());
    }
}
