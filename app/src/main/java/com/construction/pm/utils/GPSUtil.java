package com.construction.pm.utils;

import android.media.ExifInterface;

import java.io.IOException;

public class GPSUtil {

    public static final int PARAM_LATITUDE = 0;
    public static final int PARAM_LONGITUDE = 1;

    public static Float convertToDegree(final String dms) {
        Float result = null;
        String[] DMS = dms.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
    }

    public static int getLatitudeE6(final Float latitude) {
        return (int) (latitude * 1000000);
    }

    public static int getLongitudeE6(final Float longitude) {
        return (int) (longitude * 1000000);
    }

    public static Float getGeoDegreeFromExif(final ExifInterface exif, final int param) {
        if (param == PARAM_LATITUDE) {
            String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            if (latitude != null && latitudeRef != null) {
                if (latitudeRef.equals("N")) {
                    return convertToDegree(latitude);
                } else {
                    return  0 - convertToDegree(latitude);
                }
            }
        }

        if (param == PARAM_LONGITUDE) {
            String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String longitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (longitude != null && longitudeRef !=null) {
                if (longitudeRef.equals("E")) {
                    return convertToDegree(longitude);
                } else {
                    return 0 - convertToDegree(longitude);
                }
            }
        }

        return null;
    }

    public static boolean setGeoLocationToExif(final String fileLocation, final double latitude, final double longitude) {
        try {
            ExifInterface exif = new ExifInterface(fileLocation);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convertLatLong(latitude));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitudeRef(latitude));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convertLatLong(longitude));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitudeRef(longitude));
            exif.saveAttributes();

            return true;
        } catch (IOException ioException) {
            return false;
        }
    }

    protected static String latitudeRef(final double latitude) {
        return latitude < 0.0d ? "S" : "N";
    }

    protected static String longitudeRef(final double longitude) {
        return longitude < 0.0d ? "W" : "E";
    }

    protected static String convertLatLong(double latLong) {
        latLong = Math.abs(latLong);
        final int degree = (int)latLong;
        latLong *= 60;
        latLong -= degree * 60.0d;
        final int minute = (int)latLong;
        latLong *= 60;
        latLong -= minute * 60.0d;
        final int second = (int)(latLong * 1000.0d);

        StringBuilder sb = new StringBuilder(20);
        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }
}
