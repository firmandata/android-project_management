package com.construction.pm.views.project_stage;

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
import com.construction.pm.activities.fragments.ProjectStageAssignCommentDetailFragment;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

public class ProjectStageAssignCommentDetailLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL = "FRAGMENT_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL";

    protected CoordinatorLayout mProjectStageAssignCommentDetailLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectStageAssignCommentDetailLayoutListener mProjectStageAssignCommentDetailLayoutListener;

    protected ProjectStageAssignCommentDetailLayout(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentDetailLayout(final Context context, final CoordinatorLayout projectStageAssignCommentDetailLayout) {
        this(context);

        initializeView(projectStageAssignCommentDetailLayout);
    }

    public static ProjectStageAssignCommentDetailLayout buildProjectStageAssignCommentDetailLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentDetailLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentDetailLayout buildProjectStageAssignCommentDetailLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentDetailLayout(context, R.layout.project_stage_assign_comment_detail_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectStageAssignCommentDetailLayout) {
        mProjectStageAssignCommentDetailLayout = projectStageAssignCommentDetailLayout;
        mAppBarLayout = (AppBarLayout) mProjectStageAssignCommentDetailLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectStageAssignCommentDetailLayout.findViewById(R.id.contentToolbar);
    }

    public CoordinatorLayout getLayout() {
        return mProjectStageAssignCommentDetailLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectStageAssignCommentDetailLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.project_stage_assign_comment_detail_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void createProjectStageAssignCommentEditMenu(final Menu menu) {
        MenuItem menuEdit = menu.add(R.string.project_stage_assign_comment_detail_menu_edit);
        menuEdit.setIcon(R.drawable.ic_edit_light_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mProjectStageAssignCommentDetailLayoutListener != null)
                    mProjectStageAssignCommentDetailLayoutListener.onProjectStageAssignCommentDetailEditClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isProjectStageAssignCommentDetailFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL);
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

    public ProjectStageAssignCommentDetailFragment showProjectStageAssignCommentDetailFragment(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        ProjectStageAssignCommentDetailFragment projectStageAssignCommentDetailFragment = ProjectStageAssignCommentDetailFragment.newInstance(projectStageAssignCommentModel);

        loadFragment(projectStageAssignCommentDetailFragment, ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_detail_title), DateTimeUtil.ToDateTimeDisplayString(projectStageAssignCommentModel.getCommentDate()), FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL);

        return projectStageAssignCommentDetailFragment;
    }

    public void setProjectStageAssignCommentDetailLayoutListener(final ProjectStageAssignCommentDetailLayoutListener projectStageAssignCommentDetailLayoutListener) {
        mProjectStageAssignCommentDetailLayoutListener = projectStageAssignCommentDetailLayoutListener;
    }

    public interface ProjectStageAssignCommentDetailLayoutListener {
        void onProjectStageAssignCommentDetailEditClick();
    }
}
