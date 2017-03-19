package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ManagerProjectActivityGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityGetAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.networks.ManagerNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.ManagerCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class ManagerProjectActivityGetAsyncTask extends AsyncTask<ManagerProjectActivityGetAsyncTaskParam, String, ManagerProjectActivityGetAsyncTaskResult> {
    protected ManagerProjectActivityGetAsyncTaskParam mManagerProjectActivityGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ManagerProjectActivityGetAsyncTaskResult doInBackground(ManagerProjectActivityGetAsyncTaskParam... managerProjectActivityListAsyncTaskParams) {
        // Get ManagerProjectActivityGetAsyncTaskParam
        mManagerProjectActivityGetAsyncTaskParam = managerProjectActivityListAsyncTaskParams[0];
        mContext = mManagerProjectActivityGetAsyncTaskParam.getContext();
        Integer projectActivityId = mManagerProjectActivityGetAsyncTaskParam.getProjectActivityId();

        // -- Prepare ManagerProjectActivityGetAsyncTaskResult --
        ManagerProjectActivityGetAsyncTaskResult managerProjectActivityListAsyncTaskResult = new ManagerProjectActivityGetAsyncTaskResult();

        // -- Get ProjectActivityModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_get_handle_task_begin));

        // -- Prepare ManagerCachePersistent --
        ManagerCachePersistent managerCachePersistent = new ManagerCachePersistent(mContext);

        // -- Prepare ManagerNetwork --
        ManagerNetwork managerNetwork = new ManagerNetwork(mContext, mManagerProjectActivityGetAsyncTaskParam.getSettingUserModel());

        ProjectActivityModel projectActivityModel = null;
        try {
            ProjectMemberModel projectMemberModel = mManagerProjectActivityGetAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    managerNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    managerNetwork.invalidateLogin();

                    // -- Get ProjectActivityModels from server --
                    ProjectActivityModel[] projectActivityModels = managerNetwork.getProjectActivities(projectMemberModel.getProjectMemberId());

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
                        // -- Save to ManagerCachePersistent --
                        try {
                            managerCachePersistent.setProjectActivityModel(projectActivityModel, projectActivityId, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ProjectActivityModel from ManagerCachePersistent --
                        try {
                            projectActivityModel = managerCachePersistent.getProjectActivityModel(projectActivityId, projectMemberModel.getProjectMemberId());
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

        if (projectActivityModel != null) {
            // -- Set result --
            managerProjectActivityListAsyncTaskResult.setProjectActivityModel(projectActivityModel);

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.manager_activity_get_handle_task_success));
        }

        return managerProjectActivityListAsyncTaskResult;
    }
}