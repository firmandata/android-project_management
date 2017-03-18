package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ManagerProjectActivityUpdateSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityUpdateSaveAsyncTaskResult;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.ProjectActivityUpdateResponseModel;
import com.construction.pm.networks.ManagerNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class ManagerProjectActivityUpdateSaveAsyncTask extends AsyncTask<ManagerProjectActivityUpdateSaveAsyncTaskParam, String, ManagerProjectActivityUpdateSaveAsyncTaskResult> {
    protected ManagerProjectActivityUpdateSaveAsyncTaskParam mManagerProjectActivityUpdateSaveAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ManagerProjectActivityUpdateSaveAsyncTaskResult doInBackground(ManagerProjectActivityUpdateSaveAsyncTaskParam... managerProjectActivityUpdateSaveAsyncTaskParams) {
        // Get ManagerProjectActivityUpdateSaveAsyncTaskParam
        mManagerProjectActivityUpdateSaveAsyncTaskParam = managerProjectActivityUpdateSaveAsyncTaskParams[0];
        mContext = mManagerProjectActivityUpdateSaveAsyncTaskParam.getContext();
        ProjectActivityUpdateModel projectActivityUpdateModel = mManagerProjectActivityUpdateSaveAsyncTaskParam.getProjectActivityUpdateModel();

        // -- Prepare ManagerProjectActivityUpdateSaveAsyncTaskResult --
        ManagerProjectActivityUpdateSaveAsyncTaskResult managerProjectActivityUpdateSaveAsyncTaskResult = new ManagerProjectActivityUpdateSaveAsyncTaskResult();

        // -- Get ProjectActivityUpdateModel save progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_save_handle_task_begin));

        // -- Prepare ManagerNetwork --
        ManagerNetwork managerNetwork = new ManagerNetwork(mContext, mManagerProjectActivityUpdateSaveAsyncTaskParam.getSettingUserModel());

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mManagerProjectActivityUpdateSaveAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Set NotificationModel as read to server --
            try {
                ProjectActivityUpdateResponseModel projectActivityUpdateResponseModel = managerNetwork.saveProjectActivityUpdate(projectActivityUpdateModel, projectMemberModel.getUserId());
                managerProjectActivityUpdateSaveAsyncTaskResult.setProjectActivityUpdateModel(projectActivityUpdateResponseModel.getProjectActivityUpdateModel());

                // -- Set ManagerProjectActivityUpdateSaveAsyncTaskResult message --
                managerProjectActivityUpdateSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_save_handle_task_success));
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Prepare NetworkPendingPersistent --
                    NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                    // -- Create NetworkPendingModel --
                    NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.PROJECT_ACTIVITY_UPDATE_SAVE);
                    if (projectActivityUpdateModel.getProjectActivityUpdateId() != null)
                        networkPendingModel.setCommandKey(String.valueOf(projectActivityUpdateModel.getProjectActivityUpdateId()));
                    else
                        networkPendingModel.setCommandKey(DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                    try {
                        // -- Save NetworkPendingModel to NetworkPendingPersistent
                        networkPendingPersistent.createNetworkPending(networkPendingModel);

                        // -- Set ManagerProjectActivityUpdateSaveAsyncTaskResult of ProjectActivityUpdateModel --
                        managerProjectActivityUpdateSaveAsyncTaskResult.setProjectActivityUpdateModel(projectActivityUpdateModel);

                        // -- Set ManagerProjectActivityUpdateSaveAsyncTaskResult message --
                        managerProjectActivityUpdateSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_save_handle_task_success_pending));
                    } catch (PersistenceError ex) {
                    }
                } else {
                    // -- Set ManagerProjectActivityUpdateSaveAsyncTaskResult message --
                    managerProjectActivityUpdateSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_save_handle_task_failed, webApiError.getMessage()));
                }
            }
        }
        
        // -- Get NotificationModel marked as read finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_update_save_handle_task_finish));

        return managerProjectActivityUpdateSaveAsyncTaskResult;
    }
}