package com.construction.pm.models.network;

import com.construction.pm.models.ProjectStageAssignmentCommentModel;

import org.json.JSONException;

public class ProjectStageAssignmentCommentResponseModel extends SimpleResponseModel {
    protected ProjectStageAssignmentCommentModel mProjectStageAssignmentCommentModel;

    public ProjectStageAssignmentCommentResponseModel() {
        super();
    }

    public ProjectStageAssignmentCommentResponseModel(final Integer code, final String message, final ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel) {
        super(code, message);
        mProjectStageAssignmentCommentModel = projectStageAssignmentCommentModel;
    }

    public void setProjectStageAssignmentCommentModel(final ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel) {
        mProjectStageAssignmentCommentModel = projectStageAssignmentCommentModel;
    }

    public ProjectStageAssignmentCommentModel getProjectStageAssignmentCommentModel() {
        return mProjectStageAssignmentCommentModel;
    }

    public static ProjectStageAssignmentCommentResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageAssignmentCommentResponseModel projectStageAssignmentCommentResponseModel = new ProjectStageAssignmentCommentResponseModel();

        if (!jsonObject.isNull("responseCode"))
            projectStageAssignmentCommentResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            projectStageAssignmentCommentResponseModel.setMessage(jsonObject.getString("responseMessage"));
        if (!jsonObject.isNull("result")) {
            org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
            if (!jsonResultObject.isNull("projectStageAssignmentComment")) {
                org.json.JSONArray projectStageAssignmentCommentArray = jsonObject.getJSONArray("projectStageAssignmentComment");
                for (int projectStageAssignmentCommentIdx = 0; projectStageAssignmentCommentIdx < projectStageAssignmentCommentArray.length(); projectStageAssignmentCommentIdx++) {
                    ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel = ProjectStageAssignmentCommentModel.build(projectStageAssignmentCommentArray.getJSONObject(projectStageAssignmentCommentIdx));
                    projectStageAssignmentCommentResponseModel.setProjectStageAssignmentCommentModel(projectStageAssignmentCommentModel);
                }
            }
        }

        return projectStageAssignmentCommentResponseModel;
    }

    @Override
    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());
        if (getProjectStageAssignmentCommentModel() != null) {
            org.json.JSONArray projectStageAssignmentCommentArray = new org.json.JSONArray();
            projectStageAssignmentCommentArray.put(getProjectStageAssignmentCommentModel().build());

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("projectStageAssignmentComment", projectStageAssignmentCommentArray);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
