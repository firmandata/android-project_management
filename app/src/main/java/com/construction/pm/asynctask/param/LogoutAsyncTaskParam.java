package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class LogoutAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;

    public LogoutAsyncTaskParam(final Context context, final SettingUserModel settingUserModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }
}