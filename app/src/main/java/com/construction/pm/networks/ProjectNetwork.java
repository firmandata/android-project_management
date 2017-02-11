package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProjectNetwork extends AuthenticationNetwork {
    public ProjectNetwork(Context context, SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }

    public ProjectModel[] getProjects(final Integer projectMemberId) throws WebApiError {
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

        // -- Request get projects --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectAssignment", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<ProjectModel> projectModelList = new ArrayList<ProjectModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("project")) {
                    org.json.JSONArray jsonResultProjects = jsonResult.getJSONArray("project");
                    for (int resultProjectIdx = 0; resultProjectIdx < jsonResultProjects.length(); resultProjectIdx++) {
                        org.json.JSONObject jsonResultProject = jsonResultProjects.getJSONObject(resultProjectIdx);
                        projectModelList.add(ProjectModel.build(jsonResultProject));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        ProjectModel[] projectModels = new ProjectModel[projectModelList.size()];
        projectModelList.toArray(projectModels);
        return projectModels;
    }

    public ProjectResponseModel getProject(final Integer projectId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_id", projectId);

        // -- Request get projects --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProject", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectResponseModel projectResponseModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                projectResponseModel = ProjectResponseModel.build(jsonObject.getJSONObject("result"));
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        return projectResponseModel;
    }
}
