package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ReportRequestSendAsyncTaskParam;
import com.construction.pm.asynctask.result.ReportRequestSendAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.ReportRequestResponseModel;
import com.construction.pm.networks.ReportNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ReportCachePersistent;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class ReportRequestSendAsyncTask extends AsyncTask<ReportRequestSendAsyncTaskParam, String, ReportRequestSendAsyncTaskResult> {
    protected ReportRequestSendAsyncTaskParam mReportRequestSendAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ReportRequestSendAsyncTaskResult doInBackground(ReportRequestSendAsyncTaskParam... reportRequestSendAsyncTaskParams) {
        // Get ReportRequestSendAsyncTaskParam
        mReportRequestSendAsyncTaskParam = reportRequestSendAsyncTaskParams[0];
        mContext = mReportRequestSendAsyncTaskParam.getContext();
        ReportRequestModel reportRequestModel = mReportRequestSendAsyncTaskParam.getReportRequestModel();

        // -- Prepare ReportRequestSendAsyncTaskResult --
        ReportRequestSendAsyncTaskResult reportRequestSendAsyncTaskResult = new ReportRequestSendAsyncTaskResult();

        // -- Get ReportRequestModel save progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_send_handle_task_begin));

        // -- Prepare ReportNetwork --
        ReportNetwork reportNetwork = new ReportNetwork(mContext, mReportRequestSendAsyncTaskParam.getSettingUserModel());

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mReportRequestSendAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Save ReportRequestModel to server --
            try {
                ReportRequestResponseModel reportRequestResponseModel = reportNetwork.sendRequestReport(reportRequestModel, projectMemberModel.getUserId());
                reportRequestSendAsyncTaskResult.setReportRequestModels(reportRequestResponseModel.getReportRequestModels());

                // -- Set ReportRequestSendAsyncTaskResult message --
                reportRequestSendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_send_handle_task_success));
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Prepare NetworkPendingPersistent --
                    NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                    // -- Create NetworkPendingModel --
                    NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.REPORT_REQUEST_SEND);
                    if (reportRequestModel.getReportRequestId() != null)
                        networkPendingModel.setCommandKey(String.valueOf(reportRequestModel.getReportRequestId()));
                    else
                        networkPendingModel.setCommandKey(DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                    try {
                        // -- Save NetworkPendingModel to NetworkPendingPersistent
                        networkPendingPersistent.createNetworkPending(networkPendingModel);

                        // -- Get ReportRequestModels from ReportCachePersistent --
                        ReportCachePersistent reportCachePersistent = new ReportCachePersistent(mContext);
                        ReportRequestModel[] reportRequestModels = reportCachePersistent.getReportRequestModels(projectMemberModel.getProjectMemberId());

                        // -- Set ReportRequestSendAsyncTaskResult of ReportRequestModel --
                        reportRequestSendAsyncTaskResult.setReportRequestModels(reportRequestModels);

                        // -- Set ReportRequestSendAsyncTaskResult message --
                        reportRequestSendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_send_handle_task_success_pending));
                    } catch (PersistenceError ex) {
                    }
                } else {
                    // -- Set ReportRequestSendAsyncTaskResult message --
                    reportRequestSendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_send_handle_task_failed, webApiError.getMessage()));
                }
            }
        }
        
        // -- Get ReportRequestModel finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_send_handle_task_finish));

        return reportRequestSendAsyncTaskResult;
    }
}