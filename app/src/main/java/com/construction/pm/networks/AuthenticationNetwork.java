package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.models.system.UserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiRequest;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.AppUtil;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.Calendar;

public class AuthenticationNetwork {

    protected Context mContext;

    protected WebApiRequest mWebApiRequest;

    public AuthenticationNetwork(final Context context, final SettingUserModel settingUserModel) {
        mContext = context;

        mWebApiRequest = new WebApiRequest(mContext);
        mWebApiRequest.setSettingUserModel(settingUserModel);
    }

    public AccessTokenModel getAccessToken(final String username, final String password) throws Exception {
        WebApiParam formData = new WebApiParam();
        formData.add("grant_type", "password");
        formData.add("username", username);
        formData.add("password", password);
        formData.add("client_id", "mobile_apps");
        formData.add("client_secret", "mobile#123");
        formData.add("scope", "userinfo cloud file node");

        WebApiResponse webApiResponse = mWebApiRequest.post("/oauth2/PasswordCredentials", null, null, formData);

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

    public UserProjectMemberModel doLogin(final AccessTokenModel accessTokenModel, final String login, final String password) throws Exception {
        WebApiParam formData = new WebApiParam();
        formData.add("login", login);
        formData.add("password", password);

        WebApiParam headerParam = new WebApiParam();
        headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLogin", headerParam, null, formData);

        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw new Exception(webApiError.getMessage());

        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new Exception(ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        UserProjectMemberModel userProjectMemberModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonUserProjectMember = jsonObject.getJSONObject("result");
                userProjectMemberModel = UserProjectMemberModel.build(jsonUserProjectMember);
            }
        } catch (JSONException jsonException) {
            throw new Exception(jsonException.getMessage());
        }

        return userProjectMemberModel;
    }

    public UserProjectMemberModel getUserProjectMemberModel(final SessionLoginModel sessionLoginModel) throws Exception {
        UserModel userModel = null;
        if (sessionLoginModel.getUserProjectMemberModel() != null) {
            userModel = sessionLoginModel.getUserProjectMemberModel().getUserModel();
        }
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        WebApiParam formData = new WebApiParam();
        if (userModel != null) {
            formData.add("user_id", userModel.getUserId());
            formData.add("mobile_token", userModel.getMobileToken());
        }
        formData.add("imei", AppUtil.getImei(mContext));

        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null) {
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());
        }

        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLoginWithTokenKey", headerParam, null, formData);

        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw new Exception(webApiError.getMessage());

        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new Exception(ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        UserProjectMemberModel userProjectMemberModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonUserProjectMember = jsonObject.getJSONObject("result");
                userProjectMemberModel = UserProjectMemberModel.build(jsonUserProjectMember);
            }
        } catch (JSONException jsonException) {
            throw new Exception(jsonException.getMessage());
        }

        return userProjectMemberModel;
    }
}