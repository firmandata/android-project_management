package com.construction.pm.views.file;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.ImageUtil;

import java.io.File;

public class FilePhotoItemTouchView extends FilePhotoItemView {

    protected TouchImageView mPhoto;

    public FilePhotoItemTouchView(Context context) {
        super(context);
    }

    public FilePhotoItemTouchView(Context context, RelativeLayout filePhotoItemView) {
        super(context, filePhotoItemView);
    }

    public static FilePhotoItemView buildFilePhotoItemView(final Context context, final ViewGroup viewGroup) {
        return buildFilePhotoItemView(context, R.layout.file_photo_item_touch_view, viewGroup);
    }

    @Override
    protected void initializeView(final RelativeLayout filePhotoItemView) {
        mFilePhotoItemView = filePhotoItemView;

        mPhoto = (TouchImageView) mFilePhotoItemView.findViewById(R.id.photo);

        mDownloadProgressContainer = (RelativeLayout) mFilePhotoItemView.findViewById(R.id.downloadProgressContainer);
        mDownloadProgressBar = (ProgressBar) mDownloadProgressContainer.findViewById(R.id.downloadProgressBar);
        mDownloadProgressBarText = (AppCompatTextView) mDownloadProgressContainer.findViewById(R.id.downloadProgressBarText);
    }

    @Override
    public void setFilePhotoDefault() {
        mPhoto.setImageResource(R.drawable.ic_image_dark_24);
    }

    @Override
    public void setFilePhotoThumbnail(final File file) {
        ImageUtil.setImageThumbnailView(mContext, mPhoto, file);
    }

    @Override
    public void setFilePhoto(final File file) {
        ImageUtil.setImageView(mContext, mPhoto, file);
    }

    @Override
    public void setFilePhotoScaleType(AppCompatImageView.ScaleType scaleType) {
        mPhoto.setScaleType(scaleType);
    }
}
