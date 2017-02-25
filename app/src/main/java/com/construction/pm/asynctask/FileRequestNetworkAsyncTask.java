package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.asynctask.filerequest.FileRequestAsyncTaskParam;
import com.construction.pm.asynctask.filerequest.FileRequestAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.networks.FileNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;

public class FileRequestNetworkAsyncTask extends AsyncTask<FileRequestAsyncTaskParam, Integer, FileRequestAsyncTaskResult> {

    protected FileRequestAsyncTaskParam mFileRequestAsyncTaskParam;
    protected Context mContext;

    @Override
    protected FileRequestAsyncTaskResult doInBackground(FileRequestAsyncTaskParam... fileRequestAsyncTaskParams) {
        // Get FileRequestAsyncTaskParam
        mFileRequestAsyncTaskParam = fileRequestAsyncTaskParams[0];
        mContext = mFileRequestAsyncTaskParam.getContext();

        // -- Prepare FileRequestAsyncTaskResult --
        FileRequestAsyncTaskResult fileRequestAsyncTaskResult = new FileRequestAsyncTaskResult();

        // -- Prepare FileCachePersistent --
        FileCachePersistent fileCachePersistent = new FileCachePersistent(mContext);

        // -- Prepare FileNetwork --
        FileNetwork fileNetwork = new FileNetwork(mContext, mFileRequestAsyncTaskParam.getSettingUserModel());

        try {
            // -- Invalidate Access Token --
            fileNetwork.invalidateAccessToken();

            // -- Invalidate Login --
            fileNetwork.invalidateLogin();

            // -- Get FileModel from server --
            FileModel fileModel = fileNetwork.getFile(mFileRequestAsyncTaskParam.getFileId());
            fileRequestAsyncTaskResult.setFileModel(fileModel);

            if (fileModel != null) {
                // -- Save to FileCachePersistent --
                try {
                    fileCachePersistent.setFileModel(fileModel);
                } catch (PersistenceError ex) {
                }
            }
        } catch (WebApiError webApiError) {
            fileRequestAsyncTaskResult.setMessage(webApiError.getMessage());
        }

        return fileRequestAsyncTaskResult;
    }
}
