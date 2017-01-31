package com.construction.pm.utils;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.construction.pm.R;

public class ToolbarUtil {
    public static Toolbar getToolbar(Context context) {
        Toolbar toolbar = new Toolbar(context);
        toolbar.setId(ViewUtil.generateViewId());
        toolbar.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
        toolbar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.colorTextPrimary));
        return toolbar;
    }
}
