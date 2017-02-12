package com.construction.pm.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.UserChangePasswordFragment;
import com.construction.pm.activities.fragments.UserChangeProfileFragment;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.services.NotificationService;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.MainLayout;

public class MainActivity extends AppCompatActivity implements
        MainLayout.MainLayoutListener,
        UserChangeProfileFragment.UserChangeProfileFragmentListener,
        UserChangePasswordFragment.UserChangePasswordFragmentListener {

    public static final String INTENT_PARAM_SHOW_DEFAULT_FRAGMENT = "SHOW_FRAGMENT_DEFAULT";
    public static final String INTENT_PARAM_SHOW_FRAGMENT_HOME = "SHOW_FRAGMENT_HOME";
    public static final String INTENT_PARAM_SHOW_FRAGMENT_NOTIFICATION = "SHOW_FRAGMENT_NOTIFICATION";

    protected MainLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        // -- Show NotificationListFragment --
                        mMainLayout.showNotificationListFragment();

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

//        bindNotificationService();
    }

    @Override
    protected void onPause() {
//        unbindNotificationService();

        super.onPause();
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
    public void onMenuHomeSelected() {
        mMainLayout.showHomeFragment();
    }

    @Override
    public void onMenuProjectListSelected() {
        mMainLayout.showProjectListFragment();
    }

    @Override
    public void onMenuNotificationListSelected() {
        mMainLayout.showNotificationListFragment();
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


    // ----------------------------------
    // -- Notification Service Handler --
    // ----------------------------------

    protected ServiceConnection mNotificationServiceConnection;
    protected Messenger mNotificationServiceMessengerSender;
    protected Messenger mNotificationServiceMessengerReceiver;

    protected void bindNotificationService() {
        mNotificationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mNotificationServiceMessengerSender = new Messenger(iBinder);

//                try {
//                    Message message;
//
//                    // -- Send register command --
//                    message = Message.obtain(null, PaymentService.PaymentServiceHandler.MSG_REGISTER_CLIENT);
//                    message.replyTo = mPaymentMessenger;
//                    mPaymentMessengerCommunicator.send(message);
//
//                    // -- Send state command --
//                    message = Message.obtain(null, PaymentService.PaymentServiceHandler.MSG_STATE_SERVICE);
//                    mPaymentMessengerCommunicator.send(message);
//                } catch (RemoteException e) {
//
//                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(this, NotificationService.class);
        bindService(intent, mNotificationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    protected void unbindNotificationService() {
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

        // -- Prepare LogoutHandleTask --
        LogoutHandleTask logoutHandleTask = new LogoutHandleTask() {
            @Override
            public void onPostExecute(Boolean isLoggedOut) {
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

        // -- Do SessionHandleTask --
        logoutHandleTask.execute(new LogoutHandleTaskParam(this, settingUserModel));
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

    protected class LogoutHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;

        public LogoutHandleTaskParam(final Context context, final SettingUserModel settingUserModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }
    }

    protected class LogoutHandleTask extends AsyncTask<LogoutHandleTaskParam, String, Boolean> {

        protected LogoutHandleTaskParam mLogoutHandleTaskParam;
        protected Context mContext;

        @Override
        protected Boolean doInBackground(LogoutHandleTaskParam... logoutHandleTaskParams) {
            // -- Get SessionHandleTaskParam --
            mLogoutHandleTaskParam = logoutHandleTaskParams[0];
            mContext = mLogoutHandleTaskParam.getContext();

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mLogoutHandleTaskParam.getSettingUserModel());

            // -- Set publishProgress AccessTokenModel handle --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.logout_handle_task_begin));

            // -- Do logout handle --
            userNetwork.doLogout();

            // -- Set publishProgress as finish --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.logout_handle_task_success));

            return true;
        }
    }
}
