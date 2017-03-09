package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ManagerProjectActivityUpdateListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityUpdateListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.ManagerNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.ManagerCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class ManagerProjectActivityUpdateListAsyncTask extends AsyncTask<ManagerProjectActivityUpdateListAsyncTaskParam, String, ManagerProjectActivityUpdateListAsyncTaskResult> {

    protected ManagerProjectActivityUpdateListAsyncTaskParam mManagerProjectActivityUpdateListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ManagerProjectActivityUpdateListAsyncTaskResult doInBackground(ManagerProjectActivityUpdateListAsyncTaskParam... managerProjectActivityUpdateListAsyncTaskParams) {
        // Get ManagerProjectActivityUpdateListAsyncTaskParam
        mManagerProjectActivityUpdateListAsyncTaskParam = managerProjectActivityUpdateListAsyncTaskParams[0];
        mContext = mManagerProjectActivityUpdateListAsyncTaskParam.getContext();
        ProjectActivityModel projectActivityModel = mManagerProjectActivityUpdateListAsyncTaskParam.getProjectActivityModel();

        // -- Prepare ManagerProjectActivityUpdateListAsyncTaskResult --
        ManagerProjectActivityUpdateListAsyncTaskResult managerProjectActivityUpdateListAsyncTaskResult = new ManagerProjectActivityUpdateListAsyncTaskResult();

        // -- Get ProjectActivityUpdateModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_list_handle_task_begin));

        // -- Prepare ManagerCachePersistent --
        ManagerCachePersistent managerCachePersistent = new ManagerCachePersistent(mContext);

        // -- Prepare ManagerNetwork --
        ManagerNetwork managerNetwork = new ManagerNetwork(mContext, mManagerProjectActivityUpdateListAsyncTaskParam.getSettingUserModel());

        ProjectActivityUpdateModel[] projectActivityUpdateModels = null;
        try {
            ProjectMemberModel projectMemberModel = mManagerProjectActivityUpdateListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    managerNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    managerNetwork.invalidateLogin();

                    // -- Get ProjectActivityUpdateModels from server --
                    projectActivityUpdateModels = managerNetwork.getProjectActivityUpdates(projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());

                    // -- Save to ManagerCachePersistent --
                    try {
                        managerCachePersistent.setProjectActivityUpdateModels(projectActivityUpdateModels, projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityUpdateModels from ManagerCachePersistent --
                        try {
                            projectActivityUpdateModels = managerCachePersistent.getProjectActivityUpdateModels(projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            managerProjectActivityUpdateListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityUpdateModels != null) {
            // -- Set result --
            managerProjectActivityUpdateListAsyncTaskResult.setProjectActivityUpdateModels(projectActivityUpdateModels);

            // -- Get ProjectActivityUpdateModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_list_handle_task_success));
        }

        return managerProjectActivityUpdateListAsyncTaskResult;
    }
}
