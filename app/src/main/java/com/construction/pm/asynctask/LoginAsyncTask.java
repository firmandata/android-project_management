package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.LoginAsyncTaskParam;
import com.construction.pm.asynctask.result.LoginAsyncTaskResult;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class LoginAsyncTask extends AsyncTask<LoginAsyncTaskParam, String, LoginAsyncTaskResult> {
    protected LoginAsyncTaskParam mLoginAsyncTaskParam;
    protected Context mContext;

    @Override
    protected LoginAsyncTaskResult doInBackground(LoginAsyncTaskParam... loginAsyncTaskParams) {
        // Get LoginAsyncTaskParam
        mLoginAsyncTaskParam = loginAsyncTaskParams[0];
        mContext = mLoginAsyncTaskParam.getContext();

        // -- Prepare LoginAsyncTaskResult --
        LoginAsyncTaskResult loginAsyncTaskResult = new LoginAsyncTaskResult();

        // -- Login to server begin progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.login_handle_task_begin));

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mLoginAsyncTaskParam.getSettingUserModel());

        // -- Login to server --
        SessionLoginModel sessionLoginModel = null;
        try {
            sessionLoginModel = userNetwork.generateLogin(mLoginAsyncTaskParam.getLogin(), mLoginAsyncTaskParam.getPassword());
        } catch (WebApiError webApiError) {
            loginAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (sessionLoginModel != null) {
            // -- Login to server successfully --
            loginAsyncTaskResult.setSessionLoginModel(sessionLoginModel);
            loginAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.login_handle_task_success));

            // -- Login to server successfully progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.login_handle_task_success));
        }

        return loginAsyncTaskResult;
    }
}