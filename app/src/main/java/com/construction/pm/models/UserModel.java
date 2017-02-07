package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class UserModel {
    protected Integer mUserId;
    protected String mLogin;
    protected String mUserName;
    protected String mPassword;
    protected String mEmail;
    protected Calendar mLastAccessDate;
    protected String mLastAccessFrom;
    protected Calendar mExpiredDate;
    protected Boolean mFirstLogin;
    protected Boolean mAccountEnabled;
    protected Boolean mAccountExpired;
    protected Boolean mAccountLocked;
    protected Boolean mAuthorizationRequired;
    protected Calendar mLastPasswordUpdate;
    protected String mMobileToken;
    protected String mImeiNo;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public UserModel() {

    }

    public void setUserId(final Integer userId) {
        mUserId = userId;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setLogin(final String login) {
        mLogin = login;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setUserName(final String userName) {
        mUserName = userName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setPassword(final String password) {
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setLastAccessDate(final Calendar lastAccessDate) {
        mLastAccessDate = lastAccessDate;
    }

    public Calendar getLastAccessDate() {
        return mLastAccessDate;
    }

    public void setLastAccessFrom(final String lastAccessFrom) {
        mLastAccessFrom = lastAccessFrom;
    }

    public String getLastAccessFrom() {
        return mLastAccessFrom;
    }

    public void setExpiredDate(final Calendar expiredDate) {
        mExpiredDate = expiredDate;
    }

    public Calendar getExpiredDate() {
        return mExpiredDate;
    }

    public void setFirstLogin(final Boolean firstLogin) {
        mFirstLogin = firstLogin;
    }

    public Boolean getFirstLogin() {
        return mFirstLogin;
    }

    public void setAccountEnabled(final Boolean accountEnabled) {
        mAccountEnabled = accountEnabled;
    }

    public Boolean getAccountEnabled() {
        return mAccountEnabled;
    }

    public void setAccountExpired(final Boolean accountExpired) {
        mAccountExpired = accountExpired;
    }

    public Boolean getAccountExpired() {
        return mAccountExpired;
    }

    public void setAccountLocked(final Boolean accountLocked) {
        mAccountLocked = accountLocked;
    }

    public Boolean getAccountLocked() {
        return mAccountLocked;
    }

    public void setAuthorizationRequired(final Boolean authorizationRequired) {
        mAuthorizationRequired = authorizationRequired;
    }

    public Boolean getAuthorizationRequired() {
        return mAuthorizationRequired;
    }

    public void setLastPasswordUpdate(final Calendar lastPasswordUpdate) {
        mLastPasswordUpdate = lastPasswordUpdate;
    }

    public Calendar getLastPasswordUpdate() {
        return mLastPasswordUpdate;
    }

    public void setMobileToken(final String mobileToken) {
        mMobileToken = mobileToken;
    }

    public String getMobileToken() {
        return mMobileToken;
    }

    public void setImeiNo(final String imeiNo) {
        mImeiNo = imeiNo;
    }

    public String getImeiNo() {
        return mImeiNo;
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

    public static UserModel build(final org.json.JSONObject jsonObject) throws JSONException {
        UserModel userModel = new UserModel();

        if (!jsonObject.isNull("user_id"))
            userModel.setUserId(jsonObject.getInt("user_id"));
        if (!jsonObject.isNull("login"))
            userModel.setLogin(jsonObject.getString("login"));
        if (!jsonObject.isNull("user_name"))
            userModel.setUserName(jsonObject.getString("user_name"));
        if (!jsonObject.isNull("password"))
            userModel.setPassword(jsonObject.getString("password"));
        if (!jsonObject.isNull("email"))
            userModel.setEmail(jsonObject.getString("email"));
        if (!jsonObject.isNull("last_access_date"))
            userModel.setLastAccessDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_access_date")));
        if (!jsonObject.isNull("last_access_from"))
            userModel.setLastAccessFrom(jsonObject.getString("last_access_from"));
        if (!jsonObject.isNull("expired_date"))
            userModel.setExpiredDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("expired_date")));
        if (!jsonObject.isNull("first_login"))
            userModel.setFirstLogin(jsonObject.getInt("first_login") > 0);
        if (!jsonObject.isNull("account_enabled"))
            userModel.setAccountEnabled(jsonObject.getInt("account_enabled") > 0);
        if (!jsonObject.isNull("account_expired"))
            userModel.setAccountExpired(jsonObject.getInt("account_expired") > 0);
        if (!jsonObject.isNull("account_locked"))
            userModel.setAccountLocked(jsonObject.getInt("account_locked") > 0);
        if (!jsonObject.isNull("authorization_required"))
            userModel.setAuthorizationRequired(jsonObject.getInt("authorization_required") > 0);
        if (!jsonObject.isNull("last_password_update"))
            userModel.setLastPasswordUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_password_update")));
        if (!jsonObject.isNull("mobile_token"))
            userModel.setMobileToken(jsonObject.getString("mobile_token"));
        if (!jsonObject.isNull("imei_no"))
            userModel.setImeiNo(jsonObject.getString("imei_no"));
        if (!jsonObject.isNull("creator_id"))
            userModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            userModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            userModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            userModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return userModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getUserId() != null)
            jsonObject.put("user_id", getUserId());
        if (getLogin() != null)
            jsonObject.put("login", getLogin());
        if (getUserName() != null)
            jsonObject.put("user_name", getUserName());
        if (getPassword() != null)
            jsonObject.put("password", getPassword());
        if (getEmail() != null)
            jsonObject.put("email", getEmail());
        if (getLastAccessDate() != null)
            jsonObject.put("last_access_date", DateTimeUtil.ToDateTimeString(getLastAccessDate()));
        if (getLastAccessFrom() != null)
            jsonObject.put("last_access_from", getLastAccessFrom());
        if (getExpiredDate() != null)
            jsonObject.put("expired_date", DateTimeUtil.ToDateTimeString(getExpiredDate()));
        if (getFirstLogin() != null)
            jsonObject.put("first_login", getFirstLogin() ? 1 : 0);
        if (getAccountEnabled() != null)
            jsonObject.put("account_enabled", getAccountEnabled() ? 1 : 0);
        if (getAccountExpired() != null)
            jsonObject.put("account_expired", getAccountExpired() ? 1 : 0);
        if (getAccountLocked() != null)
            jsonObject.put("account_locked", getAccountLocked() ? 1 : 0);
        if (getAuthorizationRequired() != null)
            jsonObject.put("authorization_required", getAuthorizationRequired() ? 1 : 0);
        if (getLastPasswordUpdate() != null)
            jsonObject.put("last_password_update", DateTimeUtil.ToDateTimeString(getLastPasswordUpdate()));
        if (getMobileToken() != null)
            jsonObject.put("mobile_token", getMobileToken());
        if (getImeiNo() != null)
            jsonObject.put("imei_no", getImeiNo());
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
