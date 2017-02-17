package com.construction.pm.networks.webapi;

import android.content.Context;
import android.text.TextUtils;

import com.construction.pm.R;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class WebApiResponse {

    protected Context mContext;

    protected org.json.JSONObject mSuccessJSONObject;
    protected org.json.JSONArray mSuccessJSONArray;

    protected String mApiUrl;
    protected WebApiParam mHeaderParam;
    protected WebApiParam mQueryParam;
    protected WebApiParam mFormData;
    protected String mBodyData;

    protected WebApiError mWebApiError;

    public WebApiResponse() {

    }

    public WebApiResponse(final Context context) {
        this();
        mContext = context;
    }

    public void onStart() {

    }

    public void onSuccess(final org.json.JSONObject response) {
        mSuccessJSONObject = response;
    }

    public org.json.JSONObject getSuccessJsonObject() {
        return mSuccessJSONObject;
    }

    public void onSuccess(final org.json.JSONArray response) {
        mSuccessJSONArray = response;
    }

    public org.json.JSONArray getSuccessJsonArray() {
        return mSuccessJSONArray;
    }

    public void onSuccess(final int statusCode, final Headers headers, final org.json.JSONObject response) {
        WebApiError webApiError = getWebApiError(statusCode, null, response);
        if (webApiError != null) {
            onFailure(webApiError);
        } else {
            onSuccess(response);
        }
    }

    public void onSuccess(final int statusCode, final Headers headers, final org.json.JSONArray response) {
        onSuccess(response);
    }

    public void onSuccess(final int statusCode, final Headers headers, final String responseString) {
        onFailure(statusCode, headers, responseString, new Throwable(ViewUtil.getResourceString(mContext, R.string.restful_message_error_response_json)));
    }

    public void onFailure(final WebApiError webApiError) {
        mWebApiError = webApiError;
    }

    public WebApiError getWebApiError() {
        return mWebApiError;
    }

    public void onFailure(final int statusCode, final Headers headers, final Throwable throwable, final org.json.JSONObject errorResponse) {
        if (errorResponse != null) {
            String errorString = null;
            try {
                errorString = errorResponse.toString(0);
            } catch (JSONException ex) {
            }
            onFailure(statusCode, headers, errorString, throwable);
        } else {
            WebApiError webApiError = getWebApiError(statusCode, throwable, null);
            if (webApiError != null)
                onFailure(webApiError);
        }
    }

    public void onFailure(final int statusCode, final Headers headers, final Throwable throwable, final org.json.JSONArray errorResponse) {
        if (errorResponse != null) {
            String errorString = null;
            try {
                errorString = errorResponse.toString(0);
            } catch (JSONException ex) {
            }
            onFailure(statusCode, headers, errorString, throwable);
        } else {
            WebApiError webApiError = getWebApiError(statusCode, throwable, null);
            if (webApiError != null)
                onFailure(webApiError);
        }
    }

    public void onFailure(final int statusCode, final Headers headers, final String responseString, final Throwable throwable) {
        WebApiError webApiError = null;
        try {
            org.json.JSONObject errorResponse = new org.json.JSONObject();
            errorResponse.put("responseCode", statusCode);
            if (responseString != null) {
                if (!responseString.isEmpty())
                    errorResponse.put("responseMessage", responseString);
            }
            webApiError = getWebApiError(statusCode, throwable, errorResponse);
        } catch (org.json.JSONException ex) {
            webApiError = getWebApiError(statusCode, ex, null);
        } catch (Exception ex) {
            webApiError = getWebApiError(statusCode, ex, null);
        }

        if (webApiError != null)
            onFailure(webApiError);
    }

    public WebApiError getWebApiError(final int statusCode, final Throwable throwable, final org.json.JSONObject errorResponse) {
        int newErrorCode = statusCode;
        List<String> messageList = new ArrayList<String>();

        if (statusCode == 400) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_400));
        } else if (statusCode == 401) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_401));
        } else if (statusCode == 403) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_403));
        } else if (statusCode == 404) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_404));
        } else if (statusCode == 500) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_500));
        } else if (statusCode == 503) {
            messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_503));
        }

        try {
            if (errorResponse != null) {
                if (!errorResponse.isNull("responseCode")) {
                    int errorCode = errorResponse.getInt("responseCode");
                    if (errorCode >= 400) {
                        newErrorCode = errorResponse.getInt("responseCode");
                        messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_code, String.valueOf(newErrorCode)));

                        if (!errorResponse.isNull("responseMessage")) {
                            String errorMessage = errorResponse.getString("responseMessage");
                            if (!errorMessage.isEmpty()) {
                                messageList.add(ViewUtil.getResourceString(mContext, R.string.restful_message_error_message, errorMessage));
                            }
                        }
                    }
                }
            }
        } catch (org.json.JSONException ex) {
            messageList.add(ex.getMessage());
        } catch (Exception ex) {
            messageList.add(ex.getMessage());
        }

        if (throwable != null) {
            messageList.add(throwable.getMessage());
        }

        String errorMessage = null;
        if (messageList.size() > 0) {
            String[] errorMessages = new String[messageList.size()];
            messageList.toArray(errorMessages);

            errorMessage = TextUtils.join("\n", errorMessages);
        }

        WebApiError webApiError = null;
        if (errorMessage != null) {
            webApiError = new WebApiError(this, newErrorCode, errorMessage);
            if (throwable != null)
                webApiError.setCause(throwable);
        }

        return webApiError;
    }

    public void onFinish() {

    }

    public Context getContext() {
        return mContext;
    }

    public void setApiUrl(final String apiUrl) {
        mApiUrl = apiUrl;
    }

    public String getApiUrl() {
        return mApiUrl;
    }

    public void setHeaderParam(final WebApiParam headerParam) {
        mHeaderParam = headerParam;
    }

    public WebApiParam getHeaderParam() {
        return mHeaderParam;
    }

    public void setQueryParam(final WebApiParam queryParam) {
        mQueryParam = queryParam;
    }

    public WebApiParam getQueryParam() {
        return mQueryParam;
    }

    public void setFormData(final WebApiParam formData) {
        mFormData = formData;
    }

    public WebApiParam getFormData() {
        return mFormData;
    }

    public void setBodyData(final String bodyData) {
        mBodyData = bodyData;
    }

    public String getBodyData() {
        return mBodyData;
    }

    public org.json.JSONObject buildWebApiRequestCommand() throws org.json.JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getApiUrl() != null)
            jsonObject.put("apiUrl", getApiUrl());
        if (getHeaderParam() != null)
            jsonObject.put("headerParam", getHeaderParam().build());
        if (getQueryParam() != null)
            jsonObject.put("queryParam", getQueryParam().build());
        if (getFormData() != null)
            jsonObject.put("formData", getFormData().build());
        if (getBodyData() != null)
            jsonObject.put("bodyData", getBodyData());

        return jsonObject;
    }

    public static WebApiResponse buildWebApiRequestCommand(final org.json.JSONObject webApiRequestCommand) throws org.json.JSONException {
        WebApiResponse webApiResponse = new WebApiResponse();

        if (!webApiRequestCommand.isNull("apiUrl"))
            webApiResponse.setApiUrl(webApiRequestCommand.getString("apiUrl"));
        if (!webApiRequestCommand.isNull("headerParam"))
            webApiResponse.setHeaderParam(WebApiParam.build(webApiRequestCommand.getJSONObject("headerParam")));
        if (!webApiRequestCommand.isNull("queryParam"))
            webApiResponse.setQueryParam(WebApiParam.build(webApiRequestCommand.getJSONObject("queryParam")));
        if (!webApiRequestCommand.isNull("formData"))
            webApiResponse.setFormData(WebApiParam.build(webApiRequestCommand.getJSONObject("formData")));
        if (!webApiRequestCommand.isNull("bodyData"))
            webApiResponse.setBodyData(webApiRequestCommand.getString("bodyData"));

        return webApiResponse;
    }
}
