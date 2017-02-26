package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.asynctask.SessionCheckAsyncTask;
import com.construction.pm.asynctask.param.SessionCheckAsyncTaskParam;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.SplashLayout;

import java.util.ArrayList;
import java.util.List;

public class BootstrapActivity extends AppCompatActivity {

    protected List<AsyncTask> mAsyncTaskList;

    protected SplashLayout mSplashLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare SplashLayout --
        mSplashLayout = SplashLayout.buildMainLayout(this, null);

        // -- Load SplashLayout to activity
        mSplashLayout.loadLayoutToActivity(this);

        // -- Prepare SessionCheckAsyncTask --
        SessionCheckAsyncTask sessionCheckAsyncTask = new SessionCheckAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(SessionLoginModel sessionLoginModel) {
                mAsyncTaskList.remove(this);

                onSessionHandleFinish(sessionLoginModel);
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onSessionHandleProgress(messages[0]);
                    }
                }
            }
        };

        // -- showProgressBar --
        mSplashLayout.showProgressBar();

        // -- Do SessionCheckAsyncTask --
        sessionCheckAsyncTask.execute(new SessionCheckAsyncTaskParam(this, settingUserModel));
    }

    protected void onSessionHandleFinish(final SessionLoginModel sessionLoginModel) {
        mSplashLayout.hideProgressBar();

        if (sessionLoginModel != null) {
            if (sessionLoginModel.getUserProjectMemberModel() == null || sessionLoginModel.isFirstLogin()) {
                // -- Redirect to AuthenticationActivity --
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);

                // -- Close activity --
                finish();
            } else {
                // -- Redirect to MainActivity --
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                // -- Close activity --
                finish();
            }
        }
    }

    protected void onSessionHandleProgress(final String message) {
        // -- Show progress message --
        mSplashLayout.setDescription(message);
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
