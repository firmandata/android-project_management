package com.construction.pm.views.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class HomeView {
    protected Context mContext;

    protected RelativeLayout mHomeView;

    public HomeView(final Context context) {
        mContext = context;
    }

    public HomeView(final Context context, final RelativeLayout homeView) {
        this(context);

        initializeView(homeView);
    }

    public static HomeView buildHomeView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new HomeView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static HomeView buildHomeView(final Context context, final ViewGroup viewGroup) {
        return buildHomeView(context, R.layout.home_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout homeView) {
        mHomeView = homeView;
    }

    public RelativeLayout getView() {
        return mHomeView;
    }
}
