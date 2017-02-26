package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.models.system.SettingUserModel;

public class InspectorProjectActivityListAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;
    protected StatusTaskEnum mStatusTaskEnum;

    public InspectorProjectActivityListAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectMemberModel = projectMemberModel;
    }

    public InspectorProjectActivityListAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel, final StatusTaskEnum statusTaskEnum) {
        this(context, settingUserModel, projectMemberModel);

        mStatusTaskEnum = statusTaskEnum;
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

    public StatusTaskEnum getStatusTaskEnum() {
        return mStatusTaskEnum;
    }
}