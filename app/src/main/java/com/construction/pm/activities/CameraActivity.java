package com.construction.pm.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.construction.pm.R;
import com.construction.pm.activities.fragmentdialogs.PermissionConfirmationDialogFragment;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.CameraLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements CameraLayout.CameraLayoutListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected CameraLayout mCameraLayout;

    protected Handler mBackgroundHandler;

    public static final String FRAGMENT_TAG_PERMISSION_CONFIRMATION_DIALOG = "FRAGMENT_PERMISSION_CONFIRMATION_DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare CameraLayout --
        mCameraLayout = CameraLayout.buildCameraLayout(this, null);
        mCameraLayout.setCameraListener(this);

        // -- Load to Activity --
        mCameraLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());

        // -- Handle page request by parameters --
        requestPageHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }
    }

    @Override
    public void onCameraOpened() {

    }

    @Override
    public void onCameraTakenPicture(final byte[] data) {
        Log.d("CameraActivity", "onPictureTaken " + data.length);

        Toast.makeText(this, R.string.camera_picture_taken, Toast.LENGTH_SHORT).show();

        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picture.jpg");
                OutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(data);
                    os.close();
                } catch (IOException e) {
                    Log.w("CameraActivity", "Cannot write to " + file, e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (mCameraLayout != null)
                mCameraLayout.cameraStart();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            PermissionConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            ConstantUtil.INTENT_REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_TAG_PERMISSION_CONFIRMATION_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, ConstantUtil.INTENT_REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        if (mCameraLayout != null)
            mCameraLayout.cameraStop();

        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantUtil.INTENT_REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException(ViewUtil.getResourceString(this, R.string.camera_permission_request_error));
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    protected Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }
}