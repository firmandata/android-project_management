package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.models.UserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

public class UserNetwork extends AuthenticationNetwork {

    public UserNetwork(final Context context, final SettingUserModel settingUserModel) {
        super(context, settingUserModel);
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
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = SimpleResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return simpleResponseModel;
    }

    public SimpleResponseModel changePassword(final String passwordOld, final String passwordNew) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();
        UserModel userModel = sessionLoginModel.getUserModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        if (userModel != null)
            formData.add("user_id", userModel.getUserId());
        formData.add("password_old", passwordOld);
        formData.add("password", passwordNew);

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Request change password first --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/changePassword", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = SimpleResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return simpleResponseModel;
    }

    public SimpleResponseModel updateProfile(final ProjectMemberModel projectMemberModel) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();
        UserModel userModel = sessionLoginModel.getUserModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        if (userModel != null)
            formData.add("user_id", userModel.getUserId());
        if (projectMemberModel != null) {
            formData.add("project_member_id", projectMemberModel.getProjectMemberId());
            formData.add("member_name", projectMemberModel.getMemberName());
            formData.add("phone_number", projectMemberModel.getPhoneNumber());
            formData.add("description", projectMemberModel.getDescription());
        }

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Request change password first --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/updateProfile", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = SimpleResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return simpleResponseModel;
    }

    public SimpleResponseModel forgetPassword(final String login) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("login", login);

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Request reset password --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/user/lostPassword", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = SimpleResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return simpleResponseModel;
    }
}
