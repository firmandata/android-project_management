package com.construction.pm;

import android.app.Application;

import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;

public class MainApplication extends Application {

    protected SettingUserModel mSettingUserModel;
    protected SessionLoginModel mSessionLoginModel;

    @Override
    public void onCreate() {
        super.onCreate();

        // -- Get setting user model persistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        setSettingUserModel(settingPersistent.getSettingUserModel());

        // -- Get session login model persistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        setSessionLoginModel(sessionPersistent.getSessionLoginModel());
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
