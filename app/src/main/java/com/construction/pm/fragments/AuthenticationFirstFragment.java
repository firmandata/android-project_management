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
import com.construction.pm.views.system.AuthenticationFirstView;

public class AuthenticationFirstFragment extends Fragment implements AuthenticationFirstView.FirstPasswordListener {

    protected AuthenticationFirstView mAuthenticationFirstView;

    protected AuthenticationFirstFragmentListener mAuthenticationFirstFragmentListener;

    public static AuthenticationFirstFragment newInstance() {
        return new AuthenticationFirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare AuthenticationFirstView --
        mAuthenticationFirstView = AuthenticationFirstView.buildAuthenticationFirstView(getContext(), null);
        mAuthenticationFirstView.setFirstPasswordListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load AuthenticationFirstView to fragment --
        return mAuthenticationFirstView.getView();
    }

    @Override
    public void onFirstPasswordRequest(String passwordNew) {
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

        // -- Do LoginHandleTask --
        firstLoginHandleTask.execute(new FirstLoginHandleTaskParam(getContext(), settingUserModel, passwordNew));
    }

    protected void onFirstPasswordRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mAuthenticationFirstView.progressDialogShow(progressMessage);
    }

    protected void onFirstPasswordRequestSuccess(final SimpleResponseModel simpleResponseModel) {
        // -- Hide progress dialog --
        mAuthenticationFirstView.progressDialogDismiss();

        // -- Show success dialog --
        mAuthenticationFirstView.alertDialogFirstSuccess(simpleResponseModel.getMessage());

        // -- Callback to AuthenticationFirstFragmentListener --
        if (mAuthenticationFirstFragmentListener != null)
            mAuthenticationFirstFragmentListener.onFirstPasswordFinish(simpleResponseModel);
    }

    protected void onFirstPasswordRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mAuthenticationFirstView.progressDialogDismiss();

        // -- Show error dialog --
        mAuthenticationFirstView.alertDialogErrorShow(errorMessage);
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

        public String getPassword() {
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

            // -- Login to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.first_login_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mFirstLoginHandleTaskParam.getSettingUserModel());

            // -- First Login to server --
            SimpleResponseModel simpleResponseModel = null;
            try {
                simpleResponseModel = userNetwork.changePasswordFirst(mFirstLoginHandleTaskParam.getPassword());
            } catch (WebApiError webApiError) {
                firstLoginHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (simpleResponseModel != null) {
                // -- First Login to server successfully --
                firstLoginHandleTaskResult.setSimpleResponseModel(simpleResponseModel);
                firstLoginHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.first_login_handle_task_success));

                // -- First Login to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.first_login_handle_task_success));
            }

            return firstLoginHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAuthenticationFirstFragmentListener(final AuthenticationFirstFragmentListener authenticationFirstFragmentListener) {
        mAuthenticationFirstFragmentListener = authenticationFirstFragmentListener;
    }

    public interface AuthenticationFirstFragmentListener {
        void onFirstPasswordFinish(SimpleResponseModel simpleResponseModel);
    }
}