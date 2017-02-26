package com.construction.pm.asynctask.result;

import com.construction.pm.models.network.SimpleResponseModel;

public class UserChangeProfileAsyncTaskResult {

    protected SimpleResponseModel mSimpleResponseModel;
    protected String mMessage;

    public UserChangeProfileAsyncTaskResult() {

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