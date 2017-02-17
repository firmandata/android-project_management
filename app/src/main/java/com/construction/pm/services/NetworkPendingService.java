package com.construction.pm.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

public class NetworkPendingService extends Service {

    protected NetworkPendingWorker mNetworkPendingWorker;

    protected Messenger mMessengerClient;
    protected NetworkPendingMessageHandler mNetworkPendingMessageHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkPendingWorker = new NetworkPendingWorker(this);
        mNetworkPendingWorker.setDaemon(true);

        mNetworkPendingMessageHandler = new NetworkPendingMessageHandler(this);
        mMessengerClient = new Messenger(mNetworkPendingMessageHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mNetworkPendingWorker.isAlive())
            mNetworkPendingWorker.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessengerClient.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mNetworkPendingWorker.interrupt();

        super.onDestroy();
    }
}
