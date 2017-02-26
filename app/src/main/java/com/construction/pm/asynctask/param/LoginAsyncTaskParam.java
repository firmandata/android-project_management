package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class LoginAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected String mLogin;
    protected String mPassword;

    public LoginAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final String login, final String password) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mLogin = login;
        mPassword = password;
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

    public String getPassword() {
        return mPassword;
    }
}