package com.construction.pm.views.inspector;

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

public class InspectorLayout implements ProjectActivityListFragment.ProjectActivityListFragmentListener {
    protected Context mContext;

    protected CoordinatorLayout mInspectorLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

    protected InspectorLayoutListener mInspectorLayoutListener;

    protected InspectorLayout(final Context context) {
        mContext = context;
    }

    public InspectorLayout(final Context context, final CoordinatorLayout inspectorLayout) {
        this(context);

        initializeView(inspectorLayout);
    }

    public static InspectorLayout buildInspectorLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new InspectorLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static InspectorLayout buildInspectorLayout(final Context context, final ViewGroup viewGroup) {
        return buildInspectorLayout(context, R.layout.inspector_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout inspectorLayout) {
        mInspectorLayout = inspectorLayout;
        mAppBarLayout = (AppBarLayout) mInspectorLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mInspectorLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mInspectorLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mInspectorLayout.findViewById(R.id.contentBody);
    }

    public CoordinatorLayout getLayout() {
        return mInspectorLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        activity.setContentView(mInspectorLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.inspector_layout_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        mViewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mInspectorLayoutListener != null)
            mInspectorLayoutListener.onInspectorRequest();
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mAppBarLayout.removeView(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(fragment.getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mInspectorLayoutListener != null)
            mInspectorLayoutListener.onInspectorRequest();
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
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(inProgressProjectActivityModels, StatusTaskEnum.IN_PROGRESS, this), ViewUtil.getResourceString(mContext, R.string.inspector_layout_tab_in_progress));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(comingDueProjectActivityModels, StatusTaskEnum.COMING_DUE, this), ViewUtil.getResourceString(mContext, R.string.inspector_layout_tab_coming_due));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(shouldHaveStartedProjectActivityModels, StatusTaskEnum.SHOULD_HAVE_STARTED, this), ViewUtil.getResourceString(mContext, R.string.inspector_layout_tab_should_have_started));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(lateProjectActivityModels, StatusTaskEnum.LATE, this), ViewUtil.getResourceString(mContext, R.string.inspector_layout_tab_late));
        mViewPagerAdapter.addFragment(ProjectActivityListFragment.newInstance(completedProjectActivityModels, StatusTaskEnum.COMPLETED, this), ViewUtil.getResourceString(mContext, R.string.inspector_layout_tab_completed));
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProjectActivityClick(ProjectActivityModel projectActivityModel) {
        if (mInspectorLayoutListener != null)
            mInspectorLayoutListener.onProjectActivityListItemClick(projectActivityModel);
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

    public void setInspectorLayoutListener(final InspectorLayoutListener inspectorLayoutListener) {
        mInspectorLayoutListener = inspectorLayoutListener;
    }

    public interface InspectorLayoutListener {
        void onInspectorRequest();
        void onProjectActivityListItemClick(ProjectActivityModel projectActivityModel);
    }
}
