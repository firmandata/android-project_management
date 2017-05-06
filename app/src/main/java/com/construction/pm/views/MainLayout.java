package com.construction.pm.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.HomeFragment;
import com.construction.pm.activities.fragments.InspectorFragment;
import com.construction.pm.activities.fragments.ManagerFragment;
import com.construction.pm.activities.fragments.NotificationListFragment;
import com.construction.pm.activities.fragments.ProjectListFragment;
import com.construction.pm.activities.fragments.ReportRequestListFragment;
import com.construction.pm.activities.fragments.UserChangePasswordFragment;
import com.construction.pm.activities.fragments.UserChangeProfileFragment;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;

public class MainLayout implements NavigationView.OnNavigationItemSelectedListener {

    protected Context mContext;

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_HOME = "FRAGMENT_HOME";
    protected static final String FRAGMENT_TAG_NOTIFICATION_LIST = "FRAGMENT_NOTIFICATION_LIST";
    protected static final String FRAGMENT_TAG_PROJECT_LIST = "FRAGMENT_PROJECT_LIST";
    protected static final String FRAGMENT_TAG_INSPECTOR = "FRAGMENT_INSPECTOR";
    protected static final String FRAGMENT_TAG_MANAGER = "FRAGMENT_MANAGER";
    protected static final String FRAGMENT_TAG_REQUEST_REPORT = "FRAGMENT_REQUEST_REPORT";
    protected static final String FRAGMENT_TAG_USER_CHANGE_PROFILE = "FRAGMENT_USER_CHANGE_PROFILE";
    protected static final String FRAGMENT_TAG_USER_CHANGE_PASSWORD = "FRAGMENT_USER_CHANGE_PASSWORD";

    protected ReportRequestListFragment mReportRequestListFragment;
    protected NotificationListFragment mNotificationListFragment;

    protected DrawerLayout mMainLayout;
    protected ActionBar mActionBar;
    protected NavigationView mNavigationView;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    protected ProgressDialog mProgressDialog;

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

        // -- Set notification unread quantity view --
        MenuItem menuItemNotification = getMenuItemById(R.id.navigator_menu_notification);
        if (menuItemNotification != null) {
            if (Build.VERSION.SDK_INT >= 11)
                menuItemNotification.setActionView(R.layout.main_menu_notification_quantity_view);
        }

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setNotificationUnreadQuantity(final int notificationUnreadQuantity) {
        MenuItem menuItem = getMenuItemById(R.id.navigator_menu_notification);
        if (menuItem != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                AppCompatTextView textView = (AppCompatTextView) menuItem.getActionView().findViewById(R.id.unreadNotificationQuantity);
                if (notificationUnreadQuantity > 0)
                    textView.setText(StringUtil.numberFormat(notificationUnreadQuantity));
                else
                    textView.setText(null);
            }
        }
    }

    protected MenuItem getMenuItemById(final int id) {
        for (int menuItemIdx = 0; menuItemIdx < mNavigationView.getMenu().size(); menuItemIdx++) {
            MenuItem menuItem = mNavigationView.getMenu().getItem(menuItemIdx);
            if (menuItem.getItemId() == id) {
                return menuItem;
            }
        }
        return null;
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
        mActionBar = mActivity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.app_name);
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

    public void createReportRequestMenu(final Menu menu) {
        MenuItem menuItemUpdateActivity = menu.add(R.string.main_layout_request_report_menu_add);
        menuItemUpdateActivity.setIcon(R.drawable.ic_create_new_light_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuItemUpdateActivity.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuItemUpdateActivity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuReportRequestAdd();
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigator_menu_home:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuHomeSelected();
                break;
            case R.id.navigator_menu_notification:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuNotificationListSelected();
                break;
            case R.id.navigator_menu_project_list:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuProjectListSelected();
                break;
            case R.id.navigator_menu_inspector:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuInspectorSelected();
                break;
            case R.id.navigator_menu_manager:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuManagerSelected();
                break;
            case R.id.navigator_menu_request_report:
                if (mMainLayoutListener != null)
                    mMainLayoutListener.onMenuReportRequestSelected();
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
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_NOTIFICATION_LIST) && menuItem.getItemId() == R.id.navigator_menu_notification)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_LIST) && menuItem.getItemId() == R.id.navigator_menu_project_list)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_INSPECTOR) && menuItem.getItemId() == R.id.navigator_menu_inspector)
                    isChecked = true;
                if (mFragmentTagSelected.equals(FRAGMENT_TAG_MANAGER) && menuItem.getItemId() == R.id.navigator_menu_manager)
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
        if (mFragmentTagSelected != null)
            return mFragmentTagSelected.equals(FRAGMENT_TAG_HOME);
        return false;
    }

    public boolean isNotificationListFragmentShow() {
        if (mFragmentTagSelected != null)
            return mFragmentTagSelected.equals(FRAGMENT_TAG_NOTIFICATION_LIST);
        return false;
    }

    public boolean isReportRequestListFragmentShow() {
        if (mFragmentTagSelected != null)
            return mFragmentTagSelected.equals(FRAGMENT_TAG_REQUEST_REPORT);
        return false;
    }

    public void addNotificationModelsToNotificationListFragment(final NotificationModel[] notificationModels) {
        if (mNotificationListFragment != null)
            mNotificationListFragment.addNotificationModels(notificationModels);
    }

    protected void loadFragment(final Fragment fragment, final String title, final String tag) {
        if (mActivityHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
            if (mFragmentTagSelected.equals(FRAGMENT_TAG_NOTIFICATION_LIST))
                mNotificationListFragment = null;
        }

        mFragmentTagSelected = tag;

        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }

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

        loadFragment(homeFragment, ViewUtil.getResourceString(mContext, R.string.menu_home_title), FRAGMENT_TAG_HOME);

        return homeFragment;
    }

    public ProjectListFragment showProjectListFragment() {
        ProjectListFragment projectListFragment = ProjectListFragment.newInstance();

        loadFragment(projectListFragment, ViewUtil.getResourceString(mContext, R.string.menu_project_title), FRAGMENT_TAG_PROJECT_LIST);

        return projectListFragment;
    }

    public NotificationListFragment showNotificationListFragment(final NotificationListFragment.NotificationListFragmentListener notificationListFragmentListener) {
        NotificationListFragment notificationListFragment = NotificationListFragment.newInstance();
        notificationListFragment.setNotificationListFragmentListener(notificationListFragmentListener);

        loadFragment(notificationListFragment, ViewUtil.getResourceString(mContext, R.string.menu_inbox_title), FRAGMENT_TAG_NOTIFICATION_LIST);

        mNotificationListFragment = notificationListFragment;

        return notificationListFragment;
    }

    public InspectorFragment showInspectorFragment() {
        InspectorFragment inspectorFragment = InspectorFragment.newInstance();

        loadFragment(inspectorFragment, ViewUtil.getResourceString(mContext, R.string.menu_inspector_title), FRAGMENT_TAG_INSPECTOR);

        return inspectorFragment;
    }

    public ManagerFragment showManagerFragment() {
        ManagerFragment managerFragment = ManagerFragment.newInstance();

        loadFragment(managerFragment, ViewUtil.getResourceString(mContext, R.string.menu_manager_title), FRAGMENT_TAG_MANAGER);

        return managerFragment;
    }

    public ReportRequestListFragment showReportRequestListFragment() {
        ReportRequestListFragment reportRequestListFragment = ReportRequestListFragment.newInstance();

        loadFragment(reportRequestListFragment, ViewUtil.getResourceString(mContext, R.string.menu_request_report_title), FRAGMENT_TAG_REQUEST_REPORT);

        mReportRequestListFragment = reportRequestListFragment;

        return reportRequestListFragment;
    }

    public void addReportRequestListFragment() {
        if (mReportRequestListFragment != null)
            mReportRequestListFragment.addReportRequest();
    }

    public UserChangeProfileFragment showUserChangeProfileFragment(final UserChangeProfileFragment.UserChangeProfileFragmentListener userChangeProfileFragmentListener) {
        UserChangeProfileFragment userChangeProfileFragment = UserChangeProfileFragment.newInstance();
        userChangeProfileFragment.setUserChangeProfileFragmentListener(userChangeProfileFragmentListener);

        loadFragment(userChangeProfileFragment, ViewUtil.getResourceString(mContext, R.string.menu_profile_title), FRAGMENT_TAG_USER_CHANGE_PROFILE);

        return userChangeProfileFragment;
    }

    public UserChangePasswordFragment showUserChangePasswordFragment(final UserChangePasswordFragment.UserChangePasswordFragmentListener userChangePasswordFragmentListener) {
        UserChangePasswordFragment userChangePasswordFragment = UserChangePasswordFragment.newInstance();
        userChangePasswordFragment.setUserChangePasswordFragmentListener(userChangePasswordFragmentListener);

        loadFragment(userChangePasswordFragment, ViewUtil.getResourceString(mContext, R.string.menu_change_password_title), FRAGMENT_TAG_USER_CHANGE_PASSWORD);

        return userChangePasswordFragment;
    }

    public void setMainLayoutListener(final MainLayoutListener mainLayoutListener) {
        mMainLayoutListener = mainLayoutListener;
    }

    public interface MainLayoutListener {
        void onMenuHomeSelected();
        void onMenuProjectListSelected();
        void onMenuInspectorSelected();
        void onMenuManagerSelected();
        void onMenuNotificationListSelected();
        void onMenuReportRequestSelected();
        void onMenuReportRequestAdd();
        void onMenuUserChangeProfileSelected();
        void onMenuUserChangePasswordSelected();
        void onMenuLogoutClick();
    }
}
