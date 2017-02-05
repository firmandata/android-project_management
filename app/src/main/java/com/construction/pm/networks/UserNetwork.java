package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.models.system.UserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.AppUtil;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

public class UserNetwork extends AuthenticationNetwork {

    public UserNetwork(final Context context, final SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }


    // ----------------------------------
    // -- SessionLoginModel Persistent --
    // ----------------------------------

    public void logoutSessionLoginModel() {
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        sessionLoginModel.setUserProjectMemberModel(null);
        saveSessionLoginModel(sessionLoginModel);
    }


    // ---------------------------
    // -- UserNetwork Operation --
    // ---------------------------

    public UserProjectMemberModel doLogin(final String login, final String password) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("login", login);
        formData.add("password", password);

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", accessTokenModel.getTokenType() + " " + accessTokenModel.getAccessToken());

        // -- Request user login --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLogin", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        UserProjectMemberModel userProjectMemberModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonUserProjectMember = jsonObject.getJSONObject("result");
                userProjectMemberModel = UserProjectMemberModel.build(jsonUserProjectMember);
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return userProjectMemberModel;
    }

    public SessionLoginModel generateLogin(final String login, final String password) throws WebApiError {
        // -- Get doLogin result --
        UserProjectMemberModel userProjectMemberModel = doLogin(login, password);
        if (userProjectMemberModel != null) {
            // -- Get SessionLoginModel --
            SessionLoginModel sessionLoginModel = getSessionLoginModel();

            // -- Set UserProjectMemberModel --
            sessionLoginModel.setUserProjectMemberModel(userProjectMemberModel);

            // -- Save SessionLoginModel --
            saveSessionLoginModel(sessionLoginModel);

            return sessionLoginModel;
        }

        return null;
    }

    public UserProjectMemberModel getUserProjectMemberModel() throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();
        UserModel userModel = sessionLoginModel.getUserModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        if (userModel != null) {
            formData.add("user_id", userModel.getUserId());
            formData.add("mobile_token", userModel.getMobileToken());
        }
        formData.add("imei", AppUtil.getImei(mContext));

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Request user update --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/mobileLoginWithTokenKey", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        UserProjectMemberModel userProjectMemberModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonUserProjectMember = jsonObject.getJSONObject("result");
                userProjectMemberModel = UserProjectMemberModel.build(jsonUserProjectMember);
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return userProjectMemberModel;
    }

    public SessionLoginModel invalidateLogin() throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();

        try {
            // -- Get UserProjectMemberModel --
            UserProjectMemberModel userProjectMemberModel = getUserProjectMemberModel();
            if (userProjectMemberModel != null)
            {
                // -- Set new UserProjectMemberModel --
                sessionLoginModel.setUserProjectMemberModel(userProjectMemberModel);

                // -- Save SessionLoginModel --
                saveSessionLoginModel(sessionLoginModel);
            }
        } catch (WebApiError webApiError) {
            // -- Error with 400 means user login as expire --
            if (webApiError.getCode() == 400) {
                // -- Set UserProjectMemberModel as null --
                sessionLoginModel.setUserProjectMemberModel(null);

                // -- Save SessionLoginModel --
                saveSessionLoginModel(sessionLoginModel);
            }

            throw webApiError;
        }

        return sessionLoginModel;
    }

    public SimpleResponseModel changePasswordFirst(final String passwordNew) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();
        UserModel userModel = sessionLoginModel.getUserModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        if (userModel != null)
            formData.add("user_id", userModel.getUserId());
        formData.add("password", passwordNew);

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Request change password first --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/changePasswordFirstLogin", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = SimpleResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return simpleResponseModel;
    }
}
