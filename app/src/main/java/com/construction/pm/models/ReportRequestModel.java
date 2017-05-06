package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ReportRequestModel {

    protected Integer mReportRequestId;
    protected Integer mProjectId;
    protected Integer mProjectMemberId;
    protected Calendar mRequestDate;
    protected String mComment;
    protected Calendar mLastReportSentToEmailDate;
    protected String mRequestStatus;
    protected String mProjectName;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ReportRequestModel() {

    }

    public void setReportRequestId(final Integer reportRequestId) {
        mReportRequestId = reportRequestId;
    }

    public Integer getReportRequestId() {
        return mReportRequestId;
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

    public void setRequestDate(final Calendar requestDate) {
        mRequestDate = requestDate;
    }

    public Calendar getRequestDate() {
        return mRequestDate;
    }

    public void setComment(final String comment) {
        mComment = comment;
    }

    public String getComment() {
        return mComment;
    }

    public void setLastReportSentToEmailDate(final Calendar lastReportSentToEmailDate) {
        mLastReportSentToEmailDate = lastReportSentToEmailDate;
    }

    public Calendar getLastReportSentToEmailDate() {
        return mLastReportSentToEmailDate;
    }

    public void setRequestStatus(final String comment) {
        mRequestStatus = comment;
    }

    public String getRequestStatus() {
        return mRequestStatus;
    }

    public void setProjectName(final String projectName) {
        mProjectName = projectName;
    }

    public String getProjectName() {
        return mProjectName;
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

    public static ReportRequestModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ReportRequestModel reportRequestModel = new ReportRequestModel();

        if (!jsonObject.isNull("report_request_id"))
            reportRequestModel.setReportRequestId(jsonObject.getInt("report_request_id"));
        if (!jsonObject.isNull("project_id"))
            reportRequestModel.setProjectId(jsonObject.getInt("project_id"));
        if (!jsonObject.isNull("project_member_id"))
            reportRequestModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("request_date"))
            reportRequestModel.setRequestDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("request_date")));
        if (!jsonObject.isNull("comment"))
            reportRequestModel.setComment(jsonObject.getString("comment"));
        if (!jsonObject.isNull("last_report_sent_to_email_date"))
            reportRequestModel.setLastReportSentToEmailDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_report_sent_to_email_date")));
        if (!jsonObject.isNull("request_status"))
            reportRequestModel.setRequestStatus(jsonObject.getString("request_status"));
        if (!jsonObject.isNull("project_name"))
            reportRequestModel.setProjectName(jsonObject.getString("project_name"));
        if (!jsonObject.isNull("creator_id"))
            reportRequestModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            reportRequestModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            reportRequestModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            reportRequestModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return reportRequestModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getReportRequestId() != null)
            jsonObject.put("report_request_id", getReportRequestId());
        if (getProjectId() != null)
            jsonObject.put("project_id", getProjectId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getRequestDate() != null)
            jsonObject.put("request_date", DateTimeUtil.ToDateTimeString(getRequestDate()));
        if (getComment() != null)
            jsonObject.put("comment", getComment());
        if (getLastReportSentToEmailDate() != null)
            jsonObject.put("last_report_sent_to_email_date", DateTimeUtil.ToDateTimeString(getLastReportSentToEmailDate()));
        if (getRequestStatus() != null)
            jsonObject.put("request_status", getRequestStatus());
        if (getProjectName() != null)
            jsonObject.put("project_name", getProjectName());
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
