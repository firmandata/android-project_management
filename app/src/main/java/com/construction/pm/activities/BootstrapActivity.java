package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.R;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.SplashLayout;

public class BootstrapActivity extends AppCompatActivity {

    protected SplashLayout mSplashLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare SplashLayout --
        mSplashLayout = SplashLayout.buildMainLayout(this, null);

        // -- Load SplashLayout to activity
        mSplashLayout.loadLayoutToActivity(this);

        // -- Prepare SessionHandleTask --
        SessionHandleTask sessionHandleTask = new SessionHandleTask() {
            @Override
            public void onPostExecute(SessionLoginModel sessionLoginModel) {
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

        // -- Do SessionHandleTask --
        sessionHandleTask.execute(new SessionHandleTaskParam(this, settingUserModel));
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

    protected class SessionHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;

        public SessionHandleTaskParam(final Context context, final SettingUserModel settingUserModel) {
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

    protected class SessionHandleTask extends AsyncTask<SessionHandleTaskParam, String, SessionLoginModel> {

        protected static final int WAIT_FOR_USER_READ_ERROR_MESSAGE = 2000;

        protected SessionHandleTaskParam mSessionHandleTaskParam;
        protected Context mContext;

        @Override
        protected SessionLoginModel doInBackground(SessionHandleTaskParam... sessionHandleTaskParams) {
            // -- Get SessionHandleTaskParam --
            mSessionHandleTaskParam = sessionHandleTaskParams[0];
            mContext = mSessionHandleTaskParam.getContext();

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mSessionHandleTaskParam.getSettingUserModel());

            // -- AuthenticationNetwork handle --
            SessionLoginModel sessionLoginModel = null;
            try {
                // -- Set publishProgress AccessTokenModel handle --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_handle_access_token));

                // -- AccessTokenModel handle --
                sessionLoginModel = userNetwork.invalidateAccessToken();

                if (sessionLoginModel.getUserProjectMemberModel() != null) {
                    // -- Set publishProgress UserProjectMemberModel handle --
                    publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_handle_user_project_member));

                    // -- UserProjectMemberModel handle --
                    sessionLoginModel = userNetwork.invalidateLogin();
                }

                // -- Set publishProgress as finish --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_finish));
            } catch (WebApiError webApiError) {
                // -- Set new SessionLoginModel --
                sessionLoginModel = userNetwork.getSessionLoginModel();

                // -- Set publishProgress --
                publishProgress(webApiError.getMessage());

                // -- Pause process for user read error message --
                try {
                    Thread.sleep(WAIT_FOR_USER_READ_ERROR_MESSAGE);
                } catch (InterruptedException interruptedException) {
                }
            }

            return sessionLoginModel;
        }
    }
}
