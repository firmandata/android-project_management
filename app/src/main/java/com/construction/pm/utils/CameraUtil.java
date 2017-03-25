package com.construction.pm.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

public class CameraUtil {
    public static boolean isCameraHardwareExist(final Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(); // attempt to get a Camera instance
        } catch (Exception exception) {
            // Camera is not available (in use or does not exist)
        }
        return camera; // returns null if camera is unavailable
    }
}
