package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class LoginFirstAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected String mPasswordNew;

    public LoginFirstAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final String passwordNew) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mPasswordNew = passwordNew;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public String getPasswordNew() {
        return mPasswordNew;
    }
}