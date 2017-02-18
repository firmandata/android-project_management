package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectStageAssignmentModel {

    protected Integer mProjectStageAssignmentId;
    protected Integer mProjectStageId;
    protected Integer mProjectMemberId;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected String mMemberCode;
    protected String mMemberName;
    protected String mEmail;
    protected String mPhoneNumber;
    protected String mDescription;

    public ProjectStageAssignmentModel() {

    }

    public void setProjectStageAssignmentId(final Integer projectStageAssignmentId) {
        mProjectStageAssignmentId = projectStageAssignmentId;
    }

    public Integer getProjectStageAssignmentId() {
        return mProjectStageAssignmentId;
    }

    public void setProjectStageId(final Integer projectStageId) {
        mProjectStageId = projectStageId;
    }

    public Integer getProjectStageId() {
        return mProjectStageId;
    }

    public void setProjectMemberId(final Integer projectMemberId) {
        mProjectMemberId = projectMemberId;
    }

    public Integer getProjectMemberId() {
        return mProjectMemberId;
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

    public static ProjectStageAssignmentModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageAssignmentModel projectStageAssignmentModel = new ProjectStageAssignmentModel();

        if (!jsonObject.isNull("project_stage_assignment_id"))
            projectStageAssignmentModel.setProjectStageAssignmentId(jsonObject.getInt("project_stage_assignment_id"));
        if (!jsonObject.isNull("project_stage_id"))
            projectStageAssignmentModel.setProjectStageId(jsonObject.getInt("project_stage_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectStageAssignmentModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("creator_id"))
            projectStageAssignmentModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectStageAssignmentModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("member_code"))
            projectStageAssignmentModel.setMemberCode(jsonObject.getString("member_code"));
        if (!jsonObject.isNull("member_name"))
            projectStageAssignmentModel.setMemberName(jsonObject.getString("member_name"));
        if (!jsonObject.isNull("email"))
            projectStageAssignmentModel.setEmail(jsonObject.getString("email"));
        if (!jsonObject.isNull("phone_number"))
            projectStageAssignmentModel.setPhoneNumber(jsonObject.getString("phone_number"));
        if (!jsonObject.isNull("description"))
            projectStageAssignmentModel.setDescription(jsonObject.getString("description"));

        return projectStageAssignmentModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectStageAssignmentId() != null)
            jsonObject.put("project_stage_assignment_id", getProjectStageAssignmentId());
        if (getProjectStageId() != null)
            jsonObject.put("project_stage_id", getProjectStageId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());
        if (getCreatorId() != null)
            jsonObject.put("creator_id", getCreatorId());
        if (getCreateDate() != null)
            jsonObject.put("create_date", DateTimeUtil.ToDateTimeString(getCreateDate()));
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

        return jsonObject;
    }
}
