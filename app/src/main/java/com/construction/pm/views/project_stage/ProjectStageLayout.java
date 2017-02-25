package com.construction.pm.views.project_stage;

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
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectStageLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected CoordinatorLayout mProjectStageLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;

    protected ProjectStageDetailView mProjectStageDetailView;
    protected ProjectStageAssignmentListView mProjectStageAssignmentListView;
    protected ProjectStageAssignCommentListView mProjectStageAssignCommentListView;

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

        mProjectStageDetailView = new ProjectStageDetailView(mContext, (RelativeLayout) mProjectStageLayout.findViewById(R.id.project_stage_detail_view));
        mProjectStageAssignmentListView = new ProjectStageAssignmentListView(mContext, (RelativeLayout) mProjectStageLayout.findViewById(R.id.project_stage_assignment_list_view));
        mProjectStageAssignCommentListView = new ProjectStageAssignCommentListView(mContext, (RelativeLayout) mProjectStageLayout.findViewById(R.id.project_stage_assign_comment_list_view));
    }

    public CoordinatorLayout getLayout() {
        return mProjectStageLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity, final ProjectStageModel projectStageModel) {
        mFragmentHandler = new Handler();
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

        if (mProjectStageLayoutListener != null)
            mProjectStageLayoutListener.onProjectStageRequest(projectStageModel);
    }

    public void loadLayoutToFragment(final Fragment fragment, final ProjectStageModel projectStageModel) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);

        if (mProjectStageLayoutListener != null)
            mProjectStageLayoutListener.onProjectStageRequest(projectStageModel);
    }

    public void setLayoutData(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageDetailView.setProjectModel(projectStageModel);
        mProjectStageAssignmentListView.setProjectStageAssignmentModels(projectStageAssignmentModels);
        mProjectStageAssignCommentListView.setProjectStageAssignCommentModels(projectStageAssignCommentModels);
    }

    public void setProjectStageLayoutListener(final ProjectStageLayoutListener projectStageLayoutListener) {
        mProjectStageLayoutListener = projectStageLayoutListener;
    }

    public interface ProjectStageLayoutListener {
        void onProjectStageRequest(ProjectStageModel projectStageModel);
    }
}
