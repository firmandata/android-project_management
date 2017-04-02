package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.FileModel;
import com.construction.pm.models.system.SettingUserModel;

public class FileGetAsyncTaskParam {

    protected Context mContext;

    protected SettingUserModel mSettingUserModel;
    protected Integer mFileId;
    protected FileModel mFileModelCache;

    public FileGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final Integer fileId, final FileModel fileModelCache) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mFileId = fileId;
        mFileModelCache = fileModelCache;
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

    public FileModel getFileModelCache() {
        return mFileModelCache;
    }
}
