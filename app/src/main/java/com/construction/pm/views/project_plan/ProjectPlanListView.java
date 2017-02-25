package com.construction.pm.views.project_plan;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvProjectPlanList.addItemDecoration(dividerItemDecoration);

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

        protected ProjectPlanModel[] mProjectPlanModels;

        public ProjectPlanListAdapter() {

        }

        public ProjectPlanListAdapter(final ProjectPlanModel[] projectPlanModels) {
            this();
            mProjectPlanModels = projectPlanModels;
        }

        public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
            mProjectPlanModels = projectPlanModels;

            notifyDataSetChanged();
        }

        public ProjectPlanModel getItem(final int position) {
            if (mProjectPlanModels == null)
                return null;
            if ((position + 1) > mProjectPlanModels.length)
                return null;

            return mProjectPlanModels[position];
        }

        @Override
        public ProjectPlanListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_plan_list_item_view, parent, false);
            return new ProjectPlanListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectPlanListViewHolder holder, int position) {
            if (mProjectPlanModels == null)
                return;
            if ((position + 1) > mProjectPlanModels.length)
                return;

            ProjectPlanModel projectPlanModel = mProjectPlanModels[position];
            holder.setProjectPlanModel(projectPlanModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectPlanModels == null)
                return 0;

            return mProjectPlanModels.length;
        }
    }

    protected class ProjectPlanListViewHolder extends RecyclerView.ViewHolder {

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

            mEtTaskName = (AppCompatTextView) view.findViewById(R.id.taskName);
            mEtPlanStartDate = (AppCompatTextView) view.findViewById(R.id.planStartDate);
            mEtPlanEndDate = (AppCompatTextView) view.findViewById(R.id.planEndDate);
            mEtTaskWeightPercentage = (AppCompatTextView) view.findViewById(R.id.taskWeightPercentage);
            mEtRealizationStartDate = (AppCompatTextView) view.findViewById(R.id.realizationStartDate);
            mEtRealizationEndDate = (AppCompatTextView) view.findViewById(R.id.realizationEndDate);
            mEtRealizationStatus = (AppCompatTextView) view.findViewById(R.id.realizationStatus);
            mEtPercentComplete = (AppCompatTextView) view.findViewById(R.id.percentComplete);
        }

        public void setProjectPlanModel(final ProjectPlanModel projectPlanModel) {
            mEtTaskName.setText(projectPlanModel.getTaskName());
            mEtPlanStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanStartDate()));
            mEtPlanEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getPlanEndDate()));
            mEtTaskWeightPercentage.setText(StringUtil.numberFormat(projectPlanModel.getTaskWeightPercentage()));
            mEtRealizationStartDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationStartDate()));
            mEtRealizationEndDate.setText(DateTimeUtil.ToDateDisplayString(projectPlanModel.getRealizationEndDate()));
            mEtRealizationStatus.setText(projectPlanModel.getRealizationStatus());
            mEtPercentComplete.setText(StringUtil.numberFormat(projectPlanModel.getPercentComplete()));
        }
    }
}
