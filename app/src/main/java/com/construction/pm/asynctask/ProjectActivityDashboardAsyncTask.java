package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectActivityDashboardAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectActivityDashboardAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.ProjectActivityDashboardResponseModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectActivityDashboardAsyncTask extends AsyncTask<ProjectActivityDashboardAsyncTaskParam, String, ProjectActivityDashboardAsyncTaskResult> {
    protected ProjectActivityDashboardAsyncTaskParam mProjectActivityDashboardAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectActivityDashboardAsyncTaskResult doInBackground(ProjectActivityDashboardAsyncTaskParam... projectActivityDashboardAsyncTaskParams) {
        // Get ProjectActivityDashboardAsyncTaskParam
        mProjectActivityDashboardAsyncTaskParam = projectActivityDashboardAsyncTaskParams[0];
        mContext = mProjectActivityDashboardAsyncTaskParam.getContext();

        // -- Prepare ProjectActivityDashboardAsyncTaskResult --
        ProjectActivityDashboardAsyncTaskResult projectActivityDashboardAsyncTaskResult = new ProjectActivityDashboardAsyncTaskResult();

        // -- Get ProjectActivityDashboardResponseModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_activity_dashboard_list_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectActivityDashboardAsyncTaskParam.getSettingUserModel());

        ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel = null;
        try {
            ProjectMemberModel projectMemberModel = mProjectActivityDashboardAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get projectActivityDashboard from server --
                    projectActivityDashboardResponseModel = projectNetwork.getProjectActivityDashboard(projectMemberModel.getProjectMemberId());

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectActivityDashboardResponseModel(projectActivityDashboardResponseModel, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityDashboardResponseModel from ProjectCachePersistent --
                        try {
                            projectActivityDashboardResponseModel = projectCachePersistent.getProjectActivityDashboardResponseModel(projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectActivityDashboardAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityDashboardResponseModel != null) {
            // -- Set ProjectActivityDashboardResponseModel to result --
            projectActivityDashboardAsyncTaskResult.setProjectActivityDashboardResponseModel(projectActivityDashboardResponseModel);

            // -- Get ProjectActivityDashboardResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_activity_dashboard_list_handle_task_success));
        }

        return projectActivityDashboardAsyncTaskResult;
    }
}