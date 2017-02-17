package com.construction.pm.networks.webapi;

import java.io.IOException;
import java.net.ConnectException;

public class WebApiError extends Throwable {

    protected int mCode;
    protected String mMessage;
    protected Throwable mThrowable;
    protected WebApiResponse mWebApiResponse;

    public WebApiError(final WebApiResponse webApiResponse, final int code, final String message) {
        setWebApiResponse(webApiResponse);
        setCode(code);
        setMessage(message);
    }

    public WebApiError(final WebApiResponse webApiResponse, final int code, final String message, final Throwable cause) {
        this(webApiResponse, code, message);
        setCause(cause);
    }

    public void setWebApiResponse(final WebApiResponse webApiResponse) {
        mWebApiResponse = webApiResponse;
    }

    public WebApiResponse getWebApiResponse() {
        return mWebApiResponse;
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

    public void setCause(final Throwable cause) {
        mThrowable = cause;
    }

    public Throwable getCause() {
        return mThrowable;
    }

    public boolean isErrorConnection() {
        return isErrorConnection(getCause());
    }

    public boolean isErrorConnection(final Throwable throwable) {
        if (throwable == null)
            return false;

        if ((throwable instanceof ConnectException) || (throwable instanceof IOException))
            return true;

        return isErrorConnection(throwable.getCause());
    }
}
