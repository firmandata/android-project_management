package com.construction.pm.networks.webapi;

public class WebApiError {

    protected int mCode;
    protected String mMessage;
    protected Throwable mThrowable;

    public WebApiError(final int code, final String message) {
        setCode(code);
        setMessage(message);
    }

    public void setCode(final int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }
}
