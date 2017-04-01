package com.construction.pm.views.listeners;

import android.widget.ImageView;

public interface ImageRequestDuplicateListener {
    void onImageRequestDuplicate(ImageView imageView, ImageView duplicateImageView, Integer fileId);
}
