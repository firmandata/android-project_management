package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.UserChangeProfileAsyncTask;
import com.construction.pm.asynctask.param.UserChangeProfileAsyncTaskParam;
import com.construction.pm.asynctask.result.UserChangeProfileAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.user.UserChangeProfileView;

import java.util.ArrayList;
import java.util.List;

public class UserChangeProfileFragment extends Fragment implements UserChangeProfileView.UserChangeProfileListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected UserChangeProfileView mUserChangeProfileView;

    protected UserChangeProfileFragmentListener mUserChangeProfileFragmentListener;

    public static UserChangeProfileFragment newInstance() {
        return new UserChangeProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

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

        // -- Prepare UserChangeProfileAsyncTask --
        UserChangeProfileAsyncTask userChangeProfileAsyncTask = new UserChangeProfileAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(UserChangeProfileAsyncTaskResult userChangeProfileHandleTaskResult) {
                mAsyncTaskList.remove(this);

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

        // -- Do UserChangeProfileAsyncTask --
        userChangeProfileAsyncTask.execute(new UserChangeProfileAsyncTaskParam(getContext(), settingUserModel, projectMemberModel));
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

    public void setUserChangeProfileFragmentListener(final UserChangeProfileFragmentListener userChangeProfileFragmentListener) {
        mUserChangeProfileFragmentListener = userChangeProfileFragmentListener;
    }

    public interface UserChangeProfileFragmentListener {
        void onUserChangeProfileSuccess(SimpleResponseModel simpleResponseModel);
    }
}
