package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ManagerProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.ManagerNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.ManagerCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class ManagerProjectActivityListAsyncTask extends AsyncTask<ManagerProjectActivityListAsyncTaskParam, String, ManagerProjectActivityListAsyncTaskResult> {
    protected ManagerProjectActivityListAsyncTaskParam mManagerProjectActivityListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ManagerProjectActivityListAsyncTaskResult doInBackground(ManagerProjectActivityListAsyncTaskParam... managerProjectActivityListAsyncTaskParams) {
        // Get ManagerProjectActivityListAsyncTaskParam
        mManagerProjectActivityListAsyncTaskParam = managerProjectActivityListAsyncTaskParams[0];
        mContext = mManagerProjectActivityListAsyncTaskParam.getContext();

        // -- Prepare ManagerProjectActivityListAsyncTaskResult --
        ManagerProjectActivityListAsyncTaskResult managerProjectActivityListAsyncTaskResult = new ManagerProjectActivityListAsyncTaskResult();

        // -- Get ProjectActivityModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_list_handle_task_begin));

        // -- Prepare ManagerCachePersistent --
        ManagerCachePersistent managerCachePersistent = new ManagerCachePersistent(mContext);

        // -- Prepare ManagerNetwork --
        ManagerNetwork managerNetwork = new ManagerNetwork(mContext, mManagerProjectActivityListAsyncTaskParam.getSettingUserModel());

        ProjectActivityModel[] projectActivityModels = null;
        try {
            ProjectMemberModel projectMemberModel = mManagerProjectActivityListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    managerNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    managerNetwork.invalidateLogin();

                    // -- Get ProjectActivityModels from server --
                    projectActivityModels = managerNetwork.getProjectActivities(projectMemberModel.getProjectMemberId(), mManagerProjectActivityListAsyncTaskParam.getStatusTaskEnum());

                    // -- Save to ManagerCachePersistent --
                    try {
                        managerCachePersistent.setProjectActivityModels(projectActivityModels, mManagerProjectActivityListAsyncTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityModels from ManagerCachePersistent --
                        try {
                            projectActivityModels = managerCachePersistent.getProjectActivityModels(mManagerProjectActivityListAsyncTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            managerProjectActivityListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityModels != null) {
            // -- Set result --
            managerProjectActivityListAsyncTaskResult.setProjectActivityModels(projectActivityModels);

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_list_handle_task_success));
        }

        return managerProjectActivityListAsyncTaskResult;
    }
}