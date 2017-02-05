package com.construction.pm.models.network;

import org.json.JSONException;

public class SimpleResponseModel {

    protected Integer mCode;
    protected String mMessage;

    public SimpleResponseModel() {

    }

    public SimpleResponseModel(final Integer code, final String message) {
        this();

        mCode = code;
        mMessage = message;
    }

    public void setCode(final Integer code) {
        mCode = code;
    }

    public Integer getCode() {
        return mCode;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isSuccess() {
        if (getCode() != null)
            return (getCode() == 200);

        return false;
    }

    public static SimpleResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        SimpleResponseModel simpleResponseModel = new SimpleResponseModel();

        if (!jsonObject.isNull("responseCode"))
            simpleResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            simpleResponseModel.setMessage(jsonObject.getString("responseMessage"));

        return simpleResponseModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());

        return jsonObject;
    }
}
