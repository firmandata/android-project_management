package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.views.file.FilePhotoListView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;

public class ProjectActivityMonitoringDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityMonitoringDetailView;

    protected AppCompatTextView mMonitoringDate;
    protected AppCompatTextView mActualStartDate;
    protected AppCompatTextView mActualEndDate;
    protected AppCompatTextView mActivityStatus;
    protected AppCompatTextView mPercentComplete;
    protected AppCompatTextView mComment;
    protected ImageView mPhotoId;
    protected FilePhotoListView mFilePhotoListView;

    protected ImageRequestListener mImageRequestListener;
    protected ImageRequestClickListener mImageRequestClickListener;

    public ProjectActivityMonitoringDetailView(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringDetailView(final Context context, final RelativeLayout projectActivityMonitoringDetailView) {
        this(context);

        initializeView(projectActivityMonitoringDetailView);
    }

    public static ProjectActivityMonitoringDetailView buildProjectActivityMonitoringDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringDetailView buildProjectActivityMonitoringDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringDetailView(context, R.layout.project_activity_monitoring_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityMonitoringDetailView) {
        mProjectActivityMonitoringDetailView = projectActivityMonitoringDetailView;

        mMonitoringDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.monitoringDate);
        mActualStartDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.actualStartDate);
        mActualEndDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.activityStatus);
        mPercentComplete = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.percentComplete);
        mComment = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.comment);
        mPhotoId = (ImageView) mProjectActivityMonitoringDetailView.findViewById(R.id.photoId);
        mFilePhotoListView = new FilePhotoListView(mContext, (RelativeLayout) mProjectActivityMonitoringDetailView.findViewById(R.id.file_photo_list_view));
    }

    public RelativeLayout getView() {
        return mProjectActivityMonitoringDetailView;
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
        mFilePhotoListView.setImageRequestListener(mImageRequestListener);
    }

    public void setImageRequestClickListener(final ImageRequestClickListener imageRequestClickListener) {
        mImageRequestClickListener = imageRequestClickListener;
        mFilePhotoListView.setImageRequestClickListener(imageRequestClickListener);
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mMonitoringDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate()));
        mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualStartDate()));
        mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualEndDate()));
        mActivityStatus.setText(projectActivityMonitoringModel.getActivityStatus());
        mPercentComplete.setText(StringUtil.numberFormat(projectActivityMonitoringModel.getPercentComplete()));
        mComment.setText(projectActivityMonitoringModel.getComment());

        if (projectActivityMonitoringModel.getPhotoId() != null) {
            if (mImageRequestListener != null)
                mImageRequestListener.onImageRequest(mPhotoId, projectActivityMonitoringModel.getPhotoId());
            if (mImageRequestClickListener != null) {
                mPhotoId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mImageRequestClickListener.onImageRequestClick(projectActivityMonitoringModel.getPhotoId());
                    }
                });
            }
        }
        if (projectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            mFilePhotoListView.addFileId(projectActivityMonitoringModel.getPhotoAdditional1Id());
        if (projectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            mFilePhotoListView.addFileId(projectActivityMonitoringModel.getPhotoAdditional2Id());
        if (projectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            mFilePhotoListView.addFileId(projectActivityMonitoringModel.getPhotoAdditional3Id());
        if (projectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            mFilePhotoListView.addFileId(projectActivityMonitoringModel.getPhotoAdditional4Id());
        if (projectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            mFilePhotoListView.addFileId(projectActivityMonitoringModel.getPhotoAdditional5Id());
    }
}
