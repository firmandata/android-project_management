package com.construction.pm.asynctask.result;

import com.construction.pm.models.NotificationModel;

public class NotificationListAsyncTaskResult {

    protected NotificationModel[] mNotificationModels;
    protected String mMessage;

    public NotificationListAsyncTaskResult() {

    }

    public void setNotificationModels(final NotificationModel[] notificationModels) {
        mNotificationModels = notificationModels;
    }

    public NotificationModel[] getNotificationModels() {
        return mNotificationModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}