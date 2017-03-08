package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class InspectorProjectActivityMonitoringListAsyncTaskParam {
    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectActivityModel mProjectActivityModel;
    protected ProjectMemberModel mProjectMemberModel;

    public InspectorProjectActivityMonitoringListAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectActivityModel projectActivityModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectMemberModel = projectMemberModel;
        mProjectActivityModel = projectActivityModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public ProjectActivityModel getProjectActivityModel() {
        return mProjectActivityModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}
