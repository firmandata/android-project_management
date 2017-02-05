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
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.Calendar;

public class AuthenticationNetwork {

    protected Context mContext;

    protected static final String OAUTH2_USER_NAME = "user";
    protected static final String OAUTH2_PASSWORD = "pass";
    protected static final String OAUTH2_CLIENT_ID = "mobile_apps";
    protected static final String OAUTH2_CLIENT_SECRET = "mobile#123";
    protected static final String OAUTH2_CLIENT_SCOPE = "userinfo cloud file node";

    protected SessionPersistent mSessionPersistent;
    protected SessionLoginModel mSessionLoginModel;
    protected WebApiRequest mWebApiRequest;

    public AuthenticationNetwork(final Context context, final SettingUserModel settingUserModel) {
        mContext = context;

        // -- Prepare SessionPersistent --
        mSessionPersistent = new SessionPersistent(mContext);

        // -- Prepare WebApiRequest --
        mWebApiRequest = new WebApiRequest(mContext);
        mWebApiRequest.setSettingUserModel(settingUserModel);
    }


    // ----------------------------------
    // -- SessionLoginModel Persistent --
    // ----------------------------------

    public void saveSessionLoginModel(final SessionLoginModel sessionLoginModel) {
        // -- Save SessionLoginModel to SessionPersistent --
        mSessionPersistent.setSessionLoginModel(sessionLoginModel);

        // -- Set SessionLoginModel cache --
        mSessionLoginModel = sessionLoginModel;
    }

    public SessionLoginModel getSessionLoginModel() {
        if (mSessionLoginModel == null) {
            // -- Get SessionLoginModel from SessionPersistent --
            return mSessionPersistent.getSessionLoginModel();
        }

        // -- Get SessionLoginModel cache --
        return mSessionLoginModel;
    }

    public void clearSessionLoginModel() {
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        sessionLoginModel.setAccessTokenModel(null);
        sessionLoginModel.setUserProjectMemberModel(null);
        saveSessionLoginModel(sessionLoginModel);
    }


    // --------------------
    // -- OAuth2 Handler --
    // --------------------

    public AccessTokenModel generateAccessToken() throws WebApiError {
        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("grant_type", "password");
        formData.add("username", OAUTH2_USER_NAME);
        formData.add("password", OAUTH2_PASSWORD);
        formData.add("client_id", OAUTH2_CLIENT_ID);
        formData.add("client_secret", OAUTH2_CLIENT_SECRET);
        formData.add("scope", OAUTH2_CLIENT_SCOPE);

        // -- Request new access token --
        WebApiResponse webApiResponse = mWebApiRequest.post("/oauth2/PasswordCredentials", null, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        AccessTokenModel accessTokenModel;
        try {
            accessTokenModel = AccessTokenModel.build(jsonObject);
            accessTokenModel.setTokenGenerateTime(Calendar.getInstance());
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return accessTokenModel;
    }

    public AccessTokenModel refreshAccessToken() throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", OAUTH2_CLIENT_ID);
        formData.add("client_secret", OAUTH2_CLIENT_SECRET);
        formData.add("scope", OAUTH2_CLIENT_SCOPE);
        if (accessTokenModel != null)
            formData.add("refresh_token", accessTokenModel.getRefreshToken());

        // -- Request refresh token --
        WebApiResponse webApiResponse = mWebApiRequest.post("/oauth2/RefreshToken", null, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        AccessTokenModel newAccessTokenModel;
        try {
            newAccessTokenModel = AccessTokenModel.build(jsonObject);
            newAccessTokenModel.setTokenGenerateTime(Calendar.getInstance());
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return newAccessTokenModel;
    }

    public SessionLoginModel invalidateAccessToken() throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        if (sessionLoginModel != null) {
            // -- Check if isAccessTokenExpired --
            if (sessionLoginModel.isAccessTokenExpired()) {
                // -- Get new AccessTokenModel --
                AccessTokenModel accessTokenModel = generateAccessToken();

                // -- Set new AccessTokenModel --
                sessionLoginModel.setAccessTokenModel(accessTokenModel);

                // -- Save SessionLoginModel --
                saveSessionLoginModel(sessionLoginModel);
            }
        } else {
            // -- Get AccessTokenModel --
            AccessTokenModel accessTokenModel = generateAccessToken();

            // -- Create new SessionLoginModel and set AccessTokenModel --
            sessionLoginModel = new SessionLoginModel();
            sessionLoginModel.setAccessTokenModel(accessTokenModel);

            // -- Save SessionLoginModel --
            saveSessionLoginModel(sessionLoginModel);
        }

        return sessionLoginModel;
    }
}