package com.construction.pm.views.home;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.construction.pm.R;

public class HomeLayout {

    protected Context mContext;

    protected DrawerLayout mHomeLayout;

    protected Toolbar mToolbar;

    protected HomeLayout(final Context context) {
        mContext = context;
    }

    public HomeLayout(final Context context, final ViewGroup viewGroup) {
        this(context);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mHomeLayout = (DrawerLayout) inflater.inflate(R.layout.home_layout, viewGroup);
        mToolbar = (Toolbar) mHomeLayout.findViewById(R.id.contentToolbar);
    }

    public DrawerLayout getView() {
        return mHomeLayout;
    }
}
