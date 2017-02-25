package com.construction.pm.asynctask;

import android.content.Context;

import com.construction.pm.models.system.SettingUserModel;

public class FileRequestAsyncTaskParam {

    protected Context mContext;

    protected SettingUserModel mSettingUserModel;
    protected Integer mFileId;

    public FileRequestAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final Integer fileId) {
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
