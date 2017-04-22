package com.construction.pm.models.network;

import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectStageResponseModel {
    protected ProjectStageModel mProjectStageModel;
    protected List<ProjectStageAssignmentModel> mProjectStageAssignmentModelList;
    protected List<ProjectStageDocumentModel> mProjectStageDocumentModelList;
    protected List<ProjectStageAssignCommentModel> mProjectStageAssignCommentModelList;

    public ProjectStageResponseModel() {
        mProjectStageAssignmentModelList = new ArrayList<ProjectStageAssignmentModel>();
        mProjectStageDocumentModelList = new ArrayList<ProjectStageDocumentModel>();
        mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();
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

    public void setProjectStageDocumentModels(ProjectStageDocumentModel[] projectStageDocumentModels) {
        mProjectStageDocumentModelList = new ArrayList<ProjectStageDocumentModel>(Arrays.asList(projectStageDocumentModels));
    }

    public void addProjectStageDocumentModel(ProjectStageDocumentModel projectStageDocumentModel) {
        mProjectStageDocumentModelList.add(projectStageDocumentModel);
    }

    public void removeProjectStageDocumentModel(ProjectStageDocumentModel projectStageDocumentModel) {
        mProjectStageDocumentModelList.remove(projectStageDocumentModel);
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        if (mProjectStageDocumentModelList.size() == 0)
            return null;

        ProjectStageDocumentModel[] projectStageDocumentModels = new ProjectStageDocumentModel[mProjectStageDocumentModelList.size()];
        mProjectStageDocumentModelList.toArray(projectStageDocumentModels);
        return projectStageDocumentModels;
    }

    public void setProjectStageAssignCommentModels(ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>(Arrays.asList(projectStageAssignCommentModels));
    }

    public void addProjectStageAssignCommentModel(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentModelList.add(projectStageAssignCommentModel);
    }

    public void removeProjectStageAssignCommentModel(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentModelList.remove(projectStageAssignCommentModel);
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
        if (mProjectStageAssignCommentModelList.size() == 0)
            return null;

        ProjectStageAssignCommentModel[] projectStageAssignCommentModels = new ProjectStageAssignCommentModel[mProjectStageAssignCommentModelList.size()];
        mProjectStageAssignCommentModelList.toArray(projectStageAssignCommentModels);
        return projectStageAssignCommentModels;
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
        if (!jsonObject.isNull("projectStageDocument")) {
            org.json.JSONArray jsonResultProjectStageDocuments = jsonObject.getJSONArray("projectStageDocument");
            for (int resultProjectStageDocumentIdx = 0; resultProjectStageDocumentIdx < jsonResultProjectStageDocuments.length(); resultProjectStageDocumentIdx++) {
                org.json.JSONObject jsonResultProjectStageDocument = jsonResultProjectStageDocuments.getJSONObject(resultProjectStageDocumentIdx);
                projectStageResponseModel.addProjectStageDocumentModel(ProjectStageDocumentModel.build(jsonResultProjectStageDocument));
            }
        }
        if (!jsonObject.isNull("projectStageAssignmentComment")) {
            org.json.JSONArray jsonResultProjectStageAssignComments = jsonObject.getJSONArray("projectStageAssignmentComment");
            for (int resultProjectStageAssignCommentIdx = 0; resultProjectStageAssignCommentIdx < jsonResultProjectStageAssignComments.length(); resultProjectStageAssignCommentIdx++) {
                org.json.JSONObject jsonResultProjectStageAssignComment = jsonResultProjectStageAssignComments.getJSONObject(resultProjectStageAssignCommentIdx);
                projectStageResponseModel.addProjectStageAssignCommentModel(ProjectStageAssignCommentModel.build(jsonResultProjectStageAssignComment));
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
        if (getProjectStageDocumentModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageDocumentModel projectStageDocumentModel : getProjectStageDocumentModels()) {
                jsonArray.put(projectStageDocumentModel.build());
            }
            jsonObject.put("projectStageDocument", jsonArray);
        }
        if (getProjectStageAssignCommentModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageAssignCommentModel projectStageAssignCommentModel : getProjectStageAssignCommentModels()) {
                jsonArray.put(projectStageAssignCommentModel.build());
            }
            jsonObject.put("projectStageAssignmentComment", jsonArray);
        }

        return jsonObject;
    }
}
