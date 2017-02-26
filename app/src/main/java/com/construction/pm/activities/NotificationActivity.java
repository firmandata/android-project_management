package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.asynctask.NotificationReadAsyncTask;
import com.construction.pm.asynctask.param.NotificationReadAsyncTaskParam;
import com.construction.pm.asynctask.result.NotificationReadAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.notification.NotificationLayout;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationLayout.NotificationLayoutListener {

    public static final String INTENT_PARAM_NOTIFICATION_MODEL = "NOTIFICATION_MODEL";
    public static final String INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE = "NOTIFICATION_FROM_NOTIFICATION_SERVICE";

    protected boolean mIsFromNotificationService;

    protected NotificationModel mNotificationModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected NotificationLayout mNotificationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare NotificationLayout --
        mNotificationLayout = NotificationLayout.buildNotificationLayout(this, null);
        mNotificationLayout.setNotificationLayoutListener(this);

        // -- Load NotificationLayout to activity --
        mNotificationLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());

        // -- Handle page request by parameters --
        requestPageHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {
            // -- Get notification service flag parameter --
            if (bundle.containsKey(INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE)) {
                mIsFromNotificationService = bundle.getBoolean(INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE);
            }

            // -- Get NotificationModel parameter --
            if (bundle.containsKey(INTENT_PARAM_NOTIFICATION_MODEL)) {
                String notificationModelJson = bundle.getString(INTENT_PARAM_NOTIFICATION_MODEL);
                if (notificationModelJson != null) {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(notificationModelJson);
                        mNotificationModel = NotificationModel.build(jsonObject);
                    } catch (org.json.JSONException ex) {
                    }
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        if (mNotificationModel != null) {
            // -- Load NotificationFragment --
            mNotificationLayout.showNotificationDetailFragment(mNotificationModel);
        }
    }

    @Override
    public void onNotificationReadRequest(NotificationModel notificationModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Mark NotificationModel object as read --
            notificationModel.setNotificationStatus(NotificationModel.NOTIFICATION_STATUS_READ);
            notificationModel.setLastUserId(projectMemberModel.getUserId());

            // -- Prepare NotificationReadAsyncTask --
            NotificationReadAsyncTask notificationReadAsyncTask = new NotificationReadAsyncTask() {
                @Override
                public void onPreExecute() {
                    mAsyncTaskList.add(this);
                }

                @Override
                public void onPostExecute(NotificationReadAsyncTaskResult notificationReadAsyncTaskResult) {
                    mAsyncTaskList.remove(this);

                    if (notificationReadAsyncTaskResult != null) {
                        onNotificationReadRequestSuccess(notificationReadAsyncTaskResult.getNotificationModel());
                        if (notificationReadAsyncTaskResult.getMessage() != null)
                            onNotificationReadRequestMessage(notificationReadAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            onNotificationReadRequestMessage(messages[0]);
                        }
                    }
                }
            };

            // -- Do NotificationReadAsyncTask --
            notificationReadAsyncTask.execute(new NotificationReadAsyncTaskParam(this, settingUserModel, notificationModel, sessionLoginModel.getProjectMemberModel()));
        }
    }

    protected void onNotificationReadRequestSuccess(final NotificationModel notificationModel) {

    }

    protected void onNotificationReadRequestMessage(final String message) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (mNotificationLayout.isNotificationFragmentShow()) {
                    handleFinish();
                    finish();
                    return true;
                } else {
                    // -- Load NotificationFragment --
                    mNotificationLayout.showNotificationDetailFragment(mNotificationModel);
                }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        handleFinish();
        super.onBackPressed();
    }

    protected void handleFinish() {
        String notificationModelJson = null;
        if (mNotificationModel != null) {
            try {
                org.json.JSONObject jsonObject = mNotificationModel.build();
                notificationModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }
        }

        if (mIsFromNotificationService) {
            // -- Start MainActivity --
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.INTENT_PARAM_SHOW_DEFAULT_FRAGMENT, MainActivity.INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION);
            intent.putExtra(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL, notificationModelJson);
            startActivity(intent);
        } else {
            // -- Set result callback --
            Intent intent = new Intent();
            intent.putExtra(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL, notificationModelJson);
            setResult(RESULT_OK, intent);
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
