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
import android.media.ExifInterface;
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
import android.util.Log;
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

public class CameraActivity extends AppCompatActivity implements CameraLayout.CameraLayoutListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected CameraLayout mCameraLayout;

    protected LocationManager mLocationManager;
    protected LocationUpdateListener mLocationUpdateListener;
    protected Handler mBackgroundHandler;

    public static final String FRAGMENT_TAG_PERMISSION_CONFIRMATION_DIALOG = "FRAGMENT_PERMISSION_CONFIRMATION_DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Get the location manager --
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationUpdateListener = new LocationUpdateListener(this);

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
                String imageFileName = "PICTURE_" + DateTimeUtil.ToStringFormat(Calendar.getInstance(), "yyyyMMdd_HHmmss") + ".jpg";

//                File file = null;
//                File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                try {
//                    file = File.createTempFile(imageFileName, ".jpg", storageDirectory);
//                } catch (IOException ioException) {
//                }

//                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
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

                // -- Set image location --
                if (mLocationUpdateListener != null) {
                    Double latitude = mLocationUpdateListener.getLatitude();
                    Double longitude = mLocationUpdateListener.getLongitude();
                    if (latitude != null && longitude != null)
                        GPSUtil.setGeoLocationToExif(file.getAbsolutePath(), latitude, longitude);
                }

                // -- Share image to gallery --
                ImageUtil.addImageToGallery(CameraActivity.this, file.getAbsolutePath());
            }
        });
    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> showRequestPermissionList = new ArrayList<String>();
        List<String> showRequestPermissionRationaleList = new ArrayList<String>();

        // -- Camera handler --
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (mCameraLayout != null)
                mCameraLayout.cameraStart();
        } else {
            // -- Register camera permission --
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                showRequestPermissionRationaleList.add(Manifest.permission.CAMERA);
            else
                showRequestPermissionList.add(Manifest.permission.CAMERA);
        }

        // -- GPS handler --
        if (isLocationEnabled()) {
            if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = mLocationManager.getBestProvider(criteria, true);
                if (provider != null) {
                    int minTime = 1 * 60 * 1000; // 1 minute
                    int minDistance = 10; // 10 meters
                    mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mLocationUpdateListener);
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
            showGPSDeviceRequestDialog();
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
                mLocationManager.removeUpdates(mLocationUpdateListener);
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

    // -----------------
    // -- GPS Handler --
    // -----------------

    protected boolean isLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected void showGPSDeviceRequestDialog() {
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

    protected class LocationUpdateListener implements LocationListener {

        protected Context mContext;
        protected Location mLocation;

        public LocationUpdateListener(final Context context) {
            mContext = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        public Double getLatitude() {
            if (mLocation == null)
                return null;

            return mLocation.getLatitude();
        }

        public Double getLongitude() {
            if (mLocation == null)
                return null;

            return mLocation.getLongitude();
        }
    }
}