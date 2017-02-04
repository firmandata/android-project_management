package com.construction.pm.views;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class SplashLayout {

    protected Context mContext;

    protected RelativeLayout mSplashLayout;
    protected ProgressBar mPbWaiting;
    protected AppCompatTextView mTvDescription;

    public SplashLayout(final Context context) {
        mContext = context;
    }

    public SplashLayout(final Context context, final RelativeLayout splashLayout) {
        this(context);

        initializeView(splashLayout);
    }

    public static SplashLayout buildMainLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new SplashLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static SplashLayout buildMainLayout(final Context context, final ViewGroup viewGroup) {
        return buildMainLayout(context, R.layout.splash_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout splashLayout) {
        mSplashLayout = splashLayout;
        mTvDescription = (AppCompatTextView) mSplashLayout.findViewById(R.id.tvDescription);
        mPbWaiting = (ProgressBar) mSplashLayout.findViewById(R.id.pbWaiting);
    }

    public void setDescription(final String description) {
        if (mTvDescription != null)
            mTvDescription.setText(description);
    }

    public void showProgressBar() {
        if (mPbWaiting != null)
            mPbWaiting.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (mPbWaiting != null)
            mPbWaiting.setVisibility(View.INVISIBLE);
    }

    public void loadLayoutToActivity(Activity activity) {
        activity.setContentView(mSplashLayout);
    }
}
