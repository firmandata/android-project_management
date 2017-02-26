package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.LoginFirstAsyncTaskParam;
import com.construction.pm.asynctask.result.LoginFirstAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class LoginFirstAsyncTask extends AsyncTask<LoginFirstAsyncTaskParam, String, LoginFirstAsyncTaskResult> {
    protected LoginFirstAsyncTaskParam mLoginFirstAsyncTaskParam;
    protected Context mContext;

    @Override
    protected LoginFirstAsyncTaskResult doInBackground(LoginFirstAsyncTaskParam... loginFirstAsyncTaskParams) {
        // Get LoginFirstAsyncTaskParam
        mLoginFirstAsyncTaskParam = loginFirstAsyncTaskParams[0];
        mContext = mLoginFirstAsyncTaskParam.getContext();

        // -- Prepare LoginFirstAsyncTaskResult --
        LoginFirstAsyncTaskResult loginFirstAsyncTaskResult = new LoginFirstAsyncTaskResult();

        // -- Login first change password to server begin progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_begin));

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mLoginFirstAsyncTaskParam.getSettingUserModel());

        // -- Login first change password to server --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = userNetwork.changePasswordFirst(mLoginFirstAsyncTaskParam.getPasswordNew());
        } catch (WebApiError webApiError) {
            loginFirstAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (simpleResponseModel != null) {
            // -- Login first change password to server successfully --
            loginFirstAsyncTaskResult.setSimpleResponseModel(simpleResponseModel);
            loginFirstAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_success));

            // -- Login first change password to server successfully progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.login_first_handle_task_success));
        }

        return loginFirstAsyncTaskResult;
    }
}