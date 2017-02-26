package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.NotificationReadAsyncTaskParam;
import com.construction.pm.asynctask.result.NotificationReadAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.networks.NotificationNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class NotificationReadAsyncTask extends AsyncTask<NotificationReadAsyncTaskParam, String, NotificationReadAsyncTaskResult> {
    protected NotificationReadAsyncTaskParam mNotificationReadAsyncTaskParam;
    protected Context mContext;

    @Override
    protected NotificationReadAsyncTaskResult doInBackground(NotificationReadAsyncTaskParam... notificationReadAsyncTaskParams) {
        // Get NotificationReadAsyncTaskParam
        mNotificationReadAsyncTaskParam = notificationReadAsyncTaskParams[0];
        mContext = mNotificationReadAsyncTaskParam.getContext();
        NotificationModel notificationModel = mNotificationReadAsyncTaskParam.getNotificationModel();

        // -- Prepare NotificationReadAsyncTaskResult --
        NotificationReadAsyncTaskResult notificationReadAsyncTaskResult = new NotificationReadAsyncTaskResult();
        notificationReadAsyncTaskResult.setNotificationModel(notificationModel);

        // -- Get NotificationModel marking as read progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_begin));

        // -- Prepare NotificationNetwork --
        NotificationNetwork notificationNetwork = new NotificationNetwork(mContext, mNotificationReadAsyncTaskParam.getSettingUserModel());

        // -- Prepare NotificationPersistent --
        NotificationPersistent notificationPersistent = new NotificationPersistent(mContext);

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = mNotificationReadAsyncTaskParam.getProjectMemberModel();
        if (projectMemberModel != null) {
            try {
                // -- Save NotificationModel to NotificationPersistent --
                notificationPersistent.saveNotificationModel(notificationModel);

                // -- Set NotificationModel as read to server --
                try {
                    notificationNetwork.setNotificationRead(notificationModel.getProjectNotificationId(), projectMemberModel.getUserId());

                    // -- Set NotificationReadAsyncTaskResult message --
                    notificationReadAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_success));
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Prepare NetworkPendingPersistent --
                        NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                        // -- Create NetworkPendingModel --
                        NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.NOTIFICATION_READ);
                        networkPendingModel.setCommandKey(String.valueOf(notificationModel.getProjectNotificationId()));
                        try {
                            // -- Save NetworkPendingModel to NetworkPendingPersistent
                            networkPendingPersistent.createNetworkPending(networkPendingModel);

                            // -- Set NotificationReadAsyncTaskResult message --
                            notificationReadAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_success_pending));
                        } catch (PersistenceError ex) {
                        }
                    } else {
                        // -- Set NotificationReadAsyncTaskResult message --
                        notificationReadAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_failed, webApiError.getMessage()));
                    }
                }
            } catch (PersistenceError persistenceError) {
                // -- Set NotificationReadAsyncTaskResult message --
                notificationReadAsyncTaskResult.setMessage(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_failed, persistenceError.getMessage()));
            }
        }

        // -- Get NotificationModel marked as read finish progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_read_handle_task_finish));

        return notificationReadAsyncTaskResult;
    }
}