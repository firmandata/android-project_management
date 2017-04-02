package com.construction.pm.views.project_plan;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectPlanListView {
    protected Context mContext;

    protected RelativeLayout mProjectPlanListView;

    protected SwipeRefreshLayout mSrlProjectPlanList;
    protected RecyclerView mRvProjectPlanList;
    protected ProjectPlanListAdapter mProjectPlanListAdapter;

    protected ProjectPlanListListener mProjectPlanListListener;

    public ProjectPlanListView(final Context context) {
        mContext = context;
    }

    public ProjectPlanListView(final Context context, final RelativeLayout projectPlanListView) {
        this(context);

        initializeView(projectPlanListView);
    }

    public static ProjectPlanListView buildProjectPlanListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectPlanListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectPlanListView buildProjectPlanListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectPlanListView(context, R.layout.project_plan_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectPlanListView) {
        mProjectPlanListView = projectPlanListView;

        mSrlProjectPlanList = (SwipeRefreshLayout) mProjectPlanListView.findViewById(R.id.projectPlanListSwipeRefresh);
        mSrlProjectPlanList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mProjectPlanListListener != null)
                    mProjectPlanListListener.onProjectPlanListRequest();
            }
        });

        mRvProjectPlanList = (RecyclerView) mProjectPlanListView.findViewById(R.id.projectPlanList);
        mRvProjectPlanList.setItemAnimator(new DefaultItemAnimator());
        mRvProjectPlanList.setHasFixedSize(true);
        mRvProjectPlanList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvProjectPlanList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectPlanModel projectPlanModel = mProjectPlanListAdapter.getItem(position);
                if (projectPlanModel != null) {
                    if (mProjectPlanListListener != null)
                        mProjectPlanListListener.onProjectPlanItemClick(projectPlanModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvProjectPlanList.setLayoutManager(layoutManager);

        mProjectPlanListAdapter = new ProjectPlanListAdapter();
        mRvProjectPlanList.setAdapter(mProjectPlanListAdapter);

        if (mProjectPlanListListener != null)
            mProjectPlanListListener.onProjectPlanListRequest();
    }

    public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
        mProjectPlanListAdapter.setProjectPlanModels(projectPlanModels);
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlProjectPlanList.isRefreshing())
            mSrlProjectPlanList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlProjectPlanList.isRefreshing())
            mSrlProjectPlanList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mProjectPlanListView;
    }

    public void setProjectPlanListListener(final ProjectPlanListListener projectPlanListListener) {
        mProjectPlanListListener = projectPlanListListener;
    }

    public interface ProjectPlanListListener {
        void onProjectPlanListRequest();
        void onProjectPlanItemClick(ProjectPlanModel projectPlanModel);
    }

    protected class ProjectPlanListAdapter extends RecyclerView.Adapter<ProjectPlanListViewHolder> {

        protected ProjectPlanModelView[] mProjectPlanModelViews;

        public ProjectPlanListAdapter() {

        }

        public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
            mProjectPlanModelViews = getProjectPlanModelViews(projectPlanModels);
            notifyDataSetChanged();
        }

        public ProjectPlanModel getItem(final int position) {
            if (mProjectPlanModelViews == null)
                return null;
            if ((position + 1) > mProjectPlanModelViews.length)
                return null;

            return mProjectPlanModelViews[position].getProjectPlanModel();
        }

        @Override
        public ProjectPlanListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_plan_list_item_view, parent, false);
            return new ProjectPlanListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectPlanListViewHolder holder, int position) {
            if (mProjectPlanModelViews == null)
                return;
            if ((position + 1) > mProjectPlanModelViews.length)
                return;

            ProjectPlanModelView projectPlanModelView = mProjectPlanModelViews[position];
            holder.setProjectPlanModelView(projectPlanModelView);
        }

        @Override
        public int getItemCount() {
            if (mProjectPlanModelViews == null)
                return 0;

            return mProjectPlanModelViews.length;
        }

        protected ProjectPlanModelView[] getProjectPlanModelViews(final ProjectPlanModel[] projectPlanModels) {
            ProjectPlanModelView[] projectPlanModelViews = buildHierarchyProjectPlanModelViews(projectPlanModels, null);
            return extractProjectPlanModelViews(projectPlanModelViews);
        }

        protected ProjectPlanModelView[] extractProjectPlanModelViews(final ProjectPlanModelView[] projectPlanModelViews) {
            List<ProjectPlanModelView> projectPlanModelViewList = new ArrayList<ProjectPlanModelView>();

            for (ProjectPlanModelView projectPlanModelView : projectPlanModelViews) {
                projectPlanModelViewList.add(projectPlanModelView);
                if (projectPlanModelView.getChilds() != null) {
                    ProjectPlanModelView[] projectPlanModelViewChilds = extractProjectPlanModelViews(projectPlanModelView.getChilds());
                    if (projectPlanModelViewChilds != null) {
                        for (ProjectPlanModelView projectPlanModelViewChild : projectPlanModelViewChilds) {
                            projectPlanModelViewList.add(projectPlanModelViewChild);
                        }
                    }
                }
            }

            ProjectPlanModelView[] newProjectPlanModelViews = new ProjectPlanModelView[projectPlanModelViewList.size()];
            projectPlanModelViewList.toArray(newProjectPlanModelViews);
            return newProjectPlanModelViews;
        }

        protected ProjectPlanModelView[] buildHierarchyProjectPlanModelViews(final ProjectPlanModel[] projectPlanModels, final ProjectPlanModelView projectPlanModelViewParent) {
            List<ProjectPlanModelView> projectPlanModelViewList = new ArrayList<ProjectPlanModelView>();

            Integer projectPlanIdParentRef = null;
            if (projectPlanModelViewParent != null)
                projectPlanIdParentRef = projectPlanModelViewParent.getProjectPlanModel().getProjectPlanId();

            for (ProjectPlanModel projectPlanModel : projectPlanModels) {
                ProjectPlanModelView projectPlanModelView = null;

                Integer parentProjectPlanId = projectPlanModel.getParentProjectPlanId();
                if (projectPlanIdParentRef == null && parentProjectPlanId == null) {
                    projectPlanModelView = new ProjectPlanModelView(projectPlanModel);
                } else if (projectPlanIdParentRef != null && parentProjectPlanId != null) {
                    if (parentProjectPlanId.equals(projectPlanIdParentRef)) {
                        projectPlanModelView = new ProjectPlanModelView(projectPlanModel);
                    }
                }

                if (projectPlanModelView != null) {
                    projectPlanModelView.setParent(projectPlanModelViewParent);
                    projectPlanModelView.setChilds(buildHierarchyProjectPlanModelViews(projectPlanModels, projectPlanModelView));
                    projectPlanModelViewList.add(projectPlanModelView);
                }
            }

            ProjectPlanModelView[] projectPlanModelViews = new ProjectPlanModelView[projectPlanModelViewList.size()];
            projectPlanModelViewList.toArray(projectPlanModelViews);
            return projectPlanModelViews;
        }
    }

    protected class ProjectPlanModelView {

        protected ProjectPlanModelView mParent;
        protected ProjectPlanModel mProjectPlanModel;
        protected List<ProjectPlanModelView> mChildList;

        public ProjectPlanModelView(final ProjectPlanModel projectPlanModel) {
            mProjectPlanModel = projectPlanModel;
            mChildList = new ArrayList<ProjectPlanModelView>();
        }

        public ProjectPlanModel getProjectPlanModel() {
            return mProjectPlanModel;
        }

        public void setParent(final ProjectPlanModelView parent) {
            mParent = parent;
        }

        public ProjectPlanModelView getParent() {
            return mParent;
        }

        public int getParentSize() {
            if (mParent == null)
                return 0;
            return mParent.getParentSize() + 1;
        }

        public void setChilds(final ProjectPlanModelView[] childs) {
            mChildList = new ArrayList<ProjectPlanModelView>(Arrays.asList(childs));
        }

        public void addChild(final ProjectPlanModelView child) {
            mChildList.add(child);
        }

        public ProjectPlanModelView[] getChilds() {
            ProjectPlanModelView[] childs = new ProjectPlanModelView[mChildList.size()];
            mChildList.toArray(childs);
            return childs;
        }
    }

    protected class ProjectPlanListViewHolder extends RecyclerView.ViewHolder {

        protected CardView mContentBody;
        protected AppCompatTextView mEtTaskName;
        protected AppCompatTextView mEtPlanStartDate;
        protected AppCompatTextView mEtPlanEndDate;
        protected AppCompatTextView mEtTaskWeightPercentage;
        protected AppCompatTextView mEtRealizationStartDate;
        protected AppCompatTextView mEtRealizationEndDate;
        protected AppCompatTextView mEtRealizationStatus;
        protected AppCompatTextView mEtPercentComplete;

        public ProjectPlanListViewHolder(View view) {
            super(view);

            mContentBody = (CardView) view.findViewById(R.id.contentBody);
            mEtTaskName = (AppCompatTextView) view.findViewById(R.id.taskName);
            mEtPlanStartDate = (AppCompatTextView) view.findViewById(R.id.planStartDate);
            mEtPlanEndDate = (AppCompatTextView) view.findViewById(R.id.planEndDate);
            mEtTaskWeightPercentage = (AppCompatTextView) view.findViewById(R.id.taskWeightPercentage);
            mEtRealizationStartDate = (AppCompatTextView) view.findViewById(R.id.realizationStartDate);
            mEtRealizationEndDate = (AppCompatTextView) view.findViewById(R.id.realizationEndDate);
            mEtRealizationStatus = (AppCompatTextView) view.findViewById(R.id.realizationStatus);
            mEtPercentComplete = (AppCompatTextView) view.findViewById(R.id.percentComplete);

            int margin = (int) mContentBody.getCardElevation();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentBody.getLayoutParams();
            layoutParams.setMargins(margin, margin, margin, margin);
        }

        public void setProjectPlanModelView(final ProjectPlanModelView projectPlanModelView) {
            ProjectPlanModel projectPlanModel = projectPlanModelView.getProjectPlanModel();
            if (projectPlanModel == null)
                return;

            mEtTaskName.setText(projectPlanModel.getTaskName());
            mEtPlanStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanStartDate()));
            mEtPlanEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanEndDate()));
            mEtTaskWeightPercentage.setText(StringUtil.numberPercentFormat(projectPlanModel.getTaskWeightPercentage()));
            mEtRealizationStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationStartDate()));
            mEtRealizationEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationEndDate()));
            mEtRealizationStatus.setText(projectPlanModel.getRealizationStatus());
            mEtPercentComplete.setText(StringUtil.numberPercentFormat(projectPlanModel.getPercentComplete()));

            Context context = getView().getContext();

            int margin = (int) mContentBody.getCardElevation(); //(int) context.getResources().getDimension(R.dimen.gap);
            int marginLeft = ViewUtil.getPxFromDp(context, projectPlanModelView.getParentSize() * 10) + margin;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentBody.getLayoutParams();
            layoutParams.setMargins(marginLeft, margin, margin, margin);
        }
    }
}
