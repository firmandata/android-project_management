package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ManagerProjectActivityMonitoringListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityMonitoringListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.ManagerNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.ManagerCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class ManagerProjectActivityMonitoringListAsyncTask extends AsyncTask<ManagerProjectActivityMonitoringListAsyncTaskParam, String, ManagerProjectActivityMonitoringListAsyncTaskResult> {

    protected ManagerProjectActivityMonitoringListAsyncTaskParam mManagerProjectActivityMonitoringListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ManagerProjectActivityMonitoringListAsyncTaskResult doInBackground(ManagerProjectActivityMonitoringListAsyncTaskParam... managerProjectActivityMonitoringListAsyncTaskParams) {
        // Get ManagerProjectActivityMonitoringListAsyncTaskParam
        mManagerProjectActivityMonitoringListAsyncTaskParam = managerProjectActivityMonitoringListAsyncTaskParams[0];
        mContext = mManagerProjectActivityMonitoringListAsyncTaskParam.getContext();
        ProjectActivityModel projectActivityModel = mManagerProjectActivityMonitoringListAsyncTaskParam.getProjectActivityModel();

        // -- Prepare ManagerProjectActivityMonitoringListAsyncTaskResult --
        ManagerProjectActivityMonitoringListAsyncTaskResult managerProjectActivityMonitoringListAsyncTaskResult = new ManagerProjectActivityMonitoringListAsyncTaskResult();

        // -- Get ProjectActivityMonitoringModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_monitoring_list_handle_task_begin));

        // -- Prepare ManagerCachePersistent --
        ManagerCachePersistent managerCachePersistent = new ManagerCachePersistent(mContext);

        // -- Prepare ManagerNetwork --
        ManagerNetwork managerNetwork = new ManagerNetwork(mContext, mManagerProjectActivityMonitoringListAsyncTaskParam.getSettingUserModel());

        ProjectActivityMonitoringModel[] projectActivityMonitoringModels = null;
        try {
            ProjectMemberModel projectMemberModel = mManagerProjectActivityMonitoringListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    managerNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    managerNetwork.invalidateLogin();

                    // -- Get ProjectActivityMonitoringModels from server --
                    projectActivityMonitoringModels = managerNetwork.getProjectActivityMonitoring(projectActivityModel.getProjectActivityId());

                    // -- Save to ManagerCachePersistent --
                    try {
                        managerCachePersistent.setProjectActivityMonitoringModels(projectActivityMonitoringModels, projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityMonitoringModels from ManagerCachePersistent --
                        try {
                            projectActivityMonitoringModels = managerCachePersistent.getProjectActivityMonitoringModels(projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            managerProjectActivityMonitoringListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityMonitoringModels != null) {
            // -- Set result --
            managerProjectActivityMonitoringListAsyncTaskResult.setProjectActivityMonitoringModels(projectActivityMonitoringModels);

            // -- Get ProjectActivityMonitoringModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_monitoring_list_handle_task_success));
        }

        return managerProjectActivityMonitoringListAsyncTaskResult;
    }
}
