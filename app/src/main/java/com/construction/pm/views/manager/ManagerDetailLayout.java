package com.construction.pm.views.manager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectActivityMonitoringListFragment;
import com.construction.pm.activities.fragments.ProjectActivityUpdateListFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project_activity.ProjectActivityDetailView;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailLayout implements
        ProjectActivityUpdateListFragment.ProjectActivityUpdateListFragmentListener,
        ProjectActivityMonitoringListFragment.ProjectActivityMonitoringListFragmentListener {

    protected Context mContext;

    protected CoordinatorLayout mManagerDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

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
        mTabLayout = (TabLayout) mManagerDetailLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mManagerDetailLayout.findViewById(R.id.contentBody);

        mProjectActivityDetailView = new ProjectActivityDetailView(mContext, (RelativeLayout) mManagerDetailLayout.findViewById(R.id.project_activity_detail_view));
    }

    public CoordinatorLayout getLayout() {
        return mManagerDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectActivityModel projectActivityModel) {
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

        mViewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onManagerDetailRequest(projectActivityModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectActivityModel projectActivityModel) {
        mAppBarLayout.removeView(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(fragment.getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mManagerDetailLayoutListener != null)
            mManagerDetailLayoutListener.onManagerDetailRequest(projectActivityModel);
    }

    public void setLayoutData(final ProjectActivityModel projectActivityModel) {
        mProjectActivityDetailView.setProjectActivityModel(projectActivityModel);

        mViewPagerAdapter.clearFragments();
        mViewPagerAdapter.addFragment(ProjectActivityUpdateListFragment.newInstance(projectActivityModel, this), ViewUtil.getResourceString(mContext, R.string.manager_detail_layout_tab_update));
        mViewPagerAdapter.addFragment(ProjectActivityMonitoringListFragment.newInstance(projectActivityModel, this), ViewUtil.getResourceString(mContext, R.string.manager_detail_layout_tab_monitoring));
        mViewPagerAdapter.notifyDataSetChanged();
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

    protected class ViewPagerAdapter extends FragmentPagerAdapter {

        protected List<Fragment> mFragmentList;
        protected List<String> mTitleList;

        public ViewPagerAdapter(final FragmentManager fragmentManager) {
            super(fragmentManager);

            mFragmentList = new ArrayList<Fragment>();
            mTitleList = new ArrayList<String>();
        }

        public void addFragment(final Fragment fragment, final String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        public void removeFragment(final Fragment fragment) {
            int position = mFragmentList.indexOf(fragment);
            removeFragment(position);
        }

        public void removeFragment(final int position) {
            mFragmentList.remove(position);
            mTitleList.remove(position);
        }

        public void clearFragments() {
            mFragmentList.clear();
            mTitleList.clear();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
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
