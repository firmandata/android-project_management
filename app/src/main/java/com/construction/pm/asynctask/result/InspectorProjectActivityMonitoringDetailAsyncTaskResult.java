package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityMonitoringModel;

public class InspectorProjectActivityMonitoringDetailAsyncTaskResult {
    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;
    protected String mMessage;

    public InspectorProjectActivityMonitoringDetailAsyncTaskResult() {

    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringModel = projectActivityMonitoringModel;
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        return mProjectActivityMonitoringModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
