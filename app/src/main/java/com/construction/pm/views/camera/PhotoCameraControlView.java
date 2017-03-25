package com.construction.pm.views.camera;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.construction.pm.R;

public class PhotoCameraControlView {

    protected Context mContext;

    protected FrameLayout mPhotoCameraControlView;

    protected PhotoCameraControlListener mPhotoCameraControlListener;

    public PhotoCameraControlView(final Context context) {
        mContext = context;
    }

    public PhotoCameraControlView(final Context context, final FrameLayout photoCameraControlView) {
        this(context);

        initializeView(photoCameraControlView);
    }

    public static PhotoCameraControlView buildPhotoCameraControlView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new PhotoCameraControlView(context, (FrameLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static PhotoCameraControlView buildPhotoCameraControlView(final Context context, final ViewGroup viewGroup) {
        return buildPhotoCameraControlView(context, R.layout.file_photo_camera_control_view, viewGroup);
    }

    protected void initializeView(final FrameLayout photoCameraControlView) {
        mPhotoCameraControlView = photoCameraControlView;

        FloatingActionButton takePicture = (FloatingActionButton) mPhotoCameraControlView.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoCameraControlListener != null)
                    mPhotoCameraControlListener.onCameraTakePictureClick();
            }
        });

        FloatingActionButton cameraSwitch = (FloatingActionButton) mPhotoCameraControlView.findViewById(R.id.cameraSwitch);
        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoCameraControlListener != null)
                    mPhotoCameraControlListener.onCameraSwitchClick();
            }
        });

        FloatingActionButton cameraAspectRatio = (FloatingActionButton) mPhotoCameraControlView.findViewById(R.id.cameraAspectRatio);
        cameraAspectRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoCameraControlListener != null)
                    mPhotoCameraControlListener.onCameraAspectRatioClick();
            }
        });

        FloatingActionButton cameraFlash = (FloatingActionButton) mPhotoCameraControlView.findViewById(R.id.cameraFlash);
        cameraFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoCameraControlListener != null)
                    mPhotoCameraControlListener.onCameraFlashClick();
            }
        });
    }

    public FrameLayout getView() {
        return mPhotoCameraControlView;
    }

    public void setPhotoCameraControlListener(final PhotoCameraControlListener photoCameraControlListener) {
        mPhotoCameraControlListener = photoCameraControlListener;
    }

    public interface PhotoCameraControlListener {
        void onCameraTakePictureClick();
        void onCameraSwitchClick();
        void onCameraAspectRatioClick();
        void onCameraFlashClick();
    }
}
