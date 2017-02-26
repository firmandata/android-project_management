package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.networks.FileNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;

public class FileGetNetworkAsyncTask extends AsyncTask<FileGetAsyncTaskParam, Integer, FileGetAsyncTaskResult> {

    protected FileGetAsyncTaskParam mFileGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected FileGetAsyncTaskResult doInBackground(FileGetAsyncTaskParam... fileGetAsyncTaskParams) {
        // Get FileGetAsyncTaskParam
        mFileGetAsyncTaskParam = fileGetAsyncTaskParams[0];
        mContext = mFileGetAsyncTaskParam.getContext();

        // -- Prepare FileGetAsyncTaskResult --
        FileGetAsyncTaskResult fileGetAsyncTaskResult = new FileGetAsyncTaskResult();

        // -- Prepare FileCachePersistent --
        FileCachePersistent fileCachePersistent = new FileCachePersistent(mContext);

        // -- Prepare FileNetwork --
        FileNetwork fileNetwork = new FileNetwork(mContext, mFileGetAsyncTaskParam.getSettingUserModel());

        try {
            // -- Invalidate Access Token --
            fileNetwork.invalidateAccessToken();

            // -- Invalidate Login --
            fileNetwork.invalidateLogin();

            // -- Get FileModel from server --
            FileModel fileModel = fileNetwork.getFile(mFileGetAsyncTaskParam.getFileId());
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
