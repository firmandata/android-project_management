package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringSaveAsyncTaskResult;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.ProjectActivityMonitoringResponseModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class InspectorProjectActivityMonitoringSaveAsyncTask extends AsyncTask<InspectorProjectActivityMonitoringSaveAsyncTaskParam, String, InspectorProjectActivityMonitoringSaveAsyncTaskResult> {
    protected InspectorProjectActivityMonitoringSaveAsyncTaskParam mInspectorProjectActivityMonitoringSaveAsyncTaskParam;
    protected Context mContext;

    @Override
    protected InspectorProjectActivityMonitoringSaveAsyncTaskResult doInBackground(InspectorProjectActivityMonitoringSaveAsyncTaskParam... inspectorProjectActivityMonitoringSaveAsyncTaskParams) {
        // Get InspectorProjectActivityMonitoringSaveAsyncTaskParam
        mInspectorProjectActivityMonitoringSaveAsyncTaskParam = inspectorProjectActivityMonitoringSaveAsyncTaskParams[0];
        mContext = mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getContext();
        ProjectActivityMonitoringModel projectActivityMonitoringModel  = mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getProjectActivityMonitoringModel();

        // -- Prepare InspectorProjectActivityMonitoringSaveAsyncTaskResult --
        InspectorProjectActivityMonitoringSaveAsyncTaskResult inspectorProjectActivityMonitoringSaveAsyncTaskResult = new InspectorProjectActivityMonitoringSaveAsyncTaskResult();

        // -- Get ProjectActivityMonitoringModel save progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_project_activity_monitoring_save_handle_task_begin));

        // -- Prepare InspectorNetwork --
        InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getSettingUserModel());

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Save ProjectActivityMonitoringModel to server --
            try {
                ProjectActivityMonitoringResponseModel projectActivityMonitoringResponseModel = inspectorNetwork.saveProjectActivityMonitoring(
                    projectActivityMonitoringModel,
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhoto(),
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhotoAdditional1(),
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhotoAdditional2(),
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhotoAdditional3(),
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhotoAdditional4(),
                    mInspectorProjectActivityMonitoringSaveAsyncTaskParam.getPhotoAdditional5(),
                    projectMemberModel.getUserId());
                inspectorProjectActivityMonitoringSaveAsyncTaskResult.setProjectActivityMonitoringModel(projectActivityMonitoringResponseModel.getProjectActivityMonitoringModel());

                // -- Set InspectorProjectActivityMonitoringSaveAsyncTaskResult message --
                inspectorProjectActivityMonitoringSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.inspector_project_activity_monitoring_save_handle_task_success));
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Prepare NetworkPendingPersistent --
                    NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                    // -- Create NetworkPendingModel --
                    NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.INSPECTOR_PROJECT_ACTIVITY_MONITORING_SAVE);
                    if (projectActivityMonitoringModel.getProjectActivityMonitoringId() != null)
                        networkPendingModel.setCommandKey(String.valueOf(projectActivityMonitoringModel.getProjectActivityMonitoringId()));
                    else
                        networkPendingModel.setCommandKey(DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                    try {
                        // -- Save NetworkPendingModel to NetworkPendingPersistent
                        networkPendingPersistent.createNetworkPending(networkPendingModel);

                        // -- Set InspectorProjectActivityMonitoringSaveAsyncTaskResult of ProjectActivityMonitoringModel --
                        inspectorProjectActivityMonitoringSaveAsyncTaskResult.setProjectActivityMonitoringModel(projectActivityMonitoringModel);

                        // -- Set InspectorProjectActivityMonitoringSaveAsyncTaskResult message --
                        inspectorProjectActivityMonitoringSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.inspector_project_activity_monitoring_save_handle_task_success_pending));
                    } catch (PersistenceError ex) {
                    }
                } else {
                    // -- Set InspectorProjectActivityMonitoringSaveAsyncTaskResult message --
                    inspectorProjectActivityMonitoringSaveAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.inspector_project_activity_monitoring_save_handle_task_failed, webApiError.getMessage()));
                }
            }
        }

        // -- Get ProjectActivityMonitoringModel finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_project_activity_monitoring_save_handle_task_finish));

        return inspectorProjectActivityMonitoringSaveAsyncTaskResult;
    }
}