package com.construction.pm.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.user.UserChangePasswordView;

public class UserChangePasswordFragment extends Fragment implements UserChangePasswordView.UserChangePasswordListener {

    protected UserChangePasswordView mUserChangePasswordView;

    protected UserChangePasswordFragmentListener mUserChangePasswordFragmentListener;

    public static UserChangePasswordFragment newInstance() {
        return new UserChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // -- Prepare UserChangePasswordHandleTask --
        UserChangePasswordHandleTask userChangePasswordHandleTask = new UserChangePasswordHandleTask() {
            @Override
            public void onPostExecute(UserChangePasswordHandleTaskResult userChangePasswordHandleTaskResult) {
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

        // -- Do UserChangePasswordHandleTask --
        userChangePasswordHandleTask.execute(new UserChangePasswordHandleTaskParam(getContext(), settingUserModel, passwordOld, passwordNew));
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

    protected class UserChangePasswordHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected String mPasswordOld;
        protected String mPasswordNew;

        public UserChangePasswordHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final String passwordOld, final String passwordNew) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mPasswordOld = passwordOld;
            mPasswordNew = passwordNew;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public String getPasswordOld() {
            return mPasswordNew;
        }

        public String getPasswordNew() {
            return mPasswordNew;
        }
    }

    protected class UserChangePasswordHandleTaskResult {

        protected SimpleResponseModel mSimpleResponseModel;
        protected String mMessage;

        public UserChangePasswordHandleTaskResult() {

        }

        public void setSimpleResponseModel(final SimpleResponseModel simpleResponseModel) {
            mSimpleResponseModel = simpleResponseModel;
        }

        public SimpleResponseModel getSimpleResponseModel() {
            return mSimpleResponseModel;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class UserChangePasswordHandleTask extends AsyncTask<UserChangePasswordHandleTaskParam, String, UserChangePasswordHandleTaskResult> {
        protected UserChangePasswordHandleTaskParam mUserChangePasswordHandleTaskParam;
        protected Context mContext;

        @Override
        protected UserChangePasswordHandleTaskResult doInBackground(UserChangePasswordHandleTaskParam... userChangePasswordHandleTaskParams) {
            // Get UserChangePasswordHandleTaskParam
            mUserChangePasswordHandleTaskParam = userChangePasswordHandleTaskParams[0];
            mContext = mUserChangePasswordHandleTaskParam.getContext();

            // -- Prepare UserChangePasswordHandleTaskResult --
            UserChangePasswordHandleTaskResult userChangePasswordHandleTaskResult = new UserChangePasswordHandleTaskResult();

            // -- Change password to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mUserChangePasswordHandleTaskParam.getSettingUserModel());

            // -- Change password to server --
            SimpleResponseModel simpleResponseModel = null;
            try {
                simpleResponseModel = userNetwork.changePassword(mUserChangePasswordHandleTaskParam.getPasswordOld(), mUserChangePasswordHandleTaskParam.getPasswordNew());
            } catch (WebApiError webApiError) {
                userChangePasswordHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (simpleResponseModel != null) {
                // -- Change password to server successfully --
                userChangePasswordHandleTaskResult.setSimpleResponseModel(simpleResponseModel);
                userChangePasswordHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_success));

                // -- Change password to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_success));
            }

            return userChangePasswordHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setUserChangePasswordFragmentListener(final UserChangePasswordFragmentListener userChangePasswordFragmentListener) {
        mUserChangePasswordFragmentListener = userChangePasswordFragmentListener;
    }

    public interface UserChangePasswordFragmentListener {
        void onUserChangePasswordSuccess(SimpleResponseModel simpleResponseModel);
    }
}
