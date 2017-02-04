package com.construction.pm.models.system;

import org.json.JSONException;

public class SettingUserModel {
    protected String mServerUrl;

    public SettingUserModel() {

    }

    public void setServerUrl(final String serverUrl) {
        mServerUrl = serverUrl;
    }

    public String getServerUrl() {
        return mServerUrl;
    }

    public static SettingUserModel buildDefault() {
        SettingUserModel settingUserModel = new SettingUserModel();
        settingUserModel.setServerUrl("http://sim-konstruksi.rafazsa.com");
        return settingUserModel;
    }

    public static SettingUserModel build(final org.json.JSONObject dataJson) throws JSONException {
        SettingUserModel settingUserModel = new SettingUserModel();

        if (!dataJson.isNull("server_url"))
            settingUserModel.setServerUrl(dataJson.getString("server_url"));

        return settingUserModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getServerUrl() != null)
            jsonObject.put("server_url", getServerUrl());

        return jsonObject;
    }
}
