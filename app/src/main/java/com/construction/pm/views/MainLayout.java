package com.construction.pm.views;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;

public class MainLayout {

    protected Context mContext;

    protected DrawerLayout mMainLayout;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;

    protected MainLayout(final Context context) {
        mContext = context;
    }

    public MainLayout(final Context context, final DrawerLayout mainLayout) {
        this(context);

        initializeView(mainLayout);
    }

    public static MainLayout buildMainLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new MainLayout(context, (DrawerLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static MainLayout buildMainLayout(final Context context, final ViewGroup viewGroup) {
        return buildMainLayout(context, R.layout.main_layout, viewGroup);
    }

    protected void initializeView(final DrawerLayout mainLayout) {
        mMainLayout = mainLayout;
        mToolbar = (Toolbar) mMainLayout.findViewById(R.id.contentToolbar);
    }

    public DrawerLayout getLayout() {
        return mMainLayout;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void loadLayoutToActivity(AppCompatActivity activity) {
        activity.setContentView(mMainLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(activity, mMainLayout, mToolbar, R.string.main_drawer_open, R.string.main_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mMainLayout.addDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle.syncState();
    }
}
