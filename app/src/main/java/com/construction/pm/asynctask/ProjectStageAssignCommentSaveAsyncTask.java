package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ProjectStageAssignCommentSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageAssignCommentSaveAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.ProjectStageAssignCommentResponseModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class ProjectStageAssignCommentSaveAsyncTask extends AsyncTask<ProjectStageAssignCommentSaveAsyncTaskParam, String, ProjectStageAssignCommentSaveAsyncTaskResult> {
    protected ProjectStageAssignCommentSaveAsyncTaskParam mProjectStageAssignCommentSaveAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ProjectStageAssignCommentSaveAsyncTaskResult doInBackground(ProjectStageAssignCommentSaveAsyncTaskParam... projectStageAssignCommentSaveAsyncTaskParams) {
        // Get ProjectStageAssignCommentSaveAsyncTaskParam
        mProjectStageAssignCommentSaveAsyncTaskParam = projectStageAssignCommentSaveAsyncTaskParams[0];
        mContext = mProjectStageAssignCommentSaveAsyncTaskParam.getContext();
        ProjectStageAssignCommentModel projectStageAssignCommentModel  = mProjectStageAssignCommentSaveAsyncTaskParam.getProjectStageAssignCommentModel();

        // -- Prepare ProjectStageAssignCommentSaveAsyncTaskResult --
        ProjectStageAssignCommentSaveAsyncTaskResult projectStageAssignCommentSaveAsyncTaskResult = new ProjectStageAssignCommentSaveAsyncTaskResult();

        // -- Get ProjectStageAssignCommentModel save progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_save_handle_task_begin));

        // -- Prepare ProjectNetwork --
        ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectStageAssignCommentSaveAsyncTaskParam.getSettingUserModel());

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mProjectStageAssignCommentSaveAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Save ProjectStageAssignCommentModel to server --
            try {
                ProjectStageAssignCommentResponseModel projectStageAssignCommentResponseModel = projectNetwork.saveProjectStageAssignComment(
                    projectStageAssignCommentModel,
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhoto(),
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhotoAdditional1(),
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhotoAdditional2(),
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhotoAdditional3(),
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhotoAdditional4(),
                    mProjectStageAssignCommentSaveAsyncTaskParam.getPhotoAdditional5(),
                    projectMemberModel.getUserId());
                projectStageAssignCommentSaveAsyncTaskResult.setProjectStageAssignCommentModel(projectStageAssignCommentResponseModel.getProjectStageAssignCommentModel());

                // -- Set ProjectStageAssignCommentSaveAsyncTaskResult message --
                projectStageAssignCommentSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_save_handle_task_success));
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Prepare NetworkPendingPersistent --
                    NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                    // -- Create NetworkPendingModel --
                    NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.PROJECT_STAGE_ASSIGN_COMMENT_SAVE);
                    if (projectStageAssignCommentModel.getProjectStageAssignCommentId() != null)
                        networkPendingModel.setCommandKey(String.valueOf(projectStageAssignCommentModel.getProjectStageAssignCommentId()));
                    else
                        networkPendingModel.setCommandKey(DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                    try {
                        // -- Save NetworkPendingModel to NetworkPendingPersistent
                        networkPendingPersistent.createNetworkPending(networkPendingModel);

                        // -- Set ProjectStageAssignCommentSaveAsyncTaskResult of ProjectStageAssignCommentModel --
                        projectStageAssignCommentSaveAsyncTaskResult.setProjectStageAssignCommentModel(projectStageAssignCommentModel);

                        // -- Set ProjectStageAssignCommentSaveAsyncTaskResult message --
                        projectStageAssignCommentSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_save_handle_task_success_pending));
                    } catch (PersistenceError ex) {
                    }
                } else {
                    // -- Set ProjectStageAssignCommentSaveAsyncTaskResult message --
                    projectStageAssignCommentSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_save_handle_task_failed, webApiError.getMessage()));
                }
            }
        }

        // -- Get ProjectStageAssignCommentModel finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_save_handle_task_finish));

        return projectStageAssignCommentSaveAsyncTaskResult;
    }
}