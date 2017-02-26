package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class UserChangePasswordAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected String mPasswordOld;
    protected String mPasswordNew;

    public UserChangePasswordAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final String passwordOld, final String passwordNew) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mPasswordOld = passwordOld;
        mPasswordNew = passwordNew;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public String getPasswordOld() {
        return mPasswordOld;
    }

    public String getPasswordNew() {
        return mPasswordNew;
    }
}