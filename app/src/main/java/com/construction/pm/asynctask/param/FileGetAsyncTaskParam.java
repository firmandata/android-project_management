package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class FileGetAsyncTaskParam {

    protected Context mContext;

    protected SettingUserModel mSettingUserModel;
    protected Integer mFileId;

    public FileGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final Integer fileId) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mFileId = fileId;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public Integer getFileId() {
        return mFileId;
    }
}
