package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringDetailAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringDetailAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class InspectorProjectActivityMonitoringDetailAsyncTask extends AsyncTask<InspectorProjectActivityMonitoringDetailAsyncTaskParam, String, InspectorProjectActivityMonitoringDetailAsyncTaskResult> {

    protected InspectorProjectActivityMonitoringDetailAsyncTaskParam mInspectorProjectActivityMonitoringDetailAsyncTaskParam;
    protected Context mContext;

    @Override
    protected InspectorProjectActivityMonitoringDetailAsyncTaskResult doInBackground(InspectorProjectActivityMonitoringDetailAsyncTaskParam... inspectorProjectActivityMonitoringDetailAsyncTaskParams) {
        // Get InspectorProjectActivityMonitoringDetailAsyncTaskParam
        mInspectorProjectActivityMonitoringDetailAsyncTaskParam = inspectorProjectActivityMonitoringDetailAsyncTaskParams[0];
        mContext = mInspectorProjectActivityMonitoringDetailAsyncTaskParam.getContext();
        Integer projectActivityMonitoringId = mInspectorProjectActivityMonitoringDetailAsyncTaskParam.getProjectActivityMonitoringId();
        Integer projectActivityId = mInspectorProjectActivityMonitoringDetailAsyncTaskParam.getProjectActivityId();

        // -- Prepare InspectorProjectActivityMonitoringDetailAsyncTaskResult --
        InspectorProjectActivityMonitoringDetailAsyncTaskResult inspectorProjectActivityMonitoringDetailAsyncTaskResult = new InspectorProjectActivityMonitoringDetailAsyncTaskResult();

        // -- Get ProjectActivityMonitoringModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_monitoring_detail_handle_task_begin));

        // -- Prepare InspectorCachePersistent --
        InspectorCachePersistent inspectorCachePersistent = new InspectorCachePersistent(mContext);

        // -- Prepare InspectorNetwork --
        InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mInspectorProjectActivityMonitoringDetailAsyncTaskParam.getSettingUserModel());

        ProjectActivityMonitoringModel projectActivityMonitoringModel = null;
        try {
            ProjectMemberModel projectMemberModel = mInspectorProjectActivityMonitoringDetailAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    inspectorNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    inspectorNetwork.invalidateLogin();

                    // -- Get ProjectActivityMonitoringModel from server --
                    projectActivityMonitoringModel = inspectorNetwork.getProjectActivityMonitoring(projectActivityMonitoringId, projectActivityId, projectMemberModel.getProjectMemberId());

                    // -- Save to InspectorCachePersistent --
                    try {
                        inspectorCachePersistent.setProjectActivityMonitoringModel(projectActivityMonitoringModel, projectActivityMonitoringId, projectActivityId, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityMonitoringModel from InspectorCachePersistent --
                        try {
                            projectActivityMonitoringModel = inspectorCachePersistent.getProjectActivityMonitoringModel(projectActivityMonitoringId, projectActivityId, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            inspectorProjectActivityMonitoringDetailAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityMonitoringModel != null) {
            // -- Set result --
            inspectorProjectActivityMonitoringDetailAsyncTaskResult.setProjectActivityMonitoringModel(projectActivityMonitoringModel);

            // -- Get ProjectActivityMonitoringModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_monitoring_detail_handle_task_success));
        }

        return inspectorProjectActivityMonitoringDetailAsyncTaskResult;
    }
}
