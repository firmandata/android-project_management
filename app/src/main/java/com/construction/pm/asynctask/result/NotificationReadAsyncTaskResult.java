package com.construction.pm.asynctask.result;

import com.construction.pm.models.NotificationModel;

public class NotificationReadAsyncTaskResult {

    protected NotificationModel mNotificationModel;
    protected String mMessage;

    public NotificationReadAsyncTaskResult() {

    }

    public void setNotificationModel(final NotificationModel notificationModel) {
        mNotificationModel = notificationModel;
    }

    public NotificationModel getNotificationModel() {
        return mNotificationModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}