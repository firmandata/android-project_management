package com.construction.pm.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MessageClientHandler extends Handler {

    public static final int MSG_CLIENT_REGISTER = 1;
    public static final int MSG_CLIENT_UNREGISTER = 2;

    protected Context mContext;

    protected List<Messenger> mClients = new ArrayList<Messenger>();

    public MessageClientHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handleMessage(Message receiveMessage) {
        switch (receiveMessage.what) {
            case MSG_CLIENT_REGISTER:
                mClients.add(receiveMessage.replyTo);
                break;
            case MSG_CLIENT_UNREGISTER:
                mClients.remove(receiveMessage.replyTo);
                break;
            default:
                super.handleMessage(receiveMessage);
        }
    }

    public void sendMessage(int what, int arg1, int arg2, Bundle data) {
        for (int i = mClients.size() - 1; i >= 0; i--) {
            try {
                Message sendMessageData = Message.obtain(null, what, arg1, arg2);
                if (data != null)
                    sendMessageData.setData(data);
                mClients.get(i).send(sendMessageData);
            } catch (RemoteException e) {
                mClients.remove(i);
            }
        }
    }
}
