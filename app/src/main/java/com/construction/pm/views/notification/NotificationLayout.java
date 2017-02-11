package com.construction.pm.views.notification;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_NOTIFICATION_DETAIL = "FRAGMENT_NOTIFICATION_DETAIL";

    protected CoordinatorLayout mNotificationLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected NotificationModel mNotificationModel;

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
        mToolbar = (Toolbar) mNotificationLayout.findViewById(R.id.contentToolbar);
    }

    public void setNotificationModel(final NotificationModel notificationModel) {
        mNotificationModel = notificationModel;
    }

    public CoordinatorLayout getLayout() {
        return mNotificationLayout;
    }

    public void loadLayoutToActivity(AppCompatActivity activity) {
        mActivity = activity;

        mActivity.setContentView(mNotificationLayout);

        mActivity.setSupportActionBar(mToolbar);
        mActionBar = mActivity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.notification_layout_title);
            if (mNotificationModel != null)
                mActionBar.setSubtitle(DateTimeUtil.ToDateTimeDisplayString(mNotificationModel.getNotificationDate()));
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }

        mActivityHandler = new Handler();

        if (mNotificationLayoutListener != null)
            mNotificationLayoutListener.onNotificationRequest(mNotificationModel);
    }

    public boolean isNotificationFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_NOTIFICATION_DETAIL);
    }

    protected void loadFragment(final Fragment fragment, final String title, final String tag) {
        if (mActivityHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
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
    }

    public NotificationDetailFragment showNotificationDetailFragment(final NotificationModel notificationModel) {
        NotificationDetailFragment notificationDetailFragment = NotificationDetailFragment.newInstance(notificationModel);

        loadFragment(notificationDetailFragment, ViewUtil.getResourceString(mContext, R.string.notification_detail_title), FRAGMENT_TAG_NOTIFICATION_DETAIL);

        return notificationDetailFragment;
    }

    public void setNotificationLayoutListener(final NotificationLayoutListener notificationLayoutListener) {
        mNotificationLayoutListener = notificationLayoutListener;
    }

    public interface NotificationLayoutListener {
        void onNotificationRequest(NotificationModel notificationModel);
    }
}