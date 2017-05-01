package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.AccessTokenModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.network.ProjectPlanResponseModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.network.ProjectStageAssignCommentResponseModel;
import com.construction.pm.models.network.ProjectStageResponseModel;
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

        // -- Request get ProjectModels --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getListProjectAssignment", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

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
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
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

        // -- Request get ProjectResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProject", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectResponseModel projectResponseModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                projectResponseModel = ProjectResponseModel.build(jsonObject.getJSONObject("result"));
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectResponseModel;
    }

    public ProjectStageResponseModel getProjectStage(final Integer projectStageId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_stage_id", projectStageId);

        // -- Request get ProjectStageResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProjectStage", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectStageResponseModel projectStageResponseModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                projectStageResponseModel = ProjectStageResponseModel.build(jsonObject.getJSONObject("result"));
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectStageResponseModel;
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignComments(final Integer projectStageId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_stage_id", projectStageId);

        // -- Request get ProjectStageResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProjectStage", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectStageAssignCommentModel[] projectStageAssignCommentModels = null;
        try {
            if (!jsonObject.isNull("result")) {
                ProjectStageResponseModel projectStageResponseModel = ProjectStageResponseModel.build(jsonObject.getJSONObject("result"));
                if (projectStageResponseModel != null) {
                    projectStageAssignCommentModels = projectStageResponseModel.getProjectStageAssignCommentModels();
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectStageAssignCommentModels;
    }

    public ProjectPlanResponseModel getProjectPlan(final Integer projectPlanId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_plan_id", projectPlanId);

        // -- Request get ProjectPlanResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProjectPlan", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectPlanResponseModel projectPlanResponseModel = null;
        try {
            if (!jsonObject.isNull("result")) {
                projectPlanResponseModel = ProjectPlanResponseModel.build(jsonObject.getJSONObject("result"));
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectPlanResponseModel;
    }

    public ProjectStageAssignCommentResponseModel saveProjectStageAssignComment(
            final ProjectStageAssignCommentModel projectStageAssignCommentModel,
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
        formData.add("project_stage_assign_comment_id", projectStageAssignCommentModel.getProjectStageAssignCommentId());
        formData.add("project_stage_assignment_id", projectStageAssignCommentModel.getProjectStageAssignmentId());
        formData.add("comment_date", DateTimeUtil.ToDateTimeString(projectStageAssignCommentModel.getCommentDate()));
        formData.add("comment", projectStageAssignCommentModel.getComment());
        formData.add("photo_id", projectStageAssignCommentModel.getPhotoId());
        formData.add("photo_id", photo);
        formData.add("photo_additional1_id", projectStageAssignCommentModel.getPhotoAdditional1Id());
        formData.add("photo_additional1_id", photoAdditional1);
        formData.add("photo_additional2_id", projectStageAssignCommentModel.getPhotoAdditional2Id());
        formData.add("photo_additional2_id", photoAdditional2);
        formData.add("photo_additional3_id", projectStageAssignCommentModel.getPhotoAdditional3Id());
        formData.add("photo_additional3_id", photoAdditional3);
        formData.add("photo_additional4_id", projectStageAssignCommentModel.getPhotoAdditional4Id());
        formData.add("photo_additional4_id", photoAdditional4);
        formData.add("photo_additional5_id", projectStageAssignCommentModel.getPhotoAdditional5Id());
        formData.add("photo_additional5_id", photoAdditional5);
        formData.add("user_id", userId);

        // -- Request get ProjectStageAssignCommentResponseModel --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/saveProjectStageAssignmentComment", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(webApiResponse, 0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        ProjectStageAssignCommentResponseModel projectStageAssignCommentResponseModel = null;
        try {
            projectStageAssignCommentResponseModel = ProjectStageAssignCommentResponseModel.build(jsonObject);
        } catch (JSONException jsonException) {
            throw new WebApiError(webApiResponse, 0, jsonException.getMessage(), jsonException);
        }

        return projectStageAssignCommentResponseModel;
    }
}
