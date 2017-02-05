package com.construction.pm.models.network;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class AccessTokenModel {

    protected String mAccessToken;
    protected Integer mExpiresIn;
    protected String mTokenType;
    protected String mScope;
    protected String mRefreshToken;
    protected Calendar mTokenGenerateTime;

    public AccessTokenModel() {

    }

    public void setAccessToken(final String accessToken) {
        mAccessToken = accessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setExpiresIn(final Integer expiresIn) {
        mExpiresIn = expiresIn;
    }

    public Integer getExpiresIn() {
        return mExpiresIn;
    }

    public void setTokenType(final String tokenType) {
        mTokenType = tokenType;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public void setScope(final String scope) {
        mScope = scope;
    }

    public String getScope() {
        return mScope;
    }

    public void setRefreshToken(final String refreshToken) {
        mRefreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setTokenGenerateTime(Calendar tokenGenerateTime) {
        mTokenGenerateTime = tokenGenerateTime;
    }

    public Calendar getTokenGenerateTime() {
        return mTokenGenerateTime;
    }

    public boolean isExpired() {
        if (mTokenGenerateTime == null || mExpiresIn == null)
            return true;

        Calendar now = Calendar.getInstance();

        Calendar expiredAt = Calendar.getInstance();
        expiredAt.setTimeInMillis(mTokenGenerateTime.getTimeInMillis());
        expiredAt.add(Calendar.SECOND, mExpiresIn);

        return (now.getTimeInMillis() > expiredAt.getTimeInMillis());
    }

    public static AccessTokenModel build(org.json.JSONObject jsonObject) throws JSONException {
        AccessTokenModel accessTokenModel = new AccessTokenModel();

        if (!jsonObject.isNull("access_token"))
            accessTokenModel.setAccessToken(jsonObject.getString("access_token"));
        if (!jsonObject.isNull("expires_in"))
            accessTokenModel.setExpiresIn(jsonObject.getInt("expires_in"));
        if (!jsonObject.isNull("token_type"))
            accessTokenModel.setTokenType(jsonObject.getString("token_type"));
        if (!jsonObject.isNull("scope"))
            accessTokenModel.setScope(jsonObject.getString("scope"));
        if (!jsonObject.isNull("refresh_token"))
            accessTokenModel.setRefreshToken(jsonObject.getString("refresh_token"));
        if (!jsonObject.isNull("token_generate_time"))
            accessTokenModel.setTokenGenerateTime(DateTimeUtil.FromDateTimeString(jsonObject.getString("token_generate_time")));

        return accessTokenModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getAccessToken() != null)
            jsonObject.put("access_token", getAccessToken());
        if (getExpiresIn() != null)
            jsonObject.put("expires_in", getExpiresIn());
        if (getTokenType() != null)
            jsonObject.put("token_type", getTokenType());
        if (getScope() != null)
            jsonObject.put("scope", getScope());
        if (getRefreshToken() != null)
            jsonObject.put("refresh_token", getRefreshToken());
        if (getTokenGenerateTime() != null)
            jsonObject.put("token_generate_time", DateTimeUtil.ToDateTimeString(getTokenGenerateTime()));

        return jsonObject;
    }
}
