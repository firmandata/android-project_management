package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.NotificationNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.notification.NotificationLayout;

public class NotificationActivity extends AppCompatActivity implements NotificationLayout.NotificationLayoutListener {

    public static final String INTENT_PARAM_NOTIFICATION_MODEL = "NOTIFICATION_MODEL";
    public static final String INTENT_PARAM_NOTIFICATION_FROM_NOTIFICATION_SERVICE = "NOTIFICATION_FROM_NOTIFICATION_SERVICE";

    protected boolean mIsFromNotificationService;

    protected NotificationModel mNotificationModel;

    protected NotificationLayout mNotificationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            // -- Prepare NotificationReadHandleTask --
            NotificationReadHandleTask projectHandleTask = new NotificationReadHandleTask() {
                @Override
                public void onPostExecute(NotificationReadHandleTaskResult notificationHandleTaskResult) {
                    if (notificationHandleTaskResult != null) {
                        onNotificationReadRequestSuccess(notificationHandleTaskResult.getNotificationModel());
                        if (notificationHandleTaskResult.getMessage() != null)
                            onNotificationReadRequestMessage(notificationHandleTaskResult.getMessage());
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

            // -- Do NotificationReadHandleTask --
            projectHandleTask.execute(new NotificationReadHandleTaskParam(this, settingUserModel, notificationModel, sessionLoginModel.getProjectMemberModel()));
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

    protected class NotificationReadHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected NotificationModel mNotificationModel;
        protected ProjectMemberModel mProjectMemberModel;

        public NotificationReadHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final NotificationModel notificationModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mNotificationModel = notificationModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public NotificationModel getNotificationModel() {
            return mNotificationModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class NotificationReadHandleTaskResult {

        protected NotificationModel mNotificationModel;
        protected String mMessage;

        public NotificationReadHandleTaskResult() {

        }

        public void setNotificationModel(final NotificationModel notificationModel) {
            mNotificationModel = notificationModel;
        }

        public NotificationModel getNotificationModel() {
            return mNotificationModel;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class NotificationReadHandleTask extends AsyncTask<NotificationReadHandleTaskParam, String, NotificationReadHandleTaskResult> {
        protected NotificationReadHandleTaskParam mNotificationReadHandleTaskParam;
        protected Context mContext;

        @Override
        protected NotificationReadHandleTaskResult doInBackground(NotificationReadHandleTaskParam... notificationReadHandleTaskParams) {
            // Get NotificationReadHandleTaskParam
            mNotificationReadHandleTaskParam = notificationReadHandleTaskParams[0];
            mContext = mNotificationReadHandleTaskParam.getContext();
            NotificationModel notificationModel = mNotificationReadHandleTaskParam.getNotificationModel();

            // -- Prepare NotificationReadHandleTaskResult --
            NotificationReadHandleTaskResult notificationReadHandleTaskResult = new NotificationReadHandleTaskResult();
            notificationReadHandleTaskResult.setNotificationModel(notificationModel);

            // -- Get NotificationModel marking as read progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_begin));

            // -- Prepare NotificationNetwork --
            NotificationNetwork notificationNetwork = new NotificationNetwork(mContext, mNotificationReadHandleTaskParam.getSettingUserModel());

            // -- Prepare NotificationPersistent --
            NotificationPersistent notificationPersistent = new NotificationPersistent(mContext);

            // -- Get ProjectMemberModel --
            ProjectMemberModel projectMemberModel = mNotificationReadHandleTaskParam.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    // -- Save NotificationModel to NotificationPersistent --
                    notificationPersistent.saveNotificationModel(notificationModel);

                    // -- Set NotificationModel as read to server --
                    try {
                        notificationNetwork.setNotificationRead(notificationModel.getProjectNotificationId(), projectMemberModel.getUserId());

                        // -- Set NotificationReadHandleTaskResult message --
                        notificationReadHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_success));
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Prepare NetworkPendingPersistent --
                            NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                            // -- Create NetworkPendingModel --
                            NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.NOTIFICATION_READ);
                            networkPendingModel.setCommandKey(String.valueOf(notificationModel.getProjectNotificationId()));
                            try {
                                // -- Save NetworkPendingModel to NetworkPendingPersistent
                                networkPendingPersistent.createNetworkPending(networkPendingModel);

                                // -- Set NotificationReadHandleTaskResult message --
                                notificationReadHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_success_pending));
                            } catch (PersistenceError ex) {
                            }
                        } else {
                            // -- Set NotificationReadHandleTaskResult message --
                            notificationReadHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_failed, webApiError.getMessage()));
                        }
                    }
                } catch (PersistenceError persistenceError) {
                    // -- Set NotificationReadHandleTaskResult message --
                    notificationReadHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_failed, persistenceError.getMessage()));
                }
            }

            // -- Get NotificationModel marked as read finish progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_finish));

            return notificationReadHandleTaskResult;
        }
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
}
