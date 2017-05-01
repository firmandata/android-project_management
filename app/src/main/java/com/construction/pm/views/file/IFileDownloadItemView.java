package com.construction.pm.views.file;

public interface IFileDownloadItemView {
    void setFileId(Integer fileId);
    String getFileType();
    void startProgress();
    void setProgress(int progress, int max);
    void setProgressText(String progressText);
    void stopProgress();
}
