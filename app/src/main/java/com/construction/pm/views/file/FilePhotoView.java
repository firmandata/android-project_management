package com.construction.pm.views.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.views.listeners.ImageRequestListener;

public class FilePhotoView {

    protected Context mContext;

    protected RelativeLayout mFilePhotoView;

    protected FilePhotoItemTouchView mPhoto;

    protected ImageRequestListener mImageRequestListener;

    public FilePhotoView(final Context context) {
        mContext = context;
    }

    public FilePhotoView(final Context context, final RelativeLayout filePhotoView) {
        this(context);

        initializeView(filePhotoView);
    }

    public static FilePhotoView buildFilePhotoView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new FilePhotoView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static FilePhotoView buildFilePhotoView(final Context context, final ViewGroup viewGroup) {
        return buildFilePhotoView(context, R.layout.file_photo_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout filePhotoView) {
        mFilePhotoView = filePhotoView;

        mPhoto = new FilePhotoItemTouchView(mContext, (RelativeLayout) mFilePhotoView.findViewById(R.id.file_photo_item_touch_view));
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
    }

    public RelativeLayout getView() {
        return mFilePhotoView;
    }

    public void setFileId(final Integer fileId) {
        if (mImageRequestListener != null)
            mImageRequestListener.onImageRequest(mPhoto, fileId);
    }
}
