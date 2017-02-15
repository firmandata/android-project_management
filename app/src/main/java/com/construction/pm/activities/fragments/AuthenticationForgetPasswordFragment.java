package com.construction.pm.activities.fragments;

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
import com.construction.pm.views.system.AuthenticationForgetPasswordView;

public class AuthenticationForgetPasswordFragment extends Fragment implements AuthenticationForgetPasswordView.ForgetPasswordListener {

    protected AuthenticationForgetPasswordView mAuthenticationForgetPasswordView;

    protected AuthenticationForgetPasswordFragmentListener mAuthenticationForgetPasswordFragmentListener;

    public static AuthenticationForgetPasswordFragment newInstance() {
        return new AuthenticationForgetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // -- Prepare ForgetPasswordHandleTask --
        ForgetPasswordHandleTask forgetPasswordHandleTask = new ForgetPasswordHandleTask() {
            @Override
            public void onPostExecute(ForgetPasswordHandleTaskResult forgetPasswordHandleTaskResult) {
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

        // -- Do ForgetPasswordHandleTask --
        forgetPasswordHandleTask.execute(new ForgetPasswordHandleTaskParam(getContext(), settingUserModel, login));
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

    protected class ForgetPasswordHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected String mLogin;

        public ForgetPasswordHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final String login) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mLogin = login;
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
    }

    protected class ForgetPasswordHandleTaskResult {

        protected SimpleResponseModel mSimpleResponseModel;
        protected String mMessage;

        public ForgetPasswordHandleTaskResult() {

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

    protected class ForgetPasswordHandleTask extends AsyncTask<ForgetPasswordHandleTaskParam, String, ForgetPasswordHandleTaskResult> {
        protected ForgetPasswordHandleTaskParam mForgetPasswordHandleTaskParam;
        protected Context mContext;

        @Override
        protected ForgetPasswordHandleTaskResult doInBackground(ForgetPasswordHandleTaskParam... forgetPasswordHandleTaskParams) {
            // Get ForgetPasswordHandleTaskParam
            mForgetPasswordHandleTaskParam = forgetPasswordHandleTaskParams[0];
            mContext = mForgetPasswordHandleTaskParam.getContext();

            // -- Prepare ForgetPasswordHandleTaskResult --
            ForgetPasswordHandleTaskResult forgetPasswordHandleTaskResult = new ForgetPasswordHandleTaskResult();

            // -- Send request reset password to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.forget_password_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mForgetPasswordHandleTaskParam.getSettingUserModel());

            // -- Send request reset password to server --
            SimpleResponseModel simpleResponseModel = null;
            try {
                simpleResponseModel = userNetwork.forgetPassword(mForgetPasswordHandleTaskParam.getLogin());
            } catch (WebApiError webApiError) {
                forgetPasswordHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (simpleResponseModel != null) {
                // -- Send request reset password to server successfully --
                forgetPasswordHandleTaskResult.setSimpleResponseModel(simpleResponseModel);
                forgetPasswordHandleTaskResult.setMessage(simpleResponseModel.getMessage());

                // -- Send request reset password to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.forget_password_handle_task_success));
            }

            return forgetPasswordHandleTaskResult;
        }
    }

    public void setAuthenticationForgetPasswordFragmentListener(final AuthenticationForgetPasswordFragmentListener authenticationForgetPasswordFragmentListener) {
        mAuthenticationForgetPasswordFragmentListener = authenticationForgetPasswordFragmentListener;
    }

    public interface AuthenticationForgetPasswordFragmentListener {
        void onForgetPasswordSuccess(SimpleResponseModel simpleResponseModel);
    }
}
