package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectStageDocumentModel {

    protected Integer mProjectStageDocumentId;
    protected Integer mProjectStageId;
    protected Calendar mDocumentDate;
    protected String mDocumentName;
    protected Integer mFileId;
    protected Integer mFileAdditional1Id;
    protected Integer mFileAdditional2Id;
    protected Integer mFileAdditional3Id;
    protected Integer mFileAdditional4Id;
    protected Integer mFileAdditional5Id;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectStageDocumentModel() {

    }

    public void setProjectStageDocumentId(final Integer projectStageDocumentId) {
        mProjectStageDocumentId = projectStageDocumentId;
    }

    public Integer getProjectStageDocumentId() {
        return mProjectStageDocumentId;
    }

    public void setProjectStageId(final Integer projectStageId) {
        mProjectStageId = projectStageId;
    }

    public Integer getProjectStageId() {
        return mProjectStageId;
    }

    public void setDocumentDate(final Calendar documentDate) {
        mDocumentDate = documentDate;
    }

    public Calendar getDocumentDate() {
        return mDocumentDate;
    }

    public void setDocumentName(final String documentName) {
        mDocumentName = documentName;
    }

    public String getDocumentName() {
        return mDocumentName;
    }
    
    public void setFileId(final Integer fileId) {
        mFileId = fileId;
    }

    public Integer getFileId() {
        return mFileId;
    }

    public void setFileAdditional1Id(final Integer fileAdditional1Id) {
        mFileAdditional1Id = fileAdditional1Id;
    }

    public Integer getFileAdditional1Id() {
        return mFileAdditional1Id;
    }

    public void setFileAdditional2Id(final Integer fileAdditional2Id) {
        mFileAdditional2Id = fileAdditional2Id;
    }

    public Integer getFileAdditional2Id() {
        return mFileAdditional2Id;
    }

    public void setFileAdditional3Id(final Integer fileAdditional3Id) {
        mFileAdditional3Id = fileAdditional3Id;
    }

    public Integer getFileAdditional3Id() {
        return mFileAdditional3Id;
    }

    public void setFileAdditional4Id(final Integer fileAdditional4Id) {
        mFileAdditional4Id = fileAdditional4Id;
    }

    public Integer getFileAdditional4Id() {
        return mFileAdditional4Id;
    }

    public void setFileAdditional5Id(final Integer fileAdditional5Id) {
        mFileAdditional5Id = fileAdditional5Id;
    }

    public Integer getFileAdditional5Id() {
        return mFileAdditional5Id;
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

    public static ProjectStageDocumentModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageDocumentModel projectStageDocumentModel = new ProjectStageDocumentModel();

        if (!jsonObject.isNull("project_stage_document_id"))
            projectStageDocumentModel.setProjectStageDocumentId(jsonObject.getInt("project_stage_document_id"));
        if (!jsonObject.isNull("project_stage_id"))
            projectStageDocumentModel.setProjectStageId(jsonObject.getInt("project_stage_id"));
        if (!jsonObject.isNull("document_date"))
            projectStageDocumentModel.setDocumentDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("document_date")));
        if (!jsonObject.isNull("document_name"))
            projectStageDocumentModel.setDocumentName(jsonObject.getString("document_name"));
        if (!jsonObject.isNull("file_id")) {
            Integer fileId = jsonObject.getInt("file_id");
            if (fileId != 0)
                projectStageDocumentModel.setFileId(fileId);
        }
        if (!jsonObject.isNull("file_additional1_id")) {
            Integer fileAdditional1Id = jsonObject.getInt("file_additional1_id");
            if (fileAdditional1Id != 0)
                projectStageDocumentModel.setFileAdditional1Id(fileAdditional1Id);
        }
        if (!jsonObject.isNull("file_additional2_id")) {
            Integer fileAdditional2Id = jsonObject.getInt("file_additional2_id");
            if (fileAdditional2Id != 0)
                projectStageDocumentModel.setFileAdditional2Id(fileAdditional2Id);
        }
        if (!jsonObject.isNull("file_additional3_id")) {
            Integer fileAdditional3Id = jsonObject.getInt("file_additional3_id");
            if (fileAdditional3Id != 0)
                projectStageDocumentModel.setFileAdditional3Id(fileAdditional3Id);
        }
        if (!jsonObject.isNull("file_additional4_id")) {
            Integer fileAdditional4Id = jsonObject.getInt("file_additional4_id");
            if (fileAdditional4Id != 0)
                projectStageDocumentModel.setFileAdditional4Id(fileAdditional4Id);
        }
        if (!jsonObject.isNull("file_additional5_id")) {
            Integer fileAdditional5Id = jsonObject.getInt("file_additional5_id");
            if (fileAdditional5Id != 0)
                projectStageDocumentModel.setFileAdditional5Id(fileAdditional5Id);
        }
        if (!jsonObject.isNull("creator_id"))
            projectStageDocumentModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectStageDocumentModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectStageDocumentModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectStageDocumentModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectStageDocumentModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectStageDocumentId() != null)
            jsonObject.put("project_stage_document_id", getProjectStageDocumentId());
        if (getProjectStageId() != null)
            jsonObject.put("project_stage_id", getProjectStageId());
        if (getDocumentDate() != null)
            jsonObject.put("document_date", DateTimeUtil.ToDateTimeString(getDocumentDate()));
        if (getDocumentName() != null)
            jsonObject.put("document_name", getDocumentName());
        if (getFileId() != null)
            jsonObject.put("file_id", getFileId());
        if (getFileAdditional1Id() != null)
            jsonObject.put("file_additional1_id", getFileAdditional1Id());
        if (getFileAdditional2Id() != null)
            jsonObject.put("file_additional2_id", getFileAdditional2Id());
        if (getFileAdditional3Id() != null)
            jsonObject.put("file_additional3_id", getFileAdditional3Id());
        if (getFileAdditional4Id() != null)
            jsonObject.put("file_additional4_id", getFileAdditional4Id());
        if (getFileAdditional5Id() != null)
            jsonObject.put("file_additional5_id", getFileAdditional5Id());
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
