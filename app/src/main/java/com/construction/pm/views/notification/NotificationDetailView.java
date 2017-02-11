package com.construction.pm.views.notification;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.utils.DateTimeUtil;

public class NotificationDetailView {
    protected Context mContext;

    protected RelativeLayout mNotificationDetailView;
    protected AppCompatTextView mTvNotificationDate;
    protected AppCompatTextView mTvNotificationMessage;

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
    }

    public RelativeLayout getView() {
        return mNotificationDetailView;
    }

    public void setNotificationModel(final NotificationModel notificationModel) {
        if (notificationModel == null)
            return;

        mTvNotificationDate.setText(DateTimeUtil.ToDateTimeDisplayString(notificationModel.getNotificationDate()));
        mTvNotificationMessage.setText(notificationModel.getNotificationMessage());
    }
}
