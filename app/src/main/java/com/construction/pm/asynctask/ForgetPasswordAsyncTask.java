package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.ForgetPasswordAsyncTaskParam;
import com.construction.pm.asynctask.result.ForgetPasswordAsyncTaskResult;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class ForgetPasswordAsyncTask extends AsyncTask<ForgetPasswordAsyncTaskParam, String, ForgetPasswordAsyncTaskResult> {
    protected ForgetPasswordAsyncTaskParam mForgetPasswordAsyncTaskParam;
    protected Context mContext;

    @Override
    protected ForgetPasswordAsyncTaskResult doInBackground(ForgetPasswordAsyncTaskParam... forgetPasswordAsyncTaskParams) {
        // Get ForgetPasswordAsyncTaskParam
        mForgetPasswordAsyncTaskParam = forgetPasswordAsyncTaskParams[0];
        mContext = mForgetPasswordAsyncTaskParam.getContext();

        // -- Prepare ForgetPasswordAsyncTaskResult --
        ForgetPasswordAsyncTaskResult forgetPasswordAsyncTaskResult = new ForgetPasswordAsyncTaskResult();

        // -- Send request reset password to server begin progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.forget_password_handle_task_begin));

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mForgetPasswordAsyncTaskParam.getSettingUserModel());

        // -- Send request reset password to server --
        SimpleResponseModel simpleResponseModel = null;
        try {
            simpleResponseModel = userNetwork.forgetPassword(mForgetPasswordAsyncTaskParam.getLogin());
        } catch (WebApiError webApiError) {
            forgetPasswordAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (simpleResponseModel != null) {
            // -- Send request reset password to server successfully --
            forgetPasswordAsyncTaskResult.setSimpleResponseModel(simpleResponseModel);
            forgetPasswordAsyncTaskResult.setMessage(simpleResponseModel.getMessage());

            // -- Send request reset password to server successfully progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.forget_password_handle_task_success));
        }

        return forgetPasswordAsyncTaskResult;
    }
}