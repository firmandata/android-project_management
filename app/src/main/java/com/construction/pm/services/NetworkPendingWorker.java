package com.construction.pm.services;

import android.content.Context;

import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.UserNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiRequest;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.persistence.NetworkPendingPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SettingPersistent;

public class NetworkPendingWorker extends Thread {

    protected final static int WAIT_TIME = 20000;

    protected Context mContext;

    public NetworkPendingWorker(final Context context) {
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
                        // -- Prepare NetworkPendingPersistent --
                        NetworkPendingPersistent networkPendingPersistent = new NetworkPendingPersistent(mContext);

                        // -- Get Unsent NetworkPendingModels from NetworkPendingPersistent --
                        NetworkPendingModel[] networkPendingModels = null;
                        try {
                            networkPendingModels = networkPendingPersistent.getUnSentNetworkPendingModels(projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError persistenceError) {
                        }

                        // -- Send NetworkPendingModel --
                        if (networkPendingModels != null) {
                            for (NetworkPendingModel networkPendingModel : networkPendingModels) {
                                if (sendNetworkPendingModel(settingUserModel, networkPendingModel)) {
                                    try {
                                        // -- NetworkPendingModel mark as sent --
                                        networkPendingModel.setSent(true);

                                        // -- Save to NetworkPendingPersistent
                                        networkPendingPersistent.setNetworkPendingSent(networkPendingModel);
                                    } catch (PersistenceError persistenceError) {
                                    }
                                }
                            }
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

    protected boolean sendNetworkPendingModel(final SettingUserModel settingUserModel, final NetworkPendingModel networkPendingModel) {
        boolean isSent = false;

        WebApiResponse webApiResponseRequest = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(networkPendingModel.getCommand());
            webApiResponseRequest = WebApiResponse.buildWebApiRequestCommand(jsonObject);
        } catch (org.json.JSONException e) {
        }

        if (webApiResponseRequest != null) {
            WebApiRequest webApiRequest = new WebApiRequest(mContext);
            webApiRequest.setSettingUserModel(settingUserModel);

            WebApiResponse webApiResponse = null;
            if (networkPendingModel.getType() == NetworkPendingModel.ECommandType.NOTIFICATION_RECEIVED) {
                webApiResponse = webApiRequest.post(webApiResponseRequest.getApiUrl(), webApiResponseRequest.getHeaderParam(), null, webApiResponseRequest.getFormData());
            } else if (networkPendingModel.getType() == NetworkPendingModel.ECommandType.NOTIFICATION_READ) {
                webApiResponse = webApiRequest.post(webApiResponseRequest.getApiUrl(), webApiResponseRequest.getHeaderParam(), null, webApiResponseRequest.getFormData());
            } else if (networkPendingModel.getType() == NetworkPendingModel.ECommandType.PROJECT_ACTIVITY_UPDATE_SAVE) {
                webApiResponse = webApiRequest.post(webApiResponseRequest.getApiUrl(), webApiResponseRequest.getHeaderParam(), null, webApiResponseRequest.getFormData());
            } else if (networkPendingModel.getType() == NetworkPendingModel.ECommandType.PROJECT_STAGE_ASSIGN_COMMENT_SAVE) {
                webApiResponse = webApiRequest.post(webApiResponseRequest.getApiUrl(), webApiResponseRequest.getHeaderParam(), null, webApiResponseRequest.getFormData());
            } else if (networkPendingModel.getType() == NetworkPendingModel.ECommandType.INSPECTOR_PROJECT_ACTIVITY_MONITORING_SAVE) {
                webApiResponse = webApiRequest.post(webApiResponseRequest.getApiUrl(), webApiResponseRequest.getHeaderParam(), null, webApiResponseRequest.getFormData());
            }

            if (webApiResponse != null) {
                // -- Throw WebApiError if existing --
                WebApiError webApiError = webApiResponse.getWebApiError();
                if (webApiError != null) {
                    if (!webApiError.isErrorConnection())
                        isSent = true;
                } else {
                    isSent = true;
                }
            }
        }

        return isSent;
    }
}
