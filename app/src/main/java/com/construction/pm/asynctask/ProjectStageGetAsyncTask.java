package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectStageGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageGetAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.network.ProjectStageResponseModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectStageGetAsyncTask extends AsyncTask<ProjectStageGetAsyncTaskParam, String, ProjectStageGetAsyncTaskResult> {
    protected ProjectStageGetAsyncTaskParam mProjectStageGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectStageGetAsyncTaskResult doInBackground(ProjectStageGetAsyncTaskParam... projectStageGetAsyncTaskParams) {
        // Get ProjectStageGetAsyncTaskParam
        mProjectStageGetAsyncTaskParam = projectStageGetAsyncTaskParams[0];
        mContext = mProjectStageGetAsyncTaskParam.getContext();

        // -- Prepare ProjectStageGetAsyncTaskResult --
        ProjectStageGetAsyncTaskResult projectStageGetAsyncTaskResult = new ProjectStageGetAsyncTaskResult();

        // -- Get ProjectStageResponseModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectStageGetAsyncTaskParam.getSettingUserModel());

        ProjectStageResponseModel projectStageResponseModel = null;
        try {
            Integer projectStageId = mProjectStageGetAsyncTaskParam.getProjectStageId();
            ProjectMemberModel projectMemberModel = mProjectStageGetAsyncTaskParam.getProjectMemberModel();
            if (projectStageId != null && projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get project from server --
                    projectStageResponseModel = projectNetwork.getProjectStage(projectStageId);

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectStageResponseModel(projectStageResponseModel, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectStageResponseModel from ProjectCachePersistent --
                        try {
                            projectStageResponseModel = projectCachePersistent.getProjectStageResponseModel(projectStageId, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectStageGetAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectStageResponseModel != null) {
            // -- Set result --
            projectStageGetAsyncTaskResult.setProjectStageModel(projectStageResponseModel.getProjectStageModel());
            projectStageGetAsyncTaskResult.setProjectStageAssignmentModels(projectStageResponseModel.getProjectStageAssignmentModels());
            projectStageGetAsyncTaskResult.setProjectStageDocumentModels(projectStageResponseModel.getProjectStageDocumentModels());
            projectStageGetAsyncTaskResult.setProjectStageAssignCommentModels(projectStageResponseModel.getProjectStageAssignCommentModels());

            // -- Get ProjectStageResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_handle_task_success));
        }

        return projectStageGetAsyncTaskResult;
    }
}