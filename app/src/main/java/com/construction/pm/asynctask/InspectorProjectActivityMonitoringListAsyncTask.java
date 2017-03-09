package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringListAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class InspectorProjectActivityMonitoringListAsyncTask extends AsyncTask<InspectorProjectActivityMonitoringListAsyncTaskParam, String, InspectorProjectActivityMonitoringListAsyncTaskResult> {

    protected InspectorProjectActivityMonitoringListAsyncTaskParam mInspectorProjectActivityMonitoringListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected InspectorProjectActivityMonitoringListAsyncTaskResult doInBackground(InspectorProjectActivityMonitoringListAsyncTaskParam... inspectorProjectActivityMonitoringListAsyncTaskParams) {
        // Get InspectorProjectActivityMonitoringListAsyncTaskParam
        mInspectorProjectActivityMonitoringListAsyncTaskParam = inspectorProjectActivityMonitoringListAsyncTaskParams[0];
        mContext = mInspectorProjectActivityMonitoringListAsyncTaskParam.getContext();
        ProjectActivityModel projectActivityModel = mInspectorProjectActivityMonitoringListAsyncTaskParam.getProjectActivityModel();

        // -- Prepare InspectorProjectActivityMonitoringListAsyncTaskResult --
        InspectorProjectActivityMonitoringListAsyncTaskResult inspectorProjectActivityMonitoringListAsyncTaskResult = new InspectorProjectActivityMonitoringListAsyncTaskResult();

        // -- Get ProjectActivityMonitoringModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_monitoring_list_handle_task_begin));

        // -- Prepare InspectorCachePersistent --
        InspectorCachePersistent inspectorCachePersistent = new InspectorCachePersistent(mContext);

        // -- Prepare InspectorNetwork --
        InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mInspectorProjectActivityMonitoringListAsyncTaskParam.getSettingUserModel());

        ProjectActivityMonitoringModel[] projectActivityMonitoringModels = null;
        try {
            ProjectMemberModel projectMemberModel = mInspectorProjectActivityMonitoringListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    inspectorNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    inspectorNetwork.invalidateLogin();

                    // -- Get ProjectActivityMonitoringModels from server --
                    projectActivityMonitoringModels = inspectorNetwork.getProjectActivityMonitoring(projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());

                    // -- Save to InspectorCachePersistent --
                    try {
                        inspectorCachePersistent.setProjectActivityMonitoringModels(projectActivityMonitoringModels, projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityMonitoringModels from InspectorCachePersistent --
                        try {
                            projectActivityMonitoringModels = inspectorCachePersistent.getProjectActivityMonitoringModels(projectActivityModel.getProjectActivityId(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            inspectorProjectActivityMonitoringListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityMonitoringModels != null) {
            // -- Set result --
            inspectorProjectActivityMonitoringListAsyncTaskResult.setProjectActivityMonitoringModels(projectActivityMonitoringModels);

            // -- Get ProjectActivityMonitoringModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_monitoring_list_handle_task_success));
        }

        return inspectorProjectActivityMonitoringListAsyncTaskResult;
    }
}
