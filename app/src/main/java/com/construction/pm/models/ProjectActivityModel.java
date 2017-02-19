package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectActivityModel {

    protected Integer mProjectActivityId;
    protected Integer mProjectPlanId;
    protected Integer mProjectId;
    protected Integer mProjectMemberId;
    protected String mTaskName;
    protected Calendar mPlanStartDate;
    protected Calendar mPlanEndDate;
    protected Calendar mActualStartDate;
    protected Calendar mActualEndDate;
    protected String mActivityStatus;
    protected Double mPercentComplete;
    protected Calendar mCurrentDate;
    protected StatusTaskEnum mStatusTask;

    public ProjectActivityModel() {

    }

    public void setProjectActivityId(final Integer projectActivityId) {
        mProjectActivityId = projectActivityId;
    }

    public Integer getProjectActivityId() {
        return mProjectActivityId;
    }

    public void setProjectPlanId(final Integer projectPlanId) {
        mProjectPlanId = projectPlanId;
    }

    public Integer getProjectPlanId() {
        return mProjectPlanId;
    }

    public void setProjectId(final Integer projectId) {
        mProjectId = projectId;
    }

    public Integer getProjectId() {
        return mProjectId;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setTaskName(final String taskName) {
        mTaskName = taskName;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setPlanStartDate(final Calendar planStartDate) {
        mPlanStartDate = planStartDate;
    }

    public Calendar getPlanStartDate() {
        return mPlanStartDate;
    }

    public void setPlanEndDate(final Calendar planEndDate) {
        mPlanEndDate = planEndDate;
    }

    public Calendar getPlanEndDate() {
        return mPlanEndDate;
    }

    public void setActualStartDate(final Calendar actualStartDate) {
        mActualStartDate = actualStartDate;
    }

    public Calendar getActualStartDate() {
        return mActualStartDate;
    }

    public void setActualEndDate(final Calendar actualEndDate) {
        mActualEndDate = actualEndDate;
    }

    public Calendar getActualEndDate() {
        return mActualEndDate;
    }

    public void setActivityStatus(final String activityStatus) {
        mActivityStatus = activityStatus;
    }

    public String getActivityStatus() {
        return mActivityStatus;
    }

    public void setPercentComplete(final Double percentComplete) {
        mPercentComplete = percentComplete;
    }

    public Double getPercentComplete() {
        return mPercentComplete;
    }

    public void setCurrentDate(final Calendar currentDate) {
        mCurrentDate = currentDate;
    }

    public Calendar getCurrentDate() {
        return mCurrentDate;
    }

    public void setStatusTask(final StatusTaskEnum statusTask) {
        mStatusTask = statusTask;
    }

    public StatusTaskEnum getStatusTask() {
        return mStatusTask;
    }

    public static ProjectActivityModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityModel projectActivityModel = new ProjectActivityModel();

        if (!jsonObject.isNull("project_activity_id"))
            projectActivityModel.setProjectActivityId(jsonObject.getInt("project_activity_id"));
        if (!jsonObject.isNull("project_plan_id"))
            projectActivityModel.setProjectPlanId(jsonObject.getInt("project_plan_id"));
        if (!jsonObject.isNull("project_id"))
            projectActivityModel.setProjectId(jsonObject.getInt("project_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectActivityModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("task_name"))
            projectActivityModel.setTaskName(jsonObject.getString("task_name"));
        if (!jsonObject.isNull("plan_start_date"))
            projectActivityModel.setPlanStartDate(DateTimeUtil.FromDateString(jsonObject.getString("plan_start_date")));
        if (!jsonObject.isNull("plan_end_date"))
            projectActivityModel.setPlanEndDate(DateTimeUtil.FromDateString(jsonObject.getString("plan_end_date")));
        if (!jsonObject.isNull("actual_start_date"))
            projectActivityModel.setActualStartDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_start_date")));
        if (!jsonObject.isNull("actual_end_date"))
            projectActivityModel.setActualEndDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_end_date")));
        if (!jsonObject.isNull("activity_status"))
            projectActivityModel.setActivityStatus(jsonObject.getString("activity_status"));
        if (!jsonObject.isNull("percent_complete"))
            projectActivityModel.setPercentComplete(jsonObject.getDouble("percent_complete"));
        if (!jsonObject.isNull("_current_date"))
            projectActivityModel.setCurrentDate(DateTimeUtil.FromDateString(jsonObject.getString("_current_date")));
        if (!jsonObject.isNull("status_task"))
            projectActivityModel.setStatusTask(StatusTaskEnum.fromString(jsonObject.getString("status_task")));

        return projectActivityModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectActivityId() != null)
            jsonObject.put("project_activity_id", getProjectActivityId());
        if (getProjectPlanId() != null)
            jsonObject.put("project_plan_id", getProjectPlanId());
        if (getProjectId() != null)
            jsonObject.put("project_id", getProjectId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getTaskName() != null)
            jsonObject.put("task_name", getTaskName());
        if (getPlanStartDate() != null)
            jsonObject.put("plan_start_date", DateTimeUtil.ToDateString(getPlanStartDate()));
        if (getPlanEndDate() != null)
            jsonObject.put("plan_end_date", DateTimeUtil.ToDateString(getPlanEndDate()));
        if (getActualStartDate() != null)
            jsonObject.put("actual_start_date", DateTimeUtil.ToDateString(getActualStartDate()));
        if (getActualEndDate() != null)
            jsonObject.put("actual_end_date", DateTimeUtil.ToDateString(getActualEndDate()));
        if (getActivityStatus() != null)
            jsonObject.put("activity_status", getActivityStatus());
        if (getPercentComplete() != null)
            jsonObject.put("percent_complete", getPercentComplete());
        if (getCurrentDate() != null)
            jsonObject.put("_current_date", DateTimeUtil.ToDateString(getCurrentDate()));
        if (getStatusTask() != null)
            jsonObject.put("status_task", getStatusTask().getValue());

        return jsonObject;
    }
}
