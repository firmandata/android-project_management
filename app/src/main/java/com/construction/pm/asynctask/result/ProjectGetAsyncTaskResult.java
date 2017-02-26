package com.construction.pm.asynctask.result;

import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;

public class ProjectGetAsyncTaskResult {

    protected ContractModel mContractModel;
    protected ProjectModel mProjectModel;
    protected ProjectStageModel[] mProjectStageModels;
    protected ProjectPlanModel[] mProjectPlanModels;
    protected String mMessage;

    public ProjectGetAsyncTaskResult() {

    }

    public void setContractModel(final ContractModel contractModel) {
        mContractModel = contractModel;
    }

    public ContractModel getContractModel() {
        return mContractModel;
    }

    public void setProjectModel(final ProjectModel projectModel) {
        mProjectModel = projectModel;
    }

    public ProjectModel getProjectModel() {
        return mProjectModel;
    }

    public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
        mProjectStageModels = projectStageModels;
    }

    public ProjectStageModel[] getProjectStageModels() {
        return mProjectStageModels;
    }

    public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
        mProjectPlanModels = projectPlanModels;
    }

    public ProjectPlanModel[] getProjectPlanModels() {
        return mProjectPlanModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}