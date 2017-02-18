package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectActivityUpdateModel {

    protected Integer mProjectActivityUpdateId;
    protected Integer mProjectActivityMonitoringId;
    protected Integer mProjectMemberId;
    protected Calendar mUpdateDate;
    protected Calendar mActualStartDate;
    protected Calendar mActualEndDate;
    protected String mActivityStatus;
    protected Double mPercentComplete;
    protected String mComment;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;
    protected Integer mProjectActivityId;
    protected Integer mProjectPlanId;

    public ProjectActivityUpdateModel() {

    }

    public void setProjectActivityUpdateId(final Integer projectActivityUpdateId) {
        mProjectActivityUpdateId = projectActivityUpdateId;
    }

    public Integer getProjectActivityUpdateId() {
        return mProjectActivityUpdateId;
    }

    public void setProjectActivityMonitoringId(final Integer projectActivityMonitoringId) {
        mProjectActivityMonitoringId = projectActivityMonitoringId;
    }

    public Integer getProjectActivityMonitoringId() {
        return mProjectActivityMonitoringId;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setUpdateDate(final Calendar updateDate) {
        mUpdateDate = updateDate;
    }

    public Calendar getUpdateDate() {
        return mUpdateDate;
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

    public void setComment(final String comment) {
        mComment = comment;
    }

    public String getComment() {
        return mComment;
    }

    public void setCreatorId(final Integer creatorId) {
        mCreatorId = creatorId;
    }

    public Integer getCreatorId() {
        return mCreatorId;
    }

    public void setCreateDate(final Calendar createDate) {
        mCreateDate = createDate;
    }

    public Calendar getCreateDate() {
        return mCreateDate;
    }

    public void setLastUserId(final Integer lastUserId) {
        mLastUserId = lastUserId;
    }

    public Integer getLastUserId() {
        return mLastUserId;
    }

    public void setLastUpdate(final Calendar lastUpdate) {
        mLastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return mLastUpdate;
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

    public static ProjectActivityUpdateModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityUpdateModel projectActivityUpdateModel = new ProjectActivityUpdateModel();

        if (!jsonObject.isNull("project_activity_update_id"))
            projectActivityUpdateModel.setProjectActivityUpdateId(jsonObject.getInt("project_activity_update_id"));
        if (!jsonObject.isNull("project_activity_monitoring_id"))
            projectActivityUpdateModel.setProjectActivityMonitoringId(jsonObject.getInt("project_activity_monitoring_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectActivityUpdateModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("update_date"))
            projectActivityUpdateModel.setUpdateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("update_date")));
        if (!jsonObject.isNull("actual_start_date"))
            projectActivityUpdateModel.setActualStartDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_start_date")));
        if (!jsonObject.isNull("actual_end_date"))
            projectActivityUpdateModel.setActualEndDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_end_date")));
        if (!jsonObject.isNull("activity_status"))
            projectActivityUpdateModel.setActivityStatus(jsonObject.getString("activity_status"));
        if (!jsonObject.isNull("percent_complete"))
            projectActivityUpdateModel.setPercentComplete(jsonObject.getDouble("percent_complete"));
        if (!jsonObject.isNull("comment"))
            projectActivityUpdateModel.setComment(jsonObject.getString("comment"));
        if (!jsonObject.isNull("creator_id"))
            projectActivityUpdateModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectActivityUpdateModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectActivityUpdateModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectActivityUpdateModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));
        if (!jsonObject.isNull("project_activity_id"))
            projectActivityUpdateModel.setProjectActivityId(jsonObject.getInt("project_activity_id"));
        if (!jsonObject.isNull("project_plan_id"))
            projectActivityUpdateModel.setProjectPlanId(jsonObject.getInt("project_plan_id"));

        return projectActivityUpdateModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectActivityUpdateId() != null)
            jsonObject.put("project_activity_update_id", getProjectActivityUpdateId());
        if (getProjectActivityMonitoringId() != null)
            jsonObject.put("project_activity_monitoring_id", getProjectActivityMonitoringId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getUpdateDate() != null)
            jsonObject.put("update_date", DateTimeUtil.ToDateTimeString(getUpdateDate()));
        if (getActualStartDate() != null)
            jsonObject.put("actual_start_date", DateTimeUtil.ToDateString(getActualStartDate()));
        if (getActualEndDate() != null)
            jsonObject.put("actual_end_date", DateTimeUtil.ToDateString(getActualEndDate()));
        if (getActivityStatus() != null)
            jsonObject.put("activity_status", getActivityStatus());
        if (getPercentComplete() != null)
            jsonObject.put("percent_complete", getPercentComplete());
        if (getComment() != null)
            jsonObject.put("comment", getComment());
        if (getCreatorId() != null)
            jsonObject.put("creator_id", getCreatorId());
        if (getCreateDate() != null)
            jsonObject.put("create_date", DateTimeUtil.ToDateTimeString(getCreateDate()));
        if (getLastUserId() != null)
            jsonObject.put("last_user_id", getLastUserId());
        if (getLastUpdate() != null)
            jsonObject.put("last_update", DateTimeUtil.ToDateTimeString(getLastUpdate()));
        if (getProjectActivityId() != null)
            jsonObject.put("project_activity_id", getProjectActivityId());
        if (getProjectPlanId() != null)
            jsonObject.put("project_plan_id", getProjectPlanId());

        return jsonObject;
    }
}
