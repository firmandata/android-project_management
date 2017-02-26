package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.LogoutAsyncTaskParam;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.utils.ViewUtil;

public class LogoutAsyncTask extends AsyncTask<LogoutAsyncTaskParam, String, Boolean> {

    protected LogoutAsyncTaskParam mLogoutAsyncTaskParam;
    protected Context mContext;

    @Override
    protected Boolean doInBackground(LogoutAsyncTaskParam... logoutAsyncTaskParams) {
        // -- Get LogoutAsyncTaskParam --
        mLogoutAsyncTaskParam = logoutAsyncTaskParams[0];
        mContext = mLogoutAsyncTaskParam.getContext();

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mLogoutAsyncTaskParam.getSettingUserModel());

        // -- Set publishProgress AccessTokenModel handle --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.logout_handle_task_begin));

        // -- Do logout handle --
        userNetwork.doLogout();

        // -- Set publishProgress as finish --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.logout_handle_task_success));

        return true;
    }
}