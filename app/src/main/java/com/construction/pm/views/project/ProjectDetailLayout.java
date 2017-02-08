package com.construction.pm.views.project;

import android.content.Context;
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
import com.construction.pm.activities.fragments.ProjectDetailContractFragment;
import com.construction.pm.activities.fragments.ProjectDetailProjectFragment;
import com.construction.pm.activities.fragments.ProjectDetailPlanListFragment;
import com.construction.pm.activities.fragments.ProjectDetailStageListFragment;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailLayout {

    protected Context mContext;

    protected AppCompatActivity mActivity;

    protected CoordinatorLayout mProjectDetailLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;

    protected ProjectModel mProjectModel;

    protected ProjectDetailLayout(final Context context) {
        mContext = context;
    }

    public ProjectDetailLayout(final Context context, final CoordinatorLayout projectDetailLayout) {
        this(context);

        initializeView(projectDetailLayout);
    }

    public static ProjectDetailLayout buildProjectDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectDetailLayout buildProjectDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailLayout(context, R.layout.project_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectDetailLayout) {
        mProjectDetailLayout = projectDetailLayout;
        mToolbar = (Toolbar) mProjectDetailLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mProjectDetailLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mProjectDetailLayout.findViewById(R.id.contentBody);
    }

    public void setProjectModel(final ProjectModel projectModel) {
        mProjectModel = projectModel;
    }

    public CoordinatorLayout getLayout() {
        return mProjectDetailLayout;
    }

    public void loadLayoutToActivity(AppCompatActivity activity) {
        mActivity = activity;

        mActivity.setContentView(mProjectDetailLayout);

        mActivity.setSupportActionBar(mToolbar);
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            if (mProjectModel != null) {
                actionBar.setTitle(mProjectModel.getContractNo());
                actionBar.setSubtitle(mProjectModel.getProjectName());
            } else
                actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager());
        viewPagerAdapter.addFragment(ProjectDetailProjectFragment.newInstance(), ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_project));
        viewPagerAdapter.addFragment(ProjectDetailContractFragment.newInstance(), ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_contract));
        viewPagerAdapter.addFragment(ProjectDetailStageListFragment.newInstance(), ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_stage));
        viewPagerAdapter.addFragment(ProjectDetailPlanListFragment.newInstance(), ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_plan_realization));
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
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
}
