package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.LoginFirstAsyncTask;
import com.construction.pm.asynctask.param.LoginFirstAsyncTaskParam;
import com.construction.pm.asynctask.result.LoginFirstAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.system.AuthenticationLoginFirstView;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationLoginFirstFragment extends Fragment implements AuthenticationLoginFirstView.AuthenticationLoginFirstListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected AuthenticationLoginFirstView mAuthenticationLoginFirstView;

    protected AuthenticationLoginFirstFragmentListener mAuthenticationLoginFirstFragmentListener;

    public static AuthenticationLoginFirstFragment newInstance() {
        return new AuthenticationLoginFirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare AuthenticationLoginFirstView --
        mAuthenticationLoginFirstView = AuthenticationLoginFirstView.buildAuthenticationLoginFirstView(getContext(), null);
        mAuthenticationLoginFirstView.setAuthenticationLoginFirstListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load AuthenticationLoginFirstView to fragment --
        return mAuthenticationLoginFirstView.getView();
    }

    @Override
    public void onLoginFirstRequest(String passwordNew) {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare LoginFirstAsyncTask --
        LoginFirstAsyncTask loginFirstAsyncTask = new LoginFirstAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(LoginFirstAsyncTaskResult firstLoginHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (firstLoginHandleTaskResult != null) {
                    SimpleResponseModel simpleResponseModel = firstLoginHandleTaskResult.getSimpleResponseModel();
                    if (simpleResponseModel != null) {
                        if (simpleResponseModel.isSuccess())
                            onFirstPasswordRequestSuccess(simpleResponseModel);
                        else
                            onFirstPasswordRequestFailed(simpleResponseModel.getMessage());
                    } else {
                        onFirstPasswordRequestFailed(firstLoginHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onFirstPasswordRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do LoginFirstAsyncTask --
        loginFirstAsyncTask.execute(new LoginFirstAsyncTaskParam(getContext(), settingUserModel, passwordNew));
    }

    protected void onFirstPasswordRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mAuthenticationLoginFirstView.progressDialogShow(progressMessage);
    }

    protected void onFirstPasswordRequestSuccess(final SimpleResponseModel simpleResponseModel) {
        // -- Hide progress dialog --
        mAuthenticationLoginFirstView.progressDialogDismiss();

        // -- Show success dialog --
        mAuthenticationLoginFirstView.alertDialogFirstSuccess(simpleResponseModel.getMessage());

        // -- Callback to AuthenticationLoginFirstFragmentListener --
        if (mAuthenticationLoginFirstFragmentListener != null)
            mAuthenticationLoginFirstFragmentListener.onLoginFirstSuccess(simpleResponseModel);
    }

    protected void onFirstPasswordRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mAuthenticationLoginFirstView.progressDialogDismiss();

        // -- Show error dialog --
        mAuthenticationLoginFirstView.alertDialogErrorShow(errorMessage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public void setAuthenticationLoginFirstFragmentListener(final AuthenticationLoginFirstFragmentListener authenticationLoginFirstFragmentListener) {
        mAuthenticationLoginFirstFragmentListener = authenticationLoginFirstFragmentListener;
    }

    public interface AuthenticationLoginFirstFragmentListener {
        void onLoginFirstSuccess(SimpleResponseModel simpleResponseModel);
    }
}