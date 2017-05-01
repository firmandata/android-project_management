package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectStageAssignCommentListAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageAssignCommentListAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ProjectStageAssignCommentListAsyncTask extends AsyncTask<ProjectStageAssignCommentListAsyncTaskParam, String, ProjectStageAssignCommentListAsyncTaskResult> {
    protected ProjectStageAssignCommentListAsyncTaskParam mProjectStageAssignCommentListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectStageAssignCommentListAsyncTaskResult doInBackground(ProjectStageAssignCommentListAsyncTaskParam... projectStageAssignCommentListAsyncTaskParams) {
        // Get ProjectStageAssignCommentListAsyncTaskParam
        mProjectStageAssignCommentListAsyncTaskParam = projectStageAssignCommentListAsyncTaskParams[0];
        mContext = mProjectStageAssignCommentListAsyncTaskParam.getContext();

        // -- Prepare ProjectStageAssignCommentListAsyncTaskResult --
        ProjectStageAssignCommentListAsyncTaskResult projectStageAssignCommentListAsyncTaskResult = new ProjectStageAssignCommentListAsyncTaskResult();

        // -- Get ProjectStageResponseModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_handle_task_begin));

        // -- Prepare ProjectCachePersistent --
        ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectStageAssignCommentListAsyncTaskParam.getSettingUserModel());

        ProjectStageAssignCommentModel[] projectStageAssignCommentModels = null;
        try {
            Integer projectStageId = mProjectStageAssignCommentListAsyncTaskParam.getProjectStageId();
            ProjectMemberModel projectMemberModel = mProjectStageAssignCommentListAsyncTaskParam.getProjectMemberModel();
            if (projectStageId != null && projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    projectNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    projectNetwork.invalidateLogin();

                    // -- Get ProjectStageAssignComments from server --
                    projectStageAssignCommentModels = projectNetwork.getProjectStageAssignComments(projectStageId);

                    // -- Save to ProjectCachePersistent --
                    try {
                        projectCachePersistent.setProjectStageAssignCommentModels(projectStageId, projectStageAssignCommentModels, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectStageResponseModel from ProjectCachePersistent --
                        try {
                            projectStageAssignCommentModels = projectCachePersistent.getProjectStageAssignCommentModels(projectStageId, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            projectStageAssignCommentListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectStageAssignCommentModels != null) {
            // -- Set result --
            projectStageAssignCommentListAsyncTaskResult.setProjectStageAssignCommentModels(projectStageAssignCommentModels);

            // -- Get ProjectStageResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_handle_task_success));
        }

        return projectStageAssignCommentListAsyncTaskResult;
    }
}