package com.construction.pm.activities;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.activities.fragments.NotificationListFragment;
import com.construction.pm.activities.fragments.UserChangePasswordFragment;
import com.construction.pm.activities.fragments.UserChangeProfileFragment;
import com.construction.pm.asynctask.LogoutAsyncTask;
import com.construction.pm.asynctask.param.LogoutAsyncTaskParam;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NetworkPendingService;
import com.construction.pm.services.NotificationMessageHandler;
import com.construction.pm.services.NotificationService;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.MainLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MainLayout.MainLayoutListener,
        UserChangeProfileFragment.UserChangeProfileFragmentListener,
        UserChangePasswordFragment.UserChangePasswordFragmentListener,
        NotificationMessageHandler.NotificationMessageHandlerListener,
        NotificationListFragment.NotificationListFragmentListener {

    public static final String INTENT_PARAM_SHOW_DEFAULT_FRAGMENT = "SHOW_FRAGMENT_DEFAULT";
    public static final String INTENT_PARAM_SHOW_FRAGMENT_HOME = "SHOW_FRAGMENT_HOME";
    public static final String INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION = "SHOW_FRAGMENT_NOTIFICATION";

    protected List<AsyncTask> mAsyncTaskList;

    protected MainLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare MainLayout --
        mMainLayout = MainLayout.buildMainLayout(this, null);
        mMainLayout.setMainLayoutListener(this);

        // -- Load MainLayout to activity --
        mMainLayout.loadLayoutToActivity(this);

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
        // -- Start NetworkPendingService --
        Intent networkPendingServiceStart = new Intent(this, NetworkPendingService.class);
        startService(networkPendingServiceStart);

        // -- Start NotificationService --
        Intent notificationServiceStart = new Intent(this, NotificationService.class);
        startService(notificationServiceStart);

        if (bundle != null) {

        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        boolean isDefaultFragmentShowed = false;

        if (bundle != null) {
            // -- Get intent request default fragment --
            if (bundle.containsKey(INTENT_PARAM_SHOW_DEFAULT_FRAGMENT)) {
                String showFragment = bundle.getString(INTENT_PARAM_SHOW_DEFAULT_FRAGMENT);
                if (showFragment != null) {
                    if (showFragment.equals(INTENT_PARAM_SHOW_FRAGMENT_HOME)) {
                        // -- Show HomeFragment --
                        mMainLayout.showHomeFragment();

                        isDefaultFragmentShowed = true;
                    } else if (showFragment.equals(INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION)) {
                        if (!mMainLayout.isNotificationListFragmentShow()) {
                            // -- Show NotificationListFragment --
                            mMainLayout.showNotificationListFragment(this);
                        }

                        isDefaultFragmentShowed = true;
                    }
                }
            }
        }

        // -- Set default fragment
        if (!isDefaultFragmentShowed) {
            // -- Show HomeFragment --
            mMainLayout.showHomeFragment();
        }

        // -- Calculate notification unread quantity --
        invalidateNotificationUnreadQuantity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bindNotificationService();
    }

    @Override
    protected void onPause() {
        unbindNotificationService();

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mMainLayout.getLayout().isDrawerOpen(GravityCompat.START)) {
            mMainLayout.getLayout().closeDrawers();
            return;
        }

        if (!mMainLayout.isHomeFragmentShow()) {
            mMainLayout.showHomeFragment();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }


    // -----------------
    // -- Menu Select --
    // -----------------

    @Override
    public void onMenuHomeSelected() {
        mMainLayout.showHomeFragment();
    }

    @Override
    public void onMenuProjectListSelected() {
        mMainLayout.showProjectListFragment();
    }

    @Override
    public void onMenuInspectorSelected() {
        mMainLayout.showInspectorFragment();
    }

    @Override
    public void onMenuManagerSelected() {
        mMainLayout.showManagerFragment();
    }

    @Override
    public void onMenuNotificationListSelected() {
        mMainLayout.showNotificationListFragment(this);
    }

    @Override
    public void onMenuUserChangeProfileSelected() {
        mMainLayout.showUserChangeProfileFragment(this);
    }

    @Override
    public void onUserChangeProfileSuccess(SimpleResponseModel simpleResponseModel) {

    }

    @Override
    public void onMenuUserChangePasswordSelected() {
        mMainLayout.showUserChangePasswordFragment(this);
    }

    @Override
    public void onUserChangePasswordSuccess(SimpleResponseModel simpleResponseModel) {
        // -- Show home --
        mMainLayout.showHomeFragment();
    }

    @Override
    public void onMenuLogoutClick() {
        doLogout();
    }


    // ---------------------------------------
    // -- NotificationListFragment Listener --
    // ---------------------------------------

    @Override
    public void onNotificationListRead(NotificationModel notificationModel) {
        // -- Recalculate notification unread quantity --
        invalidateNotificationUnreadQuantity();
    }


    // ----------------------------------
    // -- Notification Service Handler --
    // ----------------------------------

    protected ServiceConnection mNotificationServiceConnection;
    protected Messenger mNotificationServiceMessengerSender;
    protected Messenger mNotificationServiceMessengerReceiver;

    protected void bindNotificationService() {
        // -- Prepare message receiver --
        NotificationMessageHandler notificationMessageHandler = new NotificationMessageHandler(this);
        notificationMessageHandler.setNotificationMessageHandlerListener(this);
        mNotificationServiceMessengerReceiver = new Messenger(notificationMessageHandler);

        // -- Prepare NotificationServiceConnection --
        mNotificationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mNotificationServiceMessengerSender = new Messenger(iBinder);
                NotificationMessageHandler.sendRegister(mNotificationServiceMessengerSender, mNotificationServiceMessengerReceiver);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mNotificationServiceMessengerSender = null;
            }
        };

        // -- Bind NotificationService --
        Intent intent = new Intent(this, NotificationService.class);
        bindService(intent, mNotificationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onNotificationReceives(NotificationModel[] notificationModels) {
        if (mMainLayout.isNotificationListFragmentShow()) {
            // -- Attach to NotificationListFragment --
            mMainLayout.addNotificationModelsToNotificationListFragment(notificationModels);

            // -- Prepare NotificationManager --
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // -- Clear Notification --
            notificationManager.cancel(ConstantUtil.NOTIFICATION_ID_NOTIFICATION);
        } else {
            // -- Recalculate notification unread quantity --
            invalidateNotificationUnreadQuantity();
        }
    }

    @Override
    public void onNotificationRequestLogin(SessionLoginModel sessionLoginModel) {

    }

    @Override
    public void onNotificationServiceStop() {
        unbindNotificationService();
    }

    protected void unbindNotificationService() {
        if (mNotificationServiceMessengerSender != null)
            NotificationMessageHandler.sendUnregister(mNotificationServiceMessengerSender, mNotificationServiceMessengerReceiver);
        if (mNotificationServiceConnection != null)
            unbindService(mNotificationServiceConnection);
    }


    // --------------------
    // -- Logout Handler --
    // --------------------

    protected void doLogout() {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare LogoutAsyncTask --
        LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(Boolean isLoggedOut) {
                mAsyncTaskList.remove(this);

                onLogoutHandleFinish(isLoggedOut != null ? isLoggedOut : false);
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onLogoutHandleProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do LogoutAsyncTask --
        logoutAsyncTask.execute(new LogoutAsyncTaskParam(this, settingUserModel));
    }

    protected void onLogoutHandleFinish(final boolean isLoggedOut) {
        // -- Close progress dialog --
        mMainLayout.progressDialogDismiss();

        if (isLoggedOut) {
            // -- Redirect to AuthenticationActivity --
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);

            // -- Close activity --
            finish();
        }
    }

    protected void onLogoutHandleProgress(final String progressMessage) {
        // -- Show progress dialog --
        mMainLayout.progressDialogShow(progressMessage);
    }


    // -------------
    // -- Helpers --
    // -------------

    protected void invalidateNotificationUnreadQuantity() {
        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = null;
        if (sessionLoginModel != null)
            projectMemberModel = sessionLoginModel.getProjectMemberModel();

        // -- Get unread NotificationModels count from NotificationPersistent --
        int unreadNotificationModelCount = 0;
        if (projectMemberModel != null) {
            NotificationPersistent notificationPersistent = new NotificationPersistent(this);
            try {
                unreadNotificationModelCount = notificationPersistent.getUnreadNotificationModelCount(projectMemberModel.getProjectMemberId());
            } catch (PersistenceError ex) {
            }
        }

        // -- Show notification unread quantity to notification menu item --
        mMainLayout.setNotificationUnreadQuantity(unreadNotificationModelCount);
    }
}
