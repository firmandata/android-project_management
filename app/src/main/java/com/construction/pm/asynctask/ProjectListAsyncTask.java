package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectListAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectListAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectListAsyncTask extends AsyncTask<ProjectListAsyncTaskParam, String, ProjectListAsyncTaskResult> {
    protected ProjectListAsyncTaskParam mProjectListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectListAsyncTaskResult doInBackground(ProjectListAsyncTaskParam... projectListAsyncTaskParams) {
        // Get ProjectListAsyncTaskParam
        mProjectListAsyncTaskParam = projectListAsyncTaskParams[0];
        mContext = mProjectListAsyncTaskParam.getContext();

        // -- Prepare ProjectListAsyncTaskResult --
        ProjectListAsyncTaskResult projectListAsyncTaskResult = new ProjectListAsyncTaskResult();

        // -- Get ProjectModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_list_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectListAsyncTaskParam.getSettingUserModel());

        ProjectModel[] projectModels = null;
        try {
            ProjectMemberModel projectMemberModel = mProjectListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get projects from server --
                    projectModels = projectNetwork.getProjects(projectMemberModel.getProjectMemberId());

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectModels(projectModels, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectModels from ProjectCachePersistent --
                        try {
                            projectModels = projectCachePersistent.getProjectModels(projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectModels != null) {
            // -- Set ProjectModels to result --
            projectListAsyncTaskResult.setProjectModels(projectModels);

            // -- Get ProjectModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_list_handle_task_success));
        }

        return projectListAsyncTaskResult;
    }
}