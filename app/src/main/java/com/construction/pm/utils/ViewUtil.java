package com.construction.pm.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtil {

    public static final String INTENT_EXTRA_SESSION_LOGIN_MODEL = "SESSION_LOGIN_MODEL";
    public static final String INTENT_EXTRA_BLUETOOTH_MODEL = "BLUETOOTH_MODEL";

    public static final int REQUEST_PERMISSION = 10;

    public static final int REQUEST_CONTROLLER_BASE_DRAWER = 100;
    public static final int REQUEST_CONTROLLER_AUTHENTICATION = 200;
    public static final int REQUEST_CONTROLLER_SETTING = 300;
    public static final int REQUEST_CONTROLLER_BLUETOOTH_PRINTER = 400;
    public static final int REQUEST_CONTROLLER_BLUETOOTH_CARD_READER = 401;
    public static final int REQUEST_CONTROLLER_BLUETOOTH_ENABLE = 450;

    public static final int RESULT_CONTROLLER_BASE_DRAWER_CLOSE_ALL = 101;
    public static final int RESULT_CONTROLLER_SETTING_CHANGED = 301;

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
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pixel = Math.round(dps * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        final float scale = context.getResources().getDisplayMetrics().density;
        int pixel_2 = (int) (dps * scale + 0.5f);

        return pixel_2;

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        display.getMetrics(displaymetrics);
//        return (int) (dps * displaymetrics.density + 0.5f);
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

    public static String getResourceString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String getResourceString(Context context, int id, String... args) {
        String string = context.getResources().getString(id);
        for (int arg = 1; arg <= args.length; arg++) {
            String argValue = args[arg - 1];
            if (argValue == null)
                argValue = "";
            string = string.replace("$s" + String.valueOf(arg), argValue);
        }
        return string;
    }

    public static boolean isBluetoothNameValid(final CharSequence[] bluetoothNameFilters, final String bluetoothName) {
        boolean valid = true;

        if (bluetoothNameFilters != null) {
            if (bluetoothNameFilters.length > 0) {
                valid = false;
                if (bluetoothName != null) {
                    if (!bluetoothName.isEmpty()) {
                        for (CharSequence bluetoothNameFilter : bluetoothNameFilters) {
                            if (bluetoothNameFilter != null) {
                                if (bluetoothName.contains(bluetoothNameFilter)) {
                                    valid = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    public static void setImageViewFromBytes(final ImageView imageView, final byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (bitmap != null) {
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
        }
    }
}
