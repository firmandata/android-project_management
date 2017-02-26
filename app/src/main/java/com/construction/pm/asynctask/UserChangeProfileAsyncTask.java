package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.UserChangeProfileAsyncTaskParam;
import com.construction.pm.asynctask.result.UserChangeProfileAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class UserChangeProfileAsyncTask extends AsyncTask<UserChangeProfileAsyncTaskParam, String, UserChangeProfileAsyncTaskResult> {
    protected UserChangeProfileAsyncTaskParam mUserChangeProfileAsyncTaskParam;
    protected Context mContext;

    @Override
    protected UserChangeProfileAsyncTaskResult doInBackground(UserChangeProfileAsyncTaskParam... userChangeProfileAsyncTaskParams) {
        // Get UserChangeProfileAsyncTaskParam
        mUserChangeProfileAsyncTaskParam = userChangeProfileAsyncTaskParams[0];
        mContext = mUserChangeProfileAsyncTaskParam.getContext();

        // -- Prepare UserChangeProfileAsyncTaskResult --
        UserChangeProfileAsyncTaskResult userChangeProfileAsyncTaskResult = new UserChangeProfileAsyncTaskResult();

        // -- Change Profile to server begin progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_begin));

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mUserChangeProfileAsyncTaskParam.getSettingUserModel());

        SimpleResponseModel simpleResponseModel = null;
        try {
            // -- Invalidate Access Token --
            userNetwork.invalidateAccessToken();

            // -- Change Profile to server --
            simpleResponseModel = userNetwork.updateProfile(mUserChangeProfileAsyncTaskParam.getProjectMemberModel());

            // -- Invalidate Login --
            userNetwork.invalidateLogin();
        } catch (WebApiError webApiError) {
            userChangeProfileAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (simpleResponseModel != null) {
            // -- Change Profile to server successfully --
            userChangeProfileAsyncTaskResult.setSimpleResponseModel(simpleResponseModel);
            userChangeProfileAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_success));

            // -- Change Profile to server successfully progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_profile_handle_task_success));
        }

        return userChangeProfileAsyncTaskResult;
    }
}