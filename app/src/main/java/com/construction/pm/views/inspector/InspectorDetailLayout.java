package com.construction.pm.views.inspector;

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
import com.construction.pm.activities.fragments.ProjectActivityMonitoringListFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.views.project_activity.ProjectActivityDetailView;

public class InspectorDetailLayout implements ProjectActivityMonitoringListFragment.ProjectActivityMonitoringListFragmentListener {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_LIST = "FRAGMENT_PROJECT_ACTIVITY_MONITORING_LIST";

    protected CoordinatorLayout mInspectorDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectActivityDetailView mProjectActivityDetailView;
    protected ProjectActivityMonitoringListFragment mProjectActivityMonitoringListFragment;

    protected InspectorDetailLayoutListener mInspectorDetailLayoutListener;

    protected InspectorDetailLayout(final Context context) {
        mContext = context;
    }

    public InspectorDetailLayout(final Context context, final CoordinatorLayout inspectorDetailLayout) {
        this(context);

        initializeView(inspectorDetailLayout);
    }

    public static InspectorDetailLayout buildInspectorDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new InspectorDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static InspectorDetailLayout buildInspectorDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildInspectorDetailLayout(context, R.layout.inspector_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout inspectorDetailLayout) {
        mInspectorDetailLayout = inspectorDetailLayout;
        mAppBarLayout = (AppBarLayout) mInspectorDetailLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mInspectorDetailLayout.findViewById(R.id.contentToolbar);

        mProjectActivityDetailView = new ProjectActivityDetailView(mContext, (RelativeLayout) mInspectorDetailLayout.findViewById(R.id.project_activity_detail_view));
    }

    public CoordinatorLayout getLayout() {
        return mInspectorDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectActivityModel projectActivityModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mInspectorDetailLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            if (projectActivityModel != null) {
                mActionBar.setTitle(projectActivityModel.getTaskName());
                mActionBar.setSubtitle(projectActivityModel.getActivityStatus());
            } else
                mActionBar.setTitle(R.string.inspector_detail_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }

        if (mInspectorDetailLayoutListener != null)
            mInspectorDetailLayoutListener.onInspectorDetailRequest(projectActivityModel);
    }

    public void createProjectActivityMonitoringAddMenu(final Menu menu) {
        MenuItem menuItemUpdateActivity = menu.add(R.string.inspector_detail_layout_menu_monitoring_add);
        menuItemUpdateActivity.setIcon(R.drawable.ic_create_new_dark_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuItemUpdateActivity.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuItemUpdateActivity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mInspectorDetailLayoutListener != null)
                    mInspectorDetailLayoutListener.onProjectActivityMonitoringAddMenuClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectActivityModel projectActivityModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        if (mInspectorDetailLayoutListener != null)
            mInspectorDetailLayoutListener.onInspectorDetailRequest(projectActivityModel);
    }

    public void setLayoutData(final ProjectActivityModel projectActivityModel) {
        mProjectActivityDetailView.setProjectActivityModel(projectActivityModel);
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mProjectActivityDetailView.setProjectActivityModel(projectActivityModel);
    }

    public boolean isProjectActivityMonitoringListFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_LIST);
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

    public ProjectActivityMonitoringListFragment showProjectActivityMonitoringListFragment(final ProjectActivityModel projectActivityModel) {
        if (mProjectActivityMonitoringListFragment == null)
            mProjectActivityMonitoringListFragment = ProjectActivityMonitoringListFragment.newInstance(projectActivityModel, this);

        loadFragment(mProjectActivityMonitoringListFragment, projectActivityModel.getTaskName(), projectActivityModel.getActivityStatus(), FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_LIST);

        return mProjectActivityMonitoringListFragment;
    }

    public void addProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringListFragment.addProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mInspectorDetailLayoutListener != null)
            mInspectorDetailLayoutListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
    }

    public void setInspectorDetailLayoutListener(final InspectorDetailLayoutListener inspectorDetailLayoutListener) {
        mInspectorDetailLayoutListener = inspectorDetailLayoutListener;
    }

    public interface InspectorDetailLayoutListener {
        void onInspectorDetailRequest(ProjectActivityModel projectActivityModel);
        void onProjectActivityMonitoringAddMenuClick();
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
