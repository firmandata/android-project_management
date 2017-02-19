package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityUpdateModel;

import org.json.JSONException;

public class ProjectActivityUpdateResponseModel extends SimpleResponseModel {

    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;

    public ProjectActivityUpdateResponseModel() {
        super();
    }

    public ProjectActivityUpdateResponseModel(final Integer code, final String message, final ProjectActivityUpdateModel projectActivityUpdateModel) {
        super(code, message);
        mProjectActivityUpdateModel = projectActivityUpdateModel;
    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateModel = projectActivityUpdateModel;
    }

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        return mProjectActivityUpdateModel;
    }

    public static ProjectActivityUpdateResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityUpdateResponseModel projectActivityUpdateResponseModel = new ProjectActivityUpdateResponseModel();

        if (!jsonObject.isNull("responseCode"))
            projectActivityUpdateResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            projectActivityUpdateResponseModel.setMessage(jsonObject.getString("responseMessage"));
        if (!jsonObject.isNull("result")) {
            org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
            if (!jsonResultObject.isNull("projectActivityUpdate")) {
                org.json.JSONArray projectActivityUpdateArray = jsonObject.getJSONArray("projectActivityUpdate");
                for (int projectActivityUpdateIdx = 0; projectActivityUpdateIdx < projectActivityUpdateArray.length(); projectActivityUpdateIdx++) {
                    ProjectActivityUpdateModel projectActivityUpdateModel = ProjectActivityUpdateModel.build(projectActivityUpdateArray.getJSONObject(projectActivityUpdateIdx));
                    projectActivityUpdateResponseModel.setProjectActivityUpdateModel(projectActivityUpdateModel);
                }
            }
        }

        return projectActivityUpdateResponseModel;
    }

    @Override
    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());
        if (getProjectActivityUpdateModel() != null) {
            org.json.JSONArray projectActivityUpdateArray = new org.json.JSONArray();
            projectActivityUpdateArray.put(getProjectActivityUpdateModel().build());

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("projectActivityUpdate", projectActivityUpdateArray);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
