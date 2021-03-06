package com.construction.pm.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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

    public static void setImageThumbnailView(final Context context, final ImageView imageView, final File file) {
        setImageThumbnailView(context, imageView, file.getAbsolutePath());
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
        final int widthFixed = width;

        Glide
            .with(context)
            .load(byteArray)
            .asBitmap()
            .transform(new BitmapTransformation(context) {
                @Override
                protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                    float aspectRatio = toTransform.getWidth() / (float) toTransform.getHeight();
                    int height = Math.round(widthFixed / aspectRatio); // based on width
                    return Bitmap.createScaledBitmap(toTransform, widthFixed, height, true);
                }

                @Override
                public String getId() {
                    return "com.construction.pm";
                }
            })
            .into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imageView.setImageBitmap(resource);
                }
            });

//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        if (bitmap != null) {
//            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
//            // int width = Math.round(height * aspectRatio); // based on height
//            int height = Math.round(width / aspectRatio); // based on width
//            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
//        }
    }

    public static void setImageView(final Context context, final ImageView imageView, final File file) {
        setImageView(context, imageView, file.getAbsolutePath());
    }

    public static void setImageView(final Context context, final ImageView imageView, final String fileLocation) {
        int width = imageView.getWidth();
        if (width == 0) {
            Point point = ViewUtil.getScreenPoint(context);
            width = point.x;
        }
        final int widthFixed = width;

        Glide
                .with(context)
                .load(fileLocation)
                .asBitmap()
                .transform(new BitmapTransformation(context) {
                    @Override
                    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                        float aspectRatio = toTransform.getWidth() / (float) toTransform.getHeight();
                        int height = Math.round(widthFixed / aspectRatio); // based on width
                        return Bitmap.createScaledBitmap(toTransform, widthFixed, height, true);
                    }

                    @Override
                    public String getId() {
                        return "com.construction.pm";
                    }
                })
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });

//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        if (bitmap != null) {
//            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
//            // int width = Math.round(height * aspectRatio); // based on height
//            int height = Math.round(width / aspectRatio); // based on width
//            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
//        }
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

    public static Bitmap scaleImage(final String fileLocation, int maxWidth, int maxHeight) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileLocation);
        return scaleImage(bitmap, maxWidth, maxHeight);
    }

    public static Bitmap scaleImage(final Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null)
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static File copyImageFileToCache(final Context context, final File fileSource, final String fileCacheName, final int maxWidth, final int maxHeight) {
        File fileNew = null;

        // -- Get scaled image --
        Bitmap scaledImage = ImageUtil.scaleImage(fileSource.getAbsolutePath(), maxWidth, maxHeight);
        if (scaledImage != null) {
            // -- Save to file cache --
            fileNew = FileUtil.saveToFileCache(context, fileCacheName, getImageData(scaledImage));

            if (fileNew != null) {
                if (fileNew.exists()) {
                    // -- Get exif --
                    ExifInterface exifInterface = null;
                    try {
                        exifInterface = new ExifInterface(fileSource.getAbsolutePath());
                    } catch (IOException ioException) {
                    }

                    if (exifInterface != null) {
                        // -- Set exif --
                        try {
                            ExifInterface exifInterfaceCache = new ExifInterface(fileNew.getAbsolutePath());

                            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                            String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                            String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                            if (latitude != null)
                                exifInterfaceCache.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
                            if (latitudeRef != null)
                                exifInterfaceCache.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitudeRef);
                            if (longitude != null)
                                exifInterfaceCache.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
                            if (longitudeRef != null)
                                exifInterfaceCache.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitudeRef);

                            exifInterfaceCache.saveAttributes();
                        } catch (IOException ioException) {
                        }
                    }
                }
            }
        }

        return fileNew;
    }

    public static byte[] getImageData(final File file) {
        return getImageData(file.getAbsolutePath());
    }

    public static byte[] getImageData(final File file, final Bitmap.CompressFormat compressFormat, final int quality) {
        return getImageData(file.getAbsolutePath(), compressFormat, quality);
    }

    public static byte[] getImageData(final String fileLocation) {
        return getImageData(fileLocation, Bitmap.CompressFormat.JPEG, 100);
    }

    public static byte[] getImageData(final String fileLocation, final Bitmap.CompressFormat compressFormat, final int quality) {
        return getImageData(BitmapFactory.decodeFile(fileLocation), compressFormat, quality);
    }

    public static byte[] getImageData(final Bitmap bitmap) {
        return getImageData(bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    public static byte[] getImageData(final Bitmap bitmap, final Bitmap.CompressFormat compressFormat, final int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(compressFormat, quality, byteArrayOutputStream))
            return byteArrayOutputStream.toByteArray();
        return null;
    }
}
