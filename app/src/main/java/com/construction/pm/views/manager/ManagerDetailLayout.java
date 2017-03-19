package com.construction.pm.views.manager;

import android.content.Context;
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ManagerDetailFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.views.project_activity.ProjectActivityDetailView;

public class ManagerDetailLayout implements ManagerDetailFragment.ManagerDetailFragmentListener {

    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected CoordinatorLayout mManagerDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ManagerDetailFragment mManagerDetailFragment;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_MANAGER_DETAIL = "FRAGMENT_MANAGER_DETAIL";

    protected ProjectActivityDetailView mProjectActivityDetailView;

    protected ManagerDetailLayoutListener mManagerDetailLayoutListener;

    protected ManagerDetailLayout(final Context context) {
        mContext = context;
    }

    public ManagerDetailLayout(final Context context, final CoordinatorLayout managerDetailLayout) {
        this(context);

        initializeView(managerDetailLayout);
    }

    public static ManagerDetailLayout buildManagerDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ManagerDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ManagerDetailLayout buildManagerDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildManagerDetailLayout(context, R.layout.manager_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout managerDetailLayout) {
        mManagerDetailLayout = managerDetailLayout;
        mAppBarLayout = (AppBarLayout) mManagerDetailLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mManagerDetailLayout.findViewById(R.id.contentToolbar);

        mProjectActivityDetailView = new ProjectActivityDetailView(mContext, (RelativeLayout) mManagerDetailLayout.findViewById(R.id.project_activity_detail_view));
    }

    public CoordinatorLayout getLayout() {
        return mManagerDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectActivityModel projectActivityModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mManagerDetailLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            if (projectActivityModel != null) {
                mActionBar.setTitle(projectActivityModel.getTaskName());
                mActionBar.setSubtitle(projectActivityModel.getActivityStatus());
            } else
                mActionBar.setTitle(R.string.manager_detail_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }

        setProjectActivityModel(projectActivityModel);

        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onManagerDetailRequest(projectActivityModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectActivityModel projectActivityModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        setProjectActivityModel(projectActivityModel);

        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onManagerDetailRequest(projectActivityModel);
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mProjectActivityDetailView.setProjectActivityModel(projectActivityModel);
    }

    public boolean isManagerFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_MANAGER_DETAIL);
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

    public void showManagerDetailFragment(final ProjectActivityModel projectActivityModel) {
        if (mManagerDetailFragment == null)
            mManagerDetailFragment = ManagerDetailFragment.newInstance(projectActivityModel, this);

        loadFragment(mManagerDetailFragment, projectActivityModel.getTaskName(), projectActivityModel.getActivityStatus(), FRAGMENT_TAG_MANAGER_DETAIL);
    }

    public void reloadProjectActivityUpdateList(final ProjectActivityModel projectActivityModel) {
        if (mManagerDetailFragment != null)
            mManagerDetailFragment.reloadProjectActivityUpdateList(projectActivityModel);
    }

    public void reloadProjectActivityMonitoringList(final ProjectActivityModel projectActivityModel) {
        if (mManagerDetailFragment != null)
            mManagerDetailFragment.reloadProjectActivityMonitoringList(projectActivityModel);
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onProjectActivityUpdateListItemClick(projectActivityUpdateModel);
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
    }

    public void setManagerDetailLayoutListener(final ManagerDetailLayoutListener managerDetailLayoutListener) {
        mManagerDetailLayoutListener = managerDetailLayoutListener;
    }

    public interface ManagerDetailLayoutListener {
        void onManagerDetailRequest(ProjectActivityModel projectActivityModel);
        void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel);
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
