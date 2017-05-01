package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.ForgetPasswordAsyncTask;
import com.construction.pm.asynctask.param.ForgetPasswordAsyncTaskParam;
import com.construction.pm.asynctask.result.ForgetPasswordAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.system.AuthenticationForgetPasswordView;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationForgetPasswordFragment extends Fragment implements AuthenticationForgetPasswordView.ForgetPasswordListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected AuthenticationForgetPasswordView mAuthenticationForgetPasswordView;

    protected AuthenticationForgetPasswordFragmentListener mAuthenticationForgetPasswordFragmentListener;

    public static AuthenticationForgetPasswordFragment newInstance() {
        return new AuthenticationForgetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare AuthenticationForgetPasswordView --
        mAuthenticationForgetPasswordView = AuthenticationForgetPasswordView.buildAuthenticationForgetPasswordView(getContext(), null);
        mAuthenticationForgetPasswordView.setForgetPasswordListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load AuthenticationForgetPasswordView to fragment --
        return mAuthenticationForgetPasswordView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onForgetPasswordRequest(String login) {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare ForgetPasswordAsyncTask --
        ForgetPasswordAsyncTask forgetPasswordAsyncTask = new ForgetPasswordAsyncTask() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ForgetPasswordAsyncTaskResult forgetPasswordHandleTaskResult) {
                if (forgetPasswordHandleTaskResult != null) {
                    SimpleResponseModel simpleResponseModel = forgetPasswordHandleTaskResult.getSimpleResponseModel();
                    if (simpleResponseModel != null) {
                        if (simpleResponseModel.isSuccess())
                            onForgetPasswordRequestSuccess(simpleResponseModel);
                        else
                            onForgetPasswordRequestFailed(simpleResponseModel.getMessage());
                    } else {
                        onForgetPasswordRequestFailed(forgetPasswordHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onForgetPasswordRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ForgetPasswordAsyncTask --
        forgetPasswordAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ForgetPasswordAsyncTaskParam(getContext(), settingUserModel, login));
    }

    protected void onForgetPasswordRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mAuthenticationForgetPasswordView.progressDialogShow(progressMessage);
    }

    protected void onForgetPasswordRequestSuccess(final SimpleResponseModel simpleResponseModel) {
        // -- Hide progress dialog --
        mAuthenticationForgetPasswordView.progressDialogDismiss();

        // -- Show success dialog --
        mAuthenticationForgetPasswordView.alertDialogSuccessShow(simpleResponseModel.getMessage());

        // -- Callback to AuthenticationForgetPasswordFragmentListener --
        if (mAuthenticationForgetPasswordFragmentListener != null)
            mAuthenticationForgetPasswordFragmentListener.onForgetPasswordSuccess(simpleResponseModel);
    }

    protected void onForgetPasswordRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mAuthenticationForgetPasswordView.progressDialogDismiss();

        // -- Show error dialog --
        mAuthenticationForgetPasswordView.alertDialogErrorShow(errorMessage);
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

    public void setAuthenticationForgetPasswordFragmentListener(final AuthenticationForgetPasswordFragmentListener authenticationForgetPasswordFragmentListener) {
        mAuthenticationForgetPasswordFragmentListener = authenticationForgetPasswordFragmentListener;
    }

    public interface AuthenticationForgetPasswordFragmentListener {
        void onForgetPasswordSuccess(SimpleResponseModel simpleResponseModel);
    }
}
