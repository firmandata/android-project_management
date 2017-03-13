package com.construction.pm.views.project_activity;

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
import com.construction.pm.activities.fragments.ProjectActivityMonitoringDetailFragment;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectActivityMonitoringDetailLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_DETAIL = "FRAGMENT_PROJECT_ACTIVITY_MONITORING_DETAIL";

    protected CoordinatorLayout mProjectActivityMonitoringDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectActivityMonitoringDetailLayoutListener mProjectActivityMonitoringDetailLayoutListener;

    protected ProjectActivityMonitoringDetailLayout(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringDetailLayout(final Context context, final CoordinatorLayout projectActivityMonitoringDetailLayout) {
        this(context);

        initializeView(projectActivityMonitoringDetailLayout);
    }

    public static ProjectActivityMonitoringDetailLayout buildProjectActivityMonitoringDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringDetailLayout buildProjectActivityMonitoringDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringDetailLayout(context, R.layout.project_activity_monitoring_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectActivityMonitoringDetailLayout) {
        mProjectActivityMonitoringDetailLayout = projectActivityMonitoringDetailLayout;
        mAppBarLayout = (AppBarLayout) mProjectActivityMonitoringDetailLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectActivityMonitoringDetailLayout.findViewById(R.id.contentToolbar);
    }

    public CoordinatorLayout getLayout() {
        return mProjectActivityMonitoringDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectActivityMonitoringDetailLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.project_activity_monitoring_detail_title);
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

    public boolean isProjectActivityMonitoringDetailFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_DETAIL);
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

    public ProjectActivityMonitoringDetailFragment showProjectActivityMonitoringDetailFragment(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        ProjectActivityMonitoringDetailFragment projectActivityMonitoringDetailFragment = ProjectActivityMonitoringDetailFragment.newInstance(projectActivityMonitoringModel);

        loadFragment(projectActivityMonitoringDetailFragment, DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate()), StringUtil.numberFormat(projectActivityMonitoringModel.getPercentComplete()), FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_DETAIL);

        return projectActivityMonitoringDetailFragment;
    }

    public void setProjectActivityMonitoringDetailLayoutListener(final ProjectActivityMonitoringDetailLayoutListener projectActivityMonitoringDetailLayoutListener) {
        mProjectActivityMonitoringDetailLayoutListener = projectActivityMonitoringDetailLayoutListener;
    }

    public interface ProjectActivityMonitoringDetailLayoutListener {
    }
}
