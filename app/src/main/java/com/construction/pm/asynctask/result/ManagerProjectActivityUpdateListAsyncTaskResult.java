package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityUpdateModel;

public class ManagerProjectActivityUpdateListAsyncTaskResult {
    protected ProjectActivityUpdateModel[] mProjectActivityUpdateModels;
    protected String mMessage;

    public ManagerProjectActivityUpdateListAsyncTaskResult() {

    }

    public void setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectActivityUpdateModels = projectActivityUpdateModels;
    }

    public ProjectActivityUpdateModel[] getProjectActivityUpdateModels() {
        return mProjectActivityUpdateModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
