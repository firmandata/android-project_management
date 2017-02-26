package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.system.SettingUserModel;

public class ProjectPlanGetAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectPlanModel mProjectPlanModel;
    protected ProjectMemberModel mProjectMemberModel;

    public ProjectPlanGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectPlanModel projectPlanModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectPlanModel = projectPlanModel;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public ProjectPlanModel getProjectPlanModel() {
        return mProjectPlanModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}