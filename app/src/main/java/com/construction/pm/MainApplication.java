package com.construction.pm;

import android.app.Application;

import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;

public class MainApplication extends Application {

    protected SettingUserModel mSettingUserModel;
    protected SessionLoginModel mSessionLoginModel;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setSettingUserModel(final SettingUserModel settingUserModel) {
        mSettingUserModel = settingUserModel;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }

    public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
        mSessionLoginModel = sessionLoginModel;
    }

    public SessionLoginModel getSessionLoginModel() {
        return mSessionLoginModel;
    }
}
