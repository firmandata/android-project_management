package com.construction.pm.views.camera;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragmentdialogs.PhotoCameraAspectRatioFragment;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

import java.util.Set;

public class PhotoCameraLayout extends CameraView.Callback implements PhotoCameraAspectRatioFragment.PhotoCameraAspectRatioListener {

    protected Context mContext;
    protected FragmentManager mFragmentManager;

    protected static final String FRAGMENT_TAG_PHOTO_CAMERA_ASPECT_RATIO = "FRAGMENT_PHOTO_CAMERA_ASPECT_RATIO";

    protected static final int[] FLASH_OPTIONS = {
        CameraView.FLASH_AUTO,
        CameraView.FLASH_OFF,
        CameraView.FLASH_ON,
    };

    protected static final int[] FLASH_ICONS = {
        R.drawable.ic_flash_auto,
        R.drawable.ic_flash_off,
        R.drawable.ic_flash_on,
    };

    protected RelativeLayout mPhotoCameraLayout;

    protected CameraView mCameraView;

    protected int mCameraFlashCurrent;

    protected PhotoCameraListener mPhotoCameraListener;

    protected PhotoCameraLayout(final Context context) {
        mContext = context;
    }

    public PhotoCameraLayout(final Context context, final RelativeLayout photoCameraLayout) {
        this(context);

        initializeView(photoCameraLayout);
    }

    public static PhotoCameraLayout buildPhotoCameraLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new PhotoCameraLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static PhotoCameraLayout buildPhotoCameraLayout(final Context context, final ViewGroup viewGroup) {
        return buildPhotoCameraLayout(context, R.layout.file_photo_camera_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout photoCameraLayout) {
        mPhotoCameraLayout = photoCameraLayout;

        mCameraView = (CameraView) mPhotoCameraLayout.findViewById(R.id.cameraView);
        mCameraView.addCallback(this);

        FloatingActionButton takePicture = (FloatingActionButton) mPhotoCameraLayout.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
            }
        });

        FloatingActionButton cameraSwitch = (FloatingActionButton) mPhotoCameraLayout.findViewById(R.id.cameraSwitch);
        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
            }
        });

        FloatingActionButton cameraAspectRatio = (FloatingActionButton) mPhotoCameraLayout.findViewById(R.id.cameraAspectRatio);
        cameraAspectRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    PhotoCameraAspectRatioFragment photoCameraAspectRatioFragment = PhotoCameraAspectRatioFragment.newInstance(ratios, currentRatio, PhotoCameraLayout.this);
                    if (mFragmentManager != null) {
                        photoCameraAspectRatioFragment.show(mFragmentManager, FRAGMENT_TAG_PHOTO_CAMERA_ASPECT_RATIO);
                    }
                }
            }
        });

        final FloatingActionButton cameraFlash = (FloatingActionButton) mPhotoCameraLayout.findViewById(R.id.cameraFlash);
        cameraFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    mCameraFlashCurrent = (mCameraFlashCurrent + 1) % FLASH_OPTIONS.length;
                    cameraFlash.setImageResource(FLASH_ICONS[mCameraFlashCurrent]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCameraFlashCurrent]);
                }
            }
        });
    }

    public RelativeLayout getLayout() {
        return mPhotoCameraLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mPhotoCameraLayout);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void cameraStart() {
        if (mCameraView != null)
            mCameraView.start();
    }

    public void cameraStop() {
        if (mCameraView != null)
            mCameraView.stop();
    }

    @Override
    public void onCameraOpened(CameraView cameraView) {
        if (mPhotoCameraListener != null)
            mPhotoCameraListener.onCameraOpened();
    }

    @Override
    public void onCameraClosed(CameraView cameraView) {
        if (mPhotoCameraListener != null)
            mPhotoCameraListener.onCameraClosed();
    }

    @Override
    public void onPictureTaken(CameraView cameraView, final byte[] data) {
        if (mPhotoCameraListener != null)
            mPhotoCameraListener.onCameraTakenPicture(data);
    }

    @Override
    public void onCameraAspectRatioSelected(AspectRatio aspectRatio) {
        if (mCameraView != null) {
            if (aspectRatio != null)
                mCameraView.setAspectRatio(aspectRatio);
        }
    }

    public void setPhotoCameraListener(final PhotoCameraListener photoCameraListener) {
        mPhotoCameraListener = photoCameraListener;
    }

    public interface PhotoCameraListener {
        void onCameraOpened();
        void onCameraTakenPicture(byte[] data);
        void onCameraClosed();
    }
}
