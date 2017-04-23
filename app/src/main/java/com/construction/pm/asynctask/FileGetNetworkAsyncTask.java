package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.networks.FileNetwork;
import com.construction.pm.networks.webapi.IWebApiProgress;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.StringUtil;

import java.io.File;

public class FileGetNetworkAsyncTask extends AsyncTask<FileGetAsyncTaskParam, String, FileGetAsyncTaskResult> {

    protected FileGetAsyncTaskParam mFileGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected FileGetAsyncTaskResult doInBackground(FileGetAsyncTaskParam... fileGetAsyncTaskParams) {
        // Get FileGetAsyncTaskParam
        mFileGetAsyncTaskParam = fileGetAsyncTaskParams[0];
        mContext = mFileGetAsyncTaskParam.getContext();
        FileModel fileModelCache = mFileGetAsyncTaskParam.getFileModelCache();

        // -- Prepare FileGetAsyncTaskResult --
        FileGetAsyncTaskResult fileGetAsyncTaskResult = new FileGetAsyncTaskResult();
        fileGetAsyncTaskResult.setFileModelCache(fileModelCache);

        // -- Prepare FileCachePersistent --
        FileCachePersistent fileCachePersistent = new FileCachePersistent(mContext);

        // -- Prepare FileNetwork --
        FileNetwork fileNetwork = new FileNetwork(mContext, mFileGetAsyncTaskParam.getSettingUserModel());

        try {
            // -- Invalidate Access Token --
            // fileNetwork.invalidateAccessToken(); // Disable for fast response

            // -- Invalidate Login --
            // fileNetwork.invalidateLogin(); // Disable for fast response

            // -- Download process --
            IWebApiProgress webApiProgress = new IWebApiProgress() {
                @Override
                public void onProgress(final long bytesWritten, final long totalSize) {
                    if (totalSize > 0) {
                        publishProgress(StringUtil.numberPercentFormat((int) (((double) bytesWritten / (double) totalSize) * 100)));
                    } else {
                        publishProgress(StringUtil.numberFileSizeFormat(bytesWritten));
                    }
                }
            };

            FileModel fileModel = null;
            if (fileModelCache != null) {
                long lastUpdateCache = 0;
                File fileCache = fileModelCache.getFile(mContext);
                if (fileModelCache.getLastUpdate() != null)
                    lastUpdateCache = fileModelCache.getLastUpdate().getTimeInMillis();

                // -- Get FileModel from server --
                FileModel fileModelInfo = fileNetwork.getFile(mFileGetAsyncTaskParam.getFileId());
                if (fileModelInfo != null) {
                    long lastUpdateInfo = 0;
                    if (fileModelInfo.getLastUpdate() != null)
                        lastUpdateInfo = fileModelInfo.getLastUpdate().getTimeInMillis();

                    // -- Compare last update of cache and info --
                    if (lastUpdateCache != lastUpdateInfo || fileCache == null) {
                        // -- Download file to FileModel from server --
                        fileModel = fileNetwork.downloadFile(mFileGetAsyncTaskParam.getFileId(), webApiProgress);
                    }
                }
            } else {
                // -- Download file to FileModel from server --
                fileModel = fileNetwork.downloadFile(mFileGetAsyncTaskParam.getFileId(), webApiProgress);
            }

            fileGetAsyncTaskResult.setFileModel(fileModel);

            if (fileModel != null) {
                // -- Save to FileCachePersistent --
                try {
                    fileCachePersistent.setFileModel(fileModel);
                } catch (PersistenceError ex) {
                }
            }
        } catch (WebApiError webApiError) {
            fileGetAsyncTaskResult.setMessage(webApiError.getMessage());
        }

        return fileGetAsyncTaskResult;
    }
}
