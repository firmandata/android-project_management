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
import com.construction.pm.views.system.AuthenticationLoginFirstView;

public class AuthenticationLoginFirstFragment extends Fragment implements AuthenticationLoginFirstView.AuthenticationLoginFirstListener {

    protected AuthenticationLoginFirstView mAuthenticationLoginFirstView;

    protected AuthenticationLoginFirstFragmentListener mAuthenticationLoginFirstFragmentListener;

    public static AuthenticationLoginFirstFragment newInstance() {
        return new AuthenticationLoginFirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // -- Prepare FirstLoginHandleTask --
        FirstLoginHandleTask firstLoginHandleTask = new FirstLoginHandleTask() {
            @Override
            public void onPostExecute(FirstLoginHandleTaskResult firstLoginHandleTaskResult) {
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

        // -- Do FirstLoginHandleTask --
        firstLoginHandleTask.execute(new FirstLoginHandleTaskParam(getContext(), settingUserModel, passwordNew));
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

    protected class FirstLoginHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected String mPasswordNew;

        public FirstLoginHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final String passwordNew) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mPasswordNew = passwordNew;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public String getPasswordNew() {
            return mPasswordNew;
        }
    }

    protected class FirstLoginHandleTaskResult {

        protected SimpleResponseModel mSimpleResponseModel;
        protected String mMessage;

        public FirstLoginHandleTaskResult() {

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

    protected class FirstLoginHandleTask extends AsyncTask<FirstLoginHandleTaskParam, String, FirstLoginHandleTaskResult> {
        protected FirstLoginHandleTaskParam mFirstLoginHandleTaskParam;
        protected Context mContext;

        @Override
        protected FirstLoginHandleTaskResult doInBackground(FirstLoginHandleTaskParam... firstLoginHandleTaskParams) {
            // Get FirstLoginHandleTaskParam
            mFirstLoginHandleTaskParam = firstLoginHandleTaskParams[0];
            mContext = mFirstLoginHandleTaskParam.getContext();

            // -- Prepare FirstLoginHandleTaskResult --
            FirstLoginHandleTaskResult firstLoginHandleTaskResult = new FirstLoginHandleTaskResult();

            // -- Login first change password to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mFirstLoginHandleTaskParam.getSettingUserModel());

            // -- Login first change password to server --
            SimpleResponseModel simpleResponseModel = null;
            try {
                simpleResponseModel = userNetwork.changePasswordFirst(mFirstLoginHandleTaskParam.getPasswordNew());
            } catch (WebApiError webApiError) {
                firstLoginHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (simpleResponseModel != null) {
                // -- Login first change password to server successfully --
                firstLoginHandleTaskResult.setSimpleResponseModel(simpleResponseModel);
                firstLoginHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_success));

                // -- Login first change password to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_success));
            }

            return firstLoginHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAuthenticationLoginFirstFragmentListener(final AuthenticationLoginFirstFragmentListener authenticationLoginFirstFragmentListener) {
        mAuthenticationLoginFirstFragmentListener = authenticationLoginFirstFragmentListener;
    }

    public interface AuthenticationLoginFirstFragmentListener {
        void onLoginFirstSuccess(SimpleResponseModel simpleResponseModel);
    }
}