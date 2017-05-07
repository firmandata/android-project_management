package com.construction.pm.models.network;

import com.construction.pm.models.ProjectActivityDashboardModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectActivityDashboardResponseModel {

    protected List<ProjectActivityDashboardModel> mProjectActivityDashboardModelList;
    protected List<ProjectActivityDashboardModel> mProjectActivityMonitoringDashboardModelList;

    public ProjectActivityDashboardResponseModel() {
        mProjectActivityDashboardModelList = new ArrayList<ProjectActivityDashboardModel>();
        mProjectActivityMonitoringDashboardModelList = new ArrayList<ProjectActivityDashboardModel>();
    }

    public void setProjectActivityDashboardModels(ProjectActivityDashboardModel[] projectActivityDashboardModels) {
        mProjectActivityDashboardModelList = new ArrayList<ProjectActivityDashboardModel>(Arrays.asList(projectActivityDashboardModels));
    }

    public void addProjectActivityDashboardModel(ProjectActivityDashboardModel projectActivityDashboardModel) {
        mProjectActivityDashboardModelList.add(projectActivityDashboardModel);
    }

    public void removeProjectActivityDashboardModel(ProjectActivityDashboardModel projectActivityDashboardModel) {
        mProjectActivityDashboardModelList.remove(projectActivityDashboardModel);
    }

    public ProjectActivityDashboardModel[] getProjectActivityDashboardModels() {
        if (mProjectActivityDashboardModelList.size() == 0)
            return null;

        ProjectActivityDashboardModel[] projectActivityDashboardModels = new ProjectActivityDashboardModel[mProjectActivityDashboardModelList.size()];
        mProjectActivityDashboardModelList.toArray(projectActivityDashboardModels);
        return projectActivityDashboardModels;
    }

    public void setProjectActivityMonitoringDashboardModels(ProjectActivityDashboardModel[] projectActivityDashboardModels) {
        mProjectActivityMonitoringDashboardModelList = new ArrayList<ProjectActivityDashboardModel>(Arrays.asList(projectActivityDashboardModels));
    }

    public void addProjectActivityMonitoringDashboardModel(ProjectActivityDashboardModel projectActivityDashboardModel) {
        mProjectActivityMonitoringDashboardModelList.add(projectActivityDashboardModel);
    }

    public void removeProjectActivityMonitoringDashboardModel(ProjectActivityDashboardModel projectActivityDashboardModel) {
        mProjectActivityMonitoringDashboardModelList.remove(projectActivityDashboardModel);
    }

    public ProjectActivityDashboardModel[] getProjectActivityMonitoringDashboardModels() {
        if (mProjectActivityMonitoringDashboardModelList.size() == 0)
            return null;

        ProjectActivityDashboardModel[] projectActivityDashboardModels = new ProjectActivityDashboardModel[mProjectActivityMonitoringDashboardModelList.size()];
        mProjectActivityMonitoringDashboardModelList.toArray(projectActivityDashboardModels);
        return projectActivityDashboardModels;
    }

    public static ProjectActivityDashboardResponseModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityDashboardResponseModel projectResponseModel = new ProjectActivityDashboardResponseModel();

        if (!jsonObject.isNull("projectActivityDashboard")) {
            org.json.JSONArray jsonResultProjectActivityDashboards = jsonObject.getJSONArray("projectActivityDashboard");
            for (int jsonResultProjectActivityDashboardIdx = 0; jsonResultProjectActivityDashboardIdx < jsonResultProjectActivityDashboards.length(); jsonResultProjectActivityDashboardIdx++) {
                org.json.JSONObject jsonResultProjectActivityDashboard = jsonResultProjectActivityDashboards.getJSONObject(jsonResultProjectActivityDashboardIdx);
                projectResponseModel.addProjectActivityDashboardModel(ProjectActivityDashboardModel.build(jsonResultProjectActivityDashboard));
            }
        }
        if (!jsonObject.isNull("projectActivityMonitoringDashboard")) {
            org.json.JSONArray jsonResultProjectActivityMonitoringDashboards = jsonObject.getJSONArray("projectActivityMonitoringDashboard");
            for (int jsonResultProjectActivityMonitoringDashboardIdx = 0; jsonResultProjectActivityMonitoringDashboardIdx < jsonResultProjectActivityMonitoringDashboards.length(); jsonResultProjectActivityMonitoringDashboardIdx++) {
                org.json.JSONObject jsonResultProjectActivityMonitoringDashboard = jsonResultProjectActivityMonitoringDashboards.getJSONObject(jsonResultProjectActivityMonitoringDashboardIdx);
                projectResponseModel.addProjectActivityMonitoringDashboardModel(ProjectActivityDashboardModel.build(jsonResultProjectActivityMonitoringDashboard));
            }
        }

        return projectResponseModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectActivityDashboardModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityDashboardModel projectActivityDashboardModel : getProjectActivityDashboardModels()) {
                jsonArray.put(projectActivityDashboardModel.build());
            }
            jsonObject.put("projectActivityDashboard", jsonArray);
        }
        if (getProjectActivityMonitoringDashboardModels() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityDashboardModel projectActivityDashboardModel : getProjectActivityMonitoringDashboardModels()) {
                jsonArray.put(projectActivityDashboardModel.build());
            }
            jsonObject.put("projectActivityMonitoringDashboard", jsonArray);
        }

        return jsonObject;
    }
}
