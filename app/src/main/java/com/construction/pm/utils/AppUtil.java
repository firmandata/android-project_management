package com.construction.pm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class AppUtil {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidString = String.valueOf(uuid);

        return uuidString.replaceAll("-", "");
    }

    public static String getDeviceId(final Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getImei(final Context context) {
        String imei = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception ex) {
        }
        return imei;
    }

    public static String getVersionName(final Context context) {
        String versionName = null;

        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo info = info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }
}
