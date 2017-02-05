package com.construction.pm.models.system;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.network.UserProjectMemberModel;

import org.json.JSONException;

public class SessionLoginModel {
    protected AccessTokenModel mAccessTokenModel;
    protected UserProjectMemberModel mUserProjectMemberModel;

    public SessionLoginModel() {

    }

    public void setAccessTokenModel(final AccessTokenModel accessTokenModel) {
        mAccessTokenModel = accessTokenModel;
    }

    public AccessTokenModel getAccessTokenModel() {
        return mAccessTokenModel;
    }

    public void setUserProjectMemberModel(final UserProjectMemberModel userProjectMemberModel) {
        mUserProjectMemberModel = userProjectMemberModel;
    }

    public UserProjectMemberModel getUserProjectMemberModel() {
        return mUserProjectMemberModel;
    }

    public UserModel getUserModel() {
        UserProjectMemberModel userProjectMemberModel = getUserProjectMemberModel();
        if (userProjectMemberModel != null)
            return userProjectMemberModel.getUserModel();
        return null;
    }

    public ProjectMemberModel getProjectMemberModel() {
        if (mUserProjectMemberModel != null)
            return mUserProjectMemberModel.getProjectMemberModel();
        return null;
    }

    public boolean isFirstLogin() {
        UserModel userModel = getUserModel();
        if (userModel != null)
            return userModel.getFirstLogin();

        return false;
    }

    public boolean isAccessTokenExpired() {
        boolean isExpired = true;

        if (mAccessTokenModel != null) {
            isExpired = mAccessTokenModel.isExpired();
        }

        return isExpired;
    }

    public static SessionLoginModel build(final org.json.JSONObject dataJson) throws JSONException {
        SessionLoginModel sessionLoginModel = new SessionLoginModel();

        if (!dataJson.isNull("access_token")) {
            org.json.JSONObject accessTokenJson = dataJson.getJSONObject("access_token");
            sessionLoginModel.setAccessTokenModel(AccessTokenModel.build(accessTokenJson));
        }
        if (!dataJson.isNull("user_project_member")) {
            org.json.JSONObject userProjectMemberJson = dataJson.getJSONObject("user_project_member");
            sessionLoginModel.setUserProjectMemberModel(UserProjectMemberModel.build(userProjectMemberJson));
        }

        return sessionLoginModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        AccessTokenModel accessTokenModel = getAccessTokenModel();
        if (accessTokenModel != null)
            jsonObject.put("access_token", accessTokenModel.build());

        UserProjectMemberModel userProjectMemberModel = getUserProjectMemberModel();
        if (userProjectMemberModel != null)
            jsonObject.put("user_project_member", userProjectMemberModel.build());

        return jsonObject;
    }
}
