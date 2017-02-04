package com.construction.pm.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.construction.pm.models.system.SessionLoginModel;

import org.json.JSONException;

public class SessionPersistent {
    protected static final String SESSION_LOGIN = "PROJECT_MANAGEMENT_SESSION_LOGIN";
    protected static final String SESSION_LOGIN_DATA = "DATA";

    protected Context mContext;

    public SessionPersistent(final Context context) {
        mContext = context;
    }

    public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SESSION_LOGIN, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(SESSION_LOGIN_DATA, sessionLoginModel.build().toString(0));
        } catch (JSONException ex) {
        }

        editor.commit();
    }

    public SessionLoginModel getSessionLoginModel() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SESSION_LOGIN, Context.MODE_PRIVATE);

        SessionLoginModel sessionLoginModel = null;
        String settingUserModelJson = sharedPreferences.getString(SESSION_LOGIN_DATA, null);
        if (settingUserModelJson != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(settingUserModelJson);
                sessionLoginModel = SessionLoginModel.build(jsonObject);
            } catch (JSONException ex) {
            }
        }

        return sessionLoginModel;
    }
}
