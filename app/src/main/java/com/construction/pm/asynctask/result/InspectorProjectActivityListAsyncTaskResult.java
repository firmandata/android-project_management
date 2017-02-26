package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityModel;

public class InspectorProjectActivityListAsyncTaskResult {

    protected ProjectActivityModel[] mProjectActivityModels;
    protected String mMessage;

    public InspectorProjectActivityListAsyncTaskResult() {

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