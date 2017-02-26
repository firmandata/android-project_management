package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SettingUserModel;

public class ProjectStageGetAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectStageModel mProjectStageModel;
    protected ProjectMemberModel mProjectMemberModel;

    public ProjectStageGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectStageModel projectStageModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectStageModel = projectStageModel;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public ProjectStageModel getProjectStageModel() {
        return mProjectStageModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}