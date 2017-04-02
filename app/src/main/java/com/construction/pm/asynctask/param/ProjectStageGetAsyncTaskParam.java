package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class ProjectStageGetAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected Integer mProjectStageId;
    protected ProjectMemberModel mProjectMemberModel;

    public ProjectStageGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final Integer projectStageId, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectStageId = projectStageId;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public Integer getProjectStageId() {
        return mProjectStageId;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}