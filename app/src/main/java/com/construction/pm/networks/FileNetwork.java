package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.FileUtil;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

public class FileNetwork extends AuthenticationNetwork {
    public FileNetwork(Context context, SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }

    public FileModel getFileInfo(final Integer fileId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("file_id", fileId);

        // -- Request get FileModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/file/getFile", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        FileModel fileModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
                if (!jsonResultObject.isNull("file"))
                    fileModel = FileModel.build(jsonResultObject.getJSONObject("file"));
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return fileModel;
    }

    public FileModel getFile(final Integer fileId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("file_id", fileId);

        // -- Request get FileModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/file/getFileWithBinary", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        FileModel fileModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
                if (!jsonResultObject.isNull("file")) {
                    org.json.JSONObject jsonResultObjectFile = jsonResultObject.getJSONObject("file");
                    fileModel = FileModel.build(jsonResultObjectFile);

                    // -- Save data to file cache --
                    if (!jsonResultObjectFile.isNull("binaryData")) {
                        byte[] fileData = FileUtil.toByteArray(jsonResultObjectFile.getString("binaryData"));
                        if (fileData != null)
                            fileModel.saveDataToFileCache(mContext, fileData);
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return fileModel;
    }
}
