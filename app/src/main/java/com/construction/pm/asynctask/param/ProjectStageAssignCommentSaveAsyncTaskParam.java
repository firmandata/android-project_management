package com.construction.pm.asynctask.param;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiParam;

public class ProjectStageAssignCommentSaveAsyncTaskParam {
    protected Context mContext;
    protected SettingUserModel mSettingUserModel;
    protected ProjectMemberModel mProjectMemberModel;
    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;

    protected WebApiParam.WebApiParamFile mPhoto;
    protected WebApiParam.WebApiParamFile mPhotoAdditional1;
    protected WebApiParam.WebApiParamFile mPhotoAdditional2;
    protected WebApiParam.WebApiParamFile mPhotoAdditional3;
    protected WebApiParam.WebApiParamFile mPhotoAdditional4;
    protected WebApiParam.WebApiParamFile mPhotoAdditional5;

    public ProjectStageAssignCommentSaveAsyncTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectStageAssignCommentModel projectStageAssignCommentModel, final ProjectMemberModel projectMemberModel) {
        mContext = context;
        mSettingUserModel = settingUserModel;
        mProjectStageAssignCommentModel = projectStageAssignCommentModel;
        mProjectMemberModel = projectMemberModel;
    }

    public Context getContext() {
        return mContext;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        return mProjectStageAssignCommentModel;
    }

    public void setPhoto(final WebApiParam.WebApiParamFile photo) {
        mPhoto = photo;
    }

    public WebApiParam.WebApiParamFile getPhoto() {
        return mPhoto;
    }

    public void setPhotoAdditional1(final WebApiParam.WebApiParamFile photoAdditional1) {
        mPhotoAdditional1 = photoAdditional1;
    }

    public WebApiParam.WebApiParamFile getPhotoAdditional1() {
        return mPhotoAdditional1;
    }

    public void setPhotoAdditional2(final WebApiParam.WebApiParamFile photoAdditional2) {
        mPhotoAdditional2 = photoAdditional2;
    }

    public WebApiParam.WebApiParamFile getPhotoAdditional2() {
        return mPhotoAdditional2;
    }

    public void setPhotoAdditional3(final WebApiParam.WebApiParamFile photoAdditional3) {
        mPhotoAdditional3 = photoAdditional3;
    }

    public WebApiParam.WebApiParamFile getPhotoAdditional3() {
        return mPhotoAdditional3;
    }

    public void setPhotoAdditional4(final WebApiParam.WebApiParamFile photoAdditional4) {
        mPhotoAdditional4 = photoAdditional4;
    }

    public WebApiParam.WebApiParamFile getPhotoAdditional4() {
        return mPhotoAdditional4;
    }

    public void setPhotoAdditional5(final WebApiParam.WebApiParamFile photoAdditional5) {
        mPhotoAdditional5 = photoAdditional5;
    }

    public WebApiParam.WebApiParamFile getPhotoAdditional5() {
        return mPhotoAdditional5;
    }
}
