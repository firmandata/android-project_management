package com.construction.pm.models.system;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class SessionLoginModel {
    protected String mToken;
    protected UserModel mUserModel;
    protected Calendar mExpiredTime;

    public SessionLoginModel() {

    }

    public void setToken(final String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }

    public void setUser(final UserModel userModel) {
        mUserModel = userModel;
    }

    public UserModel getUser() {
        return mUserModel;
    }

    public void setExpiredTime(final Calendar expiredTime) {
        mExpiredTime = expiredTime;
    }

    public Calendar getExpiredTime() {
        return mExpiredTime;
    }

    public boolean isExpired() {
        boolean isExpired = true;

        if (mExpiredTime != null) {
            Calendar now = Calendar.getInstance();
            if (mExpiredTime.getTimeInMillis() > now.getTimeInMillis())
                isExpired = false;
            else
                isExpired = true;
        }

        return isExpired;
    }

    public static SessionLoginModel build(final org.json.JSONObject dataJson) throws JSONException {
        SessionLoginModel sessionLoginModel = new SessionLoginModel();

        if (!dataJson.isNull("token"))
            sessionLoginModel.setToken(dataJson.getString("token"));
        if (!dataJson.isNull("expired_time"))
            sessionLoginModel.setExpiredTime(DateTimeUtil.FromDateTimeString(dataJson.getString("expired_time")));
        if (!dataJson.isNull("user")) {
            org.json.JSONObject userJson = dataJson.getJSONObject("user");
            sessionLoginModel.setUser(UserModel.build(userJson));
        }

        return sessionLoginModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        jsonObject.put("token", getToken());
        jsonObject.put("expired_time", DateTimeUtil.ToDateTimeString(getExpiredTime()));
        UserModel userModel = getUser();
        if (userModel != null)
            jsonObject.put("user", userModel.build());

        return jsonObject;
    }
}
