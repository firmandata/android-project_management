package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectPlanModel {

    protected Integer mProjectPlanId;
    protected Integer mProjectId;
    protected Integer mParentProjectPlanId;
    protected Integer mSequenceNo;
    protected String mTaskName;
    protected Double mTaskWeightPercentage;
    protected Calendar mPlanStartDate;
    protected Calendar mPlanEndDate;
    protected Calendar mRealizationStartDate;
    protected Calendar mRealizationEndDate;
    protected String mRealizationStatus;
    protected Integer mProjectAddendumId;
    protected Double mPercentComplete;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectPlanModel() {

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

    public void setParentProjectPlanId(final Integer parentProjectPlanId) {
        mParentProjectPlanId = parentProjectPlanId;
    }

    public Integer getParentProjectPlanId() {
        return mParentProjectPlanId;
    }

    public void setSequenceNo(final Integer sequenceNo) {
        mSequenceNo = sequenceNo;
    }

    public Integer getSequenceNo() {
        return mSequenceNo;
    }

    public void setTaskName(final String taskName) {
        mTaskName = taskName;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskWeightPercentage(final Double taskWeightPercentage) {
        mTaskWeightPercentage = taskWeightPercentage;
    }

    public Double getTaskWeightPercentage() {
        return mTaskWeightPercentage;
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

    public void setRealizationStartDate(final Calendar realizationStartDate) {
        mRealizationStartDate = realizationStartDate;
    }

    public Calendar getRealizationStartDate() {
        return mRealizationStartDate;
    }

    public void setRealizationEndDate(final Calendar realizationEndDate) {
        mRealizationEndDate = realizationEndDate;
    }

    public Calendar getRealizationEndDate() {
        return mRealizationEndDate;
    }

    public void setRealizationStatus(final String realizationStatus) {
        mRealizationStatus = realizationStatus;
    }

    public String getRealizationStatus() {
        return mRealizationStatus;
    }

    public void setProjectAddendumId(final Integer projectAddendumId) {
        mProjectAddendumId = projectAddendumId;
    }

    public Integer getProjectAddendumId() {
        return mProjectAddendumId;
    }

    public void setPercentComplete(final Double percentComplete) {
        mPercentComplete = percentComplete;
    }

    public Double getPercentComplete() {
        return mPercentComplete;
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

    public static ProjectPlanModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectPlanModel projectPlanModel = new ProjectPlanModel();

        if (!jsonObject.isNull("project_plan_id"))
            projectPlanModel.setProjectPlanId(jsonObject.getInt("project_plan_id"));
        if (!jsonObject.isNull("project_id"))
            projectPlanModel.setProjectId(jsonObject.getInt("project_id"));
        if (!jsonObject.isNull("parent_project_plan_id"))
            projectPlanModel.setParentProjectPlanId(jsonObject.getInt("parent_project_plan_id"));
        if (!jsonObject.isNull("sequence_no"))
            projectPlanModel.setSequenceNo(jsonObject.getInt("sequence_no"));
        if (!jsonObject.isNull("task_name"))
            projectPlanModel.setTaskName(jsonObject.getString("task_name"));
        if (!jsonObject.isNull("task_weight_percentage"))
            projectPlanModel.setTaskWeightPercentage(jsonObject.getDouble("task_weight_percentage"));
        if (!jsonObject.isNull("plan_start_date"))
            projectPlanModel.setPlanStartDate(DateTimeUtil.FromDateString(jsonObject.getString("plan_start_date")));
        if (!jsonObject.isNull("plan_end_date"))
            projectPlanModel.setPlanEndDate(DateTimeUtil.FromDateString(jsonObject.getString("plan_end_date")));
        if (!jsonObject.isNull("realization_start_date"))
            projectPlanModel.setRealizationStartDate(DateTimeUtil.FromDateString(jsonObject.getString("realization_start_date")));
        if (!jsonObject.isNull("realization_end_date"))
            projectPlanModel.setRealizationEndDate(DateTimeUtil.FromDateString(jsonObject.getString("realization_end_date")));
        if (!jsonObject.isNull("realization_status"))
            projectPlanModel.setRealizationStatus(jsonObject.getString("realization_status"));
        if (!jsonObject.isNull("project_addendum_id"))
            projectPlanModel.setProjectAddendumId(jsonObject.getInt("project_addendum_id"));
        if (!jsonObject.isNull("percent_complete"))
            projectPlanModel.setPercentComplete(jsonObject.getDouble("percent_complete"));
        if (!jsonObject.isNull("creator_id"))
            projectPlanModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectPlanModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectPlanModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectPlanModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectPlanModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectPlanId() != null)
            jsonObject.put("project_plan_id", getProjectPlanId());
        if (getProjectId() != null)
            jsonObject.put("project_id", getProjectId());
        if (getParentProjectPlanId() != null)
            jsonObject.put("parent_project_plan_id", getParentProjectPlanId());
        if (getSequenceNo() != null)
            jsonObject.put("sequence_no", getSequenceNo());
        if (getTaskName() != null)
            jsonObject.put("task_name", getTaskName());
        if (getTaskWeightPercentage() != null)
            jsonObject.put("task_weight_percentage", getTaskWeightPercentage());
        if (getPlanStartDate() != null)
            jsonObject.put("plan_start_date", DateTimeUtil.ToDateString(getPlanStartDate()));
        if (getPlanEndDate() != null)
            jsonObject.put("plan_end_date", DateTimeUtil.ToDateString(getPlanEndDate()));
        if (getRealizationStartDate() != null)
            jsonObject.put("realization_start_date", DateTimeUtil.ToDateString(getRealizationStartDate()));
        if (getRealizationEndDate() != null)
            jsonObject.put("realization_end_date", DateTimeUtil.ToDateString(getRealizationEndDate()));
        if (getRealizationStatus() != null)
            jsonObject.put("realization_status", getRealizationStatus());
        if (getProjectAddendumId() != null)
            jsonObject.put("project_addendum_id", getProjectAddendumId());
        if (getPercentComplete() != null)
            jsonObject.put("percent_complete", getPercentComplete());
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
