package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SettingUserModel;

public class InspectorProjectActivityGetAsyncTaskParam {
    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;
    protected Integer mProjectActivityId;

    public InspectorProjectActivityGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectMemberModel = projectMemberModel;
    }

    public InspectorProjectActivityGetAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel, final Integer projectActivityId) {
        this(context, settingUserModel, projectMemberModel);

        mProjectActivityId = projectActivityId;
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

    public Integer getProjectActivityId() {
        return mProjectActivityId;
    }
}
