package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectMemberModel {

    protected Integer mProjectMemberId;
    protected Integer mUserId;
    protected String mMemberCode;
    protected String mMemberName;
    protected String mEmail;
    protected String mPhoneNumber;
    protected String mDescription;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ProjectMemberModel() {

    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
    }

    public void setUserId(final Integer userId) {
        mUserId = userId;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setMemberCode(final String memberCode) {
        mMemberCode = memberCode;
    }

    public String getMemberCode() {
        return mMemberCode;
    }

    public void setMemberName(final String memberName) {
        mMemberName = memberName;
    }

    public String getMemberName() {
        return mMemberName;
    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setDescription(final String description) {
        mDescription= description;
    }

    public String getDescription() {
        return mDescription;
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

    public static ProjectMemberModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectMemberModel projectMemberModel = new ProjectMemberModel();

        if (!jsonObject.isNull("project_member_id"))
            projectMemberModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("user_id"))
            projectMemberModel.setUserId(jsonObject.getInt("user_id"));
        if (!jsonObject.isNull("member_code"))
            projectMemberModel.setMemberCode(jsonObject.getString("member_code"));
        if (!jsonObject.isNull("member_name"))
            projectMemberModel.setMemberName(jsonObject.getString("member_name"));
        if (!jsonObject.isNull("email"))
            projectMemberModel.setEmail(jsonObject.getString("email"));
        if (!jsonObject.isNull("phone_number"))
            projectMemberModel.setPhoneNumber(jsonObject.getString("phone_number"));
        if (!jsonObject.isNull("description"))
            projectMemberModel.setDescription(jsonObject.getString("description"));
        if (!jsonObject.isNull("creator_id"))
            projectMemberModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectMemberModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectMemberModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectMemberModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return projectMemberModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getUserId() != null)
            jsonObject.put("user_id", getUserId());
        if (getMemberCode() != null)
            jsonObject.put("member_code", getMemberCode());
        if (getMemberName() != null)
            jsonObject.put("member_name", getMemberName());
        if (getEmail() != null)
            jsonObject.put("email", getEmail());
        if (getPhoneNumber() != null)
            jsonObject.put("phone_number", getPhoneNumber());
        if (getDescription() != null)
            jsonObject.put("description", getDescription());
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
