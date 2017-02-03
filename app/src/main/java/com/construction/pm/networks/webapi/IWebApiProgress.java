package com.construction.pm.networks.webapi;

public interface IWebApiProgress {
    void onProgress(final long bytesWritten, final long totalSize);
}
