package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;

public class InspectorProjectActivityMonitoringListAsyncTaskResult {
    protected ProjectActivityModel mProjectActivityModel;
    protected ProjectActivityMonitoringModel[] mProjectActivityMonitoringModels;
    protected String mMessage;

    public InspectorProjectActivityMonitoringListAsyncTaskResult() {

    }

    public void setProjectActivityModel(final ProjectActivityModel projectPlanModels) {
        mProjectActivityModel = projectPlanModels;
    }

    public ProjectActivityModel getProjectActivityModel() {
        return mProjectActivityModel;
    }

    public void setProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
        mProjectActivityMonitoringModels = projectActivityMonitoringModels;
    }

    public ProjectActivityMonitoringModel[] getProjectActivityMonitoringModels() {
        return mProjectActivityMonitoringModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
