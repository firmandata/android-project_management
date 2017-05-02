package com.construction.pm.views.notification;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.DateTimeUtil;

public class NotificationDetailView {
    protected Context mContext;

    protected RelativeLayout mNotificationDetailView;
    protected AppCompatTextView mTvNotificationDate;
    protected AppCompatTextView mTvNotificationMessage;

    protected LinearLayout mButtonContainer;

    protected FrameLayout mBtnProjectStageContainer;
    protected AppCompatButton mBtnProjectStage;
    protected ProgressBar mBtnProjectStageProgressBar;

    protected FrameLayout mBtnManagerProjectActivityContainer;
    protected AppCompatButton mBtnManagerProjectActivity;
    protected ProgressBar mBtnManagerProjectActivityProgressBar;

    protected FrameLayout mBtnInspectorProjectActivityContainer;
    protected AppCompatButton mBtnInspectorProjectActivity;
    protected ProgressBar mBtnInspectorProjectActivityProgressBar;

    protected ProjectStageModel mProjectStageModel;
    protected ProjectActivityModel mManagerProjectActivityModel;
    protected ProjectActivityModel mInspectorProjectActivityModel;

    protected NotificationDetailListener mNotificationDetailListener;

    public NotificationDetailView(final Context context) {
        mContext = context;
    }

    public NotificationDetailView(final Context context, final RelativeLayout notificationDetailView) {
        this(context);

        initializeView(notificationDetailView);
    }

    public static NotificationDetailView buildNotificationDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new NotificationDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static NotificationDetailView buildNotificationDetailView(final Context context, final ViewGroup viewGroup) {
        return buildNotificationDetailView(context, R.layout.notification_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout notificationDetailView) {
        mNotificationDetailView = notificationDetailView;

        mTvNotificationDate = (AppCompatTextView) mNotificationDetailView.findViewById(R.id.notificationDate);
        mTvNotificationMessage = (AppCompatTextView) mNotificationDetailView.findViewById(R.id.notificationMessage);

        mButtonContainer = (LinearLayout) mNotificationDetailView.findViewById(R.id.buttonContainer);

        mBtnProjectStageContainer = (FrameLayout) mButtonContainer.findViewById(R.id.projectStageContainer);
        mBtnProjectStage = (AppCompatButton) mBtnProjectStageContainer.findViewById(R.id.projectStage);
        mBtnProjectStageProgressBar = (ProgressBar) mBtnProjectStageContainer.findViewById(R.id.projectStageProgressBar);

        mBtnManagerProjectActivityContainer = (FrameLayout) mButtonContainer.findViewById(R.id.managerProjectActivityContainer);
        mBtnManagerProjectActivity = (AppCompatButton) mBtnManagerProjectActivityContainer.findViewById(R.id.managerProjectActivity);
        mBtnManagerProjectActivityProgressBar = (ProgressBar) mBtnManagerProjectActivityContainer.findViewById(R.id.managerProjectActivityProgressBar);

        mBtnInspectorProjectActivityContainer = (FrameLayout) mButtonContainer.findViewById(R.id.inspectorProjectActivityContainer);
        mBtnInspectorProjectActivity = (AppCompatButton) mBtnInspectorProjectActivityContainer.findViewById(R.id.inspectorProjectActivity);
        mBtnInspectorProjectActivityProgressBar = (ProgressBar) mBtnInspectorProjectActivityContainer.findViewById(R.id.inspectorProjectActivityProgressBar);

        mBtnProjectStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjectStageModel != null) {
                    if (mNotificationDetailListener != null)
                        mNotificationDetailListener.onProjectStageClick(mProjectStageModel);
                }
            }
        });

        mBtnManagerProjectActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mManagerProjectActivityModel != null) {
                    if (mNotificationDetailListener != null)
                        mNotificationDetailListener.onManagerProjectActivityClick(mManagerProjectActivityModel);
                }
            }
        });

        mBtnInspectorProjectActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInspectorProjectActivityModel != null) {
                    if (mNotificationDetailListener != null)
                        mNotificationDetailListener.onInspectorProjectActivityClick(mInspectorProjectActivityModel);
                }
            }
        });

        ButtonUtil.setButtonInfo(mContext, mBtnProjectStage);
        ButtonUtil.setButtonInfo(mContext, mBtnManagerProjectActivity);
        ButtonUtil.setButtonInfo(mContext, mBtnInspectorProjectActivity);
    }

    public RelativeLayout getView() {
        return mNotificationDetailView;
    }

    public void setNotificationModel(final NotificationModel notificationModel) {
        if (notificationModel == null)
            return;

        mTvNotificationDate.setText(DateTimeUtil.ToDateTimeDisplayString(notificationModel.getNotificationDate()));
        mTvNotificationMessage.setText(notificationModel.getNotificationMessage());

        boolean isMonitoring = false;
        if (notificationModel.getIsMonitoring() != null)
            isMonitoring = notificationModel.getIsMonitoring();

        boolean isUpdateTask = false;
        if (notificationModel.getIsUpdateTask() != null)
            isUpdateTask = notificationModel.getIsUpdateTask();

        if (notificationModel.getProjectStageId() == null)
            mButtonContainer.removeView(mBtnProjectStageContainer);
        else
            mBtnProjectStage.setEnabled(false);
        if (notificationModel.getProjectActivityId() == null) {
            mButtonContainer.removeView(mBtnManagerProjectActivityContainer);
            mButtonContainer.removeView(mBtnInspectorProjectActivityContainer);
        } else {
            if (!isMonitoring)
                mButtonContainer.removeView(mBtnInspectorProjectActivityContainer);
            else
                mBtnManagerProjectActivity.setEnabled(false);
            if (!isUpdateTask)
                mButtonContainer.removeView(mBtnManagerProjectActivityContainer);
            else
                mBtnInspectorProjectActivity.setEnabled(false);
        }

        if (mNotificationDetailListener != null) {
            if (notificationModel.getProjectStageId() != null)
                mNotificationDetailListener.onRequestProjectStage(notificationModel.getProjectStageId());
            if (notificationModel.getProjectActivityId() != null && (isUpdateTask || isMonitoring))
                mNotificationDetailListener.onRequestProjectActivity(notificationModel.getProjectActivityId());
        }
    }

    public void setProjectStageModel(final ProjectStageModel projectStageModel) {
        mProjectStageModel = projectStageModel;
        if (mProjectStageModel == null)
            mButtonContainer.removeView(mBtnProjectStageContainer);
        else
            mBtnProjectStage.setEnabled(true);
    }

    public void setManagerProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mManagerProjectActivityModel = projectActivityModel;
        if (mManagerProjectActivityModel == null)
            mButtonContainer.removeView(mBtnManagerProjectActivityContainer);
        else
            mBtnManagerProjectActivity.setEnabled(true);
    }

    public void setInspectorProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mInspectorProjectActivityModel = projectActivityModel;
        if (mInspectorProjectActivityModel == null)
            mButtonContainer.removeView(mBtnInspectorProjectActivityContainer);
        else
            mBtnInspectorProjectActivity.setEnabled(true);
    }

    public void setNotificationDetailListener(final NotificationDetailListener notificationDetailListener) {
        mNotificationDetailListener = notificationDetailListener;
    }

    public interface NotificationDetailListener {
        void onRequestProjectStage(Integer projectStageId);
        void onRequestProjectActivity(Integer projectActivityId);
        void onProjectStageClick(ProjectStageModel projectStageModel);
        void onManagerProjectActivityClick(ProjectActivityModel projectActivityModel);
        void onInspectorProjectActivityClick(ProjectActivityModel projectActivityModel);
    }
}
