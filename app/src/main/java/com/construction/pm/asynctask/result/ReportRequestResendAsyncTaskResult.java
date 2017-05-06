package com.construction.pm.asynctask.result;

import com.construction.pm.models.ReportRequestModel;

public class ReportRequestResendAsyncTaskResult {
    protected ReportRequestModel mReportRequestModel;
    protected String mMessage;

    public ReportRequestResendAsyncTaskResult() {

    }

    public void setReportRequestModel(final ReportRequestModel reportRequestModel) {
        mReportRequestModel = reportRequestModel;
    }

    public ReportRequestModel getReportRequestModel() {
        return mReportRequestModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
