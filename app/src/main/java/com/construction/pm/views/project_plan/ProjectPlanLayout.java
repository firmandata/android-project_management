package com.construction.pm.views.project_plan;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectPlanAssignmentModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectPlanLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected CoordinatorLayout mProjectPlanLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;

    protected ProjectPlanDetailView mProjectPlanDetailView;
    protected ProjectPlanAssignmentListView mProjectPlanAssignmentListView;
    protected ProjectActivityUpdateListView mProjectActivityUpdateListView;

    protected ProjectPlanLayoutListener mProjectPlanLayoutListener;

    protected ProjectPlanLayout(final Context context) {
        mContext = context;
    }

    public ProjectPlanLayout(final Context context, final CoordinatorLayout projectPlanLayout) {
        this(context);

        initializeView(projectPlanLayout);
    }

    public static ProjectPlanLayout buildProjectPlanLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectPlanLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectPlanLayout buildProjectPlanLayout(final Context context, final ViewGroup viewGroup) {
        return buildProjectPlanLayout(context, R.layout.project_plan_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout projectLayout) {
        mProjectPlanLayout = projectLayout;
        mAppBarLayout = (AppBarLayout) mProjectPlanLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mProjectPlanLayout.findViewById(R.id.contentToolbar);

        mProjectPlanDetailView = new ProjectPlanDetailView(mContext, (RelativeLayout) mProjectPlanLayout.findViewById(R.id.project_plan_detail_view));
        mProjectPlanAssignmentListView = new ProjectPlanAssignmentListView(mContext, (RelativeLayout) mProjectPlanLayout.findViewById(R.id.project_plan_assignment_list_view));
        mProjectActivityUpdateListView = new ProjectActivityUpdateListView(mContext, (RelativeLayout) mProjectPlanLayout.findViewById(R.id.project_activity_update_list_view));
    }

    public CoordinatorLayout getLayout() {
        return mProjectPlanLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectPlanModel projectPlanModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mProjectPlanLayout);

        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (projectPlanModel != null) {
                actionBar.setTitle(projectPlanModel.getTaskName());
                actionBar.setSubtitle(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanStartDate()));
            } else
                actionBar.setTitle(R.string.project_plan_layout_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        if (mProjectPlanLayoutListener != null)
            mProjectPlanLayoutListener.onProjectPlanRequest(projectPlanModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectPlanModel projectPlanModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        if (mProjectPlanLayoutListener != null)
            mProjectPlanLayoutListener.onProjectPlanRequest(projectPlanModel);
    }

    public void setLayoutData(final ProjectPlanModel projectPlanModel, final ProjectPlanAssignmentModel[] projectPlanAssignmentModels, final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectPlanDetailView.setProjectPlanModel(projectPlanModel);
        mProjectPlanAssignmentListView.setProjectPlanAssignmentModels(projectPlanAssignmentModels);
        mProjectActivityUpdateListView.setProjectActivityUpdateModels(projectActivityUpdateModels);
    }

    public void setProjectPlanLayoutListener(final ProjectPlanLayoutListener projectPlanLayoutListener) {
        mProjectPlanLayoutListener = projectPlanLayoutListener;
    }

    public interface ProjectPlanLayoutListener {
        void onProjectPlanRequest(ProjectPlanModel projectPlanModel);
    }
}
