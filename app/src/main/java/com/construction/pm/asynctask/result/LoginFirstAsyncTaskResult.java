package com.construction.pm.asynctask.result;

import com.construction.pm.models.network.SimpleResponseModel;

public class LoginFirstAsyncTaskResult {

    protected SimpleResponseModel mSimpleResponseModel;
    protected String mMessage;

    public LoginFirstAsyncTaskResult() {

    }

    public void setSimpleResponseModel(final SimpleResponseModel simpleResponseModel) {
        mSimpleResponseModel = simpleResponseModel;
    }

    public SimpleResponseModel getSimpleResponseModel() {
        return mSimpleResponseModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}