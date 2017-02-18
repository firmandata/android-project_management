package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectPlanAssignmentModel;
import com.construction.pm.models.ProjectPlanModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectPlanResponseModel {
    protected ProjectPlanModel mProjectPlanModel;
    protected List<ProjectPlanAssignmentModel> mProjectPlanAssignmentModelList;
    protected List<ProjectActivityUpdateModel> mProjectActivityUpdateModelList;

    public ProjectPlanResponseModel() {
        mProjectPlanAssignmentModelList = new ArrayList<ProjectPlanAssignmentModel>();
        mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>();
    }

    public void setProjectPlanModel(final ProjectPlanModel projectPlanModel) {
        mProjectPlanModel = projectPlanModel;
    }

    public ProjectPlanModel getProjectPlanModel() {
        return mProjectPlanModel;
    }

    public void setProjectPlanAssignmentModels(ProjectPlanAssignmentModel[] projectPlanAssignmentModels) {
        mProjectPlanAssignmentModelList = new ArrayList<ProjectPlanAssignmentModel>(Arrays.asList(projectPlanAssignmentModels));
    }

    public void addProjectPlanAssignmentModel(ProjectPlanAssignmentModel projectPlanAssignmentModel) {
        mProjectPlanAssignmentModelList.add(projectPlanAssignmentModel);
    }

    public void removeProjectPlanAssignmentModel(ProjectPlanAssignmentModel projectPlanAssignmentModel) {
        mProjectPlanAssignmentModelList.remove(projectPlanAssignmentModel);
    }

    public ProjectPlanAssignmentModel[] getProjectPlanAssignmentModels() {
        if (mProjectPlanAssignmentModelList.size() == 0)
            return null;

        ProjectPlanAssignmentModel[] projectPlanAssignmentModels = new ProjectPlanAssignmentModel[mProjectPlanAssignmentModelList.size()];
        mProjectPlanAssignmentModelList.toArray(projectPlanAssignmentModels);
        return projectPlanAssignmentModels;
    }

    public void setProjectActivityUpdateModels(ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>(Arrays.asList(projectActivityUpdateModels));
    }

    public void addProjectActivityUpdateModel(ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateModelList.add(projectActivityUpdateModel);
    }

    public void removeProjectActivityUpdateModel(ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateModelList.remove(projectActivityUpdateModel);
    }

    public ProjectActivityUpdateModel[] getProjectActivityUpdateModels() {
        if (mProjectActivityUpdateModelList.size() == 0)
            return null;

        ProjectActivityUpdateModel[] projectActivityUpdateModels = new ProjectActivityUpdateModel[mProjectActivityUpdateModelList.size()];
        mProjectActivityUpdateModelList.toArray(projectActivityUpdateModels);
        return projectActivityUpdateModels;
    }

    public static ProjectPlanResponseModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectPlanResponseModel projectPlanResponseModel = new ProjectPlanResponseModel();

        if (!jsonObject.isNull("projectPlan"))
            projectPlanResponseModel.setProjectPlanModel(ProjectPlanModel.build(jsonObject.getJSONObject("projectPlan")));
        if (!jsonObject.isNull("projectPlanAssignment")) {
            org.json.JSONArray jsonResultProjectPlanAssignments = jsonObject.getJSONArray("projectPlanAssignment");
            for (int resultProjectPlanAssignmentIdx = 0; resultProjectPlanAssignmentIdx < jsonResultProjectPlanAssignments.length(); resultProjectPlanAssignmentIdx++) {
                org.json.JSONObject jsonResultProjectPlanAssignment = jsonResultProjectPlanAssignments.getJSONObject(resultProjectPlanAssignmentIdx);
                projectPlanResponseModel.addProjectPlanAssignmentModel(ProjectPlanAssignmentModel.build(jsonResultProjectPlanAssignment));
            }
        }
        if (!jsonObject.isNull("projectPlanActivityUpdateByPlan")) {
            org.json.JSONArray jsonResultProjectActivityUpdates = jsonObject.getJSONArray("projectPlanActivityUpdateByPlan");
            for (int resultProjectActivityUpdateIdx = 0; resultProjectActivityUpdateIdx < jsonResultProjectActivityUpdates.length(); resultProjectActivityUpdateIdx++) {
                org.json.JSONObject jsonResultProjectActivityUpdate = jsonResultProjectActivityUpdates.getJSONObject(resultProjectActivityUpdateIdx);
                projectPlanResponseModel.addProjectActivityUpdateModel(ProjectActivityUpdateModel.build(jsonResultProjectActivityUpdate));
            }
        }

        return projectPlanResponseModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectPlanModel() != null)
            jsonObject.put("projectPlan", getProjectPlanModel().build());
        if (getProjectPlanAssignmentModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectPlanAssignmentModel projectPlanAssignmentModel : getProjectPlanAssignmentModels()) {
                jsonArray.put(projectPlanAssignmentModel.build());
            }
            jsonObject.put("projectPlanAssignment", jsonArray);
        }
        if (getProjectActivityUpdateModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityUpdateModel projectActivityUpdateModel : getProjectActivityUpdateModels()) {
                jsonArray.put(projectActivityUpdateModel.build());
            }
            jsonObject.put("projectPlanActivityUpdateByPlan", jsonArray);
        }

        return jsonObject;
    }
}
