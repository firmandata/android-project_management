package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class NotificationModel {

    public final static String NOTIFICATION_STATUS_SENT = "SENT";
    public final static String NOTIFICATION_STATUS_UNREAD = "UNREAD";
    public final static String NOTIFICATION_STATUS_READ = "READ";

    protected Integer mProjectNotificationId;
    protected Integer mProjectStageId;
    protected Integer mProjectActivityId;
    protected Integer mProjectAddendumId;
    protected Integer mProjectMemberId;
    protected Calendar mNotificationDate;
    protected String mNotificationMessage;
    protected String mNotificationStatus;
    protected Boolean mIsMonitoring;
    protected Boolean mIsUpdateTask;

    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public NotificationModel() {

    }

    public void setProjectNotificationId(final Integer projectNotificationId) {
        mProjectNotificationId = projectNotificationId;
    }

    public Integer getProjectNotificationId() {
        return mProjectNotificationId;
    }

    public void setProjectStageId(final Integer projectStageId) {
        mProjectStageId = projectStageId;
    }

    public Integer getProjectStageId() {
        return mProjectStageId;
    }

    public void setProjectActivityId(final Integer projectActivityId) {
        mProjectActivityId = projectActivityId;
    }

    public Integer getProjectActivityId() {
        return mProjectActivityId;
    }

    public void setProjectAddendumId(final Integer projectAddendumId) {
        mProjectAddendumId = projectAddendumId;
    }

    public Integer getProjectAddendumId() {
        return mProjectAddendumId;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setNotificationDate(final Calendar notificationDate) {
        mNotificationDate = notificationDate;
    }

    public Calendar getNotificationDate() {
        return mNotificationDate;
    }

    public void setNotificationMessage(final String notificationMessage) {
        mNotificationMessage = notificationMessage;
    }

    public String getNotificationMessage() {
        return mNotificationMessage;
    }

    public void setNotificationStatus(final String notificationStatus) {
        mNotificationStatus = notificationStatus;
    }

    public String getNotificationStatus() {
        return mNotificationStatus;
    }

    public void setIsMonitoring(final Boolean isMonitoring) {
        mIsMonitoring = isMonitoring;
    }

    public Boolean getIsMonitoring() {
        return mIsMonitoring;
    }

    public void setIsUpdateTask(final Boolean isUpdateTask) {
        mIsUpdateTask = isUpdateTask;
    }

    public Boolean getIsUpdateTask() {
        return mIsUpdateTask;
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

    public Boolean isRead() {
        if (mNotificationStatus == null)
            return false;

        return mNotificationStatus.equals(NOTIFICATION_STATUS_READ);
    }

    public static NotificationModel build(final org.json.JSONObject jsonObject) throws JSONException {
        NotificationModel notificationModel = new NotificationModel();

        if (!jsonObject.isNull("project_notification_id"))
            notificationModel.setProjectNotificationId(jsonObject.getInt("project_notification_id"));
        if (!jsonObject.isNull("project_stage_id"))
            notificationModel.setProjectStageId(jsonObject.getInt("project_stage_id"));
        if (!jsonObject.isNull("project_activity_id"))
            notificationModel.setProjectActivityId(jsonObject.getInt("project_activity_id"));
        if (!jsonObject.isNull("project_addendum_id"))
            notificationModel.setProjectAddendumId(jsonObject.getInt("project_addendum_id"));
        if (!jsonObject.isNull("project_member_id"))
            notificationModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("notification_date"))
            notificationModel.setNotificationDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("notification_date")));
        if (!jsonObject.isNull("notification_message"))
            notificationModel.setNotificationMessage(jsonObject.getString("notification_message"));
        if (!jsonObject.isNull("notification_status"))
            notificationModel.setNotificationStatus(jsonObject.getString("notification_status"));
        if (!jsonObject.isNull("is_monitoring"))
            notificationModel.setIsMonitoring(jsonObject.getInt("is_monitoring") > 0);
        if (!jsonObject.isNull("is_update_task"))
            notificationModel.setIsUpdateTask(jsonObject.getInt("is_update_task") > 0);
        if (!jsonObject.isNull("creator_id"))
            notificationModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            notificationModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            notificationModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            notificationModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return notificationModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectNotificationId() != null)
            jsonObject.put("project_notification_id", getProjectNotificationId());
        if (getProjectStageId() != null)
            jsonObject.put("project_stage_id", getProjectStageId());
        if (getProjectActivityId() != null)
            jsonObject.put("project_activity_id", getProjectActivityId());
        if (getProjectAddendumId() != null)
            jsonObject.put("project_addendum_id", getProjectAddendumId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getNotificationDate() != null)
            jsonObject.put("notification_date", DateTimeUtil.ToDateTimeString(getNotificationDate()));
        if (getNotificationMessage() != null)
            jsonObject.put("notification_message", getNotificationMessage());
        if (getNotificationStatus() != null)
            jsonObject.put("notification_status", getNotificationStatus());
        if (getIsMonitoring() != null)
            jsonObject.put("is_monitoring", getIsMonitoring() ? 1 : 0);
        if (getIsUpdateTask() != null)
            jsonObject.put("is_update_task", getIsUpdateTask() ? 1 : 0);
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
