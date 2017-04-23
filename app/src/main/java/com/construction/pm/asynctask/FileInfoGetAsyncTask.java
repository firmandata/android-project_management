package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.R;
import com.construction.pm.asynctask.param.FileInfoGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.networks.FileNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.utils.ViewUtil;

public class FileInfoGetAsyncTask extends AsyncTask<FileInfoGetAsyncTaskParam, String, FileGetAsyncTaskResult> {

    protected FileInfoGetAsyncTaskParam mFileInfoGetAsyncTaskParam;
    protected Context mContext;

    @Override
    protected FileGetAsyncTaskResult doInBackground(FileInfoGetAsyncTaskParam... fileGetAsyncTaskParams) {
        // Get FileInfoGetAsyncTaskParam
        mFileInfoGetAsyncTaskParam = fileGetAsyncTaskParams[0];
        mContext = mFileInfoGetAsyncTaskParam.getContext();

        // -- Prepare FileGetAsyncTaskResult --
        FileGetAsyncTaskResult fileGetAsyncTaskResult = new FileGetAsyncTaskResult();

        // -- Get FileModel progress --
        publishProgress(ViewUtil.getResourceString(mContext, R.string.file_info_handle_task_begin));

        // -- Prepare FileCachePersistent --
        FileCachePersistent fileCachePersistent = new FileCachePersistent(mContext);

        // -- Prepare FileNetwork --
        FileNetwork fileNetwork = new FileNetwork(mContext, mFileInfoGetAsyncTaskParam.getSettingUserModel());

        FileModel fileModel = null;
        try {
            try {
                // -- Invalidate Access Token --
                // fileNetwork.invalidateAccessToken(); // Disable for fast response

                // -- Invalidate Login --
                // fileNetwork.invalidateLogin(); // Disable for fast response

                // -- Get FileModel file from server --
                fileModel = fileNetwork.getFile(mFileInfoGetAsyncTaskParam.getFileId());

                if (fileModel != null) {
                    // -- Save to FileCachePersistent --
                    try {
                        fileCachePersistent.setFileModel(fileModel);
                    } catch (PersistenceError ex) {
                    }
                }
            } catch (WebApiError webApiError) {
                if (webApiError.isErrorConnection()) {
                    // -- Get File from FileCachePersistent --
                    try {
                        fileCachePersistent.getFileModel(mFileInfoGetAsyncTaskParam.getFileId());
                    } catch (PersistenceError ex) {
                    }
                } else
                    throw webApiError;
            }
        } catch (WebApiError webApiError) {
            fileGetAsyncTaskResult.setMessage(webApiError.getMessage());
            publishProgress(webApiError.getMessage());
        }

        if (fileModel != null) {
            // -- Set result --
            fileGetAsyncTaskResult.setFileModel(fileModel);

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.file_info_handle_task_success));
        }

        return fileGetAsyncTaskResult;
    }
}
