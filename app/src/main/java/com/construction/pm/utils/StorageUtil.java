package com.construction.pm.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class StorageUtil {

    public static File getSDCardDirectory(final Context context, final String type) {
        File directory = context.getExternalFilesDir(type);
        if (Build.VERSION.SDK_INT >= 19) {
            File[] directories = context.getExternalFilesDirs(type);
            if (directories.length > 1) {
                directory = directories[1];
            }
        } else {
            String[] directories = getStorageDirectories();
            if (directories.length > 1) {
                if (type != null)
                    directory = new File(directories[1], type);
                else
                    directory = new File(directories[1]);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
        }
        return directory;
    }

    protected static String[] getStorageDirectories() {
        final Pattern DIR_SEPARATOR = Pattern.compile("/");

        // Final set of paths
        final Set<String> rv = new HashSet<String>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStorageStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");

        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            } else {
                rv.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch(NumberFormatException ignored) {

                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if(TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }

        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStorageStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStorageStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }

        return rv.toArray(new String[rv.size()]);
    }
}
