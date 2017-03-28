package com.construction.pm.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ImageUtil {
    public static File createImageFile(final Context context) throws IOException {
        // Create an image file name
        String timeStamp = DateTimeUtil.ToStringFormat(Calendar.getInstance(), "yyyyMMdd_HHmmss");
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public static void setImageThumbnailView(final Context context, final ImageView imageView, final byte[] byteArray) {
        int width = imageView.getWidth();
        if (width == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            width = point.x;
        }

        Glide
            .with(context)
            .load(byteArray)
            .centerCrop()
            .into(imageView);
//        Bitmap bitmap = getImageThumbnail(byteArray, width);
//        if (bitmap != null) {
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageBitmap(bitmap);
//        }
    }

    public static void setImageThumbnailView(final Context context, final ImageView imageView, final String fileLocation) {
        int width = imageView.getWidth();
        if (width == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            width = point.x;
        }

        Glide
            .with(context)
            .load(fileLocation)
            .centerCrop()
            .into(imageView);
//        Bitmap bitmap = getImageThumbnail(fileLocation, width);
//        if (bitmap != null) {
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageBitmap(bitmap);
//        }
    }

    public static void setImageView(final Context context, final ImageView imageView, final byte[] byteArray) {
        int width = imageView.getWidth();
        if (width == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            width = point.x;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (bitmap != null) {
            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
            // int width = Math.round(height * aspectRatio); // based on height
            int height = Math.round(width / aspectRatio); // based on width
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
        }
    }

    protected static Bitmap getImageThumbnail(final String fileLocation, final int width) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileLocation);
        if (bitmap == null)
            return null;

        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        // int width = Math.round(height * aspectRatio); // based on height
        int height = Math.round(width / aspectRatio); // based on width

        BitmapFactory.Options bitmapFactoryOption = new BitmapFactory.Options();
        bitmapFactoryOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileLocation, bitmapFactoryOption);

        int scale = 1;
        while (bitmapFactoryOption.outWidth / scale / 2 >= width && bitmapFactoryOption.outHeight / scale / 2 >= height) {
            scale *= 2;
        }

        BitmapFactory.Options bitmapFactoryOptionScaled = new BitmapFactory.Options();
        bitmapFactoryOptionScaled.inJustDecodeBounds = false;
        bitmapFactoryOptionScaled.inSampleSize = scale;
        bitmapFactoryOptionScaled.inPurgeable = true;
        return BitmapFactory.decodeFile(fileLocation, bitmapFactoryOptionScaled);
    }

    protected static Bitmap getImageThumbnail(final byte[] byteArray, final int width) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (bitmap == null)
            return null;

        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        // int width = Math.round(height * aspectRatio); // based on height
        int height = Math.round(width / aspectRatio); // based on width

        BitmapFactory.Options bitmapFactoryOption = new BitmapFactory.Options();
        bitmapFactoryOption.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapFactoryOption);

        int scale = 1;
        while (bitmapFactoryOption.outWidth / scale / 2 >= width && bitmapFactoryOption.outHeight / scale / 2 >= height) {
            scale *= 2;
        }

        BitmapFactory.Options bitmapFactoryOptionScaled = new BitmapFactory.Options();
        bitmapFactoryOptionScaled.inJustDecodeBounds = false;
        bitmapFactoryOptionScaled.inSampleSize = scale;
        bitmapFactoryOptionScaled.inPurgeable = true;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapFactoryOptionScaled);
    }

    public static boolean addImageToGallery(final Context context, final String fileLocation) {
        File file = new File(fileLocation);
        if (!file.isFile())
            return false;
        if (!file.exists())
            return false;

        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return true;
    }

    public static Bitmap compressImage(final Bitmap bitmap, final Bitmap.CompressFormat compressFormat, final int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(compressFormat, quality, byteArrayOutputStream)) {
            return BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        }
        return null;
    }

    public static byte[] getImageData(final File file) {
        return getImageData(file.getAbsolutePath());
    }

    public static byte[] getImageData(final File file, final Bitmap.CompressFormat compressFormat, final int quality) {
        return getImageData(file.getAbsolutePath(), compressFormat, quality);
    }

    public static byte[] getImageData(final String fileLocation) {
        return getImageData(fileLocation, Bitmap.CompressFormat.PNG, 100);
    }

    public static byte[] getImageData(final String fileLocation, final Bitmap.CompressFormat compressFormat, final int quality) {
        return getImageData(BitmapFactory.decodeFile(fileLocation), compressFormat, quality);
    }

    public static byte[] getImageData(final Bitmap bitmap) {
        return getImageData(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static byte[] getImageData(final Bitmap bitmap, final Bitmap.CompressFormat compressFormat, final int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(compressFormat, quality, byteArrayOutputStream))
            return byteArrayOutputStream.toByteArray();
        return null;
    }
}
