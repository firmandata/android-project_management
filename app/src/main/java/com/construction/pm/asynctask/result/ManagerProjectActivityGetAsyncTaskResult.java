package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityModel;

public class ManagerProjectActivityGetAsyncTaskResult {
    protected ProjectActivityModel mProjectActivityModel;
    protected String mMessage;

    public ManagerProjectActivityGetAsyncTaskResult() {

    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mProjectActivityModel = projectActivityModel;
    }

    public ProjectActivityModel getProjectActivityModel() {
        return mProjectActivityModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
