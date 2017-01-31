package com.construction.pm.utils;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

public class StringUtil {
    public static String removeWrap(final String data, final int length) {
        String result = data;

        if (result.length() > length) {
            result = result.substring(0, length - 1);
        }
        return result;
    }

    public static String left(final String data, final int width) {
        String result = data;

        result = removeWrap(result, width);
        return String.format("%-" + width + "s", result);
    }

    public static String right(final String data, final int width) {
        String result = data;

        result = removeWrap(result, width);
        return String.format("%-" + width + "s", result);
    }

    public static String center(final String data, final int width) {
        String result = data;
        result = removeWrap(result, width);

        if (result.length() < width) {
            StringBuilder stringData = new StringBuilder(result);
            int left = width - result.length();
            for (int i = 0; i < (left / 2); i++) {
                stringData.insert(0, " ");
                stringData.append(" ");
            }

            if (left % 2 == 1) {
                stringData.append(" ");
            }
            result = stringData.toString();
        }

        return result;
    }

    public static String repeat(final String data, final int length) {
        return new String(new char[length]).replace("\0", data);
    }

    public static String sha1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return sha1BytesToHex(sha1hash);
    }

    private static String sha1BytesToHex(final byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private static DecimalFormat getDecimalFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.UNNECESSARY);
        numberFormat.setGroupingUsed(true);

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return decimalFormat;
    }

    public static String numberFormat(final double value) {
        return getDecimalFormat().format(value);
    }

    public static String numberFormat(final int value) {
        return getDecimalFormat().format(value);
    }

    public static double numberFormat(final String value) {
        double resultValue;

        try {
            Number number = getDecimalFormat().parse(value);
            resultValue = number.doubleValue();
        } catch (ParseException ex) {
            resultValue = 0;
        }

        return resultValue;
    }
}
