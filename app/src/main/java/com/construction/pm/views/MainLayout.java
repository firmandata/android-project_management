package com.construction.pm.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.HomeFragment;
import com.construction.pm.activities.fragments.ProjectListFragment;
import com.construction.pm.activities.fragments.UserChangePasswordFragment;
import com.construction.pm.activities.fragments.UserChangeProfileFragment;

public class MainLayout implements NavigationView.OnNavigationItemSelectedListener {

    protected Context mContext;

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_HOME = "FRAGMENT_HOME";
    protected static final String FRAGMENT_TAG_INBOX = "FRAGMENT_INBOX";
    protected static final String FRAGMENT_TAG_PROJECT_LIST = "FRAGMENT_PROJECT_LIST";
    protected static final String FRAGMENT_TAG_MONITORING = "FRAGMENT_MONITORING";
    protected static final String FRAGMENT_TAG_UPDATE_TASK_PROGRESS = "FRAGMENT_UPDATE_TASK_PROGRESS";
    protected static final String FRAGMENT_TAG_REQUEST_REPORT = "FRAGMENT_REQUES_REPORT";
    protected static final String FRAGMENT_TAG_USER_CHANGE_PROFILE = "FRAGMENT_USER_CHANGE_PROFILE";
    protected static final String FRAGMENT_TAG_USER_CHANGE_PASSWORD = "FRAGMENT_USER_CHANGE_PASSWORD";

    protected DrawerLayout mMainLayout;
    protected ProgressDialog mProgressDialog;
    protected NavigationView mNavigationView;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;

    protected MainLayoutListener mMainLayoutListener;

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
        mNavigationView.setNavigationItemSelectedListener(this);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public DrawerLayout getLayout() {
        return mMainLayout;
    }

    public void progressDialogShow(final String progressMessage) {
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigator_menu_home:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuHomeSelected();
                break;
            case R.id.navigator_menu_inbox:
                break;
            case R.id.navigator_menu_project_list:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuProjectListSelected();
                break;
            case R.id.navigator_menu_monitoring:
                break;
            case R.id.navigator_menu_update_task_progress:
                break;
            case R.id.navigator_menu_request_report:
                break;
            case R.id.navigator_menu_profile:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuUserChangeProfileSelected();
                break;
            case R.id.navigator_menu_change_password:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuUserChangePasswordSelected();
                break;
            case R.id.navigator_menu_logout:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuLogoutClick();
                break;
        }

        mMainLayout.closeDrawers();

        return true;
    }

    protected void invalidateNavigationViewMenu() {
        if (mActivity == null)
            return;
        if (mFragmentTagSelected == null)
            return;

        for (int menuIdx = 0; menuIdx < mNavigationView.getMenu().size(); menuIdx++) {
            MenuItem menuItem = mNavigationView.getMenu().getItem(menuIdx);

            if (menuItem.isCheckable()) {
                boolean isChecked = false;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_HOME) && menuItem.getItemId() == R.id.navigator_menu_home)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_INBOX) && menuItem.getItemId() == R.id.navigator_menu_inbox)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_LIST) && menuItem.getItemId() == R.id.navigator_menu_project_list)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_MONITORING) && menuItem.getItemId() == R.id.navigator_menu_monitoring)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_UPDATE_TASK_PROGRESS) && menuItem.getItemId() == R.id.navigator_menu_update_task_progress)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_REQUEST_REPORT) && menuItem.getItemId() == R.id.navigator_menu_request_report)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_USER_CHANGE_PROFILE) && menuItem.getItemId() == R.id.navigator_menu_profile)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_USER_CHANGE_PASSWORD) && menuItem.getItemId() == R.id.navigator_menu_change_password)
                    isChecked = true;
                if (isChecked && !menuItem.isChecked())
                    menuItem.setChecked(true);
                else if (!isChecked && menuItem.isChecked())
                    menuItem.setChecked(false);
            }
        }
    }

    public boolean isHomeFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_HOME);
    }

    protected void loadFragment(final Fragment fragment, final String tag) {
        if (mActivityHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
        }

        mFragmentTagSelected = tag;

        mActivityHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null)
                    return;

                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentBody, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        invalidateNavigationViewMenu();
    }

    public HomeFragment showHomeFragment() {
        HomeFragment homeFragment = HomeFragment.newInstance();

        loadFragment(homeFragment, FRAGMENT_TAG_HOME);

        return homeFragment;
    }

    public ProjectListFragment showProjectList() {
        ProjectListFragment projectListFragment = ProjectListFragment.newInstance();

        loadFragment(projectListFragment, FRAGMENT_TAG_PROJECT_LIST);

        return projectListFragment;
    }

    public UserChangeProfileFragment showUserChangeProfile(final UserChangeProfileFragment.UserChangeProfileFragmentListener userChangeProfileFragmentListener) {
        UserChangeProfileFragment userChangeProfileFragment = UserChangeProfileFragment.newInstance();
        userChangeProfileFragment.setUserChangeProfileFragmentListener(userChangeProfileFragmentListener);

        loadFragment(userChangeProfileFragment, FRAGMENT_TAG_USER_CHANGE_PROFILE);

        return userChangeProfileFragment;
    }

    public UserChangePasswordFragment showUserChangePassword(final UserChangePasswordFragment.UserChangePasswordFragmentListener userChangePasswordFragmentListener) {
        UserChangePasswordFragment userChangePasswordFragment = UserChangePasswordFragment.newInstance();
        userChangePasswordFragment.setUserChangePasswordFragmentListener(userChangePasswordFragmentListener);

        loadFragment(userChangePasswordFragment, FRAGMENT_TAG_USER_CHANGE_PASSWORD);

        return userChangePasswordFragment;
    }

    public void setMainLayoutListener(final MainLayoutListener mainLayoutListener) {
        mMainLayoutListener = mainLayoutListener;
    }

    public interface MainLayoutListener {
        void onMenuHomeSelected();
        void onMenuProjectListSelected();
        void onMenuUserChangeProfileSelected();
        void onMenuUserChangePasswordSelected();
        void onMenuLogoutClick();
    }
}
