package com.construction.pm.services;

import android.content.Context;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.NotificationNetwork;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SettingPersistent;

import java.util.Calendar;

public class NotificationWorker extends Thread {

    protected final static int WAIT_TIME = 20000;

    protected Context mContext;
    protected NotificationRoutineListener mNotificationRoutineListener;

    public NotificationWorker(final Context context) {
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
                // -- Set NotificationModels as unread --
                for (NotificationModel notificationModel : notificationModels) {
                    if (notificationModel.getNotificationStatus().equals(NotificationModel.NOTIFICATION_STATUS_SENT))
                        notificationModel.setNotificationStatus(NotificationModel.NOTIFICATION_STATUS_UNREAD);
                    notificationModel.setLastUserId(projectMemberModel.getUserId());
                    notificationModel.setLastUpdate(Calendar.getInstance());
                }

                // -- Save to NotificationPersistent --
                notificationPersistent.saveNotificationModels(notificationModels);

                // -- Get last saved NotificationPersistent --
                try {
                    lastNotificationModel = notificationPersistent.getLastNotificationModel(projectMemberModel.getProjectMemberId());
                    if (lastNotificationModel != null)
                        lastProjectNotificationId = lastNotificationModel.getProjectNotificationId();
                } catch (PersistenceError persistenceError) {
                }

                // -- Set NotificationModels as unread to server --
                try {
                    notificationNetwork.setNotificationReceived(projectMemberModel.getProjectMemberId(), lastProjectNotificationId, projectMemberModel.getUserId());
                } catch (WebApiError webApiError) {
                    if (webApiError.isErrorConnection()) {
                        // -- Prepare NetworkPendingPersistent --
                        NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                        // -- Create NetworkPendingModel --
                        NetworkPendingModel networkPendingModel = new NetworkPendingModel(projectMemberModel.getProjectMemberId(), webApiError.getWebApiResponse(), NetworkPendingModel.ECommandType.NOTIFICATION_RECEIVED);
                        networkPendingModel.setCommandKey(String.valueOf(lastProjectNotificationId));
                        try {
                            // -- Save NetworkPendingModel to NetworkPendingPersistent
                            networkPendingPersistent.createNetworkPending(networkPendingModel);
                        } catch (PersistenceError ex) {
                        }
                    }
                }

                // -- Notify get new NotificationModels --
                if (mNotificationRoutineListener != null) {
                    if (notificationModels.length > 0)
                        mNotificationRoutineListener.onNotificationRoutineGetNew(notificationModels);
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