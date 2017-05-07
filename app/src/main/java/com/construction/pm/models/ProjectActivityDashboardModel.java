package com.construction.pm.models;

import org.json.JSONException;

public class ProjectActivityDashboardModel {
    protected Integer mProjectMemberId;
    protected StatusTaskEnum mStatusTask;
    protected Integer mTotalTask;

    public ProjectActivityDashboardModel() {

    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setStatusTask(final StatusTaskEnum statusTask) {
        mStatusTask = statusTask;
    }

    public StatusTaskEnum getStatusTask() {
        return mStatusTask;
    }

    public void setTotalTask(final Integer totalTask) {
        mTotalTask = totalTask;
    }

    public Integer getTotalTask() {
        return mTotalTask;
    }

    public static ProjectActivityDashboardModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityDashboardModel projectActivityDashboardModel = new ProjectActivityDashboardModel();

        if (!jsonObject.isNull("project_member_id"))
            projectActivityDashboardModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("status_task"))
            projectActivityDashboardModel.setStatusTask(StatusTaskEnum.fromString(jsonObject.getString("status_task")));
        if (!jsonObject.isNull("total_task"))
            projectActivityDashboardModel.setTotalTask(jsonObject.getInt("total_task"));

        return projectActivityDashboardModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getStatusTask() != null)
            jsonObject.put("status_task", getStatusTask().getValue());
        if (getTotalTask() != null)
            jsonObject.put("total_task", getTotalTask());

        return jsonObject;
    }
}
