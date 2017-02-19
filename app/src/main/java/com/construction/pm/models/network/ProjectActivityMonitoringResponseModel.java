package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityMonitoringModel;

import org.json.JSONException;

public class ProjectActivityMonitoringResponseModel extends SimpleResponseModel {
    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;

    public ProjectActivityMonitoringResponseModel() {
        super();
    }

    public ProjectActivityMonitoringResponseModel(final Integer code, final String message, final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        super(code, message);
        mProjectActivityMonitoringModel = projectActivityMonitoringModel;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringModel = projectActivityMonitoringModel;
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        return mProjectActivityMonitoringModel;
    }

    public static ProjectActivityMonitoringResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityMonitoringResponseModel projectActivityMonitoringResponseModel = new ProjectActivityMonitoringResponseModel();

        if (!jsonObject.isNull("responseCode"))
            projectActivityMonitoringResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            projectActivityMonitoringResponseModel.setMessage(jsonObject.getString("responseMessage"));
        if (!jsonObject.isNull("result")) {
            org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
            if (!jsonResultObject.isNull("projectActivityMonitoring")) {
                org.json.JSONArray projectActivityMonitoringArray = jsonObject.getJSONArray("projectActivityMonitoring");
                for (int projectActivityMonitoringIdx = 0; projectActivityMonitoringIdx < projectActivityMonitoringArray.length(); projectActivityMonitoringIdx++) {
                    ProjectActivityMonitoringModel projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(projectActivityMonitoringArray.getJSONObject(projectActivityMonitoringIdx));
                    projectActivityMonitoringResponseModel.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
                }
            }
        }

        return projectActivityMonitoringResponseModel;
    }

    @Override
    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());
        if (getProjectActivityMonitoringModel() != null) {
            org.json.JSONArray projectActivityMonitoringArray = new org.json.JSONArray();
            projectActivityMonitoringArray.put(getProjectActivityMonitoringModel().build());

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("projectActivityMonitoring", projectActivityMonitoringArray);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
