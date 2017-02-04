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
    protected WebApiError mWebApiError;

    public WebApiResponse(Context context) {
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
        WebApiError webApiError = getRestfulError(statusCode, null, response);
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
            WebApiError webApiError = getRestfulError(statusCode, throwable, null);
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
            WebApiError webApiError = getRestfulError(statusCode, throwable, null);
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
            webApiError = getRestfulError(statusCode, throwable, errorResponse);
        } catch (org.json.JSONException ex) {
            webApiError = getRestfulError(statusCode, ex, null);
        } catch (Exception ex) {
            webApiError = getRestfulError(statusCode, ex, null);
        }

        if (webApiError != null)
            onFailure(webApiError);
    }

    public WebApiError getRestfulError(final int statusCode, final Throwable throwable, final org.json.JSONObject errorResponse) {
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
                    if (errorCode >= 300) {
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
            webApiError = new WebApiError(newErrorCode, errorMessage);
            if (throwable != null)
                webApiError.setThrowable(throwable);
        }

        return webApiError;
    }

    public void onFinish() {

    }

    public Context getContext() {
        return mContext;
    }
}
