package com.construction.pm.views.project;

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
import com.construction.pm.activities.fragments.ContractDetailFragment;
import com.construction.pm.activities.fragments.ProjectDetailFragment;
import com.construction.pm.activities.fragments.ProjectPlanListFragment;
import com.construction.pm.activities.fragments.ProjectStageListFragment;
import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ProjectLayout {
    protected Context mContext;
    protected FragmentManager mFragmentManager;

    protected CoordinatorLayout mProjectLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

    protected ProjectLayoutListener mProjectLayoutListener;

    protected ProjectLayout(final Context context) {
        mContext = context;
    }

    public ProjectLayout(final Context context, final CoordinatorLayout projectLayout) {
        this(context);

        initializeView(projectLayout);
    }

    public static ProjectLayout buildProjectLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectLayout buildProjectLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectLayout(context, R.layout.project_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectLayout) {
        mProjectLayout = projectLayout;
        mAppBarLayout = (AppBarLayout) mProjectLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mProjectLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mProjectLayout.findViewById(R.id.contentBody);
        mViewPager.setOffscreenPageLimit(4);
    }

    public CoordinatorLayout getLayout() {
        return mProjectLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectModel projectModel) {
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (projectModel != null) {
                actionBar.setTitle(projectModel.getContractNo());
                actionBar.setSubtitle(projectModel.getProjectName());
            } else
                actionBar.setTitle(R.string.project_layout_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mProjectLayoutListener != null)
            mProjectLayoutListener.onProjectRequest(projectModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectModel projectModel) {
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mProjectLayoutListener != null)
            mProjectLayoutListener.onProjectRequest(projectModel);
    }

    public void setLayoutData(final ContractModel contractModel, final ProjectModel projectModel, final ProjectStageModel[] projectStageModels, final ProjectPlanModel[] projectPlanModels) {
        mViewPagerAdapter.clearFragments();
        mViewPagerAdapter.addFragment(ProjectDetailFragment.newInstance(projectModel), ViewUtil.getResourceString(mContext, R.string.project_layout_tab_project));
        mViewPagerAdapter.addFragment(ContractDetailFragment.newInstance(contractModel), ViewUtil.getResourceString(mContext, R.string.project_layout_tab_contract));
        mViewPagerAdapter.addFragment(ProjectStageListFragment.newInstance(projectStageModels), ViewUtil.getResourceString(mContext, R.string.project_layout_tab_stage));
        mViewPagerAdapter.addFragment(ProjectPlanListFragment.newInstance(projectPlanModels), ViewUtil.getResourceString(mContext, R.string.project_layout_tab_plan));
        mViewPagerAdapter.notifyDataSetChanged();
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

    public void setProjectLayoutListener(final ProjectLayoutListener projectLayoutListener) {
        mProjectLayoutListener = projectLayoutListener;
    }

    public interface ProjectLayoutListener {
        void onProjectRequest(ProjectModel projectModel);
    }
}
