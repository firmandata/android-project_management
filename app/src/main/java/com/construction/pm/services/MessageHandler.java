package com.construction.pm.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageHandler extends Handler {
    public static final int MSG_CLIENT_REGISTER = 1;
    public static final int MSG_CLIENT_UNREGISTER = 2;

    protected Context mContext;

    protected List<Messenger> mClients;

    protected MessageHandlerListener mMessageHandlerListener;

    public MessageHandler(Context context) {
        mContext = context;
        mClients = new ArrayList<Messenger>();
    }

    @Override
    public void handleMessage(Message receiveMessage) {
        int arg1 = receiveMessage.arg1;
        int arg2 = receiveMessage.arg2;
        Bundle bundle = receiveMessage.getData();

        switch (receiveMessage.what) {
            case MSG_CLIENT_REGISTER:
                onReceiveRegister(receiveMessage.replyTo);
                break;
            case MSG_CLIENT_UNREGISTER:
                onReceiveUnregister(receiveMessage.replyTo);
                break;
            default:
                super.handleMessage(receiveMessage);
        }
    }

    protected int sendMessage(final int what, final int arg1, final int arg2, final Bundle bundle) {
        int sentCount = 0;
        for (int i = mClients.size() - 1; i >= 0; i--) {
            Message message = Message.obtain(null, what, arg1, arg2);
            if (bundle != null)
                message.setData(bundle);
            try {
                mClients.get(i).send(message);
                sentCount++;
            } catch (RemoteException ex) {
                mClients.remove(i);
            }
        }
        return sentCount;
    }

    protected boolean sendMessage(final Messenger messenger, final int what, final int arg1, final int arg2, final Bundle bundle) {
        Message message = Message.obtain(null, what, arg1, arg2);
        if (bundle != null)
            message.setData(bundle);

        boolean isSent = false;
        try {
            messenger.send(message);
            isSent = true;
        } catch (RemoteException ex) {
        }

        return isSent;
    }

    protected void onReceiveRegister(final Messenger messenger) {
        mClients.add(messenger);

        if (mMessageHandlerListener != null)
            mMessageHandlerListener.onReceiveRegister(messenger);
    }

    protected void onReceiveUnregister(final Messenger messenger) {
        if (mMessageHandlerListener != null)
            mMessageHandlerListener.onReceiveUnregister(messenger);

        mClients.remove(messenger);
    }

    public void setMessageHandlerListener(final MessageHandlerListener messageHandlerListener) {
        mMessageHandlerListener = messageHandlerListener;
    }

    public interface MessageHandlerListener {
        void onReceiveRegister(Messenger messenger);
        void onReceiveUnregister(Messenger messenger);
    }

    public static boolean sendRegister(final Messenger messengerSender, final Messenger messengerReceiver) {
        boolean isSent = false;

        Message message = Message.obtain(null, MSG_CLIENT_REGISTER);
        message.replyTo = messengerReceiver;

        try {
            messengerSender.send(message);
            isSent = true;
        } catch (RemoteException ex) {
        }

        return isSent;
    }

    public static boolean sendUnregister(final Messenger messengerSender, final Messenger messengerReceiver) {
        boolean isSent = false;

        Message message = Message.obtain(null, MSG_CLIENT_UNREGISTER);
        message.replyTo = messengerReceiver;

        try {
            messengerSender.send(message);
            isSent = true;
        } catch (RemoteException ex) {
        }

        return isSent;
    }
}
