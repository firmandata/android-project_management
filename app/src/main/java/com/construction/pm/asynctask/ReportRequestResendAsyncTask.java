package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ReportRequestResendAsyncTaskParam;
import com.construction.pm.asynctask.result.ReportRequestResendAsyncTaskResult;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.ReportRequestResponseModel;
import com.construction.pm.networks.ReportNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class ReportRequestResendAsyncTask extends AsyncTask<ReportRequestResendAsyncTaskParam, String, ReportRequestResendAsyncTaskResult> {
    protected ReportRequestResendAsyncTaskParam mReportRequestResendAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ReportRequestResendAsyncTaskResult doInBackground(ReportRequestResendAsyncTaskParam... reportRequestResendAsyncTaskParams) {
        // Get ReportRequestResendAsyncTaskParam
        mReportRequestResendAsyncTaskParam = reportRequestResendAsyncTaskParams[0];
        mContext = mReportRequestResendAsyncTaskParam.getContext();
        ReportRequestModel reportRequestModel = mReportRequestResendAsyncTaskParam.getReportRequestModel();

        // -- Prepare ReportRequestResendAsyncTaskResult --
        ReportRequestResendAsyncTaskResult reportRequestResendAsyncTaskResult = new ReportRequestResendAsyncTaskResult();

        // -- Get ReportRequestModel save progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_resend_handle_task_begin));

        // -- Prepare ReportNetwork --
        ReportNetwork reportNetwork = new ReportNetwork(mContext, mReportRequestResendAsyncTaskParam.getSettingUserModel());

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mReportRequestResendAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Save ReportRequestModel to server --
            try {
                ReportRequestResponseModel reportRequestResponseModel = reportNetwork.resendRequestReport(reportRequestModel.getReportRequestId(), projectMemberModel.getUserId());
                reportRequestResendAsyncTaskResult.setReportRequestModel(reportRequestResponseModel.getReportRequestModel());

                // -- Set ReportRequestResendAsyncTaskResult message --
                reportRequestResendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_resend_handle_task_success));
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Prepare NetworkPendingPersistent --
                    NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                    // -- Create NetworkPendingModel --
                    NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.REPORT_REQUEST_RESEND);
                    if (reportRequestModel.getReportRequestId() != null)
                        networkPendingModel.setCommandKey(String.valueOf(reportRequestModel.getReportRequestId()));
                    else
                        networkPendingModel.setCommandKey(DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                    try {
                        // -- Save NetworkPendingModel to NetworkPendingPersistent
                        networkPendingPersistent.createNetworkPending(networkPendingModel);

                        // -- Set ReportRequestResendAsyncTaskResult of ReportRequestModel --
                        reportRequestResendAsyncTaskResult.setReportRequestModel(reportRequestModel);

                        // -- Set ReportRequestResendAsyncTaskResult message --
                        reportRequestResendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_resend_handle_task_success_pending));
                    } catch (PersistenceError ex) {
                    }
                } else {
                    // -- Set ReportRequestResendAsyncTaskResult message --
                    reportRequestResendAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.report_request_resend_handle_task_failed, webApiError.getMessage()));
                }
            }
        }
        
        // -- Get ReportRequestModel finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.report_request_resend_handle_task_finish));

        return reportRequestResendAsyncTaskResult;
    }
}