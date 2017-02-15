package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.system.AuthenticationLoginView;

public class AuthenticationLoginFragment extends Fragment implements
        AuthenticationLoginView.LoginListener,
        AuthenticationLoginView.LoginForgetPasswordListener {

    protected AuthenticationLoginView mAuthenticationLoginView;

    protected AuthenticationLoginFragmentListener mAuthenticationLoginFragmentListener;
    protected AuthenticationLoginFragmentForgetPasswordListener mAuthenticationLoginFragmentForgetPasswordListener;

    public static AuthenticationLoginFragment newInstance() {
        return new AuthenticationLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare AuthenticationLoginView --
        mAuthenticationLoginView = AuthenticationLoginView.buildAuthenticationLoginView(getContext(), null);
        mAuthenticationLoginView.setLoginListener(this);
        mAuthenticationLoginView.setLoginForgetPasswordListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load AuthenticationLayout to fragment --
        return mAuthenticationLoginView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onLoginRequest(String login, String password) {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare LoginHandleTask --
        LoginHandleTask loginHandleTask = new LoginHandleTask() {
            @Override
            public void onPostExecute(LoginHandleTaskResult loginHandleTaskResult) {
                if (loginHandleTaskResult != null) {
                    if (loginHandleTaskResult.getSessionLoginModel() != null) {
                        onLoginRequestSuccess(loginHandleTaskResult.getSessionLoginModel());
                    } else {
                        onLoginRequestFailed(loginHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onLoginRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do LoginHandleTask --
        loginHandleTask.execute(new LoginHandleTaskParam(getContext(), settingUserModel, login, password));
    }

    @Override
    public void onLoginForgetPasswordRequest() {
        if (mAuthenticationLoginFragmentForgetPasswordListener != null)
            mAuthenticationLoginFragmentForgetPasswordListener.onLoginRequestForgetPassword();
    }

    protected void onLoginRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mAuthenticationLoginView.progressDialogShow(progressMessage);
    }

    protected void onLoginRequestSuccess(final SessionLoginModel sessionLoginModel) {
        // -- Hide progress dialog --
        mAuthenticationLoginView.progressDialogDismiss();

        // -- Show first login dialog info --
        if (sessionLoginModel.isFirstLogin())
            mAuthenticationLoginView.alertDialogFirstLoginShow();

        // -- Callback to AuthenticationLoginFragmentListener --
        if (mAuthenticationLoginFragmentListener != null)
            mAuthenticationLoginFragmentListener.onLoginSuccess(sessionLoginModel);
    }

    protected void onLoginRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mAuthenticationLoginView.progressDialogDismiss();

        // -- Show error dialog --
        mAuthenticationLoginView.alertDialogErrorShow(errorMessage);
    }

    protected class LoginHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected String mLogin;
        protected String mPassword;

        public LoginHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final String login, final String password) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mLogin = login;
            mPassword = password;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public String getLogin() {
            return mLogin;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    protected class LoginHandleTaskResult {

        protected SessionLoginModel mSessionLoginModel;
        protected String mMessage;

        public LoginHandleTaskResult() {

        }

        public void setSessionLoginModel(final SessionLoginModel sessionLoginModel) {
            mSessionLoginModel = sessionLoginModel;
        }

        public SessionLoginModel getSessionLoginModel() {
            return mSessionLoginModel;
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

            // -- Prepare LoginHandleTaskResult --
            LoginHandleTaskResult loginHandleTaskResult = new LoginHandleTaskResult();

            // -- Login to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.login_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mLoginHandleTaskParam.getSettingUserModel());

            // -- Login to server --
            SessionLoginModel sessionLoginModel = null;
            try {
                sessionLoginModel = userNetwork.generateLogin(mLoginHandleTaskParam.getLogin(), mLoginHandleTaskParam.getPassword());
            } catch (WebApiError webApiError) {
                loginHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (sessionLoginModel != null) {
                // -- Login to server successfully --
                loginHandleTaskResult.setSessionLoginModel(sessionLoginModel);
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
        void onLoginSuccess(SessionLoginModel sessionLoginModel);
    }

    public void setAuthenticationLoginFragmentForgetPasswordListener(final AuthenticationLoginFragmentForgetPasswordListener authenticationLoginFragmentForgetPasswordListener) {
        mAuthenticationLoginFragmentForgetPasswordListener = authenticationLoginFragmentForgetPasswordListener;
    }

    public interface AuthenticationLoginFragmentForgetPasswordListener {
        void onLoginRequestForgetPassword();
    }
}