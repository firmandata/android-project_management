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

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectActivityListFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ManagerLayout implements ProjectActivityListFragment.ProjectActivityListFragmentListener {
    protected Context mContext;

    protected CoordinatorLayout mManagerLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

    protected ManagerLayoutListener mManagerLayoutListener;

    protected ManagerLayout(final Context context) {
        mContext = context;
    }

    public ManagerLayout(final Context context, final CoordinatorLayout managerLayout) {
        this(context);

        initializeView(managerLayout);
    }

    public static ManagerLayout buildManagerLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ManagerLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ManagerLayout buildManagerLayout(final Context context, final ViewGroup viewGroup) {
        return buildManagerLayout(context, R.layout.manager_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout managerLayout) {
        mManagerLayout = managerLayout;
        mAppBarLayout = (AppBarLayout) mManagerLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mManagerLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mManagerLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mManagerLayout.findViewById(R.id.contentBody);
    }

    public CoordinatorLayout getLayout() {
        return mManagerLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        activity.setContentView(mManagerLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.manager_layout_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        mViewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mManagerLayoutListener != null)
            mManagerLayoutListener.onManagerRequest();
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mAppBarLayout.removeView(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(fragment.getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mManagerLayoutListener != null)
            mManagerLayoutListener.onManagerRequest();
    }

    public void setLayoutData(final ProjectActivityModel[] projectActivityModels) {
        List<ProjectActivityModel> inProgressProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        List<ProjectActivityModel> comingDueProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        List<ProjectActivityModel> shouldHaveStartedProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        List<ProjectActivityModel> lateProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        List<ProjectActivityModel> completedProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        for (ProjectActivityModel projectActivityModel : projectActivityModels) {
            if (projectActivityModel.getStatusTask() == null)
                continue;

            if (projectActivityModel.getStatusTask() == StatusTaskEnum.IN_PROGRESS)
                inProgressProjectActivityModelList.add(projectActivityModel);
            else if (projectActivityModel.getStatusTask() == StatusTaskEnum.COMING_DUE)
                comingDueProjectActivityModelList.add(projectActivityModel);
            else if (projectActivityModel.getStatusTask() == StatusTaskEnum.SHOULD_HAVE_STARTED)
                shouldHaveStartedProjectActivityModelList.add(projectActivityModel);
            else if (projectActivityModel.getStatusTask() == StatusTaskEnum.LATE)
                lateProjectActivityModelList.add(projectActivityModel);
            else if (projectActivityModel.getStatusTask() == StatusTaskEnum.COMPLETED)
                completedProjectActivityModelList.add(projectActivityModel);
        }

        ProjectActivityModel[] inProgressProjectActivityModels = new ProjectActivityModel[inProgressProjectActivityModelList.size()];
        inProgressProjectActivityModelList.toArray(inProgressProjectActivityModels);

        ProjectActivityModel[] comingDueProjectActivityModels = new ProjectActivityModel[comingDueProjectActivityModelList.size()];
        comingDueProjectActivityModelList.toArray(comingDueProjectActivityModels);

        ProjectActivityModel[] shouldHaveStartedProjectActivityModels = new ProjectActivityModel[shouldHaveStartedProjectActivityModelList.size()];
        shouldHaveStartedProjectActivityModelList.toArray(shouldHaveStartedProjectActivityModels);

        ProjectActivityModel[] lateProjectActivityModels = new ProjectActivityModel[lateProjectActivityModelList.size()];
        lateProjectActivityModelList.toArray(lateProjectActivityModels);

        ProjectActivityModel[] completedProjectActivityModels = new ProjectActivityModel[completedProjectActivityModelList.size()];
        completedProjectActivityModelList.toArray(completedProjectActivityModels);

        mViewPagerAdapter.clearFragments();
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(inProgressProjectActivityModels, StatusTaskEnum.IN_PROGRESS, this), ViewUtil.getResourceString(mContext, R.string.manager_layout_tab_in_progress));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(comingDueProjectActivityModels, StatusTaskEnum.COMING_DUE, this), ViewUtil.getResourceString(mContext, R.string.manager_layout_tab_coming_due));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(shouldHaveStartedProjectActivityModels, StatusTaskEnum.SHOULD_HAVE_STARTED, this), ViewUtil.getResourceString(mContext, R.string.manager_layout_tab_should_have_started));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(lateProjectActivityModels, StatusTaskEnum.LATE, this), ViewUtil.getResourceString(mContext, R.string.manager_layout_tab_late));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(completedProjectActivityModels, StatusTaskEnum.COMPLETED, this), ViewUtil.getResourceString(mContext, R.string.manager_layout_tab_completed));
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProjectActivityClick(ProjectActivityModel projectActivityModel) {
        if (mManagerLayoutListener != null)
            mManagerLayoutListener.onProjectActivityListItemClick(projectActivityModel);
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

    public void setManagerLayoutListener(final ManagerLayoutListener managerLayoutListener) {
        mManagerLayoutListener = managerLayoutListener;
    }

    public interface ManagerLayoutListener {
        void onManagerRequest();
        void onProjectActivityListItemClick(ProjectActivityModel projectActivityModel);
    }
}
