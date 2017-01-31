package com.construction.pm.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;

import com.construction.pm.R;

public class ButtonUtil {
    public static void setButtonDefault(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_default));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimaryBackground));
    }

    public static void setButtonPrimary(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_primary));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimaryPrimaryBackground));
    }

    public static void setButtonSuccess(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_success));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimarySuccessBackground));
    }

    public static void setButtonInfo(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_info));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimaryInfoBackground));
    }

    public static void setButtonWarning(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_warning));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimaryWarningBackground));
    }

    public static void setButtonDanger(final Context context, final AppCompatButton button) {
        button.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, R.color.button_danger));
        button.setTextColor(ContextCompat.getColorStateList(context, R.color.baseColorTextPrimaryDangerBackground));
    }
}
