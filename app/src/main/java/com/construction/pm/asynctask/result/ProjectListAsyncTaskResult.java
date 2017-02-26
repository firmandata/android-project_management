package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectModel;

public class ProjectListAsyncTaskResult {

    protected ProjectModel[] mProjectModels;
    protected String mMessage;

    public ProjectListAsyncTaskResult() {

    }

    public void setProjectModels(final ProjectModel[] projectModels) {
        mProjectModels = projectModels;
    }

    public ProjectModel[] getProjectModels() {
        return mProjectModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}