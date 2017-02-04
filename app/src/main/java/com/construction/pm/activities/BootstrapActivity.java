package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.MainApplication;
import com.construction.pm.R;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.AuthenticationNetwork;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.SplashLayout;

public class BootstrapActivity extends AppCompatActivity {

    protected MainApplication mMainApplication;
    protected SplashLayout mSplashLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainApplication = (MainApplication) getApplicationContext();

        // -- Load layout --
        mSplashLayout = SplashLayout.buildMainLayout(this, null);
        mSplashLayout.loadLayoutToActivity(this);
        mSplashLayout.showProgressBar();

        // -- Session handle --
        SessionHandleTask sessionHandleTask = new SessionHandleTask() {
            @Override
            public void onPostExecute(SessionHandleTaskResult sessionHandleTaskResult) {
                if (sessionHandleTaskResult != null)
                    onSessionHandleFinish(sessionHandleTaskResult);
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
        if (mMainApplication != null) {
            sessionHandleTask.execute(new SessionHandleTaskParam(this, mMainApplication.getSettingUserModel()));
        }
    }

    protected void onSessionHandleFinish(final SessionHandleTaskResult sessionHandleTaskResult) {
        mSplashLayout.hideProgressBar();

        // -- Show result message --
        if (sessionHandleTaskResult.getMessage() != null)
            mSplashLayout.setDescription(sessionHandleTaskResult.getMessage());

        // -- Set session login model to main application as references --
        if (    sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.ACCESS_TOKEN_CREATED
            ||  sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.USER_PROJECT_MEMBER_LOGGED_IN_UPDATED) {
            if (mMainApplication != null && sessionHandleTaskResult.getSessionLoginModel() != null)
                mMainApplication.setSessionLoginModel(sessionHandleTaskResult.getSessionLoginModel());
        }

        if (    sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.ACCESS_TOKEN_CREATED
            ||  sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.USER_PROJECT_MEMBER_NOT_LOGIN) {

            // -- Redirect to AuthenticationActivity --
            Intent intent = new Intent(this, AuthenticationActivity.class);;
            startActivity(intent);
            finish();

        } else if (     sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.USER_PROJECT_MEMBER_LOGGED_IN_UPDATED
                    ||  sessionHandleTaskResult.getSessionHandleTaskResultState() == SessionHandleTaskResultState.USER_PROJECT_MEMBER_LOGGED_IN_UPDATE_ERROR) {

            // -- Redirect to MainActivity --
            Intent intent = new Intent(this, MainActivity.class);;
            startActivity(intent);
            finish();

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

    protected enum SessionHandleTaskResultState {
        NONE,
        ACCESS_TOKEN_CREATED,
        ACCESS_TOKEN_ERROR,
        USER_PROJECT_MEMBER_NOT_LOGIN,
        USER_PROJECT_MEMBER_LOGGED_IN_UPDATED,
        USER_PROJECT_MEMBER_LOGGED_IN_UPDATE_ERROR
    }

    protected class SessionHandleTaskResult {

        protected SessionLoginModel mSessionLoginModel;
        protected SessionHandleTaskResultState mSessionHandleTaskResultState;
        protected String mMessage;

        public SessionHandleTaskResult() {

        }

        public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
            mSessionLoginModel = sessionLoginModel;
        }

        public SessionLoginModel getSessionLoginModel() {
            return mSessionLoginModel;
        }

        public void setSessionHandleTaskResultState(final SessionHandleTaskResultState sessionHandleTaskResultState) {
            mSessionHandleTaskResultState = sessionHandleTaskResultState;
        }

        public SessionHandleTaskResultState getSessionHandleTaskResultState() {
            return mSessionHandleTaskResultState;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class SessionHandleTask extends AsyncTask<SessionHandleTaskParam, String, SessionHandleTaskResult> {

        protected static final int WAIT_FOR_USER_READ_ERROR_MESSAGE = 200;

        protected SessionHandleTaskParam mSessionHandleTaskParam;
        protected Context mContext;

        @Override
        protected SessionHandleTaskResult doInBackground(SessionHandleTaskParam... sessionHandleTaskParams) {
            // -- Get SessionHandleTaskParam --
            mSessionHandleTaskParam = sessionHandleTaskParams[0];
            mContext = mSessionHandleTaskParam.getContext();

            // -- Initialize SessionHandleTaskResult --
            SessionHandleTaskResult sessionHandleTaskResult = new SessionHandleTaskResult();
            sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.NONE);

            // -- Session persistent --
            SessionPersistent sessionPersistent = new SessionPersistent(mContext);

            // -- Get session login persistent progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_get_persistent));

            // -- Get session login persistent --
            boolean isRequestGenerateAccessToken = false;
            SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();
            if (sessionLoginModel != null) {
                // -- Check if access token is expired --
                if (sessionLoginModel.isAccessTokenExpired()) {
                    // -- Set flag for generate new access token --
                    isRequestGenerateAccessToken = true;
                }
            } else {
                // -- Set flag for generate new access token --
                isRequestGenerateAccessToken = true;
            }

            // -- Authentication network --
            AuthenticationNetwork authenticationNetwork = new AuthenticationNetwork(mContext, mSessionHandleTaskParam.getSettingUserModel());

            if (isRequestGenerateAccessToken) {
                // -- Access token not exist --

                // -- Get access token progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_get_access_token));

                // -- Get access token in network --
                AccessTokenModel accessTokenModel = null;
                try {
                    accessTokenModel = authenticationNetwork.getAccessToken("user", "pass");
                } catch (Exception e) {
                    publishProgress(e.getMessage());
                    sessionHandleTaskResult.setMessage(e.getMessage());
                }

                if (accessTokenModel != null) {
                    // -- Set session login persistent progress --
                    publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_set_persistent));

                    // -- Set new access token to session login model --
                    sessionLoginModel = new SessionLoginModel();
                    sessionLoginModel.setAccessTokenModel(accessTokenModel);

                    // -- Save session login persistent --
                    sessionPersistent.setSessionLoginModel(sessionLoginModel);

                    // -- Success to get access token --
                    sessionHandleTaskResult.setSessionLoginModel(sessionLoginModel);
                    sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.ACCESS_TOKEN_CREATED);
                } else {
                    // -- Failed to get access token --
                    sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.ACCESS_TOKEN_ERROR);

                    // -- Pause process for user read error message --
                    try {
                        Thread.sleep(WAIT_FOR_USER_READ_ERROR_MESSAGE);
                    } catch (InterruptedException e) {
                    }
                }
            } else {
                // -- Access token existed --

                UserProjectMemberModel userProjectMemberModel = sessionLoginModel.getUserProjectMemberModel();
                if (userProjectMemberModel != null) {
                    // -- User logged in --

                    // -- Get user project member in server progress --
                    publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_get_user_project_member));

                    // -- Get user project member in server --
                    UserProjectMemberModel userProjectMemberModelNew = null;
                    try {
                        userProjectMemberModelNew = authenticationNetwork.getUserProjectMemberModel(sessionLoginModel);
                    } catch (Exception e) {
                        publishProgress(e.getMessage());
                        sessionHandleTaskResult.setMessage(e.getMessage());
                    }

                    if (userProjectMemberModelNew != null) {
                        // -- Set session login persistent progress --
                        publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_set_persistent));

                        // -- Set new user project member model --
                        sessionLoginModel.setUserProjectMemberModel(userProjectMemberModelNew);

                        // -- Save new session login persistent --
                        sessionPersistent.setSessionLoginModel(sessionLoginModel);

                        // -- Success to set new user project member model --
                        sessionHandleTaskResult.setSessionLoginModel(sessionLoginModel);
                        sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.USER_PROJECT_MEMBER_LOGGED_IN_UPDATED);
                    } else {
                        // -- WARNING!! Failed to get new user project member model --
                        sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.USER_PROJECT_MEMBER_LOGGED_IN_UPDATE_ERROR);

                        // -- Pause process for user read error message --
                        try {
                            Thread.sleep(WAIT_FOR_USER_READ_ERROR_MESSAGE);
                        } catch (InterruptedException e) {
                        }
                    }
                } else {
                    // -- User not login yet --

                    // -- Get access token progress --
                    publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_get_access_token));

                    // -- Success to get access token --
                    sessionHandleTaskResult.setSessionLoginModel(sessionLoginModel);
                    sessionHandleTaskResult.setSessionHandleTaskResultState(SessionHandleTaskResultState.USER_PROJECT_MEMBER_NOT_LOGIN);
                }
            }

            return sessionHandleTaskResult;
        }
    }
}
