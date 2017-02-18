package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectActivityMonitoringModel {
    protected Integer mProjectActivityMonitoringId;
    protected Integer mProjectActivityId;
    protected Integer mProjectMemberId;
    protected Calendar mMonitoringDate;
    protected Calendar mActualStartDate;
    protected Calendar mActualEndDate;
    protected String mActivityStatus;
    protected Double mPercentComplete;
    protected String mComment;
    protected Integer mPhotoId;
    protected Integer mPhotoAdditional1Id;
    protected Integer mPhotoAdditional2Id;
    protected Integer mPhotoAdditional3Id;
    protected Integer mPhotoAdditional4Id;
    protected Integer mPhotoAdditional5Id;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectActivityMonitoringModel() {

    }

    public void setProjectActivityMonitoringId(final Integer projectActivityMonitoringId) {
        mProjectActivityMonitoringId = projectActivityMonitoringId;
    }

    public Integer getProjectActivityMonitoringId() {
        return mProjectActivityMonitoringId;
    }

    public void setProjectActivityId(final Integer projectActivityId) {
        mProjectActivityId = projectActivityId;
    }

    public Integer getProjectActivityId() {
        return mProjectActivityId;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setMonitoringDate(final Calendar monitoringDate) {
        mMonitoringDate = monitoringDate;
    }

    public Calendar getMonitoringDate() {
        return mMonitoringDate;
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

    public void setPhotoId(final Integer photoId) {
        mPhotoId = photoId;
    }

    public Integer getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoAdditional1Id(final Integer photoAdditional1Id) {
        mPhotoAdditional1Id = photoAdditional1Id;
    }

    public Integer getPhotoAdditional1Id() {
        return mPhotoAdditional1Id;
    }

    public void setPhotoAdditional2Id(final Integer photoAdditional2Id) {
        mPhotoAdditional2Id = photoAdditional2Id;
    }

    public Integer getPhotoAdditional2Id() {
        return mPhotoAdditional2Id;
    }

    public void setPhotoAdditional3Id(final Integer photoAdditional3Id) {
        mPhotoAdditional3Id = photoAdditional3Id;
    }

    public Integer getPhotoAdditional3Id() {
        return mPhotoAdditional3Id;
    }

    public void setPhotoAdditional4Id(final Integer photoAdditional4Id) {
        mPhotoAdditional4Id = photoAdditional4Id;
    }

    public Integer getPhotoAdditional4Id() {
        return mPhotoAdditional4Id;
    }

    public void setPhotoAdditional5Id(final Integer photoAdditional5Id) {
        mPhotoAdditional5Id = photoAdditional5Id;
    }

    public Integer getPhotoAdditional5Id() {
        return mPhotoAdditional5Id;
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

    public static ProjectActivityMonitoringModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectActivityMonitoringModel projectActivityMonitoringModel = new ProjectActivityMonitoringModel();

        if (!jsonObject.isNull("project_activity_monitoring_id"))
            projectActivityMonitoringModel.setProjectActivityMonitoringId(jsonObject.getInt("project_activity_monitoring_id"));
        if (!jsonObject.isNull("project_activity_id"))
            projectActivityMonitoringModel.setProjectActivityId(jsonObject.getInt("project_activity_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectActivityMonitoringModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("monitoring_date"))
            projectActivityMonitoringModel.setMonitoringDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("monitoring_date")));
        if (!jsonObject.isNull("actual_start_date"))
            projectActivityMonitoringModel.setActualStartDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_start_date")));
        if (!jsonObject.isNull("actual_end_date"))
            projectActivityMonitoringModel.setActualEndDate(DateTimeUtil.FromDateString(jsonObject.getString("actual_end_date")));
        if (!jsonObject.isNull("activity_status"))
            projectActivityMonitoringModel.setActivityStatus(jsonObject.getString("activity_status"));
        if (!jsonObject.isNull("percent_complete"))
            projectActivityMonitoringModel.setPercentComplete(jsonObject.getDouble("percent_complete"));
        if (!jsonObject.isNull("comment"))
            projectActivityMonitoringModel.setComment(jsonObject.getString("comment"));
        if (!jsonObject.isNull("photo_id"))
            projectActivityMonitoringModel.setPhotoId(jsonObject.getInt("photo_id"));
        if (!jsonObject.isNull("photo_additional1_id"))
            projectActivityMonitoringModel.setPhotoAdditional1Id(jsonObject.getInt("photo_additional1_id"));
        if (!jsonObject.isNull("photo_additional2_id"))
            projectActivityMonitoringModel.setPhotoAdditional2Id(jsonObject.getInt("photo_additional2_id"));
        if (!jsonObject.isNull("photo_additional3_id"))
            projectActivityMonitoringModel.setPhotoAdditional3Id(jsonObject.getInt("photo_additional3_id"));
        if (!jsonObject.isNull("photo_additional4_id"))
            projectActivityMonitoringModel.setPhotoAdditional4Id(jsonObject.getInt("photo_additional4_id"));
        if (!jsonObject.isNull("photo_additional5_id"))
            projectActivityMonitoringModel.setPhotoAdditional5Id(jsonObject.getInt("photo_additional5_id"));
        if (!jsonObject.isNull("creator_id"))
            projectActivityMonitoringModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectActivityMonitoringModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectActivityMonitoringModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectActivityMonitoringModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectActivityMonitoringModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectActivityMonitoringId() != null)
            jsonObject.put("project_activity_monitoring_id", getProjectActivityMonitoringId());
        if (getProjectActivityId() != null)
            jsonObject.put("project_activity_id", getProjectActivityId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getMonitoringDate() != null)
            jsonObject.put("monitoring_date", DateTimeUtil.ToDateTimeString(getMonitoringDate()));
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
        if (getPhotoId() != null)
            jsonObject.put("photo_id", getPhotoId());
        if (getPhotoAdditional1Id() != null)
            jsonObject.put("photo_additional1_id", getPhotoAdditional1Id());
        if (getPhotoAdditional2Id() != null)
            jsonObject.put("photo_additional2_id", getPhotoAdditional2Id());
        if (getPhotoAdditional3Id() != null)
            jsonObject.put("photo_additional3_id", getPhotoAdditional3Id());
        if (getPhotoAdditional4Id() != null)
            jsonObject.put("photo_additional4_id", getPhotoAdditional4Id());
        if (getPhotoAdditional5Id() != null)
            jsonObject.put("photo_additional5_id", getPhotoAdditional5Id());
        if (getCreatorId() != null)
            jsonObject.put("creator_id", getCreatorId());
        if (getCreateDate() != null)
            jsonObject.put("create_date", DateTimeUtil.ToDateTimeString(getCreateDate()));
        if (getLastUserId() != null)
            jsonObject.put("last_user_id", getLastUserId());
        if (getLastUpdate() != null)
            jsonObject.put("last_update", DateTimeUtil.ToDateTimeString(getLastUpdate()));

        return jsonObject;
    }
}
