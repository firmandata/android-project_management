package com.construction.pm.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.construction.pm.R;
import com.construction.pm.activities.fragmentdialogs.PermissionConfirmationDialogFragment;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.GPSUtil;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.CameraLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements
        CameraLayout.CameraLayoutListener,
        LocationListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected CameraLayout mCameraLayout;
    protected boolean isCameraTaken;

    protected LocationManager mLocationManager;
    protected Location mLocation;

    protected Handler mBackgroundHandler;

    public static final String FRAGMENT_TAG_PERMISSION_CONFIRMATION_DIALOG = "FRAGMENT_PERMISSION_CONFIRMATION_DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Get the location manager --
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
    protected void onResume() {
        super.onResume();

        List<String> showRequestPermissionList = new ArrayList<String>();
        List<String> showRequestPermissionRationaleList = new ArrayList<String>();

        // -- Camera handler --
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (!isCameraTaken) {
                if (mCameraLayout != null)
                    mCameraLayout.cameraStart();
            }
        } else {
            // -- Register camera permission --
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                showRequestPermissionRationaleList.add(Manifest.permission.CAMERA);
            else
                showRequestPermissionList.add(Manifest.permission.CAMERA);
        }

        // -- GPS handler --
        if (isGPSEnabled()) {
            if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // -- Get best GPS provider --
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = mLocationManager.getBestProvider(criteria, true);
                if (provider != null) {
                    // -- Request update location --
                    int minTime = 1 * 60 * 1000; // 1 minute
                    int minDistance = 10; // 10 meters
                    mLocationManager.requestLocationUpdates(provider, minTime, minDistance, this);

                    // -- Get last know location --
                    mLocation = mLocationManager.getLastKnownLocation(provider);
                }
            } else {
                // -- Register GPS Access Fine permission --
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                    showRequestPermissionRationaleList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                else
                    showRequestPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

                // -- Register GPS Access Coarse permission --
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    showRequestPermissionRationaleList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                else
                    showRequestPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        } else {
            showGPSEnableRequestDialog();
        }

        // -- Request permission handler --
        if (showRequestPermissionRationaleList.size() > 0) {
            String[] showRequestPermissionRationales = new String[showRequestPermissionRationaleList.size()];
            showRequestPermissionRationaleList.toArray(showRequestPermissionRationales);

            PermissionConfirmationDialogFragment permissionConfirmationDialogFragment = PermissionConfirmationDialogFragment.newInstance(
                R.string.request_permission_confirmation, showRequestPermissionRationales,
                ConstantUtil.INTENT_REQUEST_PERMISSION,
                R.string.request_permission_not_granted);
            permissionConfirmationDialogFragment.show(getSupportFragmentManager(), FRAGMENT_TAG_PERMISSION_CONFIRMATION_DIALOG);
        } else if (showRequestPermissionList.size() > 0) {
            String[] showRequestPermissions = new String[showRequestPermissionList.size()];
            showRequestPermissionList.toArray(showRequestPermissions);
            ActivityCompat.requestPermissions(this, showRequestPermissions, ConstantUtil.INTENT_REQUEST_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        if (mCameraLayout != null)
            mCameraLayout.cameraStop();

        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(this);
                mLocation = null;
            } catch (SecurityException securityException) {
            }
        }

        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantUtil.INTENT_REQUEST_PERMISSION:
                if (permissions.length != grantResults.length) {
                    Toast.makeText(this, R.string.request_permission_request_error, Toast.LENGTH_SHORT).show();
                } else {
                    boolean isFoundPermissionNotGranted = false;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            isFoundPermissionNotGranted = true;
                            break;
                        }
                    }
                    if (isFoundPermissionNotGranted)
                        Toast.makeText(this, R.string.request_permission_not_granted, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

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


    // --------------------
    // -- Camera Handler --
    // --------------------

    @Override
    public void onCameraOpened() {

    }

    @Override
    public void onCameraTakenPicture(final byte[] data) {
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                String imageFileName = "PICTURE_" + DateTimeUtil.ToStringFormat(Calendar.getInstance(), "yyyyMMdd_HHmmss") + ".jpg";

//                File filePath = StorageUtil.getSDCardDirectory(CameraActivity.this, Environment.DIRECTORY_PICTURES); // Save to Physical SDCard, can't access to gallery
//                File filePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName); // Save to application directory, can't access to gallery
//                File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); // Save to public directory, can access to gallery
                File filePath = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES); // Save to internal storage, can access to gallery
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                File file = new File(filePath, imageFileName);
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(data);
                    outputStream.close();
                } catch (IOException e) {

                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }

                onCameraTakenFile(file);
            }
        });
    }

    @Override
    public void onCameraClosed() {

    }

    public void onCameraTakenFile(final File file) {
        // -- Set image location --
        if (isGPSEnabled() && mLocation != null) {
            GPSUtil.setGeoLocationToExif(file.getAbsolutePath(), mLocation.getLatitude(), mLocation.getLongitude());
        }

        // -- Share image to gallery --
        ImageUtil.addImageToGallery(CameraActivity.this, file.getAbsolutePath());

        isCameraTaken = true;
        mCameraLayout.cameraStop();

        Intent intent = new Intent();
        intent.putExtra(ConstantUtil.INTENT_RESULT_FILE_PATH, file.getAbsolutePath());
        setResult(ConstantUtil.INTENT_REQUEST_CAMERA_ACTIVITY_RESULT_FILE, intent);

        finish();
    }

    protected Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("cameraTakenAsFile");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }


    // -----------------
    // -- GPS Handler --
    // -----------------

    protected boolean isGPSEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected void showGPSEnableRequestDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.gps_request_enable_title)
            .setMessage(R.string.gps_request_enable_message)
            .setPositiveButton(R.string.gps_request_enable_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            })
            .setNegativeButton(R.string.gps_request_enable_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
        dialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}