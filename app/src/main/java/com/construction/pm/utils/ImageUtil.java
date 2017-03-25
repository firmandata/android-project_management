package com.construction.pm.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

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
        int height = imageView.getHeight();
        if (width == 0 || height == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            if (width == 0)
                width = point.x;
            if (height == 0)
                height = point.y;
        }

        Bitmap bitmap = getImageThumbnail(byteArray, width, height);
        if (bitmap != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void setImageView(final Context context, final ImageView imageView, final byte[] byteArray) {
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if (width == 0 || height == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            if (width == 0)
                width = point.x;
            if (height == 0)
                height = point.y;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (bitmap != null) {
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
        }
    }

    public static Bitmap getImageThumbnail(final String fileLocation, final int width, final int height) {
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

    public static Bitmap getImageThumbnail(final byte[] byteArray, final int width, final int height) {
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
}