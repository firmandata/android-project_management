package com.construction.pm.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtil {
    public static boolean openFile(final Context context, final File file) {
        boolean isOpened = false;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            isOpened = true;
        } catch (ActivityNotFoundException ex) {
        }

        return isOpened;
    }

    protected static File getCacheDir(final Context context) {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWritable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWritable = false;
        }

        if (mExternalStorageAvailable && mExternalStorageWritable)
            return context.getExternalCacheDir();
        else
            return context.getCacheDir();
    }

    @Nullable
    public static File saveToFileCache(final Context context, final String fileName, final byte[] fileData) {
        // -- Save to cache directory --
        File file = new File(getCacheDir(context), fileName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
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

        if (file.exists())
            return file;

        return null;
    }

    public static File copyToFileCache(final Context context, final File fileSource, final String newFileName) {
        File fileDestination = new File(getCacheDir(context), newFileName);

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(fileSource).getChannel();
            destination = new FileOutputStream(fileDestination).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (FileNotFoundException ex) {
            // Ignore
        } catch (IOException ex) {
            // Ignore
        } finally {
            try {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            } catch (FileNotFoundException ex) {
                // Ignore
            } catch (IOException ex) {
                // Ignore
            }
        }

        if (fileDestination.exists())
            return fileDestination;

        return null;
    }

    @Nullable
    public static File getFileCache(final Context context, final String fileName) {
        File file = new File(getCacheDir(context), fileName);

        if (file.exists())
            return file;

        return null;
    }

    @Nullable
    public static byte[] toByteArray(final String base64Encode) {
        if (base64Encode == null)
            return null;

        byte[] fileData = null;
        try {
            String base64EncodeBytes = base64Encode.substring(base64Encode.indexOf(",") + 1);
            fileData = Base64.decode(base64EncodeBytes, Base64.DEFAULT);
        } catch (Exception ex) {
        }
        return fileData;
    }

    @Nullable
    public static byte[] toByteArray(final File file) {
        if (file == null)
            return null;

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Nullable
    public static String toBase64Encode(final File file) {
        return toBase64Encode(file, null);
    }

    @Nullable
    public static String toBase64Encode(final File file, final String fileMimeType) {
        if (file == null)
            return null;

        if (!file.exists())
            return null;

        String fileData = null;
        try {
            byte[] fileBytes = toByteArray(file);
            if (fileBytes != null)
                fileData = Base64.encodeToString(fileBytes, Base64.DEFAULT);
        } catch (Exception ex) {
        }

        String mimeType = getMimeType(file);
        if (fileMimeType != null)
            mimeType = fileMimeType;

        if (fileData != null)
            return "data:" + (mimeType != null ? mimeType : "") + ";base64," + fileData;

        return null;
    }

    @Nullable
    public static String getMimeType(final File file) {
        if (file == null)
            return null;

        String fileName = file.getName();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase());
    }
}
