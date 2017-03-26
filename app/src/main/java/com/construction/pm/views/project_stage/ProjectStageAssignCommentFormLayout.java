package com.construction.pm.views.project_stage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.ProjectStageAssignCommentFormFragment;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.ViewUtil;

import java.io.File;

public class ProjectStageAssignCommentFormLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_FORM = "FRAGMENT_PROJECT_STAGE_ASSIGN_COMMENT_FORM";

    protected ProjectStageAssignCommentFormFragment mProjectStageAssignCommentFormFragment;

    protected CoordinatorLayout mProjectStageAssignCommentFormLayout;
    protected ProgressDialog mProgressDialog;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectStageAssignCommentFormLayoutListener mProjectStageAssignCommentFormLayoutListener;

    protected ProjectStageAssignCommentFormLayout(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentFormLayout(final Context context, final CoordinatorLayout projectStageAssignCommentFormLayout) {
        this(context);

        initializeView(projectStageAssignCommentFormLayout);
    }

    public static ProjectStageAssignCommentFormLayout buildProjectStageAssignCommentFormLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentFormLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentFormLayout buildProjectStageAssignCommentFormLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentFormLayout(context, R.layout.project_stage_assign_comment_form_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectStageAssignCommentFormLayout) {
        mProjectStageAssignCommentFormLayout = projectStageAssignCommentFormLayout;
        mAppBarLayout = (AppBarLayout) mProjectStageAssignCommentFormLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectStageAssignCommentFormLayout.findViewById(R.id.contentToolbar);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public CoordinatorLayout getLayout() {
        return mProjectStageAssignCommentFormLayout;
    }

    public void progressDialogShow(final String progressMessage) {
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void alertDialogShow(final String alertTitle, final String alertMessage, final int iconId, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setIcon(iconId);
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_form_layout_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.project_stage_assign_comment_form_layout_alert_title_error), errorMessage, R.drawable.cancel_2_24, null);
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectStageAssignCommentFormLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.project_stage_assign_comment_form_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void createProjectStageAssignCommentSaveMenu(final Menu menu) {
        MenuItem menuItemMonitoringActivity = menu.add(R.string.project_stage_assign_comment_form_layout_menu_monitoring_activity);
        menuItemMonitoringActivity.setIcon(R.drawable.checkmark_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuItemMonitoringActivity.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuItemMonitoringActivity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mProjectStageAssignCommentFormLayoutListener != null)
                    mProjectStageAssignCommentFormLayoutListener.onProjectStageAssignCommentFormLayoutSaveMenuClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isProjectStageAssignCommentFormFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_FORM);
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
            if (title != null)
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

    public ProjectStageAssignCommentFormFragment showProjectStageAssignCommentFormFragment(final ProjectStageAssignmentModel projectStageAssignmentModel, final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentFormFragment = ProjectStageAssignCommentFormFragment.newInstance(projectStageAssignmentModel, projectStageAssignCommentModel);

        loadFragment(mProjectStageAssignCommentFormFragment, null, null, FRAGMENT_TAG_PROJECT_STAGE_ASSIGN_COMMENT_FORM);

        return mProjectStageAssignCommentFormFragment;
    }

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        if (mProjectStageAssignCommentFormFragment == null)
            return null;

        return mProjectStageAssignCommentFormFragment.getProjectStageAssignCommentModel();
    }

    public WebApiParam.WebApiParamFile getPhotoId() {
        if (mProjectStageAssignCommentFormFragment == null)
            return null;

        return mProjectStageAssignCommentFormFragment.getPhotoId();
    }

    public void setProjectStageAssignCommentFormLayoutListener(final ProjectStageAssignCommentFormLayoutListener projectStageAssignCommentFormLayoutListener) {
        mProjectStageAssignCommentFormLayoutListener = projectStageAssignCommentFormLayoutListener;
    }

    public interface ProjectStageAssignCommentFormLayoutListener {
        void onProjectStageAssignCommentFormLayoutSaveMenuClick();
    }
}