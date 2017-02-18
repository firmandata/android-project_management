package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ProjectPlanAssignmentModel {

    protected Integer mProjectPlanAssignmentId;
    protected Integer mProjectPlanId;
    protected Integer mProjectMemberId;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected String mMemberCode;
    protected String mMemberName;
    protected String mEmail;
    protected String mPhoneNumber;
    protected String mDescription;

    public ProjectPlanAssignmentModel() {

    }

    public void setProjectPlanAssignmentId(final Integer projectPlanAssignmentId) {
        mProjectPlanAssignmentId = projectPlanAssignmentId;
    }

    public Integer getProjectPlanAssignmentId() {
        return mProjectPlanAssignmentId;
    }

    public void setProjectPlanId(final Integer projectPlanId) {
        mProjectPlanId = projectPlanId;
    }

    public Integer getProjectPlanId() {
        return mProjectPlanId;
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

    public static ProjectPlanAssignmentModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectPlanAssignmentModel projectPlanAssignmentModel = new ProjectPlanAssignmentModel();

        if (!jsonObject.isNull("project_plan_assignment_id"))
            projectPlanAssignmentModel.setProjectPlanAssignmentId(jsonObject.getInt("project_plan_assignment_id"));
        if (!jsonObject.isNull("project_plan_id"))
            projectPlanAssignmentModel.setProjectPlanId(jsonObject.getInt("project_plan_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectPlanAssignmentModel.setProjectMemberId(jsonObject.getInt("project_member_id"));
        if (!jsonObject.isNull("creator_id"))
            projectPlanAssignmentModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectPlanAssignmentModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("member_code"))
            projectPlanAssignmentModel.setMemberCode(jsonObject.getString("member_code"));
        if (!jsonObject.isNull("member_name"))
            projectPlanAssignmentModel.setMemberName(jsonObject.getString("member_name"));
        if (!jsonObject.isNull("email"))
            projectPlanAssignmentModel.setEmail(jsonObject.getString("email"));
        if (!jsonObject.isNull("phone_number"))
            projectPlanAssignmentModel.setPhoneNumber(jsonObject.getString("phone_number"));
        if (!jsonObject.isNull("description"))
            projectPlanAssignmentModel.setDescription(jsonObject.getString("description"));

        return projectPlanAssignmentModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectPlanAssignmentId() != null)
            jsonObject.put("project_plan_assignment_id", getProjectPlanAssignmentId());
        if (getProjectPlanId() != null)
            jsonObject.put("project_plan_id", getProjectPlanId());
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
