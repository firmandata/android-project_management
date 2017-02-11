package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.views.notification.NotificationLayout;

public class NotificationActivity extends AppCompatActivity implements NotificationLayout.NotificationLayoutListener {

    public static final String PARAM_NOTIFICATION_MODEL = "NotificationModel";

    protected NotificationModel mNotificationModel;

    protected NotificationLayout mNotificationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Get parameters --
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // -- Get NotificationModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(PARAM_NOTIFICATION_MODEL));
                mNotificationModel = NotificationModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Prepare NotificationLayout --
        mNotificationLayout = NotificationLayout.buildNotificationLayout(this, null);
        mNotificationLayout.setNotificationModel(mNotificationModel);
        mNotificationLayout.setNotificationLayoutListener(this);

        // -- Load NotificationLayout to activity --
        mNotificationLayout.loadLayoutToActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (mNotificationLayout.isNotificationFragmentShow()) {
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
        // -- Load NotificationFragment --
        mNotificationLayout.showNotificationDetailFragment(notificationModel);
    }
}
