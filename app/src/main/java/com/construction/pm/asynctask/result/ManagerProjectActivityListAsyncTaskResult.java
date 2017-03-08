package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityModel;

public class ManagerProjectActivityListAsyncTaskResult {

    protected ProjectActivityModel[] mProjectActivityModels;
    protected String mMessage;

    public ManagerProjectActivityListAsyncTaskResult() {

    }

    public void setProjectActivityModels(final ProjectActivityModel[] projectPlanModels) {
        mProjectActivityModels = projectPlanModels;
    }

    public ProjectActivityModel[] getProjectActivityModels() {
        return mProjectActivityModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}