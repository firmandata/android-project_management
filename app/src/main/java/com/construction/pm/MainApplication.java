package com.construction.pm;

import android.app.Application;

import com.construction.pm.libraries.SqliteOpenerHandler;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SQLitePersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;

public class MainApplication extends Application {

    protected SQLitePersistent mSQLitePersistent;
    protected SettingUserModel mSettingUserModel;

    @Override
    public void onCreate() {
        super.onCreate();

        // -- Open SQLite database connection --
        mSQLitePersistent = new SQLitePersistent(this);

        // -- Get setting user model persistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        setSettingUserModel(settingPersistent.getSettingUserModel());
    }

    public SQLitePersistent getSQLitePersistent() {
        return mSQLitePersistent;
    }

    public void setSettingUserModel(final SettingUserModel settingUserModel) {
        mSettingUserModel = settingUserModel;
    }

    public SettingUserModel getSettingUserModel() {
        return mSettingUserModel;
    }
}
