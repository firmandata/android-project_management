package com.construction.pm.views.project_activity;

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

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectActivityUpdateDetailFragment;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectActivityUpdateDetailLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_ACTIVITY_UPDATE_DETAIL = "FRAGMENT_PROJECT_ACTIVITY_UPDATE_DETAIL";

    protected CoordinatorLayout mProjectActivityUpdateDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectActivityUpdateDetailLayoutListener mProjectActivityUpdateDetailLayoutListener;

    protected ProjectActivityUpdateDetailLayout(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateDetailLayout(final Context context, final CoordinatorLayout projectActivityUpdateDetailLayout) {
        this(context);

        initializeView(projectActivityUpdateDetailLayout);
    }

    public static ProjectActivityUpdateDetailLayout buildProjectActivityUpdateDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateDetailLayout buildProjectActivityUpdateDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateDetailLayout(context, R.layout.project_activity_update_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectActivityUpdateDetailLayout) {
        mProjectActivityUpdateDetailLayout = projectActivityUpdateDetailLayout;
        mAppBarLayout = (AppBarLayout) mProjectActivityUpdateDetailLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectActivityUpdateDetailLayout.findViewById(R.id.contentToolbar);
    }

    public CoordinatorLayout getLayout() {
        return mProjectActivityUpdateDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectActivityUpdateDetailLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.project_activity_update_detail_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void createProjectActivityUpdateEditMenu(final Menu menu) {
        MenuItem menuEdit = menu.add(R.string.project_activity_update_detail_menu_edit);
        menuEdit.setIcon(R.drawable.ic_edit_light_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mProjectActivityUpdateDetailLayoutListener != null)
                    mProjectActivityUpdateDetailLayoutListener.onProjectActivityUpdateDetailEditClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isProjectActivityUpdateDetailFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_ACTIVITY_UPDATE_DETAIL);
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

    public ProjectActivityUpdateDetailFragment showProjectActivityUpdateDetailFragment(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        ProjectActivityUpdateDetailFragment projectActivityUpdateDetailFragment = ProjectActivityUpdateDetailFragment.newInstance(projectActivityUpdateModel);

        loadFragment(projectActivityUpdateDetailFragment, DateTimeUtil.ToDateTimeDisplayString(projectActivityUpdateModel.getUpdateDate()), projectActivityUpdateModel.getActivityStatus(), FRAGMENT_TAG_PROJECT_ACTIVITY_UPDATE_DETAIL);

        return projectActivityUpdateDetailFragment;
    }

    public void setProjectActivityUpdateDetailLayoutListener(final ProjectActivityUpdateDetailLayoutListener projectActivityUpdateDetailLayoutListener) {
        mProjectActivityUpdateDetailLayoutListener = projectActivityUpdateDetailLayoutListener;
    }

    public interface ProjectActivityUpdateDetailLayoutListener {
        void onProjectActivityUpdateDetailEditClick();
    }
}
