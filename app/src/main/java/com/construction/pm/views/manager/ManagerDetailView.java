package com.construction.pm.views.manager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectActivityMonitoringListFragment;
import com.construction.pm.activities.fragments.ProjectActivityUpdateListFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailView implements
        ProjectActivityUpdateListFragment.ProjectActivityUpdateListFragmentListener,
        ProjectActivityMonitoringListFragment.ProjectActivityMonitoringListFragmentListener {

    protected Context mContext;

    protected LinearLayout mManagerDetailView;

    protected TabLayout mTabLayout;
    protected ActionBar mActionBar;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

    protected ManagerDetailViewListener mManagerDetailViewListener;

    protected ManagerDetailView(final Context context) {
        mContext = context;
    }

    public ManagerDetailView(final Context context, final LinearLayout managerDetailView) {
        this(context);

        initializeView(managerDetailView);
    }

    public static ManagerDetailView buildManagerDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ManagerDetailView(context, (LinearLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ManagerDetailView buildManagerDetailView(final Context context, final ViewGroup viewGroup) {
        return buildManagerDetailView(context, R.layout.manager_detail_view, viewGroup);
    }

    protected void initializeView(final LinearLayout managerDetailView) {
        mManagerDetailView = managerDetailView;
        mTabLayout = (TabLayout) mManagerDetailView.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mManagerDetailView.findViewById(R.id.contentBody);
    }

    public LinearLayout getView() {
        return mManagerDetailView;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectActivityModel projectActivityModel) {
        activity.setContentView(mManagerDetailView);

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

        if (mManagerDetailViewListener != null)
            mManagerDetailViewListener.onManagerDetailRequest(projectActivityModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectActivityModel projectActivityModel) {
        mViewPagerAdapter = new ViewPagerAdapter(fragment.getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mManagerDetailViewListener != null)
            mManagerDetailViewListener.onManagerDetailRequest(projectActivityModel);
    }

    public void setLayoutData(final ProjectActivityModel projectActivityModel) {
        mViewPagerAdapter.clearFragments();
        mViewPagerAdapter.addFragment(ProjectActivityUpdateListFragment.newInstance(projectActivityModel, this), ViewUtil.getResourceString(mContext, R.string.manager_detail_layout_tab_update));
        mViewPagerAdapter.addFragment(ProjectActivityMonitoringListFragment.newInstance(projectActivityModel, this), ViewUtil.getResourceString(mContext, R.string.manager_detail_layout_tab_monitoring));
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (mManagerDetailViewListener != null)
            mManagerDetailViewListener.onProjectActivityUpdateListItemClick(projectActivityUpdateModel);
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mManagerDetailViewListener != null)
            mManagerDetailViewListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
    }

    public void reloadProjectActivityUpdateList(final ProjectActivityModel projectActivityModel) {
        ProjectActivityUpdateListFragment projectActivityUpdateListFragment = (ProjectActivityUpdateListFragment) mViewPagerAdapter.getItem(0);
        projectActivityUpdateListFragment.reloadProjectActivityUpdateList(projectActivityModel);
    }

    public void addProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        ProjectActivityUpdateListFragment projectActivityUpdateListFragment = (ProjectActivityUpdateListFragment) mViewPagerAdapter.getItem(0);
        projectActivityUpdateListFragment.addProjectActivityUpdateModel(projectActivityUpdateModel);
    }

    public void reloadProjectActivityMonitoringList(final ProjectActivityModel projectActivityModel) {
        ProjectActivityMonitoringListFragment projectActivityMonitoringListFragment = (ProjectActivityMonitoringListFragment) mViewPagerAdapter.getItem(1);
        projectActivityMonitoringListFragment.reloadProjectActivityMonitoringListRequest(projectActivityModel);
    }

    public void addProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        ProjectActivityMonitoringListFragment projectActivityMonitoringListFragment = (ProjectActivityMonitoringListFragment) mViewPagerAdapter.getItem(1);
        projectActivityMonitoringListFragment.addProjectActivityMonitoringModel(projectActivityMonitoringModel);
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

        public void replaceFragment(final int position, final Fragment fragment, final String title) {
            mFragmentList.set(position, fragment);
            mTitleList.set(position, title);
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

    public void setManagerDetailViewListener(final ManagerDetailViewListener managerDetailViewListener) {
        mManagerDetailViewListener = managerDetailViewListener;
    }

    public interface ManagerDetailViewListener {
        void onManagerDetailRequest(ProjectActivityModel projectActivityModel);
        void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel);
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
