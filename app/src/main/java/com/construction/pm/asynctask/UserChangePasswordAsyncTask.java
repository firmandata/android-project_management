package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.UserChangePasswordAsyncTaskParam;
import com.construction.pm.asynctask.result.UserChangePasswordAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class UserChangePasswordAsyncTask extends AsyncTask<UserChangePasswordAsyncTaskParam, String, UserChangePasswordAsyncTaskResult> {
    protected UserChangePasswordAsyncTaskParam mUserChangePasswordAsyncTaskParam;
    protected Context mContext;

    @Override
    protected UserChangePasswordAsyncTaskResult doInBackground(UserChangePasswordAsyncTaskParam... userChangePasswordAsyncTaskParams) {
        // Get UserChangePasswordAsyncTaskParam
        mUserChangePasswordAsyncTaskParam = userChangePasswordAsyncTaskParams[0];
        mContext = mUserChangePasswordAsyncTaskParam.getContext();

        // -- Prepare UserChangePasswordAsyncTaskResult --
        UserChangePasswordAsyncTaskResult userChangePasswordAsyncTaskResult = new UserChangePasswordAsyncTaskResult();

        // -- Change password to server begin progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_begin));

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mUserChangePasswordAsyncTaskParam.getSettingUserModel());

        SimpleResponseModel simpleResponseModel = null;
        try {
            // -- Invalidate Access Token --
            userNetwork.invalidateAccessToken();

            // -- Change password to server --
            simpleResponseModel = userNetwork.changePassword(mUserChangePasswordAsyncTaskParam.getPasswordOld(), mUserChangePasswordAsyncTaskParam.getPasswordNew());
        } catch (WebApiError webApiError) {
            userChangePasswordAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (simpleResponseModel != null) {
            // -- Change password to server successfully --
            userChangePasswordAsyncTaskResult.setSimpleResponseModel(simpleResponseModel);
            userChangePasswordAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_success));

            // -- Change password to server successfully progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.user_change_password_handle_task_success));
        }

        return userChangePasswordAsyncTaskResult;
    }
}