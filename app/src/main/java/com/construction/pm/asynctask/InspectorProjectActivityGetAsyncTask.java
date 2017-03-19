package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.InspectorProjectActivityGetAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityGetAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class InspectorProjectActivityGetAsyncTask extends AsyncTask<InspectorProjectActivityGetAsyncTaskParam, String, InspectorProjectActivityGetAsyncTaskResult> {
    protected InspectorProjectActivityGetAsyncTaskParam mInspectorProjectActivityGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected InspectorProjectActivityGetAsyncTaskResult doInBackground(InspectorProjectActivityGetAsyncTaskParam... inspectorProjectActivityListAsyncTaskParams) {
        // Get InspectorProjectActivityGetAsyncTaskParam
        mInspectorProjectActivityGetAsyncTaskParam = inspectorProjectActivityListAsyncTaskParams[0];
        mContext = mInspectorProjectActivityGetAsyncTaskParam.getContext();
        Integer projectActivityId = mInspectorProjectActivityGetAsyncTaskParam.getProjectActivityId();

        // -- Prepare InspectorProjectActivityGetAsyncTaskResult --
        InspectorProjectActivityGetAsyncTaskResult inspectorProjectActivityListAsyncTaskResult = new InspectorProjectActivityGetAsyncTaskResult();

        // -- Get ProjectActivityModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_get_handle_task_begin));

        // -- Prepare InspectorCachePersistent --
        InspectorCachePersistent inspectorCachePersistent = new InspectorCachePersistent(mContext);

        // -- Prepare InspectorNetwork --
        InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mInspectorProjectActivityGetAsyncTaskParam.getSettingUserModel());

        ProjectActivityModel projectActivityModel = null;
        try {
            ProjectMemberModel projectMemberModel = mInspectorProjectActivityGetAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    inspectorNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    inspectorNetwork.invalidateLogin();

                    // -- Get ProjectActivityModels from server --
                    ProjectActivityModel[] projectActivityModels = inspectorNetwork.getProjectActivities(projectMemberModel.getProjectMemberId());

                    // -- Get ProjectActivityModel by projectActivityId --
                    if (projectActivityModels != null) {
                        int projectActivityIdOld = 0;
                        if (projectActivityId != null)
                            projectActivityIdOld = projectActivityId;
                        for (ProjectActivityModel projectActivityModelNew : projectActivityModels) {
                            int projectActivityIdNew = 0;
                            if (projectActivityModelNew.getProjectActivityId() != null)
                                projectActivityIdNew = projectActivityModelNew.getProjectActivityId();
                            if (projectActivityIdOld == projectActivityIdNew) {
                                projectActivityModel = projectActivityModelNew;
                                break;
                            }
                        }
                    }

                    if (projectActivityModel != null) {
                        // -- Save to InspectorCachePersistent --
                        try {
                            inspectorCachePersistent.setProjectActivityModel(projectActivityModel, projectActivityId, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityModel from InspectorCachePersistent --
                        try {
                            projectActivityModel = inspectorCachePersistent.getProjectActivityModel(projectActivityId, projectMemberModel.getProjectMemberId());
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

        if (projectActivityModel != null) {
            // -- Set result --
            inspectorProjectActivityListAsyncTaskResult.setProjectActivityModel(projectActivityModel);

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_get_handle_task_success));
        }

        return inspectorProjectActivityListAsyncTaskResult;
    }
}