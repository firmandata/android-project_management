package com.construction.pm.views.project;

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
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectStageListView {
    protected Context mContext;

    protected RelativeLayout mProjectStageListView;

    protected SwipeRefreshLayout mSrlProjectStageList;
    protected RecyclerView mRvProjectStageList;
    protected ProjectStageListAdapter mProjectStageListAdapter;

    protected ProjectStageListListener mProjectStageListListener;

    public ProjectStageListView(final Context context) {
        mContext = context;
    }

    public ProjectStageListView(final Context context, final RelativeLayout projectStageListView) {
        this(context);

        initializeView(projectStageListView);
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageListView(context, R.layout.project_stage_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageListView) {
        mProjectStageListView = projectStageListView;

        mSrlProjectStageList = (SwipeRefreshLayout) mProjectStageListView.findViewById(R.id.projectStageListSwipeRefresh);
        mSrlProjectStageList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mProjectStageListListener != null)
                    mProjectStageListListener.onProjectStageListRequest();
            }
        });

        mRvProjectStageList = (RecyclerView) mProjectStageListView.findViewById(R.id.projectStageList);
        mRvProjectStageList.setItemAnimator(new DefaultItemAnimator());
        mRvProjectStageList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvProjectStageList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectStageModel projectStageModel = mProjectStageListAdapter.getItem(position);
                if (projectStageModel != null) {
                    if (mProjectStageListListener != null)
                        mProjectStageListListener.onProjectStageItemClick(projectStageModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvProjectStageList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvProjectStageList.addItemDecoration(dividerItemDecoration);

        mProjectStageListAdapter = new ProjectStageListAdapter();
        mRvProjectStageList.setAdapter(mProjectStageListAdapter);

        if (mProjectStageListListener != null)
            mProjectStageListListener.onProjectStageListRequest();
    }

    public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
        mProjectStageListAdapter.setProjectStageModels(projectStageModels);
        mProjectStageListAdapter.notifyDataSetChanged();
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlProjectStageList.isRefreshing())
            mSrlProjectStageList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlProjectStageList.isRefreshing())
            mSrlProjectStageList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mProjectStageListView;
    }

    public void setProjectStageListListener(final ProjectStageListListener projectStageListListener) {
        mProjectStageListListener = projectStageListListener;
    }

    public interface ProjectStageListListener {
        void onProjectStageListRequest();
        void onProjectStageItemClick(ProjectStageModel projectStageModel);
    }

    protected class ProjectStageListAdapter extends RecyclerView.Adapter<ProjectStageListViewHolder> {

        protected ProjectStageModel[] mProjectStageModels;

        public ProjectStageListAdapter() {

        }

        public ProjectStageListAdapter(final ProjectStageModel[] projectStageModels) {
            this();
            mProjectStageModels = projectStageModels;
        }

        public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
            mProjectStageModels = projectStageModels;

            notifyDataSetChanged();
        }

        public ProjectStageModel getItem(final int position) {
            if (mProjectStageModels == null)
                return null;
            if ((position + 1) > mProjectStageModels.length)
                return null;

            return mProjectStageModels[position];
        }

        @Override
        public ProjectStageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_list_item_view, parent, false);
            return new ProjectStageListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageListViewHolder holder, int position) {
            if (mProjectStageModels == null)
                return;
            if ((position + 1) > mProjectStageModels.length)
                return;

            ProjectStageModel projectStageModel = mProjectStageModels[position];
            holder.setProjectStageModel(projectStageModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectStageModels == null)
                return 0;

            return mProjectStageModels.length;
        }
    }

    protected class ProjectStageListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtStageDate;
        protected AppCompatTextView mEtStageCode;
        protected AppCompatTextView mEtStageNextCode;
        protected AppCompatTextView mEtStageNextPlanDate;
        protected AppCompatTextView mEtStageNextLocation;
        protected AppCompatTextView mEtStageNextSubject;
        protected AppCompatTextView mEtStageNextMessage;

        public ProjectStageListViewHolder(View view) {
            super(view);

            mEtStageDate = (AppCompatTextView) view.findViewById(R.id.stageDate);
            mEtStageCode = (AppCompatTextView) view.findViewById(R.id.stageCode);
            mEtStageNextCode = (AppCompatTextView) view.findViewById(R.id.stageNextCode);
            mEtStageNextPlanDate = (AppCompatTextView) view.findViewById(R.id.stageNextPlanDate);
            mEtStageNextLocation = (AppCompatTextView) view.findViewById(R.id.stageNextLocation);
            mEtStageNextSubject = (AppCompatTextView) view.findViewById(R.id.stageNextSubject);
            mEtStageNextMessage = (AppCompatTextView) view.findViewById(R.id.stageNextMessage);
        }

        public void setProjectStageModel(final ProjectStageModel projectStageModel) {
            mEtStageDate.setText(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageDate()));
            mEtStageCode.setText(projectStageModel.getStageCode());
            mEtStageNextCode.setText(projectStageModel.getStageNextCode());
            mEtStageNextPlanDate.setText(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageNextPlanDate()));
            mEtStageNextLocation.setText(projectStageModel.getStageNextLocation());
            mEtStageNextSubject.setText(projectStageModel.getStageNextSubject());
            mEtStageNextMessage.setText(projectStageModel.getStageNextMessage());
        }
    }
}
