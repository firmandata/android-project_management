package com.construction.pm.libraries.widgets.slidingtab;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class SlidingTabViewPager extends ViewPager {
    private boolean mIsTouchEventEnable;

    public SlidingTabViewPager(final Context context) {
        super(context);
        mIsTouchEventEnable = true;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return mIsTouchEventEnable ? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        return mIsTouchEventEnable ? super.onInterceptTouchEvent(event) : false;
    }

    public void setTouchEventEnable(final boolean isTouchEventEnable) {
        mIsTouchEventEnable = isTouchEventEnable;
    }

    public boolean getTouchEventEnable() {
        return mIsTouchEventEnable;
    }
}