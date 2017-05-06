package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ReportRequestListAsyncTaskParam;
import com.construction.pm.asynctask.result.ReportRequestListAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.networks.ReportNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ReportCachePersistent;
import com.construction.pm.utils.ViewUtil;

public class ReportRequestListAsyncTask extends AsyncTask<ReportRequestListAsyncTaskParam, String, ReportRequestListAsyncTaskResult> {
    protected ReportRequestListAsyncTaskParam mReportRequestListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ReportRequestListAsyncTaskResult doInBackground(ReportRequestListAsyncTaskParam... reportRequestListAsyncTaskParams) {
        // Get ReportRequestListAsyncTaskParam
        mReportRequestListAsyncTaskParam = reportRequestListAsyncTaskParams[0];
        mContext = mReportRequestListAsyncTaskParam.getContext();

        // -- Prepare ReportRequestListAsyncTaskResult --
        ReportRequestListAsyncTaskResult reportRequestListAsyncTaskResult = new ReportRequestListAsyncTaskResult();

        // -- Get ReportRequestModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_list_handle_task_begin));

        // -- Prepare ReportCachePersistent --
        ReportCachePersistent reportCachePersistent = new ReportCachePersistent(mContext);

        // -- Prepare ReportNetwork --
        ReportNetwork reportNetwork = new ReportNetwork(mContext, mReportRequestListAsyncTaskParam.getSettingUserModel());

        ReportRequestModel[] reportRequestModels = null;
        try {
            ProjectMemberModel projectMemberModel = mReportRequestListAsyncTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Invalidate Access Token --
                    reportNetwork.invalidateAccessToken();

                    // -- Invalidate Login --
                    reportNetwork.invalidateLogin();

                    // -- Get reportRequests from server --
                    reportRequestModels = reportNetwork.getReportRequests(projectMemberModel.getProjectMemberId());

                    // -- Save to ReportCachePersistent --
                    try {
                        reportCachePersistent.setReportRequestModels(reportRequestModels, projectMemberModel.getProjectMemberId());
                    } catch (PersistenceError ex) {
                    }
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Get ReportRequestModels from ReportCachePersistent --
                        try {
                            reportRequestModels = reportCachePersistent.getReportRequestModels(projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } else
                        throw webApiError;
                }
            }
        } catch (WebApiError webApiError) {
            reportRequestListAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (reportRequestModels != null) {
            // -- Set ReportRequestModels to result --
            reportRequestListAsyncTaskResult.setReportRequestModels(reportRequestModels);

            // -- Get ReportRequestModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_list_handle_task_success));
        }

        return reportRequestListAsyncTaskResult;
    }
}