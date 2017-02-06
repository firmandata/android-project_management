package com.construction.pm.views;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.construction.pm.fragments.UserChangePasswordFragment;
import com.construction.pm.fragments.UserChangeProfileFragment;

public class MainLayout {

    protected Context mContext;

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected static final String FRAGMENT_USER_CHANGE_PROFILE = "FRAGMENT_USER_CHANGE_PROFILE";
    protected static final String FRAGMENT_USER_CHANGE_PASSWORD = "FRAGMENT_USER_CHANGE_PASSWORD";

    protected DrawerLayout mMainLayout;
    protected NavigationView mNavigationView;
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
        mNavigationView = (NavigationView) mMainLayout.findViewById(R.id.navigator);
    }

    public DrawerLayout getLayout() {
        return mMainLayout;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public void loadLayoutToActivity(AppCompatActivity activity) {
        mActivity = activity;

        mActivity.setContentView(mMainLayout);

        mActivity.setSupportActionBar(mToolbar);
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mMainLayout, mToolbar, R.string.main_drawer_open, R.string.main_drawer_close) {
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

        mActivityHandler = new Handler();
    }

    protected void loadFragment(final Fragment fragment, final String tag) {
        if (mActivityHandler == null)
            return;

        mActivityHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null)
                    return;

                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentBodyHolder, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

    public UserChangeProfileFragment showUserChangeProfile() {
        UserChangeProfileFragment userChangeProfileFragment = UserChangeProfileFragment.newInstance();

        loadFragment(userChangeProfileFragment, FRAGMENT_USER_CHANGE_PROFILE);

        return userChangeProfileFragment;
    }

    public UserChangePasswordFragment showUserChangePassword() {
        UserChangePasswordFragment userChangePasswordFragment = UserChangePasswordFragment.newInstance();

        loadFragment(userChangePasswordFragment, FRAGMENT_USER_CHANGE_PASSWORD);

        return userChangePasswordFragment;
    }
}
