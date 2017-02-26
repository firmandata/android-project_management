package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;

public class FileGetCacheAsyncTask extends AsyncTask<FileGetAsyncTaskParam, Integer, FileGetAsyncTaskResult> {

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

        // -- Get FileModel from FileCachePersistent --
        try {
            fileGetAsyncTaskResult.setFileModel(fileCachePersistent.getFileModel(mFileGetAsyncTaskParam.getFileId()));
        } catch (PersistenceError ex) {
        }

        return fileGetAsyncTaskResult;
    }
}