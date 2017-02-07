package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectStageModel {

    protected Integer mProjectStageId;
    protected Integer mProjectId;
    protected Calendar mStageDate;
    protected String mStageCode;
    protected String mStageFromCode;
    protected String mStageNextCode;
    protected Calendar mStageNextPlanDate;
    protected String mStageNextSubject;
    protected String mStageNextLocation;
    protected String mStageNextMessage;

    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectStageModel() {

    }

    public void setProjectStageId(final Integer projectStageId) {
        mProjectStageId = projectStageId;
    }

    public Integer getProjectStageId() {
        return mProjectStageId;
    }

    public void setCreatorId(final Integer creatorId) {
        mCreatorId = creatorId;
    }

    public Integer getCreatorId() {
        return mCreatorId;
    }

    public void setProjectId(final Integer projectId) {
        mProjectId = projectId;
    }

    public Integer getProjectId() {
        return mProjectId;
    }

    public void setStageDate(final Calendar stageDate) {
        mStageDate = stageDate;
    }

    public Calendar getStageDate() {
        return mStageDate;
    }

    public void setStageCode(final String stageCode) {
        mStageCode = stageCode;
    }

    public String getStageCode() {
        return mStageCode;
    }

    public void setStageFromCode(final String stageFromCode) {
        mStageFromCode = stageFromCode;
    }

    public String getStageFromCode() {
        return mStageFromCode;
    }

    public void setStageNextCode(final String stageNextCode) {
        mStageNextCode = stageNextCode;
    }

    public String getStageNextCode() {
        return mStageNextCode;
    }

    public void setStageNextPlanDate(final Calendar stageNextPlanDate) {
        mStageNextPlanDate = stageNextPlanDate;
    }

    public Calendar getStageNextPlanDate() {
        return mStageNextPlanDate;
    }

    public void setStageNextSubject(final String stageNextSubject) {
        mStageNextSubject = stageNextSubject;
    }

    public String getStageNextSubject() {
        return mStageNextSubject;
    }

    public void setStageNextLocation(final String stageNextLocation) {
        mStageNextLocation = stageNextLocation;
    }

    public String getStageNextLocation() {
        return mStageNextLocation;
    }

    public void setStageNextMessage(final String stageNextMessage) {
        mStageNextMessage = stageNextMessage;
    }

    public String getStageNextMessage() {
        return mStageNextMessage;
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

    public static ProjectStageModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageModel projectStageModel = new ProjectStageModel();

        if (!jsonObject.isNull("project_stage_id"))
            projectStageModel.setProjectStageId(jsonObject.getInt("project_stage_id"));
        if (!jsonObject.isNull("project_id"))
            projectStageModel.setProjectId(jsonObject.getInt("project_id"));
        if (!jsonObject.isNull("stage_date"))
            projectStageModel.setStageDate(DateTimeUtil.FromDateString(jsonObject.getString("stage_date")));
        if (!jsonObject.isNull("stage_code"))
            projectStageModel.setStageCode(jsonObject.getString("stage_code"));
        if (!jsonObject.isNull("stage_from_code"))
            projectStageModel.setStageFromCode(jsonObject.getString("stage_from_code"));
        if (!jsonObject.isNull("stage_next_code"))
            projectStageModel.setStageNextCode(jsonObject.getString("stage_next_code"));
        if (!jsonObject.isNull("stage_next_plan_date"))
            projectStageModel.setStageNextPlanDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("stage_next_plan_date")));
        if (!jsonObject.isNull("stage_next_subject"))
            projectStageModel.setStageNextSubject(jsonObject.getString("stage_next_subject"));
        if (!jsonObject.isNull("stage_next_location"))
            projectStageModel.setStageNextLocation(jsonObject.getString("stage_next_location"));
        if (!jsonObject.isNull("stage_next_message"))
            projectStageModel.setStageNextMessage(jsonObject.getString("stage_next_message"));
        if (!jsonObject.isNull("creator_id"))
            projectStageModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectStageModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectStageModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectStageModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectStageModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectStageId() != null)
            jsonObject.put("project_stage_id", getProjectStageId());
        if (getProjectId() != null)
            jsonObject.put("project_id", getProjectId());
        if (getStageDate() != null)
            jsonObject.put("stage_date", DateTimeUtil.ToDateString(getStageDate()));
        if (getStageCode() != null)
            jsonObject.put("stage_code", getStageCode());
        if (getStageFromCode() != null)
            jsonObject.put("stage_from_code", getStageFromCode());
        if (getStageNextCode() != null)
            jsonObject.put("stage_next_code", getStageNextCode());
        if (getStageNextPlanDate() != null)
            jsonObject.put("stage_next_plan_date", DateTimeUtil.ToDateTimeString(getStageNextPlanDate()));
        if (getStageNextSubject() != null)
            jsonObject.put("stage_next_subject", getStageNextSubject());
        if (getStageNextLocation() != null)
            jsonObject.put("stage_next_location", getStageNextLocation());
        if (getStageNextMessage() != null)
            jsonObject.put("stage_next_message", getStageNextMessage());
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
