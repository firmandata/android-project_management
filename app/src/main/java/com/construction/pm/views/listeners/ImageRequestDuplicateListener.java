package com.construction.pm.views.listeners;

import com.construction.pm.views.file.FilePhotoItemView;

public interface ImageRequestDuplicateListener {
    void onImageRequestDuplicate(FilePhotoItemView filePhotoItemView, FilePhotoItemView duplicateFilePhotoItemView, Integer fileId);
}
