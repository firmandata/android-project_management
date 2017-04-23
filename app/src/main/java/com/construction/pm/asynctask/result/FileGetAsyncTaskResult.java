package com.construction.pm.asynctask.result;

import com.construction.pm.models.FileModel;

public class FileGetAsyncTaskResult {

    protected FileModel mFileModel;
    protected FileModel mFileModelCache;
    protected String mMessage;

    public FileGetAsyncTaskResult() {

    }

    public void setFileModel(final FileModel fileModel) {
        mFileModel = fileModel;
    }

    public FileModel getFileModel() {
        return mFileModel;
    }

    public void setFileModelCache(final FileModel fileModelCache) {
        mFileModelCache = fileModelCache;
    }

    public FileModel getFileModelCache() {
        return mFileModelCache;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
