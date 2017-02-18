package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityMonitoringModel;

public class ProjectActivityMonitoringResponseModel extends SimpleResponseModel {
    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;

    public ProjectActivityMonitoringResponseModel() {
        super();
    }

    public ProjectActivityMonitoringResponseModel(final Integer code, final String message, final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        super(code, message);
        mProjectActivityMonitoringModel = projectActivityMonitoringModel;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringModel = projectActivityMonitoringModel;
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        return mProjectActivityMonitoringModel;
    }
}
