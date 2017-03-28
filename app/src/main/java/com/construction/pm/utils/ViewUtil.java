package com.construction.pm.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtil {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    public static boolean isScreenLarge(final Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return true;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return false;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return false;
            default:
                return false;
        }
    }

    public static int getPxFromDp(final Context context, final int dps) {
        Resources resources = context.getResources();

        int pixel = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics()
        );

        return pixel;
    }

    public static void setBackground(final View view, final Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setBackgroundSelectableItem(final Context context, final View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            view.setBackgroundResource(outValue.resourceId);
        }
    }

    public static void setTextViewCenterAlignment(final TextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setGravity(Gravity.CENTER);
    }

    public static void setTextViewHyperlink(AppCompatTextView appCompatTextView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(appCompatTextView.getText());
        spannableStringBuilder.setSpan(new URLSpan("#"), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        appCompatTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
    }

    public static String getBytesToHexLittleEndian(final byte[] bytes) {
        char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[v >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] getHexToBytes(final String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static long getBytesToDec(final byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public static long getBytesToReversed(final byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public static String getResourceString(final Context context, final int id) {
        return context.getResources().getString(id);
    }

    public static String getResourceString(final Context context, final int id, final String... args) {
        String string = context.getResources().getString(id);
        for (int arg = 1; arg <= args.length; arg++) {
            String argValue = args[arg - 1];
            if (argValue == null)
                argValue = "";
            string = string.replace("$s" + String.valueOf(arg), argValue);
        }
        return string;
    }

    public static Point getScreenPoint(final Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 13)
            display.getSize(point);
        else {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }

        return point;
    }

    public static int getScreenWidth(final Context context) {
        Point point = getScreenPoint(context);
        return point.x;
    }

    public static int getScreenHeight(final Context context) {
        Point point = getScreenPoint(context);
        return point.y;
    }
}
