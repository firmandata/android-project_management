package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectPlanGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectPlanGetAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.network.ProjectPlanResponseModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectPlanGetAsyncTask extends AsyncTask<ProjectPlanGetAsyncTaskParam, String, ProjectPlanGetAsyncTaskResult> {
    protected ProjectPlanGetAsyncTaskParam mProjectPlanGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectPlanGetAsyncTaskResult doInBackground(ProjectPlanGetAsyncTaskParam... projectPlanGetAsyncTaskParams) {
        // Get ProjectPlanGetAsyncTaskParam
        mProjectPlanGetAsyncTaskParam = projectPlanGetAsyncTaskParams[0];
        mContext = mProjectPlanGetAsyncTaskParam.getContext();

        // -- Prepare ProjectPlanGetAsyncTaskResult --
        ProjectPlanGetAsyncTaskResult projectPlanGetAsyncTaskResult = new ProjectPlanGetAsyncTaskResult();

        // -- Get ProjectPlanResponseModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_plan_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectPlanGetAsyncTaskParam.getSettingUserModel());

        ProjectPlanResponseModel projectPlanResponseModel = null;
        try {
            ProjectPlanModel projectPlanModel = mProjectPlanGetAsyncTaskParam.getProjectPlanModel();
            ProjectMemberModel projectMemberModel = mProjectPlanGetAsyncTaskParam.getProjectMemberModel();
            if (projectPlanModel != null && projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get project from server --
                    projectPlanResponseModel = projectNetwork.getProjectPlan(projectPlanModel.getProjectPlanId());

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectPlanResponseModel(projectPlanResponseModel, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectPlanResponseModel from ProjectCachePersistent --
                        try {
                            projectPlanResponseModel = projectCachePersistent.getProjectPlanResponseModel(projectPlanModel.getProjectPlanId(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectPlanGetAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectPlanResponseModel != null) {
            // -- Set result --
            projectPlanGetAsyncTaskResult.setProjectModel(projectPlanResponseModel.getProjectPlanModel());
            projectPlanGetAsyncTaskResult.setProjectPlanAssignmentModels(projectPlanResponseModel.getProjectPlanAssignmentModels());
            projectPlanGetAsyncTaskResult.setProjectActivityUpdateModels(projectPlanResponseModel.getProjectActivityUpdateModels());

            // -- Get ProjectPlanResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_plan_handle_task_success));
        }

        return projectPlanGetAsyncTaskResult;
    }
}