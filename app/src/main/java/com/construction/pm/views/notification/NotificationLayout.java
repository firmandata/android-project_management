package com.construction.pm.views.notification;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.NotificationDetailFragment;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

public class NotificationLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_NOTIFICATION_DETAIL = "FRAGMENT_NOTIFICATION_DETAIL";

    protected CoordinatorLayout mNotificationLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected NotificationLayoutListener mNotificationLayoutListener;

    protected NotificationLayout(final Context context) {
        mContext = context;
    }

    public NotificationLayout(final Context context, final CoordinatorLayout notificationLayout) {
        this(context);

        initializeView(notificationLayout);
    }

    public static NotificationLayout buildNotificationLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new NotificationLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static NotificationLayout buildNotificationLayout(final Context context, final ViewGroup viewGroup) {
        return buildNotificationLayout(context, R.layout.notification_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout mainLayout) {
        mNotificationLayout = mainLayout;
        mAppBarLayout = (AppBarLayout) mNotificationLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mNotificationLayout.findViewById(R.id.contentToolbar);
    }

    public CoordinatorLayout getLayout() {
        return mNotificationLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mNotificationLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.notification_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isNotificationFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_NOTIFICATION_DETAIL);
    }

    protected void loadFragment(final Fragment fragment, final String title, final String subtitle, final String tag) {
        if (mFragmentHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
        }

        mFragmentTagSelected = tag;

        if (mActionBar != null) {
            mActionBar.setTitle(title);
            if (subtitle != null)
                mActionBar.setSubtitle(subtitle);
        }

        mFragmentHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFragmentManager == null)
                    return;

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentBody, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

    public NotificationDetailFragment showNotificationDetailFragment(final NotificationModel notificationModel) {
        NotificationDetailFragment notificationDetailFragment = NotificationDetailFragment.newInstance(notificationModel);

        loadFragment(notificationDetailFragment, ViewUtil.getResourceString(mContext, R.string.notification_detail_title), DateTimeUtil.ToDateTimeDisplayString(notificationModel.getNotificationDate()), FRAGMENT_TAG_NOTIFICATION_DETAIL);

        if (mNotificationLayoutListener != null)
            mNotificationLayoutListener.onNotificationReadRequest(notificationModel);

        return notificationDetailFragment;
    }

    public void setNotificationLayoutListener(final NotificationLayoutListener notificationLayoutListener) {
        mNotificationLayoutListener = notificationLayoutListener;
    }

    public interface NotificationLayoutListener {
        void onNotificationReadRequest(NotificationModel notificationModel);
    }
}
