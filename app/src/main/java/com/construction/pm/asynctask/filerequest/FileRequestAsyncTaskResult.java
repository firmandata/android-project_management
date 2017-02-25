package com.construction.pm.asynctask.filerequest;

import com.construction.pm.models.FileModel;

public class FileRequestAsyncTaskResult {

    protected FileModel mFileModel;
    protected String mMessage;

    public FileRequestAsyncTaskResult() {

    }

    public void setFileModel(final FileModel fileModel) {
        mFileModel = fileModel;
    }

    public FileModel getFileModel() {
        return mFileModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
