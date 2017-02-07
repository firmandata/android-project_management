package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectModel {

    protected Integer mProjectId;
    protected String mContractNo;
    protected String mProjectName;
    protected Calendar mProjectStartDate;
    protected Calendar mProjectEndDate;
    protected String mProjectDescription;
    protected String mProjectLocation;
    protected String mStageCode;
    protected Integer mLastProjectStageId;
    protected String mProjectStatus;
    protected Integer mLastProjectAddendumId;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectModel() {

    }

    public void setProjectId(final Integer projectId) {
        mProjectId = projectId;
    }

    public Integer getProjectId() {
        return mProjectId;
    }

    public void setContractNo(final String contractNo) {
        mContractNo = contractNo;
    }

    public String getContractNo() {
        return mContractNo;
    }

    public void setProjectName(final String projectName) {
        mProjectName = projectName;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectStartDate(final Calendar projectStartDate) {
        mProjectStartDate = projectStartDate;
    }

    public Calendar getProjectStartDate() {
        return mProjectStartDate;
    }

    public void setProjectEndDate(final Calendar projectEndDate) {
        mProjectEndDate = projectEndDate;
    }

    public Calendar getProjectEndDate() {
        return mProjectEndDate;
    }

    public void setProjectDescription(final String projectDescription) {
        mProjectDescription = projectDescription;
    }

    public String getProjectDescription() {
        return mProjectDescription;
    }

    public void setProjectLocation(final String projectLocation) {
        mProjectLocation = projectLocation;
    }

    public String getProjectLocation() {
        return mProjectLocation;
    }

    public void setStageCode(final String stageCode) {
        mStageCode = stageCode;
    }

    public String getStageCode() {
        return mStageCode;
    }

    public void setLastProjectStageId(final Integer lastProjectStageId) {
        mLastProjectStageId = lastProjectStageId;
    }

    public Integer getLastProjectStageId() {
        return mLastProjectStageId;
    }

    public void setProjectStatus(final String projectStatus) {
        mProjectStatus = projectStatus;
    }

    public String getProjectStatus() {
        return mProjectStatus;
    }

    public void setLastProjectAddendumId(final Integer lastProjectAddendumId) {
        mLastProjectAddendumId = lastProjectAddendumId;
    }

    public Integer getLastProjectAddendumId() {
        return mLastProjectAddendumId;
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

    public static ProjectModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectModel projectModel = new ProjectModel();

        if (!jsonObject.isNull("project_id"))
            projectModel.setProjectId(jsonObject.getInt("project_id"));
        if (!jsonObject.isNull("contract_no"))
            projectModel.setContractNo(jsonObject.getString("contract_no"));
        if (!jsonObject.isNull("project_name"))
            projectModel.setProjectName(jsonObject.getString("project_name"));
        if (!jsonObject.isNull("project_start_date"))
            projectModel.setProjectStartDate(DateTimeUtil.FromDateString(jsonObject.getString("project_start_date")));
        if (!jsonObject.isNull("project_end_date"))
            projectModel.setProjectEndDate(DateTimeUtil.FromDateString(jsonObject.getString("project_end_date")));
        if (!jsonObject.isNull("project_description"))
            projectModel.setProjectDescription(jsonObject.getString("project_description"));
        if (!jsonObject.isNull("project_location"))
            projectModel.setProjectLocation(jsonObject.getString("project_location"));
        if (!jsonObject.isNull("stage_code"))
            projectModel.setStageCode(jsonObject.getString("stage_code"));
        if (!jsonObject.isNull("last_project_stage_id"))
            projectModel.setLastProjectStageId(jsonObject.getInt("last_project_stage_id"));
        if (!jsonObject.isNull("project_status"))
            projectModel.setProjectStatus(jsonObject.getString("project_status"));
        if (!jsonObject.isNull("last_project_addendum_id"))
            projectModel.setLastProjectAddendumId(jsonObject.getInt("last_project_addendum_id"));
        if (!jsonObject.isNull("creator_id"))
            projectModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectId() != null)
            jsonObject.put("project_id", getProjectId());
        if (getContractNo() != null)
            jsonObject.put("contract_no", getContractNo());
        if (getProjectName() != null)
            jsonObject.put("project_name", getProjectName());
        if (getProjectStartDate() != null)
            jsonObject.put("project_start_date", DateTimeUtil.ToDateString(getProjectStartDate()));
        if (getProjectEndDate() != null)
            jsonObject.put("project_end_date", DateTimeUtil.ToDateString(getProjectEndDate()));
        if (getProjectDescription() != null)
            jsonObject.put("project_description", getProjectDescription());
        if (getProjectLocation() != null)
            jsonObject.put("project_location", getProjectLocation());
        if (getStageCode() != null)
            jsonObject.put("stage_code", getStageCode());
        if (getLastProjectStageId() != null)
            jsonObject.put("last_project_stage_id", getLastProjectStageId());
        if (getProjectStatus() != null)
            jsonObject.put("project_status", getProjectStatus());
        if (getLastProjectAddendumId() != null)
            jsonObject.put("last_project_addendum_id", getLastProjectAddendumId());
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
