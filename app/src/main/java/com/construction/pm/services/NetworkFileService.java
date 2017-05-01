package com.construction.pm.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.util.SparseArray;

import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;

public class NetworkFileService extends Service implements NetworkFileMessageHandler.NetworkFileMessageHandlerInitialListener {

    protected Messenger mMessengerClient;
    protected NetworkFileMessageHandler mNotificationMessageHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("NetworkFileService", "onCreate");

        mNotificationMessageHandler = new NetworkFileMessageHandler(this);
        mNotificationMessageHandler.setNetworkFileMessageHandlerInitialListener(this);
        mMessengerClient = new Messenger(mNotificationMessageHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NetworkFileService", "onStartCommand");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("NetworkFileService", "onBind");
        return mMessengerClient.getBinder();
    }

    @Override
    public void onDownloadAdd(Integer fileId) {
        downloadFile(fileId);
    }

    protected void downloadFile(final Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        final SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileGetNetworkAsyncTask --
        final FileGetNetworkAsyncTask fileGetNetworkAsyncTask = new FileGetNetworkAsyncTask() {
            @Override
            public void onPreExecute() {
                mNotificationMessageHandler.downloadStart(fileId);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                FileModel fileModel = null;
                FileModel fileModelCache = null;
                if (fileRequestAsyncTaskResult != null) {
                    fileModel = fileRequestAsyncTaskResult.getFileModel();
                    fileModelCache = fileRequestAsyncTaskResult.getFileModelCache();
                }
                mNotificationMessageHandler.downloadFinish(fileModel, fileModelCache);
            }

            @Override
            protected void onProgressUpdate(String... progress) {
                if (progress != null) {
                    if (progress.length > 0) {
                        mNotificationMessageHandler.downloadProgress(fileId, progress[0]);
                    }
                }
            }
        };

        // -- Prepare FileGetCacheAsyncTask --
        FileGetCacheAsyncTask fileGetCacheAsyncTask = new FileGetCacheAsyncTask() {
            @Override
            public void onPreExecute() {
                mNotificationMessageHandler.start(fileId);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                FileModel fileModel = null;
                if (fileRequestAsyncTaskResult != null)
                    fileModel = fileRequestAsyncTaskResult.getFileModel();
                mNotificationMessageHandler.cacheFinish(fileModel);

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(NetworkFileService.this, settingUserModel, fileId, fileModel));
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new FileGetAsyncTaskParam(this, settingUserModel, fileId, null));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("NetworkFileService", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("NetworkFileService", "onDestroy");
        super.onDestroy();
    }
}
