package com.construction.pm.networks.webapi;

public class WebApiError extends Throwable {

    protected int mCode;
    protected String mMessage;
    protected Throwable mThrowable;

    public WebApiError(final int code, final String message) {
        setCode(code);
        setMessage(message);
    }

    public WebApiError(final int code, final String message, final Throwable cause) {
        this(code, message);
        setCause(cause);
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

    public void setCause(Throwable cause) {
        mThrowable = cause;
    }

    public Throwable getCause() {
        return mThrowable;
    }
}
