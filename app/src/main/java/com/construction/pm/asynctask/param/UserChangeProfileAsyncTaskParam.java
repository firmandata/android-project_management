package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class UserChangeProfileAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;

    public UserChangeProfileAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}