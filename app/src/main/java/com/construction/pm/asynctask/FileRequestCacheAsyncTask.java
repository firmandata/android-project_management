package com.construction.pm.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.construction.pm.asynctask.filerequest.FileRequestAsyncTaskParam;
import com.construction.pm.asynctask.filerequest.FileRequestAsyncTaskResult;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;

public class FileRequestCacheAsyncTask extends AsyncTask<FileRequestAsyncTaskParam, Integer, FileRequestAsyncTaskResult> {

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

        // -- Get FileModel from FileCachePersistent --
        try {
            fileRequestAsyncTaskResult.setFileModel(fileCachePersistent.getFileModel(mFileRequestAsyncTaskParam.getFileId()));
        } catch (PersistenceError ex) {
        }

        return fileRequestAsyncTaskResult;
    }
}