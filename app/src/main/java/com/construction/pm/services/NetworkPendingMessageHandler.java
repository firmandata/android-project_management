package com.construction.pm.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

public class NetworkPendingMessageHandler extends MessageHandler {

    protected NetworkPendingMessageHandlerListener mNetworkPendingMessageHandlerListener;

    public NetworkPendingMessageHandler(Context context) {
        super(context);
    }

    @Override
    public void handleMessage(Message receiveMessage) {
        int arg1 = receiveMessage.arg1;
        int arg2 = receiveMessage.arg2;
        Bundle bundle = receiveMessage.getData();

        switch (receiveMessage.what) {
            default:
                super.handleMessage(receiveMessage);
        }
    }

    public void setNetworkPendingMessageHandlerListener(final NetworkPendingMessageHandlerListener networkPendingMessageHandlerListener) {
        mNetworkPendingMessageHandlerListener = networkPendingMessageHandlerListener;
    }

    public interface NetworkPendingMessageHandlerListener {

    }
}
