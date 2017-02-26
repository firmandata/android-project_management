package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.SessionCheckAsyncTaskParam;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.utils.ViewUtil;

public class SessionCheckAsyncTask extends AsyncTask<SessionCheckAsyncTaskParam, String, SessionLoginModel> {

    protected static final int WAIT_FOR_USER_READ_ERROR_MESSAGE = 2000;

    protected SessionCheckAsyncTaskParam mSessionCheckAsyncTaskParam;
    protected Context mContext;

    @Override
    protected SessionLoginModel doInBackground(SessionCheckAsyncTaskParam... sessionCheckAsyncTaskParams) {
        // -- Get SessionCheckAsyncTaskParam --
        mSessionCheckAsyncTaskParam = sessionCheckAsyncTaskParams[0];
        mContext = mSessionCheckAsyncTaskParam.getContext();

        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, mSessionCheckAsyncTaskParam.getSettingUserModel());

        // -- AuthenticationNetwork handle --
        SessionLoginModel sessionLoginModel = null;
        try {
            // -- Set publishProgress AccessTokenModel handle --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_handle_access_token));

            // -- AccessTokenModel handle --
            sessionLoginModel = userNetwork.invalidateAccessToken();

            if (sessionLoginModel.getUserProjectMemberModel() != null) {
                // -- Set publishProgress UserProjectMemberModel handle --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_handle_user_project_member));

                // -- UserProjectMemberModel handle --
                sessionLoginModel = userNetwork.invalidateLogin();
            }

            // -- Set publishProgress as finish --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.session_handle_task_finish));
        } catch (WebApiError webApiError) {
            // -- Set new SessionLoginModel --
            sessionLoginModel = userNetwork.getSessionLoginModel();

            // -- Set publishProgress --
            publishProgress(webApiError.getMessage());

            // -- Pause process for user read error message --
            try {
                Thread.sleep(WAIT_FOR_USER_READ_ERROR_MESSAGE);
            } catch (InterruptedException interruptedException) {
            }
        }

        return sessionLoginModel;
    }
}