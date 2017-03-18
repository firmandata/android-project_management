package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityUpdateModel;

public class ManagerProjectActivityUpdateSaveAsyncTaskResult {
    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;
    protected String mMessage;

    public ManagerProjectActivityUpdateSaveAsyncTaskResult() {

    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateModel = projectActivityUpdateModel;
    }

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        return mProjectActivityUpdateModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
