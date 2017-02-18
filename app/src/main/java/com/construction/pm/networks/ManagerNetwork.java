package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.network.ProjectActivityUpdateResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ManagerNetwork extends AuthenticationNetwork {
    public ManagerNetwork(Context context, SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }

    public ProjectActivityModel[] getProjectActivities(final Integer projectMemberId) throws WebApiError {
        return getProjectActivities(projectMemberId, null);
    }

    public ProjectActivityModel[] getProjectActivities(final Integer projectMemberId, final String statusTask) throws WebApiError {
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
        formData.add("status_task", statusTask);

        // -- Request get ProjectActivityModels --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectActivityAssignment", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<ProjectActivityModel> projectActivityModelList = new ArrayList<ProjectActivityModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("projectActivity")) {
                    org.json.JSONArray jsonResultProjectActivities = jsonResult.getJSONArray("projectActivity");
                    for (int resultProjectActivityIdx = 0; resultProjectActivityIdx < jsonResultProjectActivities.length(); resultProjectActivityIdx++) {
                        org.json.JSONObject jsonResultProjectActivity = jsonResultProjectActivities.getJSONObject(resultProjectActivityIdx);
                        projectActivityModelList.add(ProjectActivityModel.build(jsonResultProjectActivity));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        ProjectActivityModel[] projectActivityModels = new ProjectActivityModel[projectActivityModelList.size()];
        projectActivityModelList.toArray(projectActivityModels);
        return projectActivityModels;
    }

    public ProjectActivityMonitoringModel[] getProjectActivityMonitoring(final Integer projectActivityId, final Integer projectMemberId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_activity_id", projectActivityId);
        formData.add("project_member_id", projectMemberId);

        // -- Request get ProjectActivityMonitoringModels --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectActivityMonitoringByActivity", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<ProjectActivityMonitoringModel> projectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("projectActivityMonitoringByActivity")) {
                    org.json.JSONArray jsonResultProjectActivityMonitoringArray = jsonResult.getJSONArray("projectActivityMonitoringByActivity");
                    for (int resultProjectActivityMonitoringIdx = 0; resultProjectActivityMonitoringIdx < jsonResultProjectActivityMonitoringArray.length(); resultProjectActivityMonitoringIdx++) {
                        org.json.JSONObject jsonResultProjectActivityMonitoring = jsonResultProjectActivityMonitoringArray.getJSONObject(resultProjectActivityMonitoringIdx);
                        projectActivityMonitoringModelList.add(ProjectActivityMonitoringModel.build(jsonResultProjectActivityMonitoring));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        ProjectActivityMonitoringModel[] projectActivityMonitoringModels = new ProjectActivityMonitoringModel[projectActivityMonitoringModelList.size()];
        projectActivityMonitoringModelList.toArray(projectActivityMonitoringModels);
        return projectActivityMonitoringModels;
    }

    public ProjectActivityUpdateModel[] getProjectActivityUpdates(final Integer projectActivityId, final Integer projectMemberId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_activity_id", projectActivityId);
        formData.add("project_member_id", projectMemberId);

        // -- Request get ProjectActivityUpdateModels --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectActivityByActivityMember", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<ProjectActivityUpdateModel> projectActivityMonitoringModelList = new ArrayList<ProjectActivityUpdateModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("projectActivityByActivity")) {
                    org.json.JSONArray jsonResultProjectActivityUpdates = jsonResult.getJSONArray("projectActivityByActivity");
                    for (int resultProjectActivityUpdateIdx = 0; resultProjectActivityUpdateIdx < jsonResultProjectActivityUpdates.length(); resultProjectActivityUpdateIdx++) {
                        org.json.JSONObject jsonResultProjectActivityUpdate = jsonResultProjectActivityUpdates.getJSONObject(resultProjectActivityUpdateIdx);
                        projectActivityMonitoringModelList.add(ProjectActivityUpdateModel.build(jsonResultProjectActivityUpdate));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        ProjectActivityUpdateModel[] projectActivityUpdateModels = new ProjectActivityUpdateModel[projectActivityMonitoringModelList.size()];
        projectActivityMonitoringModelList.toArray(projectActivityUpdateModels);
        return projectActivityUpdateModels;
    }

    public ProjectActivityUpdateResponseModel saveProjectActivityUpdate(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        ProjectActivityUpdateResponseModel projectActivityUpdateResponseModel = new ProjectActivityUpdateResponseModel();

        return projectActivityUpdateResponseModel;
    }
}
