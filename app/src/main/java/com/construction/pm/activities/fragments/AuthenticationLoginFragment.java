package com.construction.pm.activities.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.LoginAsyncTask;
import com.construction.pm.asynctask.param.LoginAsyncTaskParam;
import com.construction.pm.asynctask.result.LoginAsyncTaskResult;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.system.AuthenticationLoginView;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationLoginFragment extends Fragment implements
        AuthenticationLoginView.LoginListener,
        AuthenticationLoginView.LoginForgetPasswordListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected AuthenticationLoginView mAuthenticationLoginView;

    protected AuthenticationLoginFragmentListener mAuthenticationLoginFragmentListener;
    protected AuthenticationLoginFragmentForgetPasswordListener mAuthenticationLoginFragmentForgetPasswordListener;

    public static AuthenticationLoginFragment newInstance() {
        return new AuthenticationLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

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

        // -- Prepare LoginAsyncTask --
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(LoginAsyncTaskResult loginHandleTaskResult) {
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

        // -- Do LoginAsyncTask --
        loginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new LoginAsyncTaskParam(getContext(), settingUserModel, login, password));
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
        // -- Prepare NotificationManager --
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // -- Clear current notification --
        notificationManager.cancel(ConstantUtil.NOTIFICATION_ID_NOTIFICATION);

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
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