package com.construction.pm.views.project_activity;

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
import com.construction.pm.activities.fragments.ProjectActivityMonitoringFormFragment;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

public class ProjectActivityMonitoringFormLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_FORM = "FRAGMENT_PROJECT_ACTIVITY_MONITORING_FORM";

    protected ProjectActivityMonitoringFormFragment mProjectActivityMonitoringFormFragment;

    protected CoordinatorLayout mProjectActivityMonitoringFormLayout;
    protected ProgressDialog mProgressDialog;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected ProjectActivityMonitoringFormLayoutListener mProjectActivityMonitoringFormLayoutListener;

    protected ProjectActivityMonitoringFormLayout(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringFormLayout(final Context context, final CoordinatorLayout projectActivityMonitoringFormLayout) {
        this(context);

        initializeView(projectActivityMonitoringFormLayout);
    }

    public static ProjectActivityMonitoringFormLayout buildProjectActivityMonitoringFormLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringFormLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringFormLayout buildProjectActivityMonitoringFormLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringFormLayout(context, R.layout.project_activity_monitoring_form_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectActivityMonitoringFormLayout) {
        mProjectActivityMonitoringFormLayout = projectActivityMonitoringFormLayout;
        mAppBarLayout = (AppBarLayout) mProjectActivityMonitoringFormLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectActivityMonitoringFormLayout.findViewById(R.id.contentToolbar);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public CoordinatorLayout getLayout() {
        return mProjectActivityMonitoringFormLayout;
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
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_form_layout_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_form_layout_alert_title_error), errorMessage, R.drawable.ic_error_dark_24, null);
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectActivityMonitoringFormLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.project_activity_monitoring_form_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void createProjectActivityMonitoringSaveMenu(final Menu menu) {
        MenuItem menuItemMonitoringActivity = menu.add(R.string.project_activity_monitoring_form_layout_menu_monitoring_activity);
        menuItemMonitoringActivity.setIcon(R.drawable.ic_checkmark_light_24);
        if (Build.VERSION.SDK_INT > 10) {
            menuItemMonitoringActivity.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menuItemMonitoringActivity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mProjectActivityMonitoringFormLayoutListener != null)
                    mProjectActivityMonitoringFormLayoutListener.onProjectActivityMonitoringFormLayoutSaveMenuClick();
                return true;
            }
        });
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isProjectActivityMonitoringFormFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_FORM);
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

    public ProjectActivityMonitoringFormFragment showProjectActivityMonitoringFormFragment(final ProjectActivityModel projectActivityModel, final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringFormFragment = ProjectActivityMonitoringFormFragment.newInstance(projectActivityModel, projectActivityMonitoringModel);

        String subTitle = null;
        if (projectActivityMonitoringModel != null) {
            if (projectActivityMonitoringModel.getMonitoringDate() != null)
                subTitle = DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate());
        }

        loadFragment(mProjectActivityMonitoringFormFragment, null, subTitle, FRAGMENT_TAG_PROJECT_ACTIVITY_MONITORING_FORM);

        return mProjectActivityMonitoringFormFragment;
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        if (mProjectActivityMonitoringFormFragment == null)
            return null;

        return mProjectActivityMonitoringFormFragment.getProjectActivityMonitoringModel();
    }

    public WebApiParam.WebApiParamFile getPhoto(final int position) {
        if (mProjectActivityMonitoringFormFragment == null)
            return null;

        return mProjectActivityMonitoringFormFragment.getPhoto(position);
    }

    public void setProjectActivityMonitoringFormLayoutListener(final ProjectActivityMonitoringFormLayoutListener projectActivityMonitoringFormLayoutListener) {
        mProjectActivityMonitoringFormLayoutListener = projectActivityMonitoringFormLayoutListener;
    }

    public interface ProjectActivityMonitoringFormLayoutListener {
        void onProjectActivityMonitoringFormLayoutSaveMenuClick();
    }
}
