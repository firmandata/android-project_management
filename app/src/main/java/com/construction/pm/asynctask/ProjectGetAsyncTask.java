package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectGetAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectGetAsyncTask extends AsyncTask<ProjectGetAsyncTaskParam, String, ProjectGetAsyncTaskResult> {
    protected ProjectGetAsyncTaskParam mProjectGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectGetAsyncTaskResult doInBackground(ProjectGetAsyncTaskParam... projectGetAsyncTaskParams) {
        // Get ProjectGetAsyncTaskParam
        mProjectGetAsyncTaskParam = projectGetAsyncTaskParams[0];
        mContext = mProjectGetAsyncTaskParam.getContext();

        // -- Prepare ProjectGetAsyncTaskResult --
        ProjectGetAsyncTaskResult projectGetAsyncTaskResult = new ProjectGetAsyncTaskResult();

        // -- Get ProjectResponseModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectGetAsyncTaskParam.getSettingUserModel());

        ProjectResponseModel projectResponseModel = null;
        try {
            ProjectModel projectModel = mProjectGetAsyncTaskParam.getProjectModel();
            ProjectMemberModel projectMemberModel = mProjectGetAsyncTaskParam.getProjectMemberModel();
            if (projectModel != null && projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get ProjectResponseModel from server --
                    projectResponseModel = projectNetwork.getProject(projectModel.getProjectId());

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectResponseModel(projectResponseModel, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectResponseModel from ProjectCachePersistent --
                        try {
                            projectResponseModel = projectCachePersistent.getProjectResponseModel(projectModel.getProjectId(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectGetAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectResponseModel != null) {
            // -- Set result --
            projectGetAsyncTaskResult.setContractModel(projectResponseModel.getContractModel());
            projectGetAsyncTaskResult.setProjectModel(projectResponseModel.getProjectModel());
            projectGetAsyncTaskResult.setProjectStageModels(projectResponseModel.getProjectStageModels());
            projectGetAsyncTaskResult.setProjectPlanModels(projectResponseModel.getProjectPlanModels());

            // -- Get ProjectResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_success));
        }

        return projectGetAsyncTaskResult;
    }
}