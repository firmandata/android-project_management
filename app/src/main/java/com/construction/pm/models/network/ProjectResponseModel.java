package com.construction.pm.models.network;

import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectResponseModel {

    protected ProjectModel mProjectModel;
    protected ContractModel mContractModel;
    protected List<ProjectStageModel> mProjectStageModelList;
    protected List<ProjectPlanModel> mProjectPlanModelList;

    public ProjectResponseModel() {
    }

    public void setProjectModel(final ProjectModel projectModel) {
        mProjectModel = projectModel;
    }

    public ProjectModel getProjectModel() {
        return mProjectModel;
    }

    public void setContractModel(final ContractModel contractModel) {
        mContractModel = contractModel;
    }

    public ContractModel getContractModel() {
        return mContractModel;
    }

    public void setProjectStageModels(ProjectStageModel[] projectStageModels) {
        mProjectStageModelList = new ArrayList<ProjectStageModel>(Arrays.asList(projectStageModels));
    }

    public void addProjectStageModel(ProjectStageModel projectStageModel) {
        if (mProjectStageModelList == null)
            mProjectStageModelList = new ArrayList<ProjectStageModel>();

        mProjectStageModelList.add(projectStageModel);
    }

    public void removeProjectStageModel(ProjectStageModel projectStageModel) {
        if (mProjectStageModelList == null)
            return;

        mProjectStageModelList.remove(projectStageModel);
    }

    public ProjectStageModel[] getProjectStageModels() {
        if (mProjectStageModelList == null)
            return null;
        if (mProjectStageModelList.size() == 0)
            return null;

        ProjectStageModel[] projectStageModels = new ProjectStageModel[mProjectStageModelList.size()];
        mProjectStageModelList.toArray(projectStageModels);
        return projectStageModels;
    }

    public void setProjectPlanModels(ProjectPlanModel[] projectPlanModels) {
        mProjectPlanModelList = new ArrayList<ProjectPlanModel>(Arrays.asList(projectPlanModels));
    }

    public void addProjectPlanModel(ProjectPlanModel projectPlanModel) {
        if (mProjectPlanModelList == null)
            mProjectPlanModelList = new ArrayList<ProjectPlanModel>();

        mProjectPlanModelList.add(projectPlanModel);
    }

    public void removeProjectPlanModel(ProjectPlanModel projectPlanModel) {
        if (mProjectPlanModelList == null)
            return;

        mProjectPlanModelList.remove(projectPlanModel);
    }

    public ProjectPlanModel[] getProjectPlanModels() {
        if (mProjectPlanModelList == null)
            return null;
        if (mProjectPlanModelList.size() == 0)
            return null;

        ProjectPlanModel[] projectPlanModels = new ProjectPlanModel[mProjectPlanModelList.size()];
        mProjectPlanModelList.toArray(projectPlanModels);
        return projectPlanModels;
    }
}
