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
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.StatusTaskEnum;

public class ProjectActivityListView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityListView;

    protected SwipeRefreshLayout mSrlProjectActivityList;
    protected RecyclerView mRvProjectActivityList;
    protected ProjectActivityListAdapter mProjectActivityListAdapter;

    protected StatusTaskEnum mStatusTaskEnum;

    protected ProjectActivityListListener mProjectActivityListListener;

    public ProjectActivityListView(final Context context) {
        mContext = context;
    }

    public ProjectActivityListView(final Context context, final RelativeLayout projectActivityListView) {
        this(context);

        initializeView(projectActivityListView);
    }

    public static ProjectActivityListView buildProjectActivityListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityListView buildProjectActivityListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityListView(context, R.layout.project_activity_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityListView) {
        mProjectActivityListView = projectActivityListView;

        mSrlProjectActivityList = (SwipeRefreshLayout) mProjectActivityListView.findViewById(R.id.projectActivityListSwipeRefresh);
        mSrlProjectActivityList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mProjectActivityListListener != null)
                    mProjectActivityListListener.onProjectActivityListRequest(mStatusTaskEnum);
            }
        });

        mRvProjectActivityList = (RecyclerView) mProjectActivityListView.findViewById(R.id.projectActivityList);
        mRvProjectActivityList.setItemAnimator(new DefaultItemAnimator());
        mRvProjectActivityList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvProjectActivityList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectActivityModel projectActivityModel = mProjectActivityListAdapter.getItem(position);
                if (projectActivityModel != null) {
                    if (mProjectActivityListListener != null)
                        mProjectActivityListListener.onProjectActivityItemClick(projectActivityModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvProjectActivityList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvProjectActivityList.addItemDecoration(dividerItemDecoration);

        mProjectActivityListAdapter = new ProjectActivityListAdapter();
        mRvProjectActivityList.setAdapter(mProjectActivityListAdapter);
    }

    public void setProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListAdapter.setProjectActivityModels(projectActivityModels);
        mProjectActivityListAdapter.notifyDataSetChanged();
    }

    public void setStatusTaskEnum(final StatusTaskEnum statusTaskEnum) {
        mStatusTaskEnum = statusTaskEnum;
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlProjectActivityList.isRefreshing())
            mSrlProjectActivityList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlProjectActivityList.isRefreshing())
            mSrlProjectActivityList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mProjectActivityListView;
    }

    public void setProjectActivityListListener(final ProjectActivityListListener projectActivityListListener) {
        mProjectActivityListListener = projectActivityListListener;
    }

    public interface ProjectActivityListListener {
        void onProjectActivityListRequest(StatusTaskEnum statusTaskEnum);
        void onProjectActivityItemClick(ProjectActivityModel projectActivityModel);
    }

    protected class ProjectActivityListAdapter extends RecyclerView.Adapter<ProjectActivityListViewHolder> {

        protected ProjectActivityModel[] mProjectActivityModels;

        public ProjectActivityListAdapter() {

        }

        public ProjectActivityListAdapter(final ProjectActivityModel[] projectActivityModels) {
            this();
            mProjectActivityModels = projectActivityModels;
        }

        public void setProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
            mProjectActivityModels = projectActivityModels;

            notifyDataSetChanged();
        }

        public ProjectActivityModel getItem(final int position) {
            if (mProjectActivityModels == null)
                return null;
            if ((position + 1) > mProjectActivityModels.length)
                return null;

            return mProjectActivityModels[position];
        }

        @Override
        public ProjectActivityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_list_item_view, parent, false);
            return new ProjectActivityListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityListViewHolder holder, int position) {
            if (mProjectActivityModels == null)
                return;
            if ((position + 1) > mProjectActivityModels.length)
                return;

            ProjectActivityModel projectActivityModel = mProjectActivityModels[position];
            holder.setProjectActivityModel(projectActivityModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectActivityModels == null)
                return 0;

            return mProjectActivityModels.length;
        }
    }

    protected class ProjectActivityListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtTaskName;

        public ProjectActivityListViewHolder(View view) {
            super(view);

            mEtTaskName = (AppCompatTextView) view.findViewById(R.id.taskName);
        }

        public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
            mEtTaskName.setText(projectActivityModel.getTaskName());
        }
    }
}
