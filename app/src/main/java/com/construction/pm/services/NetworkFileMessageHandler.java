package com.construction.pm.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import com.construction.pm.models.FileModel;

public class NetworkFileMessageHandler extends MessageHandler {
    public static final int MSG_REQUEST_FILE = 3;
    public static final int MSG_START = 4;
    public static final int MSG_CACHE_PROGRESS = 5;
    public static final int MSG_CACHE_FINISH = 6;
    public static final int MSG_DOWNLOAD_START = 7;
    public static final int MSG_DOWNLOAD_PROGRESS = 8;
    public static final int MSG_DOWNLOAD_FINISH = 9;

    protected final SparseArray<String> mFileIdProgressList;

    protected NetworkFileMessageHandlerInitialListener mNetworkFileMessageHandlerInitialListener;
    protected NetworkFileMessageHandlerProgressListener mNetworkFileMessageHandlerProgressListener;

    public NetworkFileMessageHandler(Context context) {
        super(context);

        mFileIdProgressList = new SparseArray<String>();
    }

    @Override
    public void handleMessage(Message receiveMessage) {
        int arg1 = receiveMessage.arg1;
        int arg2 = receiveMessage.arg2;
        Bundle bundle = receiveMessage.getData();

        switch (receiveMessage.what) {
            case MSG_REQUEST_FILE:
                onRequestFile(bundle);
                break;
            case MSG_START:
                onStart(bundle);
                break;
            case MSG_CACHE_PROGRESS:
                onCacheProgress(bundle);
                break;
            case MSG_CACHE_FINISH:
                onCacheFinish(bundle);
                break;
            case MSG_DOWNLOAD_START:
                onDownloadStart(bundle);
                break;
            case MSG_DOWNLOAD_PROGRESS:
                onDownloadProgress(bundle);
                break;
            case MSG_DOWNLOAD_FINISH:
                onDownloadFinish(bundle);
                break;
            default:
                super.handleMessage(receiveMessage);
        }
    }

    @Override
    protected void onReceiveRegister(final Messenger messenger) {
        super.onReceiveRegister(messenger);

        // -- Send all current progress --
        for (int fileIdProgressIdx = 0; fileIdProgressIdx < mFileIdProgressList.size(); fileIdProgressIdx++) {
            Integer fileId = mFileIdProgressList.keyAt(fileIdProgressIdx);
            String progress = mFileIdProgressList.get(fileId);

            Bundle bundle = new Bundle();
            bundle.putInt("FileId", fileId);
            bundle.putString("progress", progress);
            sendMessage(messenger, MSG_DOWNLOAD_PROGRESS, 0, 0, bundle);
        }
    }

    public static boolean requestFile(final Messenger messengerSender, final Integer fileId) {
        boolean isSent = false;

        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt("FileId", fileId);

        Message message = Message.obtain(null, MSG_REQUEST_FILE);
        message.setData(bundle);

        try {
            messengerSender.send(message);
            isSent = true;
        } catch (RemoteException ex) {
        }

        return isSent;
    }

    protected void onRequestFile(final Bundle bundle) {
        Integer fileId = null;
        if (bundle != null) {
            fileId = bundle.getInt("FileId");
        }

        if (fileId == null)
            return;

        if (fileId == 0)
            return;

        if (mFileIdProgressList.get(fileId, null) == null) {
            if (mNetworkFileMessageHandlerInitialListener != null)
                mNetworkFileMessageHandlerInitialListener.onDownloadAdd(fileId);
        }
    }

    public int start(final Integer fileId) {
        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt("FileId", fileId);
        return sendMessage(MSG_START, 0, 0, bundle);
    }

    protected void onStart(final Bundle bundle) {
        Integer fileId = null;
        if (bundle != null) {
            fileId = bundle.getInt("FileId");
        }

        if (fileId == null)
            return;

        if (fileId == 0)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileStart(fileId);
    }

    public int cacheProgress(final Integer fileId, final String progress) {
        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt("FileId", fileId);
        bundle.putString("progress", progress);
        return sendMessage(MSG_CACHE_PROGRESS, 0, 0, bundle);
    }

    protected void onCacheProgress(final Bundle bundle) {
        Integer fileId = null;
        String progress = null;
        if (bundle != null) {
            fileId = bundle.getInt("FileId");
            progress = bundle.getString("progress");
        }

        if (fileId == null)
            return;

        if (fileId == 0)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileCacheProgress(fileId, progress);
    }

    public int cacheFinish(final FileModel fileModel) {
        String fileModelJson = null;
        if (fileModel != null) {
            try {
                fileModelJson = fileModel.build().toString(0);
            } catch (org.json.JSONException ex) {
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("FileModel", fileModelJson);
        return sendMessage(MSG_CACHE_FINISH, 0, 0, bundle);
    }

    protected void onCacheFinish(final Bundle bundle) {
        FileModel fileModel = null;
        if (bundle != null) {
            String fileModelJson = bundle.getString("FileModel");
            if (fileModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(fileModelJson);
                    fileModel = FileModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        if (fileModel == null)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileCacheFinish(fileModel);
    }

    public int downloadStart(final Integer fileId) {
        if (fileId != null)
            mFileIdProgressList.put(fileId, "");

        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt("FileId", fileId);
        return sendMessage(MSG_DOWNLOAD_START, 0, 0, bundle);
    }

    protected void onDownloadStart(final Bundle bundle) {
        Integer fileId = null;
        if (bundle != null) {
            fileId = bundle.getInt("FileId");
        }

        if (fileId == null)
            return;

        if (fileId == 0)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileDownloadStart(fileId);
    }

    public int downloadProgress(final Integer fileId, final String progress) {
        if (fileId != null)
            mFileIdProgressList.put(fileId, progress);

        Bundle bundle = new Bundle();
        if (fileId != null)
            bundle.putInt("FileId", fileId);
        bundle.putString("progress", progress);
        return sendMessage(MSG_DOWNLOAD_PROGRESS, 0, 0, bundle);
    }

    protected void onDownloadProgress(final Bundle bundle) {
        Integer fileId = null;
        String progress = null;
        if (bundle != null) {
            fileId = bundle.getInt("FileId");
            progress = bundle.getString("progress");
        }

        if (fileId == null)
            return;

        if (fileId == 0)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileDownloadProgress(fileId, progress);
    }

    public int downloadFinish(final FileModel fileModel, final FileModel fileModelCache) {
        Integer fileId = null;
        if (fileModel != null)
            fileId = fileModel.getFileId();
        else if (fileModelCache != null)
            fileId = fileModelCache.getFileId();

        if (fileId != null)
            mFileIdProgressList.delete(fileId);

        String fileModelJson = null;
        if (fileModel != null) {
            try {
                fileModelJson = fileModel.build().toString(0);
            } catch (org.json.JSONException ex) {
            }
        }
        String fileModelCacheJson = null;
        if (fileModelCache != null) {
            try {
                fileModelCacheJson = fileModelCache.build().toString(0);
            } catch (org.json.JSONException ex) {
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("FileModel", fileModelJson);
        bundle.putString("FileModelCache", fileModelCacheJson);
        return sendMessage(MSG_DOWNLOAD_FINISH, 0, 0, bundle);
    }

    protected void onDownloadFinish(final Bundle bundle) {
        FileModel fileModel = null;
        FileModel fileModelCache = null;
        if (bundle != null) {
            String fileModelJson = bundle.getString("FileModel");
            if (fileModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(fileModelJson);
                    fileModel = FileModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
            String fileModelCacheJson = bundle.getString("FileModelCache");
            if (fileModelCacheJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(fileModelCacheJson);
                    fileModelCache = FileModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }


        if (fileModel == null && fileModelCache == null)
            return;

        if (mNetworkFileMessageHandlerProgressListener != null)
            mNetworkFileMessageHandlerProgressListener.onNetworkFileDownloadFinish(fileModel, fileModelCache);
    }

    public void setNetworkFileMessageHandlerInitialListener(final NetworkFileMessageHandlerInitialListener networkFileMessageHandlerInitialListener) {
        mNetworkFileMessageHandlerInitialListener = networkFileMessageHandlerInitialListener;
    }

    public interface NetworkFileMessageHandlerInitialListener {
        void onDownloadAdd(Integer fileId);
    }

    public void setNetworkFileMessageHandlerProgressListener(final NetworkFileMessageHandlerProgressListener networkFileMessageHandlerProgressListener) {
        mNetworkFileMessageHandlerProgressListener = networkFileMessageHandlerProgressListener;
    }

    public interface NetworkFileMessageHandlerProgressListener {
        void onNetworkFileStart(Integer fileId);
        void onNetworkFileCacheProgress(Integer fileId, String progress);
        void onNetworkFileCacheFinish(FileModel fileModel);
        void onNetworkFileDownloadStart(Integer fileId);
        void onNetworkFileDownloadProgress(Integer fileId, String progress);
        void onNetworkFileDownloadFinish(FileModel fileModel, FileModel fileModelCache);
    }
}
