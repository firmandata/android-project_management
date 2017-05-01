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
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.SplashLayout;

import java.util.ArrayList;
import java.util.List;

public class BootstrapActivity extends AppCompatActivity {

    protected boolean mIsFromNotificationService;
    protected String mNotificationModelJson;

    protected List<AsyncTask> mAsyncTaskList;

    protected SplashLayout mSplashLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

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
        sessionCheckAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new SessionCheckAsyncTaskParam(this, settingUserModel));
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {
            // -- Get notification service flag parameter --
            if (bundle.containsKey(ConstantUtil.INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE)) {
                mIsFromNotificationService = bundle.getBoolean(ConstantUtil.INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE);
            }

            // -- Get notification service NotificationModel parameter --
            if (bundle.containsKey(ConstantUtil.INTENT_PARAM_NOTIFICATION_MODEL)) {
                mNotificationModelJson = bundle.getString(ConstantUtil.INTENT_PARAM_NOTIFICATION_MODEL);
            }
        }
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
                if (mIsFromNotificationService) {
                    if (mNotificationModelJson != null) {
                        // -- Redirect to NotificationActivity --
                        Intent intent = new Intent(this, NotificationActivity.class);
                        intent.putExtra(ConstantUtil.INTENT_PARAM_NOTIFICATION_MODEL, mNotificationModelJson);
                        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_NOTIFICATION_ACTIVITY);
                    } else {
                        // -- Redirect to MainActivity with default fragment is notification list --
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(MainActivity.INTENT_PARAM_SHOW_DEFAULT_FRAGMENT, MainActivity.INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION);
                        startActivity(intent);

                        // -- Close activity --
                        finish();
                    }
                } else {
                    // -- Redirect to MainActivity --
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                    // -- Close activity --
                    finish();
                }
            }
        }
    }

    protected void onSessionHandleProgress(final String message) {
        // -- Show progress message --
        mSplashLayout.setDescription(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_NOTIFICATION_ACTIVITY) {
            String notificationModelJson = null;
            if (bundle != null) {
                if (bundle.containsKey(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL)) {
                    notificationModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL);
                }
            }

            // -- Redirect to MainActivity with default fragment is notification list --
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.INTENT_PARAM_SHOW_DEFAULT_FRAGMENT, MainActivity.INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION);
            if (resultCode == RESULT_OK) {
                intent.putExtra(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL, notificationModelJson);
            }
            startActivity(intent);

            // -- Close activity --
            finish();
        }
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
