package com.construction.pm.models.system;

import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class SessionLoginModel {
    protected AccessTokenModel mAccessTokenModel;
    protected UserModel mUserModel;

    public SessionLoginModel() {

    }

    public void setAccessTokenModel(final AccessTokenModel accessTokenModel) {
        mAccessTokenModel = accessTokenModel;
    }

    public AccessTokenModel getAccessTokenModel() {
        return mAccessTokenModel;
    }

    public void setUser(final UserModel userModel) {
        mUserModel = userModel;
    }

    public UserModel getUser() {
        return mUserModel;
    }

    public boolean isExpired() {
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
        if (!dataJson.isNull("user")) {
            org.json.JSONObject userJson = dataJson.getJSONObject("user");
            sessionLoginModel.setUser(UserModel.build(userJson));
        }

        return sessionLoginModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        AccessTokenModel accessTokenModel = getAccessTokenModel();
        if (accessTokenModel != null)
            jsonObject.put("access_token", accessTokenModel.build());

        UserModel userModel = getUser();
        if (userModel != null)
            jsonObject.put("user", userModel.build());

        return jsonObject;
    }
}
