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

    protected AppCompatActivity mActivity;

    protected CoordinatorLayout mProjectLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected ContractDetailFragment mContractDetailFragment;
    protected ProjectDetailFragment mProjectDetailFragment;
    protected ProjectStageListFragment mProjectStageListFragment;
    protected ProjectPlanListFragment mProjectPlanListFragment;

    protected ProjectModel mProjectModel;

    protected ProjectLayoutListener mProjectLayoutListener;

    protected ProjectLayout(final Context context) {
        mContext = context;
    }

    public ProjectLayout(final Context context, final CoordinatorLayout projectLayout) {
        this(context);

        initializeView(projectLayout);
    }

    public static ProjectLayout buildProjectDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectLayout buildProjectDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectDetailLayout(context, R.layout.project_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectLayout) {
        mProjectLayout = projectLayout;
        mToolbar = (Toolbar) mProjectLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mProjectLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mProjectLayout.findViewById(R.id.contentBody);
    }

    public void setProjectModel(final ProjectModel projectModel) {
        mProjectModel = projectModel;
    }

    public CoordinatorLayout getLayout() {
        return mProjectLayout;
    }

    public void loadLayoutToActivity(AppCompatActivity activity) {
        mActivity = activity;

        mActivity.setContentView(mProjectLayout);

        mActivity.setSupportActionBar(mToolbar);
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            if (mProjectModel != null) {
                actionBar.setTitle(mProjectModel.getContractNo());
                actionBar.setSubtitle(mProjectModel.getProjectName());
            } else
                actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        if (mProjectLayoutListener != null)
            mProjectLayoutListener.onProjectRequest(mProjectModel);

        mProjectDetailFragment = ProjectDetailFragment.newInstance();
        mContractDetailFragment = ContractDetailFragment.newInstance();
        mProjectStageListFragment = ProjectStageListFragment.newInstance();
        mProjectPlanListFragment = ProjectPlanListFragment.newInstance();

        mViewPagerAdapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager());
        mViewPagerAdapter.addFragment(mProjectDetailFragment, ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_project));
        mViewPagerAdapter.addFragment(mContractDetailFragment, ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_contract));
        mViewPagerAdapter.addFragment(mProjectStageListFragment, ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_stage));
        mViewPagerAdapter.addFragment(mProjectPlanListFragment, ViewUtil.getResourceString(mContext, R.string.project_detail_view_tab_plan_realization));
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setContract(final ContractModel contractModel) {
        if (mContractDetailFragment != null)
            mContractDetailFragment.setContractModel(contractModel);
    }

    public void setProject(final ProjectModel projectModel) {
        if (mProjectDetailFragment != null)
            mProjectDetailFragment.setProjectModel(projectModel);
    }

    public void setProjectStages(final ProjectStageModel[] projectStageModels) {
        if (mProjectStageListFragment != null)
            mProjectStageListFragment.setProjectStageModels(projectStageModels);
    }

    public void setProjectPlans(final ProjectPlanModel[] projectPlanModels) {
        if (mProjectPlanListFragment != null)
            mProjectPlanListFragment.setProjectPlanModels(projectPlanModels);
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
