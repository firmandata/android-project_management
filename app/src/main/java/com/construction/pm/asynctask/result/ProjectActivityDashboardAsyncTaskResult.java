package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.models.network.ProjectActivityDashboardResponseModel;

public class ProjectActivityDashboardAsyncTaskResult {

    protected ProjectActivityDashboardResponseModel mProjectActivityDashboardResponseModel;
    protected String mMessage;

    public ProjectActivityDashboardAsyncTaskResult() {

    }

    public void setProjectActivityDashboardResponseModel(final ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel) {
        mProjectActivityDashboardResponseModel = projectActivityDashboardResponseModel;
    }

    public ProjectActivityDashboardResponseModel getProjectActivityDashboardResponseModel() {
        return mProjectActivityDashboardResponseModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}