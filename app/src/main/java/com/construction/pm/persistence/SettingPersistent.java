package com.construction.pm.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.construction.pm.models.system.SettingUserModel;

import org.json.JSONException;

public class SettingPersistent {

    protected static final String SETTING_USER = "PROJECT_MANAGEMENT_SETTING_USER";
    protected static final String SETTING_USER_DATA = "DATA";

    protected Context mContext;

    public SettingPersistent(final Context context) {
        mContext = context;
    }

    public void setSettingUserModel(final SettingUserModel settingUserModel) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTING_USER, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(SETTING_USER_DATA, settingUserModel.build().toString(0));
        } catch (JSONException ex) {
        }

        editor.commit();
    }

    public SettingUserModel getSettingUserModel() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTING_USER, Context.MODE_PRIVATE);

        SettingUserModel settingUserModel = null;
        String settingUserModelJson = sharedPreferences.getString(SETTING_USER_DATA, null);
        if (settingUserModelJson != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(settingUserModelJson);
                settingUserModel = SettingUserModel.build(jsonObject);
            } catch (JSONException ex) {
            }
        }
        if (settingUserModel == null)
            settingUserModel = SettingUserModel.buildDefault();

        return settingUserModel;
    }
}
