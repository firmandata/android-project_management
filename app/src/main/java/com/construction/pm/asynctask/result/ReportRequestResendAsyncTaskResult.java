package com.construction.pm.asynctask.result;

import com.construction.pm.models.ReportRequestModel;

public class ReportRequestResendAsyncTaskResult {
    protected ReportRequestModel[] mReportRequestModels;
    protected String mMessage;

    public ReportRequestResendAsyncTaskResult() {

    }

    public void setReportRequestModels(final ReportRequestModel[] reportRequestModels) {
        mReportRequestModels = reportRequestModels;
    }

    public ReportRequestModel[] getReportRequestModels() {
        return mReportRequestModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
