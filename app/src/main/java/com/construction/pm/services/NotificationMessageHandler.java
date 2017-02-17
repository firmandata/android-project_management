package com.construction.pm.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.system.SessionLoginModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationMessageHandler extends MessageHandler {
    public static final int MSG_NOTIFICATION_NEW = 3;
    public static final int MSG_REQUEST_LOGIN = 4;
    public static final int MSG_STOP = 5;

    protected NotificationMessageHandlerListener mNotificationMessageHandlerListener;

    public NotificationMessageHandler(Context context) {
        super(context);
    }

    @Override
    public void handleMessage(Message receiveMessage) {
        int arg1 = receiveMessage.arg1;
        int arg2 = receiveMessage.arg2;
        Bundle bundle = receiveMessage.getData();

        switch (receiveMessage.what) {
            case MSG_NOTIFICATION_NEW:
                onReceiveNotificationNew(bundle);
                break;
            case MSG_REQUEST_LOGIN:
                onReceiveRequestLogin(bundle);
                break;
            case MSG_STOP:
                onReceiveStop();
                break;
            default:
                super.handleMessage(receiveMessage);
        }
    }

    public int sendNotificationModels(final NotificationModel[] notificationModels) {
        String notificationModelsJson = null;
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (NotificationModel notificationModel : notificationModels) {
                jsonArray.put(notificationModel.build());
            }
            notificationModelsJson = jsonArray.toString(0);
        } catch (org.json.JSONException ex) {
        }

        Bundle bundle = new Bundle();
        bundle.putString("NotificationModels", notificationModelsJson);
        return sendMessage(MSG_NOTIFICATION_NEW, 0, 0, bundle);
    }

    public int sendRequestLogin(final SessionLoginModel sessionLoginModel) {
        String sessionLoginModelJson = null;
        try {
            sessionLoginModelJson = sessionLoginModel.build().toString(0);
        } catch (org.json.JSONException ex) {
        }

        Bundle bundle = new Bundle();
        bundle.putString("SessionLoginModel", sessionLoginModelJson);
        return sendMessage(MSG_REQUEST_LOGIN, 0, 0, null);
    }

    public int sendStop() {
        return sendMessage(MSG_STOP, 0, 0, null);
    }

    protected void onReceiveNotificationNew(final Bundle bundle) {
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();
        if (bundle != null) {
            String notificationModelsJson = bundle.getString("NotificationModels");
            if (notificationModelsJson != null) {
                try {
                    org.json.JSONArray jsonArray = new org.json.JSONArray(notificationModelsJson);
                    for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                        org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                        notificationModelList.add(NotificationModel.build(jsonObject));
                    }
                } catch (org.json.JSONException ex) {
                }
            }
        }

        if (notificationModelList.size() == 0)
            return;

        NotificationModel[] notificationModels = new NotificationModel[notificationModelList.size()];
        notificationModelList.toArray(notificationModels);

        if (mNotificationMessageHandlerListener != null)
            mNotificationMessageHandlerListener.onNotificationReceives(notificationModels);
    }

    protected void onReceiveRequestLogin(final Bundle bundle) {
        SessionLoginModel sessionLoginModel = null;
        if (bundle != null) {
            String sessionLoginModelJson = bundle.getString("SessionLoginModel");
            if (sessionLoginModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(sessionLoginModelJson);
                    sessionLoginModel = SessionLoginModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        if (sessionLoginModel == null)
            return;

        if (mNotificationMessageHandlerListener != null)
            mNotificationMessageHandlerListener.onNotificationRequestLogin(sessionLoginModel);
    }

    protected void onReceiveStop() {
        if (mNotificationMessageHandlerListener != null)
            mNotificationMessageHandlerListener.onNotificationServiceStop();
    }

    public void setNotificationMessageHandlerListener(final NotificationMessageHandlerListener notificationMessageHandlerListener) {
        mNotificationMessageHandlerListener = notificationMessageHandlerListener;
    }

    public interface NotificationMessageHandlerListener {
        void onNotificationReceives(NotificationModel[] notificationModels);
        void onNotificationRequestLogin(SessionLoginModel sessionLoginModel);
        void onNotificationServiceStop();
    }
}
