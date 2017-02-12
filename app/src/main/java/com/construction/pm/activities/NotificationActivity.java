package com.construction.pm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
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

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare NotificationPersistent --
        NotificationPersistent notificationPersistent = new NotificationPersistent(this);

        // -- Mark NotificationModel as read --
        if (mNotificationModel != null) {
            ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
            if (projectMemberModel != null) {
                try {
                    notificationPersistent.setNotificationRead(mNotificationModel, projectMemberModel.getProjectMemberId());
                } catch (PersistenceError persistenceError) {
                }
            }
        }

        // -- Prepare NotificationLayout --
        mNotificationLayout = NotificationLayout.buildNotificationLayout(this, null);
        mNotificationLayout.setNotificationModel(mNotificationModel);
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

        // -- Show NotificationDetailFragment --
        mNotificationLayout.showNotificationDetailFragment(mNotificationModel);
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
    public void onNotificationRequest(NotificationModel notificationModel) {
        // -- Show NotificationDetailFragment --
        mNotificationLayout.showNotificationDetailFragment(notificationModel);
    }

    @Override
    public void onBackPressed() {
        handleFinish();
        super.onBackPressed();
    }

    protected void handleFinish() {
        if (mIsFromNotificationService) {
            // -- Start MainActivity --
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.INTENT_PARAM_SHOW_DEFAULT_FRAGMENT, MainActivity.INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION);
            startActivity(intent);
        }
    }
}
