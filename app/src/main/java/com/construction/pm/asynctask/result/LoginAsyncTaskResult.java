package com.construction.pm.asynctask.result;

import com.construction.pm.models.system.SessionLoginModel;

public class LoginAsyncTaskResult {

    protected SessionLoginModel mSessionLoginModel;
    protected String mMessage;

    public LoginAsyncTaskResult() {

    }

    public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
        mSessionLoginModel = sessionLoginModel;
    }

    public SessionLoginModel getSessionLoginModel() {
        return mSessionLoginModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}