package com.construction.pm.models;

import org.json.JSONException;

public class UserProjectMemberModel {

    protected UserModel mUserModel;
    protected ProjectMemberModel mProjectMemberModel;

    public UserProjectMemberModel() {

    }

    public void setUserModel(final UserModel userModel) {
        mUserModel = userModel;
    }

    public UserModel getUserModel() {
        return mUserModel;
    }

    public void setProjectMemberModel(final ProjectMemberModel projectMemberModel) {
        mProjectMemberModel = projectMemberModel;
    }

    public ProjectMemberModel getProjectMemberModel() {
        return mProjectMemberModel;
    }

    public static UserProjectMemberModel build(final org.json.JSONObject dataJson) throws JSONException {
        UserProjectMemberModel userProjectMemberModel = new UserProjectMemberModel();

        if (!dataJson.isNull("user")) {
            org.json.JSONObject userJson = dataJson.getJSONObject("user");
            userProjectMemberModel.setUserModel(UserModel.build(userJson));
        }
        if (!dataJson.isNull("project_member")) {
            org.json.JSONObject projectMemberJson = dataJson.getJSONObject("project_member");
            userProjectMemberModel.setProjectMemberModel(ProjectMemberModel.build(projectMemberJson));
        }

        return userProjectMemberModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        UserModel userModel = getUserModel();
        if (userModel != null)
            jsonObject.put("user", userModel.build());

        ProjectMemberModel projectMemberModel = getProjectMemberModel();
        if (projectMemberModel != null)
            jsonObject.put("project_member", projectMemberModel.build());

        return jsonObject;
    }
}
