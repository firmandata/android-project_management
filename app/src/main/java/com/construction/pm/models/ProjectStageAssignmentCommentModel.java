package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.io.File;
import java.util.Calendar;

public class ProjectStageAssignmentCommentModel {

    protected Integer mProjectStageAssignCommentId;
    protected Integer mProjectStageAssignmentId;
    protected Calendar mCommentDate;
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
    protected Integer mProjectStageId;
    protected Integer mProjectMemberId;

    public ProjectStageAssignmentCommentModel() {

    }

    public void setProjectStageAssignCommentId(final Integer projectStageAssignCommentId) {
        mProjectStageAssignCommentId = projectStageAssignCommentId;
    }

    public Integer getProjectStageAssignCommentId() {
        return mProjectStageAssignCommentId;
    }

    public void setProjectStageAssignmentId(final Integer projectStageAssignmentId) {
        mProjectStageAssignmentId = projectStageAssignmentId;
    }

    public Integer getProjectStageAssignmentId() {
        return mProjectStageAssignmentId;
    }

    public void setCommentDate(final Calendar commentDate) {
        mCommentDate = commentDate;
    }

    public Calendar getCommentDate() {
        return mCommentDate;
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

    public static ProjectStageAssignmentCommentModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ProjectStageAssignmentCommentModel projectStageAssignmentCommentModel = new ProjectStageAssignmentCommentModel();

        if (!jsonObject.isNull("project_stage_assign_comment_id"))
            projectStageAssignmentCommentModel.setProjectStageAssignCommentId(jsonObject.getInt("project_stage_assign_comment_id"));
        if (!jsonObject.isNull("project_stage_assignment_id"))
            projectStageAssignmentCommentModel.setProjectStageAssignmentId(jsonObject.getInt("project_stage_assignment_id"));
        if (!jsonObject.isNull("comment_date"))
            projectStageAssignmentCommentModel.setCommentDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("comment_date")));
        if (!jsonObject.isNull("comment"))
            projectStageAssignmentCommentModel.setComment(jsonObject.getString("comment"));
        if (!jsonObject.isNull("photo_id"))
            projectStageAssignmentCommentModel.setPhotoId(jsonObject.getInt("photo_id"));
        if (!jsonObject.isNull("photo_additional1_id"))
            projectStageAssignmentCommentModel.setPhotoAdditional1Id(jsonObject.getInt("photo_additional1_id"));
        if (!jsonObject.isNull("photo_additional2_id"))
            projectStageAssignmentCommentModel.setPhotoAdditional2Id(jsonObject.getInt("photo_additional2_id"));
        if (!jsonObject.isNull("photo_additional3_id"))
            projectStageAssignmentCommentModel.setPhotoAdditional3Id(jsonObject.getInt("photo_additional3_id"));
        if (!jsonObject.isNull("photo_additional4_id"))
            projectStageAssignmentCommentModel.setPhotoAdditional4Id(jsonObject.getInt("photo_additional4_id"));
        if (!jsonObject.isNull("photo_additional5_id"))
            projectStageAssignmentCommentModel.setPhotoAdditional5Id(jsonObject.getInt("photo_additional5_id"));
        if (!jsonObject.isNull("creator_id"))
            projectStageAssignmentCommentModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            projectStageAssignmentCommentModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            projectStageAssignmentCommentModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            projectStageAssignmentCommentModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));
        if (!jsonObject.isNull("project_stage_id"))
            projectStageAssignmentCommentModel.setProjectStageId(jsonObject.getInt("project_stage_id"));
        if (!jsonObject.isNull("project_member_id"))
            projectStageAssignmentCommentModel.setProjectMemberId(jsonObject.getInt("project_member_id"));

        return projectStageAssignmentCommentModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getProjectStageAssignCommentId() != null)
            jsonObject.put("project_stage_assign_comment_id", getProjectStageAssignCommentId());
        if (getProjectStageAssignmentId() != null)
            jsonObject.put("project_stage_assignment_id", getProjectStageAssignmentId());
        if (getCommentDate() != null)
            jsonObject.put("comment_date", DateTimeUtil.ToDateTimeString(getCommentDate()));
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
        if (getProjectStageId() != null)
            jsonObject.put("project_stage_id", getProjectStageId());
        if (getProjectMemberId() != null)
            jsonObject.put("project_member_id", getProjectMemberId());

        return jsonObject;
    }
}
