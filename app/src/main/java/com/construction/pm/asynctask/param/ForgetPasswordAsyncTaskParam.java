package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class ForgetPasswordAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected String mLogin;

    public ForgetPasswordAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final String login) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mLogin = login;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public String getLogin() {
        return mLogin;
    }
}