package com.construction.pm.networks;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.network.AccessTokenModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.networks.webapi.WebApiResponse;
import com.construction.pm.utils.ViewUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NotificationNetwork extends AuthenticationNetwork {
    public NotificationNetwork(Context context, SettingUserModel settingUserModel) {
        super(context, settingUserModel);
    }

    public NotificationModel[] getNotifications(final Integer projectMemberId, final Integer lastProjectNotificationId) throws WebApiError {
        // -- Get SessionLoginModel --
        SessionLoginModel sessionLoginModel = getSessionLoginModel();
        AccessTokenModel accessTokenModel = sessionLoginModel.getAccessTokenModel();

        // -- Prepare WebApiParam headerParam parameters --
        WebApiParam headerParam = new WebApiParam();
        if (accessTokenModel != null)
            headerParam.add("Authorization", "Bearer " + accessTokenModel.getAccessToken());

        // -- Prepare WebApiParam formData parameters --
        WebApiParam formData = new WebApiParam();
        formData.add("project_member_id", projectMemberId);
        formData.add("last_project_notification_id", lastProjectNotificationId);

        // -- Request get projects --
        WebApiResponse webApiResponse = mWebApiRequest.post("/rest/project/getProjectNotification", headerParam, null, formData);

        // -- Throw WebApiError if existing --
        WebApiError webApiError = webApiResponse.getWebApiError();
        if (webApiError != null)
            throw webApiError;

        // -- Get request result --
        org.json.JSONObject jsonObject = webApiResponse.getSuccessJsonObject();
        if (jsonObject == null)
            throw new WebApiError(0, ViewUtil.getResourceString(mContext, R.string.network_unknown_response_expected));

        // -- Fetch result --
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();
        try {
            if (!jsonObject.isNull("result")) {
                org.json.JSONObject jsonResult = jsonObject.getJSONObject("result");
                if (!jsonResult.isNull("notification")) {
                    org.json.JSONArray jsonResultNotifications = jsonResult.getJSONArray("notification");
                    for (int resultNotificationIdx = 0; resultNotificationIdx < jsonResultNotifications.length(); resultNotificationIdx++) {
                        org.json.JSONObject jsonResultNotification = jsonResultNotifications.getJSONObject(resultNotificationIdx);
                        notificationModelList.add(NotificationModel.build(jsonResultNotification));
                    }
                }
            }
        } catch (JSONException jsonException) {
            throw new WebApiError(0, jsonException.getMessage(), jsonException);
        }

        NotificationModel[] notificationModels = new NotificationModel[notificationModelList.size()];
        notificationModelList.toArray(notificationModels);
        return notificationModels;
    }
}
