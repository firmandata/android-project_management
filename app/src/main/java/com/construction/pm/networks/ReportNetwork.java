package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.network.ReportRequestResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReportNetwork extends AuthenticationNetwork {

    public ReportNetwork(Context context, SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }

    public ReportRequestModel[] getReportRequests(final Integer projectMemberId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_member_id", projectMemberId);

        // -- Request get ReportRequestModels --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListReportRequest", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<ReportRequestModel> reportRequestModelList = new ArrayList<ReportRequestModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("reportRequest")) {
                    org.json.JSONArray jsonReportRequestModels = jsonResult.getJSONArray("reportRequest");
                    for (int reportRequestModelIdx = 0; reportRequestModelIdx < jsonReportRequestModels.length(); reportRequestModelIdx++) {
                        org.json.JSONObject jsonResultProjectActivityUpdate = jsonReportRequestModels.getJSONObject(reportRequestModelIdx);
                        reportRequestModelList.add(ReportRequestModel.build(jsonResultProjectActivityUpdate));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        ReportRequestModel[] reportRequestModels = new ReportRequestModel[reportRequestModelList.size()];
        reportRequestModelList.toArray(reportRequestModels);
        return reportRequestModels;
    }

    public ReportRequestResponseModel sendRequestReport(final ReportRequestModel reportRequestModel, final Integer userId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_id", reportRequestModel.getProjectId());
        formData.add("project_member_id", reportRequestModel.getProjectMemberId());
        formData.add("comment", reportRequestModel.getComment());
        formData.add("user_id", userId);

        // -- Request get ReportRequestResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/requestReportProject", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ReportRequestResponseModel reportRequestResponseModel = null;
        try {
            reportRequestResponseModel = ReportRequestResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return reportRequestResponseModel;
    }

    public ReportRequestResponseModel resendRequestReport(final Integer reportRequestId, final Integer userId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("report_request_id", reportRequestId);
        formData.add("last_user_id", userId);

        // -- Request get ReportRequestResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/resendReportProject", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ReportRequestResponseModel reportRequestResponseModel = null;
        try {
            reportRequestResponseModel = ReportRequestResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return reportRequestResponseModel;
    }
}
