package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.InspectorProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class InspectorProjectActivityListAsyncTask extends AsyncTask<InspectorProjectActivityListAsyncTaskParam, String, InspectorProjectActivityListAsyncTaskResult> {
    protected InspectorProjectActivityListAsyncTaskParam mInspectorProjectActivityListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected InspectorProjectActivityListAsyncTaskResult doInBackground(InspectorProjectActivityListAsyncTaskParam... inspectorProjectActivityListAsyncTaskParams) {
        // Get InspectorProjectActivityListAsyncTaskParam
        mInspectorProjectActivityListAsyncTaskParam = inspectorProjectActivityListAsyncTaskParams[0];
        mContext = mInspectorProjectActivityListAsyncTaskParam.getContext();

        // -- Prepare InspectorProjectActivityListAsyncTaskResult --
        InspectorProjectActivityListAsyncTaskResult inspectorProjectActivityListAsyncTaskResult = new InspectorProjectActivityListAsyncTaskResult();

        // -- Get ProjectActivityModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_list_handle_task_begin));

        // -- Prepare InspectorCachePersistent --
        InspectorCachePersistent inspectorCachePersistent = new InspectorCachePersistent(mContext);

        // -- Prepare InspectorNetwork --
        InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mInspectorProjectActivityListAsyncTaskParam.getSettingUserModel());

        ProjectActivityModel[] projectActivityModels = null;
        try {
            ProjectMemberModel projectMemberModel = mInspectorProjectActivityListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    inspectorNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    inspectorNetwork.invalidateLogin();

                    // -- Get ProjectActivityModels from server --
                    projectActivityModels = inspectorNetwork.getProjectActivities(projectMemberModel.getProjectMemberId(), mInspectorProjectActivityListAsyncTaskParam.getStatusTaskEnum());

                    // -- Save to InspectorCachePersistent --
                    try {
                        inspectorCachePersistent.setProjectActivityModels(projectActivityModels, mInspectorProjectActivityListAsyncTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityModels from InspectorCachePersistent --
                        try {
                            projectActivityModels = inspectorCachePersistent.getProjectActivityModels(mInspectorProjectActivityListAsyncTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            inspectorProjectActivityListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (projectActivityModels != null) {
            // -- Set result --
            inspectorProjectActivityListAsyncTaskResult.setProjectActivityModels(projectActivityModels);

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_list_handle_task_success));
        }

        return inspectorProjectActivityListAsyncTaskResult;
    }
}