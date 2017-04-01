package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class InspectorProjectActivityMonitoringDetailAsyncTaskParam {
    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected Integer mProjectActivityMonitoringId;
    protected Integer mProjectActivityId;
    protected ProjectMemberModel mProjectMemberModel;

    public InspectorProjectActivityMonitoringDetailAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final Integer projectActivityMonitoringId, final Integer projectActivityId, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectMemberModel = projectMemberModel;
        mProjectActivityMonitoringId = projectActivityMonitoringId;
        mProjectActivityId = projectActivityId;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public Integer getProjectActivityMonitoringId() {
        return mProjectActivityMonitoringId;
    }

    public Integer getProjectActivityId() {
        return mProjectActivityId;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }
}
