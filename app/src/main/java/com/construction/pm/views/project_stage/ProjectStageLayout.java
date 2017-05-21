package com.construction.pm.views.project_stage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectStageAssignCommentListFragment;
import com.construction.pm.activities.fragments.ProjectStageFragment;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ProjectStageLayout implements
        ProjectStageFragment.ProjectStageFragmentListener,
        ProjectStageAssignCommentListFragment.ProjectStageAssignCommentListFragmentListener {

    protected Context mContext;

    protected FragmentManager mFragmentManager;
    protected ProjectStageFragment mProjectStageFragment;
    protected ProjectStageAssignCommentListFragment mProjectStageAssignCommentListFragment;

    protected CoordinatorLayout mProjectStageLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;

    protected ProgressDialog mProgressDialog;

    protected ProjectStageLayoutListener mProjectStageLayoutListener;

    protected ProjectStageLayout(final Context context) {
        mContext = context;
    }

    public ProjectStageLayout(final Context context, final CoordinatorLayout projectStageLayout) {
        this(context);

        initializeView(projectStageLayout);
    }

    public static ProjectStageLayout buildProjectStageLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageLayout buildProjectStageLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageLayout(context, R.layout.project_stage_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectLayout) {
        mProjectStageLayout = projectLayout;
        mAppBarLayout = (AppBarLayout) mProjectStageLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectStageLayout.findViewById(R.id.contentToolbar);
        mTabLayout = (TabLayout) mProjectStageLayout.findViewById(R.id.contentTab);
        mViewPager = (ViewPager) mProjectStageLayout.findViewById(R.id.contentBody);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mProjectStageLayoutListener != null)
                    mProjectStageLayoutListener.onProjectStageTabChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public CoordinatorLayout getLayout() {
        return mProjectStageLayout;
    }

    public void progressDialogShow(final String progressMessage, final boolean cancelable) {
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(cancelable);
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogShow(final String progressMessage) {
        progressDialogShow(progressMessage, false);
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectStageModel projectStageModel) {
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectStageLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (projectStageModel != null) {
                actionBar.setTitle(projectStageModel.getStageCode());
                actionBar.setSubtitle(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageDate()));
            } else
                actionBar.setTitle(R.string.project_stage_layout_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mProjectStageLayoutListener != null)
            mProjectStageLayoutListener.onProjectStageRequest(projectStageModel);
    }

    public void createProjectStageAssignCommentAddMenu(final Menu menu) {
        MenuItem menuItemUpdateActivity = menu.add(R.string.project_stage_layout_menu_comment_add);
        menuItemUpdateActivity.setIcon(R.drawable.ic_create_new_light_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuItemUpdateActivity.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuItemUpdateActivity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mProjectStageLayoutListener != null)
                    mProjectStageLayoutListener.onProjectStageAssignCommentAddMenuClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectStageModel projectStageModel) {
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        if (mProjectStageLayoutListener != null)
            mProjectStageLayoutListener.onProjectStageRequest(projectStageModel);
    }

    public void setLayoutData(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageFragment = ProjectStageFragment.newInstance(projectStageModel, projectStageAssignmentModels, projectStageDocumentModels, this);
        mProjectStageAssignCommentListFragment = ProjectStageAssignCommentListFragment.newInstance(projectStageModel, projectStageAssignCommentModels, this);

        mViewPagerAdapter.clearFragments();
        mViewPagerAdapter.addFragment(mProjectStageFragment, ViewUtil.getResourceString(mContext, R.string.project_stage_layout_tab_project_stage_title));
        mViewPagerAdapter.addFragment(mProjectStageAssignCommentListFragment, ViewUtil.getResourceString(mContext, R.string.project_stage_layout_tab_comment_list_title));
        mViewPagerAdapter.notifyDataSetChanged();
    }

    public boolean isTabShowProjectStage() {
        return mViewPager.getCurrentItem() == 0;
    }

    public boolean isTabShowProjectStageAssignCommentList() {
        return mViewPager.getCurrentItem() == 1;
    }

    @Override
    public void onProjectStageAssignCommentListRequest(ProjectStageModel projectStageModel) {

    }

    @Override
    public void onProjectStageAssignCommentItemClick(ProjectStageAssignCommentModel projectStageAssignCommentModel) {

    }

    @Override
    public void onProjectStageRequest(ProjectStageModel projectStageModel) {

    }

    @Override
    public void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel) {

    }

    @Override
    public void onProjectStageDocumentItemClick(FileModel fileModel) {

    }

    @Override
    public void onProjectStageDocumentItemClick(String errorMessage) {
        alertDialogErrorShow(errorMessage);
    }

    public ProjectStageModel getProjectStageModel() {
        if (mProjectStageFragment != null)
            return mProjectStageFragment.getProjectStageModel();
        return null;
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        if (mProjectStageFragment != null)
            return mProjectStageFragment.getProjectStageAssignmentModels();
        return null;
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        if (mProjectStageFragment != null)
            return mProjectStageFragment.getProjectStageDocumentModels();
        return null;
    }

    public void showProjectStageAssignCommentForm(final ProjectStageAssignmentModel projectStageAssignmentModel) {
        if (mProjectStageAssignCommentListFragment != null)
            mProjectStageAssignCommentListFragment.showProjectStageAssignCommentForm(projectStageAssignmentModel);
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
        if (mProjectStageAssignCommentListFragment != null)
            return mProjectStageAssignCommentListFragment.getProjectStageAssignCommentModels();
        return null;
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

    public void alertDialogShow(final String alertTitle, final String alertMessage, final int iconId, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setIcon(iconId);
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_form_layout_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_form_layout_alert_title_error), errorMessage, R.drawable.ic_error_dark_24, null);
    }

    public void setProjectStageLayoutListener(final ProjectStageLayoutListener projectStageLayoutListener) {
        mProjectStageLayoutListener = projectStageLayoutListener;
    }

    public interface ProjectStageLayoutListener {
        void onProjectStageRequest(ProjectStageModel projectStageModel);
        void onProjectStageAssignCommentAddMenuClick();
        void onProjectStageTabChanged(int position);
    }
}
