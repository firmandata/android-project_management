package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectPlanAssignmentModel;
import com.construction.pm.models.ProjectPlanModel;

public class ProjectPlanGetAsyncTaskResult {

    protected ProjectPlanModel mProjectPlanModel;
    protected ProjectPlanAssignmentModel[] mProjectPlanAssignmentModels;
    protected ProjectActivityUpdateModel[] mProjectActivityUpdateModels;
    protected String mMessage;

    public ProjectPlanGetAsyncTaskResult() {

    }

    public void setProjectPlanModel(final ProjectPlanModel projectPlanModel) {
        mProjectPlanModel = projectPlanModel;
    }

    public ProjectPlanModel getProjectPlanModel() {
        return mProjectPlanModel;
    }

    public void setProjectPlanAssignmentModels(final ProjectPlanAssignmentModel[] projectPlanModels) {
        mProjectPlanAssignmentModels = projectPlanModels;
    }

    public ProjectPlanAssignmentModel[] getProjectPlanAssignmentModels() {
        return mProjectPlanAssignmentModels;
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