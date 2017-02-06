package com.construction.pm.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.user.UserChangeProfileView;

public class UserChangeProfileFragment extends Fragment implements UserChangeProfileView.UserChangeProfileListener {

    protected UserChangeProfileView mUserChangeProfileView;

    protected UserChangeProfileFragmentListener mUserChangeProfileFragmentListener;

    public static UserChangeProfileFragment newInstance() {
        return new UserChangeProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());

        // -- Get SettingUserModel from SessionPersistent --
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = null;
        if (sessionLoginModel != null)
            projectMemberModel = sessionLoginModel.getProjectMemberModel();

        // -- Prepare UserChangeProfileView --
        mUserChangeProfileView = UserChangeProfileView.buildUserChangeProfileView(getContext(), null);
        mUserChangeProfileView.setUserChangeProfileListener(this);

        // -- Set ProjectMemberModel to View --
        if (projectMemberModel != null)
            mUserChangeProfileView.setProjectMemberModel(projectMemberModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load UserChangeProfileView to fragment --
        return mUserChangeProfileView.getView();
    }

    @Override
    public void onUserChangeProfileRequest(ProjectMemberModel projectMemberModel) {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());

        // -- Get SettingUserModel from SettingPersistent --
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare UserChangeProfileHandleTask --
        UserChangeProfileHandleTask userChangeProfileHandleTask = new UserChangeProfileHandleTask() {
            @Override
            public void onPostExecute(UserChangeProfileHandleTaskResult userChangeProfileHandleTaskResult) {
                if (userChangeProfileHandleTaskResult != null) {
                    SimpleResponseModel simpleResponseModel = userChangeProfileHandleTaskResult.getSimpleResponseModel();
                    if (simpleResponseModel != null) {
                        if (simpleResponseModel.isSuccess())
                            onUserChangeProfileRequestSuccess(simpleResponseModel);
                        else
                            onUserChangeProfileRequestFailed(simpleResponseModel.getMessage());
                    } else {
                        onUserChangeProfileRequestFailed(userChangeProfileHandleTaskResult.getMessage());
                    }
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onUserChangeProfileRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do UserChangeProfileHandleTask --
        userChangeProfileHandleTask.execute(new UserChangeProfileHandleTaskParam(getContext(), settingUserModel, projectMemberModel));
    }

    protected void onUserChangeProfileRequestProgress(final String progressMessage) {
        // -- Show progress dialog --
        mUserChangeProfileView.progressDialogShow(progressMessage);
    }

    protected void onUserChangeProfileRequestSuccess(final SimpleResponseModel simpleResponseModel) {
        // -- Hide progress dialog --
        mUserChangeProfileView.progressDialogDismiss();

        // -- Show success dialog --
        mUserChangeProfileView.alertDialogFirstSuccess(simpleResponseModel.getMessage());

        // -- Callback to UserChangeProfileFragmentListener --
        if (mUserChangeProfileFragmentListener != null)
            mUserChangeProfileFragmentListener.onUserChangeProfileSuccess(simpleResponseModel);
    }

    protected void onUserChangeProfileRequestFailed(final String errorMessage) {
        // -- Hide progress dialog --
        mUserChangeProfileView.progressDialogDismiss();

        // -- Show error dialog --
        mUserChangeProfileView.alertDialogErrorShow(errorMessage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    protected class UserChangeProfileHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectMemberModel mProjectMemberModel;

        public UserChangeProfileHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class UserChangeProfileHandleTaskResult {

        protected SimpleResponseModel mSimpleResponseModel;
        protected String mMessage;

        public UserChangeProfileHandleTaskResult() {

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

    protected class UserChangeProfileHandleTask extends AsyncTask<UserChangeProfileHandleTaskParam, String, UserChangeProfileHandleTaskResult> {
        protected UserChangeProfileHandleTaskParam mUserChangeProfileHandleTaskParam;
        protected Context mContext;

        @Override
        protected UserChangeProfileHandleTaskResult doInBackground(UserChangeProfileHandleTaskParam... userChangeProfileHandleTaskParams) {
            // Get UserChangeProfileHandleTaskParam
            mUserChangeProfileHandleTaskParam = userChangeProfileHandleTaskParams[0];
            mContext = mUserChangeProfileHandleTaskParam.getContext();

            // -- Prepare UserChangeProfileHandleTaskResult --
            UserChangeProfileHandleTaskResult userChangeProfileHandleTaskResult = new UserChangeProfileHandleTaskResult();

            // -- Change Profile to server begin progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_begin));

            // -- Prepare UserNetwork --
            UserNetwork userNetwork = new UserNetwork(mContext, mUserChangeProfileHandleTaskParam.getSettingUserModel());

            SimpleResponseModel simpleResponseModel = null;
            try {
                // -- Invalidate Access Token --
                userNetwork.invalidateAccessToken();

                // -- Change Profile to server --
                simpleResponseModel = userNetwork.updateProfile(mUserChangeProfileHandleTaskParam.getProjectMemberModel());

                // -- Invalidate Login --
                userNetwork.invalidateLogin();
            } catch (WebApiError webApiError) {
                userChangeProfileHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (simpleResponseModel != null) {
                // -- Change Profile to server successfully --
                userChangeProfileHandleTaskResult.setSimpleResponseModel(simpleResponseModel);
                userChangeProfileHandleTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_success));

                // -- Change Profile to server successfully progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_success));
            }

            return userChangeProfileHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setUserChangeProfileFragmentListener(final UserChangeProfileFragmentListener userChangeProfileFragmentListener) {
        mUserChangeProfileFragmentListener = userChangeProfileFragmentListener;
    }

    public interface UserChangeProfileFragmentListener {
        void onUserChangeProfileSuccess(SimpleResponseModel simpleResponseModel);
    }
}
