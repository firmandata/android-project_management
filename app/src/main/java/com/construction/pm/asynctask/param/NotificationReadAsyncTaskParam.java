package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class NotificationReadAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected NotificationModel mNotificationModel;
    protected ProjectMemberModel mProjectMemberModel;

    public NotificationReadAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final NotificationModel notificationModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mNotificationModel = notificationModel;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public NotificationModel getNotificationModel() {
        return mNotificationModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}