package com.construction.pm.views.camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.construction.pm.R;
import com.google.android.cameraview.CameraView;

public class PhotoCameraView {
    protected Context mContext;

    protected FrameLayout mPhotoCameraView;

    protected CameraView mCameraView;

    protected PhotoCameraListener mPhotoCameraListener;

    public PhotoCameraView(final Context context) {
        mContext = context;
    }

    public PhotoCameraView(final Context context, final FrameLayout photoCameraView) {
        this(context);

        initializeView(photoCameraView);
    }

    public static PhotoCameraView buildPhotoCameraView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new PhotoCameraView(context, (FrameLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static PhotoCameraView buildPhotoCameraView(final Context context, final ViewGroup viewGroup) {
        return buildPhotoCameraView(context, R.layout.file_photo_camera_view, viewGroup);
    }

    protected void initializeView(final FrameLayout photoCameraView) {
        mPhotoCameraView = photoCameraView;

        mCameraView = (CameraView) mPhotoCameraView.findViewById(R.id.cameraView);
    }

    public FrameLayout getView() {
        return mPhotoCameraView;
    }

    public void setPhotoCameraListener(final PhotoCameraListener photoCameraListener) {
        mPhotoCameraListener = photoCameraListener;
    }

    public interface PhotoCameraListener {

    }
}
