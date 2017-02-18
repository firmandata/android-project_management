package com.construction.pm.models.network;

import com.construction.pm.models.ProjectStageAssignmentCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectStageResponseModel {
    protected ProjectStageModel mProjectStageModel;
    protected List<ProjectStageAssignmentModel> mProjectStageAssignmentModelList;
    protected List<ProjectStageAssignmentCommentModel> mProjectStageAssignmentCommentModelList;

    public ProjectStageResponseModel() {
        mProjectStageAssignmentModelList = new ArrayList<ProjectStageAssignmentModel>();
        mProjectStageAssignmentCommentModelList = new ArrayList<ProjectStageAssignmentCommentModel>();
    }

    public void setProjectStageModel(final ProjectStageModel projectStageModel) {
        mProjectStageModel = projectStageModel;
    }

    public ProjectStageModel getProjectStageModel() {
        return mProjectStageModel;
    }

    public void setProjectStageAssignmentModels(ProjectStageAssignmentModel[] projectStageAssignmentModels) {
        mProjectStageAssignmentModelList = new ArrayList<ProjectStageAssignmentModel>(Arrays.asList(projectStageAssignmentModels));
    }

    public void addProjectStageAssignmentModel(ProjectStageAssignmentModel projectStageAssignmentModel) {
        mProjectStageAssignmentModelList.add(projectStageAssignmentModel);
    }

    public void removeProjectStageAssignmentModel(ProjectStageAssignmentModel projectStageAssignmentModel) {
        mProjectStageAssignmentModelList.remove(projectStageAssignmentModel);
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        if (mProjectStageAssignmentModelList.size() == 0)
            return null;

        ProjectStageAssignmentModel[] projectStageAssignmentModels = new ProjectStageAssignmentModel[mProjectStageAssignmentModelList.size()];
        mProjectStageAssignmentModelList.toArray(projectStageAssignmentModels);
        return projectStageAssignmentModels;
    }

    public void setProjectStageAssignmentCommentModels(ProjectStageAssignmentCommentModel[] projectStageAssignmentCommentModels) {
        mProjectStageAssignmentCommentModelList = new ArrayList<ProjectStageAssignmentCommentModel>(Arrays.asList(projectStageAssignmentCommentModels));
    }

    public void addProjectStageAssignmentCommentModel(ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel) {
        mProjectStageAssignmentCommentModelList.add(projectStageAssignmentCommentModel);
    }

    public void removeProjectStageAssignmentCommentModel(ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel) {
        mProjectStageAssignmentCommentModelList.remove(projectStageAssignmentCommentModel);
    }

    public ProjectStageAssignmentCommentModel[] getProjectStageAssignmentCommentModels() {
        if (mProjectStageAssignmentCommentModelList.size() == 0)
            return null;

        ProjectStageAssignmentCommentModel[] projectStageAssignmentCommentModels = new ProjectStageAssignmentCommentModel[mProjectStageAssignmentCommentModelList.size()];
        mProjectStageAssignmentCommentModelList.toArray(projectStageAssignmentCommentModels);
        return projectStageAssignmentCommentModels;
    }

    public static ProjectStageResponseModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageResponseModel projectStageResponseModel = new ProjectStageResponseModel();

        if (!jsonObject.isNull("projectStage"))
            projectStageResponseModel.setProjectStageModel(ProjectStageModel.build(jsonObject.getJSONObject("projectStage")));
        if (!jsonObject.isNull("projectStageAssignment")) {
            org.json.JSONArray jsonResultProjectStageAssignments = jsonObject.getJSONArray("projectStageAssignment");
            for (int resultProjectStageAssignmentIdx = 0; resultProjectStageAssignmentIdx < jsonResultProjectStageAssignments.length(); resultProjectStageAssignmentIdx++) {
                org.json.JSONObject jsonResultProjectStageAssignment = jsonResultProjectStageAssignments.getJSONObject(resultProjectStageAssignmentIdx);
                projectStageResponseModel.addProjectStageAssignmentModel(ProjectStageAssignmentModel.build(jsonResultProjectStageAssignment));
            }
        }
        if (!jsonObject.isNull("projectStageAssignmentComment")) {
            org.json.JSONArray jsonResultProjectStageAssignmentComments = jsonObject.getJSONArray("projectStageAssignmentComment");
            for (int resultProjectStageAssignmentCommentIdx = 0; resultProjectStageAssignmentCommentIdx < jsonResultProjectStageAssignmentComments.length(); resultProjectStageAssignmentCommentIdx++) {
                org.json.JSONObject jsonResultProjectStageAssignmentComment = jsonResultProjectStageAssignmentComments.getJSONObject(resultProjectStageAssignmentCommentIdx);
                projectStageResponseModel.addProjectStageAssignmentCommentModel(ProjectStageAssignmentCommentModel.build(jsonResultProjectStageAssignmentComment));
            }
        }

        return projectStageResponseModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectStageModel() != null)
            jsonObject.put("projectStage", getProjectStageModel().build());
        if (getProjectStageAssignmentModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageAssignmentModel projectStageAssignmentModel : getProjectStageAssignmentModels()) {
                jsonArray.put(projectStageAssignmentModel.build());
            }
            jsonObject.put("projectStageAssignment", jsonArray);
        }
        if (getProjectStageAssignmentCommentModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel : getProjectStageAssignmentCommentModels()) {
                jsonArray.put(projectStageAssignmentCommentModel.build());
            }
            jsonObject.put("projectStageAssignmentComment", jsonArray);
        }

        return jsonObject;
    }
}
