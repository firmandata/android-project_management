package com.construction.pm.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragmentdialogs.CameraAspectRatioDialogFragment;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

import java.util.Set;

public class CameraLayout extends CameraView.Callback implements CameraAspectRatioDialogFragment.CameraAspectRatioListener {

    protected Context mContext;
    protected FragmentManager mFragmentManager;

    protected static final String FRAGMENT_TAG_CAMERA_ASPECT_RATIO = "FRAGMENT_CAMERA_ASPECT_RATIO";

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

    protected RelativeLayout mCameraLayout;

    protected CameraView mCameraView;

    protected int mCameraFlashCurrent;

    protected CameraLayoutListener mCameraLayoutListener;

    protected CameraLayout(final Context context) {
        mContext = context;
    }

    public CameraLayout(final Context context, final RelativeLayout cameraLayout) {
        this(context);

        initializeView(cameraLayout);
    }

    public static CameraLayout buildCameraLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new CameraLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static CameraLayout buildCameraLayout(final Context context, final ViewGroup viewGroup) {
        return buildCameraLayout(context, R.layout.camera_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout cameraLayout) {
        mCameraLayout = cameraLayout;

        mCameraView = (CameraView) mCameraLayout.findViewById(R.id.cameraView);
        mCameraView.addCallback(this);

        FloatingActionButton takePicture = (FloatingActionButton) mCameraLayout.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
            }
        });

        FloatingActionButton cameraSwitch = (FloatingActionButton) mCameraLayout.findViewById(R.id.cameraSwitch);
        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
            }
        });

        FloatingActionButton cameraAspectRatio = (FloatingActionButton) mCameraLayout.findViewById(R.id.cameraAspectRatio);
        cameraAspectRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    CameraAspectRatioDialogFragment cameraAspectRatioDialogFragment = CameraAspectRatioDialogFragment.newInstance(ratios, currentRatio, CameraLayout.this);
                    if (mFragmentManager != null) {
                        cameraAspectRatioDialogFragment.show(mFragmentManager, FRAGMENT_TAG_CAMERA_ASPECT_RATIO);
                    }
                }
            }
        });

        final FloatingActionButton cameraFlash = (FloatingActionButton) mCameraLayout.findViewById(R.id.cameraFlash);
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
        return mCameraLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mCameraLayout);

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
        if (mCameraLayoutListener != null)
            mCameraLayoutListener.onCameraOpened();
    }

    @Override
    public void onCameraClosed(CameraView cameraView) {
        if (mCameraLayoutListener != null)
            mCameraLayoutListener.onCameraClosed();
    }

    @Override
    public void onPictureTaken(CameraView cameraView, final byte[] data) {
        if (mCameraLayoutListener != null)
            mCameraLayoutListener.onCameraTakenPicture(data);
    }

    @Override
    public void onCameraAspectRatioSelected(AspectRatio aspectRatio) {
        if (mCameraView != null) {
            if (aspectRatio != null)
                mCameraView.setAspectRatio(aspectRatio);
        }
    }

    public void setCameraListener(final CameraLayoutListener cameraListener) {
        mCameraLayoutListener = cameraListener;
    }

    public interface CameraLayoutListener {
        void onCameraOpened();
        void onCameraTakenPicture(byte[] data);
        void onCameraClosed();
    }
}
