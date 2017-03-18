package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class ManagerProjectActivityUpdateSaveAsyncTaskParam {
    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;
    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;

    public ManagerProjectActivityUpdateSaveAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectActivityUpdateModel projectActivityUpdateModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectActivityUpdateModel = projectActivityUpdateModel;
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

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        return mProjectActivityUpdateModel;
    }
}
