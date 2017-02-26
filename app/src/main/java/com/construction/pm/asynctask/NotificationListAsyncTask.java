package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.NotificationListAsyncTaskParam;
import com.construction.pm.asynctask.result.NotificationListAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class NotificationListAsyncTask extends AsyncTask<NotificationListAsyncTaskParam, String, NotificationListAsyncTaskResult> {
    protected NotificationListAsyncTaskParam mNotificationListAsyncTaskParam;
    protected Context mContext;

    @Override
    protected NotificationListAsyncTaskResult doInBackground(NotificationListAsyncTaskParam... notificationListAsyncTaskParams) {
        // Get NotificationListAsyncTaskParam
        mNotificationListAsyncTaskParam = notificationListAsyncTaskParams[0];
        mContext = mNotificationListAsyncTaskParam.getContext();
        ProjectMemberModel projectMemberModel = mNotificationListAsyncTaskParam.getProjectMemberModel();

        // -- Prepare NotificationListAsyncTaskResult --
        NotificationListAsyncTaskResult notificationListAsyncTaskResult = new NotificationListAsyncTaskResult();

        // -- Get NotificationModels progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_list_handle_task_begin));

        // -- Prepare NotificationPersistent --
        NotificationPersistent notificationPersistent = new NotificationPersistent(mContext);

        // -- Get NotificationModels from NotificationPersistent --
        NotificationModel[] notificationModels = null;
        try {
            notificationModels = notificationPersistent.getNotificationModels(projectMemberModel.getProjectMemberId());
        } catch (PersistenceError ex) {
            notificationListAsyncTaskResult.setMessage(ex.getMessage());
            publishProgress(ex.getMessage());
        }

        if (notificationModels != null) {
            // -- Set NotificationModels to result --
            notificationListAsyncTaskResult.setNotificationModels(notificationModels);

            // -- Get NotificationModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_list_handle_task_success));
        }

        return notificationListAsyncTaskResult;
    }
}