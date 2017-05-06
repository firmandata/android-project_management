package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.system.SettingUserModel;

public class ReportRequestSendAsyncTaskParam {

    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;
    protected ReportRequestModel mReportRequestModel;

    public ReportRequestSendAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ReportRequestModel reportRequestModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mReportRequestModel = reportRequestModel;
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

    public ReportRequestModel getReportRequestModel() {
        return mReportRequestModel;
    }
}
