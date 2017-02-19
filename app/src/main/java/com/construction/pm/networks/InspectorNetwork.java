package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.network.ProjectActivityMonitoringResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InspectorNetwork extends AuthenticationNetwork {
    public InspectorNetwork(Context context, SettingUserModel settingUserModel) {
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
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectActivityMonitoringAssignment", headerParam, null, formData);

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
                if (!jsonResult.isNull("projectActivityMonitoring")) {
                    org.json.JSONArray jsonResultProjectActivities = jsonResult.getJSONArray("projectActivityMonitoring");
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
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectActivityMonitoringByActivityMember", headerParam, null, formData);

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

    public ProjectActivityMonitoringResponseModel saveProjectActivityMonitoring(
            final ProjectActivityMonitoringModel projectActivityMonitoringModel,
            final WebApiParam.WebApiParamFile photo,
            final WebApiParam.WebApiParamFile photoAdditional1,
            final WebApiParam.WebApiParamFile photoAdditional2,
            final WebApiParam.WebApiParamFile photoAdditional3,
            final WebApiParam.WebApiParamFile photoAdditional4,
            final WebApiParam.WebApiParamFile photoAdditional5,
            final Integer userId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_activity_monitoring_id", projectActivityMonitoringModel.getProjectActivityMonitoringId());
        formData.add("project_activity_id", projectActivityMonitoringModel.getProjectActivityId());
        formData.add("project_member_id", projectActivityMonitoringModel.getProjectMemberId());
        formData.add("monitoring_date", DateTimeUtil.ToDateTimeString(projectActivityMonitoringModel.getMonitoringDate()));
        formData.add("actual_start_date", DateTimeUtil.ToDateString(projectActivityMonitoringModel.getActualStartDate()));
        formData.add("actual_end_date", DateTimeUtil.ToDateString(projectActivityMonitoringModel.getActualEndDate()));
        formData.add("activity_status", projectActivityMonitoringModel.getActivityStatus());
        formData.add("percent_complete", projectActivityMonitoringModel.getPercentComplete());
        formData.add("comment", projectActivityMonitoringModel.getComment());
        formData.add("photo_id", projectActivityMonitoringModel.getPhotoId());
        formData.add("photo_id", photo);
        formData.add("photo_additional1_id", projectActivityMonitoringModel.getPhotoAdditional1Id());
        formData.add("photo_additional1_id", photoAdditional1);
        formData.add("photo_additional2_id", projectActivityMonitoringModel.getPhotoAdditional2Id());
        formData.add("photo_additional2_id", photoAdditional2);
        formData.add("photo_additional3_id", projectActivityMonitoringModel.getPhotoAdditional3Id());
        formData.add("photo_additional3_id", photoAdditional3);
        formData.add("photo_additional4_id", projectActivityMonitoringModel.getPhotoAdditional4Id());
        formData.add("photo_additional4_id", photoAdditional4);
        formData.add("photo_additional5_id", projectActivityMonitoringModel.getPhotoAdditional5Id());
        formData.add("photo_additional5_id", photoAdditional5);
        formData.add("user_id", userId);

        // -- Request get ProjectActivityMonitoringResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/saveProjectActivityMonitoring", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectActivityMonitoringResponseModel projectActivityMonitoringResponseModel = null;
        try {
            projectActivityMonitoringResponseModel = ProjectActivityMonitoringResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectActivityMonitoringResponseModel;
    }
}
