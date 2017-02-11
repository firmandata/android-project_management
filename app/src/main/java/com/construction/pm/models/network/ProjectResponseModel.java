package com.construction.pm.models.network;

import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;

import org.json.JSONException;

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

    public static ProjectResponseModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectResponseModel projectResponseModel = new ProjectResponseModel();

        if (!jsonObject.isNull("project"))
            projectResponseModel.setProjectModel(ProjectModel.build(jsonObject.getJSONObject("project")));
        if (!jsonObject.isNull("contract"))
            projectResponseModel.setContractModel(ContractModel.build(jsonObject.getJSONObject("contract")));
        if (!jsonObject.isNull("projectStage")) {
            org.json.JSONArray jsonResultProjectStages = jsonObject.getJSONArray("projectStage");
            for (int resultProjectStageIdx = 0; resultProjectStageIdx < jsonResultProjectStages.length(); resultProjectStageIdx++) {
                org.json.JSONObject jsonResultProjectStage = jsonResultProjectStages.getJSONObject(resultProjectStageIdx);
                projectResponseModel.addProjectStageModel(ProjectStageModel.build(jsonResultProjectStage));
            }
        }
        if (!jsonObject.isNull("projectPlan")) {
            org.json.JSONArray jsonResultProjectPlans = jsonObject.getJSONArray("projectPlan");
            for (int resultProjectPlanIdx = 0; resultProjectPlanIdx < jsonResultProjectPlans.length(); resultProjectPlanIdx++) {
                org.json.JSONObject jsonResultProjectPlan = jsonResultProjectPlans.getJSONObject(resultProjectPlanIdx);
                projectResponseModel.addProjectPlanModel(ProjectPlanModel.build(jsonResultProjectPlan));
            }
        }

        return projectResponseModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectModel() != null)
            jsonObject.put("project", getProjectModel().build());
        if (getContractModel() != null)
            jsonObject.put("contract", getContractModel().build());
        if (getProjectStageModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageModel projectStageModel : getProjectStageModels()) {
                jsonArray.put(projectStageModel.build());
            }
            jsonObject.put("projectStage", jsonArray);
        }
        if (getProjectPlanModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectPlanModel projectPlanModel : getProjectPlanModels()) {
                jsonArray.put(projectPlanModel.build());
            }
            jsonObject.put("projectPlan", jsonArray);
        }

        return jsonObject;
    }
}
