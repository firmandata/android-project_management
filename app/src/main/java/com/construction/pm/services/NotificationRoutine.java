package com.construction.pm.services;

import android.content.Context;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.NotificationNetwork;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SettingPersistent;

public class NotificationRoutine extends Thread {

    protected final static int WAIT_TIME = 20000;

    protected Context mContext;
    protected NotificationRoutineListener mNotificationRoutineListener;

    public NotificationRoutine(final Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        while (true) {
            // -- Get SettingUserModel --
            SettingUserModel settingUserModel = getSettingUserModel();
            if (settingUserModel != null) {
                // -- Invalidate SessionLoginModel and get invalidated SessionLoginModel --
                SessionLoginModel sessionLoginModel = invalidateSessionLoginModel(settingUserModel);
                if (sessionLoginModel != null) {
                    // -- Get ProjectMemberModel --
                    ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
                    if (projectMemberModel != null) {
                        // -- Update NotificationModels from Server --
                        updateNotification(settingUserModel, projectMemberModel);
                    } else {
                        // -- Need login --
                        if (mNotificationRoutineListener != null) {
                            mNotificationRoutineListener.onNotificationRoutineRequestLogin(sessionLoginModel);
                            break;
                        }
                    }
                }
            }

            try {
                // -- Wait 10 seconds --
                sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                break;
            }
        }

        if (mNotificationRoutineListener != null)
            mNotificationRoutineListener.onNotificationRoutineStop();
    }

    protected SettingUserModel getSettingUserModel() {
        // -- Prepare SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(mContext);

        // -- Get SettingUserModel from SettingPersistent --
        return settingPersistent.getSettingUserModel();
    }

    protected SessionLoginModel invalidateSessionLoginModel(final SettingUserModel settingUserModel) {
        // -- Prepare UserNetwork --
        UserNetwork userNetwork = new UserNetwork(mContext, settingUserModel);

        SessionLoginModel sessionLoginModel = null;
        try {
            // -- AccessTokenModel handle --
            sessionLoginModel = userNetwork.invalidateAccessToken();

            if (sessionLoginModel.getUserProjectMemberModel() != null) {
                // -- UserProjectMemberModel handle --
                sessionLoginModel = userNetwork.invalidateLogin();
            }
        } catch (WebApiError webApiError) {
        }

        return sessionLoginModel;
    }

    protected void updateNotification(final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
        // -- Prepare NotificationPersistent --
        NotificationPersistent notificationPersistent = new NotificationPersistent(mContext);

        // -- Prepare NotificationNetwork --
        NotificationNetwork notificationNetwork = new NotificationNetwork(mContext, settingUserModel);

        // -- Get last NotificationPersistent --
        NotificationModel lastNotificationModel = null;
        try {
            lastNotificationModel = notificationPersistent.getLastNotificationModel(projectMemberModel.getProjectMemberId());
        } catch (PersistenceError persistenceError) {
        }
        Integer lastProjectNotificationId = 0;
        if (lastNotificationModel != null)
            lastProjectNotificationId = lastNotificationModel.getProjectNotificationId();

        // -- Get NotificationModels from server --
        NotificationModel[] notificationModels = null;
        try {
            notificationModels = notificationNetwork.getNotifications(projectMemberModel.getProjectMemberId(), lastProjectNotificationId);
        } catch (WebApiError webApiError) {
        }

        if (notificationModels != null) {
            try {
                // -- Save to NotificationPersistent --
                notificationPersistent.saveNotificationModels(notificationModels, projectMemberModel.getProjectMemberId());

                // -- Get unread NotificationModels --
                NotificationModel[] unReadNotificationModels = notificationPersistent.getUnreadNotificationModels(projectMemberModel.getProjectMemberId());

                // -- Notify get new NotificationModels --
                if (mNotificationRoutineListener != null) {
                    if (unReadNotificationModels != null) {
                        if (unReadNotificationModels.length > 0)
                            mNotificationRoutineListener.onNotificationRoutineGetNew(unReadNotificationModels);
                    }
                }
            } catch (PersistenceError ex) {
            }
        }
    }

    public void setNotificationHandlerListener(final NotificationRoutineListener notificationRoutineListener) {
        mNotificationRoutineListener = notificationRoutineListener;
    }

    public interface NotificationRoutineListener {
        void onNotificationRoutineGetNew(NotificationModel[] notificationModels);
        void onNotificationRoutineRequestLogin(SessionLoginModel sessionLoginModel);
        void onNotificationRoutineStop();
    }
}