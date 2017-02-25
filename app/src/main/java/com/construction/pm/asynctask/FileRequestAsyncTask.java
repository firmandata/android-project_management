package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.models.FileModel;
import com.construction.pm.networks.FileNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;

public class FileRequestAsyncTask extends AsyncTask<FileRequestAsyncTaskParam, FileRequestAsyncTaskResult, Boolean> {

    protected FileRequestAsyncTaskParam mFileRequestAsyncTaskParam;
    protected Context mContext;

    @Override
    protected Boolean doInBackground(FileRequestAsyncTaskParam... fileRequestAsyncTaskParams) {
        // Get FileRequestAsyncTaskParam
        mFileRequestAsyncTaskParam = fileRequestAsyncTaskParams[0];
        mContext = mFileRequestAsyncTaskParam.getContext();

        // -- Prepare FileRequestAsyncTaskResult --
        FileRequestAsyncTaskResult fileRequestAsyncTaskResult = new FileRequestAsyncTaskResult();

        // -- Prepare FileCachePersistent --
        FileCachePersistent fileCachePersistent = new FileCachePersistent(mContext);

        // -- Get FileModel from FileCachePersistent --
        try {
            fileRequestAsyncTaskResult.setFileModel(fileCachePersistent.getFileModel(mFileRequestAsyncTaskParam.getFileId()));
            publishProgress(fileRequestAsyncTaskResult);
        } catch (PersistenceError ex) {
        }

        // -- Prepare FileNetwork --
        FileNetwork fileNetwork = new FileNetwork(mContext, mFileRequestAsyncTaskParam.getSettingUserModel());

        FileModel fileModel = null;
        try {
            // -- Invalidate Access Token --
            fileNetwork.invalidateAccessToken();

            // -- Invalidate Login --
            fileNetwork.invalidateLogin();

            // -- Get FileModel from server --
            fileModel = fileNetwork.getFile(mFileRequestAsyncTaskParam.getFileId());
        } catch (WebApiError webApiError) {
            fileRequestAsyncTaskResult.setMessage(webApiError.getMessage());
        }

        if (fileModel != null) {
            // -- Set result --
            fileRequestAsyncTaskResult.setFileModel(fileModel);
            publishProgress(fileRequestAsyncTaskResult);

            // -- Save to FileCachePersistent --
            try {
                fileCachePersistent.setFileModel(fileModel);
            } catch (PersistenceError ex) {
            }

            return true;
        }

        return false;
    }
}
