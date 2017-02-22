package com.construction.pm.models.network;

import com.construction.pm.models.ProjectStageAssignCommentModel;

import org.json.JSONException;

public class ProjectStageAssignCommentResponseModel extends SimpleResponseModel {
    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;

    public ProjectStageAssignCommentResponseModel() {
        super();
    }

    public ProjectStageAssignCommentResponseModel(final Integer code, final String message, final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        super(code, message);
        mProjectStageAssignCommentModel = projectStageAssignCommentModel;
    }

    public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentModel = projectStageAssignCommentModel;
    }

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        return mProjectStageAssignCommentModel;
    }

    public static ProjectStageAssignCommentResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageAssignCommentResponseModel projectStageAssignCommentResponseModel = new ProjectStageAssignCommentResponseModel();

        if (!jsonObject.isNull("responseCode"))
            projectStageAssignCommentResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            projectStageAssignCommentResponseModel.setMessage(jsonObject.getString("responseMessage"));
        if (!jsonObject.isNull("result")) {
            org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
            if (!jsonResultObject.isNull("projectStageAssignmentComment")) {
                org.json.JSONArray projectStageAssignCommentArray = jsonObject.getJSONArray("projectStageAssignmentComment");
                for (int projectStageAssignCommentIdx = 0; projectStageAssignCommentIdx < projectStageAssignCommentArray.length(); projectStageAssignCommentIdx++) {
                    ProjectStageAssignCommentModel projectStageAssignCommentModel = ProjectStageAssignCommentModel.build(projectStageAssignCommentArray.getJSONObject(projectStageAssignCommentIdx));
                    projectStageAssignCommentResponseModel.setProjectStageAssignCommentModel(projectStageAssignCommentModel);
                }
            }
        }

        return projectStageAssignCommentResponseModel;
    }

    @Override
    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());
        if (getProjectStageAssignCommentModel() != null) {
            org.json.JSONArray projectStageAssignCommentArray = new org.json.JSONArray();
            projectStageAssignCommentArray.put(getProjectStageAssignCommentModel().build());

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("projectStageAssignmentComment", projectStageAssignCommentArray);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
