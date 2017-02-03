package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiRequest;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.Calendar;

public class AuthenticationNetwork {

    protected Context mContext;

    protected SessionLoginModel mSessionLoginModel;
    protected SettingUserModel mSettingUserModel;

    protected WebApiRequest mWebApiRequest;

    public AuthenticationNetwork(final Context context) {
        mContext = context;

        mWebApiRequest = new WebApiRequest(mContext);
    }

    public void setSettingUserModel(final SettingUserModel settingUserModel) {
        mSettingUserModel = settingUserModel;
    }

    public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
        mSessionLoginModel = sessionLoginModel;
    }

    public AccessTokenModel generateAccessToken(final String username, final String password) throws Exception {
        WebApiParam formData = new WebApiParam();
        formData.add("grant_type", "password");
        formData.add("username", username);
        formData.add("password", password);
        formData.add("client_id", "mobile_apps");
        formData.add("client_secret", "mobile#123");
        formData.add("scope", "userinfo cloud file node");

        WebApiResponse webApiResponse = mWebApiRequest.post("/oauth2/PasswordCredentials", null, formData);

        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw new Exception(webApiError.getMessage());

        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new Exception(ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        AccessTokenModel accessTokenModel;
        try {
            accessTokenModel = AccessTokenModel.build(jsonObject);
            accessTokenModel.setTokenGenerateTime(Calendar.getInstance());
        } catch (JSONException jsonException) {
            throw new Exception(jsonException.getMessage());
        }

        return accessTokenModel;
    }

    public SessionLoginModel doLogin(final String login, final String password) throws Exception {
        WebApiParam formData = new WebApiParam();
        formData.add("login", login);
        formData.add("password", password);
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLogin", null, formData);

        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw new Exception(webApiError.getMessage());

        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new Exception(ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        SessionLoginModel sessionLoginModel;
        try {
            sessionLoginModel = SessionLoginModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new Exception(jsonException.getMessage());
        }

        return sessionLoginModel;
    }

    public SessionLoginModel updateSession() throws Exception {
        WebApiParam formData = new WebApiParam();
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLoginWithTokenKey", null, formData);

        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw new Exception(webApiError.getMessage());

        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new Exception(ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        SessionLoginModel sessionLoginModel;
        try {
            sessionLoginModel = SessionLoginModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new Exception(jsonException.getMessage());
        }

        return sessionLoginModel;
    }
}