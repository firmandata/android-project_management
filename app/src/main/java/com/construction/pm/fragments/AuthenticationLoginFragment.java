package com.construction.pm.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.MainApplication;
import com.construction.pm.R;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.AuthenticationNetwork;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.system.AuthenticationLoginView;

public class AuthenticationLoginFragment extends Fragment implements AuthenticationLoginView.LoginListener {

    protected SettingUserModel mSettingUserModel;
    protected SessionLoginModel mSessionLoginModel;

    protected AuthenticationLoginView mAuthenticationLoginView;

    protected AuthenticationLoginFragmentListener mAuthenticationLoginFragmentListener;

    public static AuthenticationLoginFragment newInstance() {
        return new AuthenticationLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication mainApplication = (MainApplication) getContext().getApplicationContext();
        if (mainApplication != null) {
            mSettingUserModel = mainApplication.getSettingUserModel();
            mSessionLoginModel = mainApplication.getSessionLoginModel();
        }

        mAuthenticationLoginView = AuthenticationLoginView.buildAuthenticationLoginView(getContext(), null);
        mAuthenticationLoginView.setLoginListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mAuthenticationLoginView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onLoginRequest(String login, String password) {
        LoginHandleTask loginHandleTask = new LoginHandleTask() {
            @Override
            public void onPostExecute(LoginHandleTaskResult loginHandleTaskResult) {
                if (loginHandleTaskResult != null) {
                    if (loginHandleTaskResult.getUserProjectMemberModel() != null) {
                        onLoginRequestSuccess(loginHandleTaskResult.getUserProjectMemberModel());
                    } else {
                        onLoginRequestFailed(loginHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {

                    }
                }
            }
        };
        if (mSettingUserModel != null && mSessionLoginModel != null) {
            loginHandleTask.execute(new LoginHandleTaskParam(getContext(), mSettingUserModel, mSessionLoginModel.getAccessTokenModel(), login, password));
        }
    }

    protected void onLoginRequestSuccess(final UserProjectMemberModel userProjectMemberModel) {

        if (mAuthenticationLoginFragmentListener != null)
            mAuthenticationLoginFragmentListener.onLoggedIn(userProjectMemberModel);
    }

    protected void onLoginRequestFailed(final String errorMessage) {

    }

    protected class LoginHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected AccessTokenModel mAccessTokenModel;
        protected String mLogin;
        protected String mPassword;

        public LoginHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final AccessTokenModel accessTokenModel, final String login, final String password) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mAccessTokenModel = accessTokenModel;
            mLogin = login;
            mPassword = password;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public AccessTokenModel getAccessTokenModel() {
            return mAccessTokenModel;
        }

        public String getLogin() {
            return mLogin;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    protected class LoginHandleTaskResult {

        protected UserProjectMemberModel mUserProjectMemberModel;
        protected String mMessage;

        public LoginHandleTaskResult() {

        }

        public void setUserProjectMemberModel(final UserProjectMemberModel userProjectMemberModel) {
            mUserProjectMemberModel = userProjectMemberModel;
        }

        public UserProjectMemberModel getUserProjectMemberModel() {
            return mUserProjectMemberModel;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class LoginHandleTask extends AsyncTask<LoginHandleTaskParam, String, LoginHandleTaskResult> {
        protected LoginHandleTaskParam mLoginHandleTaskParam;
        protected Context mContext;

        @Override
        protected LoginHandleTaskResult doInBackground(LoginHandleTaskParam... loginHandleTaskParams) {
            // Get LoginHandleTaskParam
            mLoginHandleTaskParam = loginHandleTaskParams[0];
            mContext = mLoginHandleTaskParam.getContext();

            // Initialize LoginHandleTaskResult
            LoginHandleTaskResult loginHandleTaskResult = new LoginHandleTaskResult();

            // -- Login to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.login_handle_task_begin));

            // -- Authentication network --
            AuthenticationNetwork authenticationNetwork = new AuthenticationNetwork(mContext, mLoginHandleTaskParam.getSettingUserModel());

            // -- Login to server --
            UserProjectMemberModel userProjectMemberModel = null;
            try {
                userProjectMemberModel = authenticationNetwork.doLogin(mLoginHandleTaskParam.getAccessTokenModel(), mLoginHandleTaskParam.getLogin(), mLoginHandleTaskParam.getPassword());
            } catch (Exception e) {
                loginHandleTaskResult.setMessage(e.getMessage());
                publishProgress(e.getMessage());
            }

            if (userProjectMemberModel != null) {
                // -- Login to server successfully --
                loginHandleTaskResult.setUserProjectMemberModel(userProjectMemberModel);
                loginHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.login_handle_task_success));

                // -- Login to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.login_handle_task_success));
            }

            return loginHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAuthenticationLoginFragmentListener(final AuthenticationLoginFragmentListener authenticationLoginFragmentListener) {
        mAuthenticationLoginFragmentListener = authenticationLoginFragmentListener;
    }

    public interface AuthenticationLoginFragmentListener {
        void onLoggedIn(UserProjectMemberModel userProjectMemberModel);
    }
}