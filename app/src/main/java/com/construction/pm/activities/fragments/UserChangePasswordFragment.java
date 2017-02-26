package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.UserChangePasswordAsyncTask;
import com.construction.pm.asynctask.param.UserChangePasswordAsyncTaskParam;
import com.construction.pm.asynctask.result.UserChangePasswordAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.user.UserChangePasswordView;

import java.util.ArrayList;
import java.util.List;

public class UserChangePasswordFragment extends Fragment implements UserChangePasswordView.UserChangePasswordListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected UserChangePasswordView mUserChangePasswordView;

    protected UserChangePasswordFragmentListener mUserChangePasswordFragmentListener;

    public static UserChangePasswordFragment newInstance() {
        return new UserChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare UserChangePasswordView --
        mUserChangePasswordView = UserChangePasswordView.buildAuthenticationChangePasswordView(getContext(), null);
        mUserChangePasswordView.setUserChangePasswordListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load UserChangePasswordView to fragment --
        return mUserChangePasswordView.getView();
    }

    @Override
    public void onUserChangePasswordRequest(String passwordOld, String passwordNew) {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare UserChangePasswordAsyncTask --
        UserChangePasswordAsyncTask userChangePasswordAsyncTask = new UserChangePasswordAsyncTask() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(UserChangePasswordAsyncTaskResult userChangePasswordHandleTaskResult) {
                if (userChangePasswordHandleTaskResult != null) {
                    SimpleResponseModel simpleResponseModel = userChangePasswordHandleTaskResult.getSimpleResponseModel();
                    if (simpleResponseModel != null) {
                        if (simpleResponseModel.isSuccess())
                            onUserChangePasswordRequestSuccess(simpleResponseModel);
                        else
                            onUserChangePasswordRequestFailed(simpleResponseModel.getMessage());
                    } else {
                        onUserChangePasswordRequestFailed(userChangePasswordHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onUserChangePasswordRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do UserChangePasswordAsyncTask --
        userChangePasswordAsyncTask.execute(new UserChangePasswordAsyncTaskParam(getContext(), settingUserModel, passwordOld, passwordNew));
    }

    protected void onUserChangePasswordRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mUserChangePasswordView.progressDialogShow(progressMessage);
    }

    protected void onUserChangePasswordRequestSuccess(final SimpleResponseModel simpleResponseModel) {
        // -- Hide progress dialog --
        mUserChangePasswordView.progressDialogDismiss();

        // -- Show success dialog --
        mUserChangePasswordView.alertDialogFirstSuccess(simpleResponseModel.getMessage());

        // -- Callback to UserChangePasswordFragmentListener --
        if (mUserChangePasswordFragmentListener != null)
            mUserChangePasswordFragmentListener.onUserChangePasswordSuccess(simpleResponseModel);
    }

    protected void onUserChangePasswordRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mUserChangePasswordView.progressDialogDismiss();

        // -- Show error dialog --
        mUserChangePasswordView.alertDialogErrorShow(errorMessage);
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

    public void setUserChangePasswordFragmentListener(final UserChangePasswordFragmentListener userChangePasswordFragmentListener) {
        mUserChangePasswordFragmentListener = userChangePasswordFragmentListener;
    }

    public interface UserChangePasswordFragmentListener {
        void onUserChangePasswordSuccess(SimpleResponseModel simpleResponseModel);
    }
}
