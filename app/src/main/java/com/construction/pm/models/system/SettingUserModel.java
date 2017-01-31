package com.construction.pm.models.system;

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

    public SettingUserModel duplicate() {
        SettingUserModel settingUserModel = new SettingUserModel();
        settingUserModel.setServerUrl(getServerUrl());
        return settingUserModel;
    }
}
