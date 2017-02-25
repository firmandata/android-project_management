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
import com.construction.pm.models.ProjectModel;

public class ProjectListView {
    protected Context mContext;

    protected RelativeLayout mProjectListView;

    protected SwipeRefreshLayout mSrlProjectList;
    protected RecyclerView mRvProjectList;
    protected ProjectListAdapter mProjectListAdapter;

    protected ProjectListListener mProjectListListener;

    public ProjectListView(final Context context) {
        mContext = context;
    }

    public ProjectListView(final Context context, final RelativeLayout projectListView) {
        this(context);

        initializeView(projectListView);
    }

    public static ProjectListView buildProjectListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectListView buildProjectListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectListView(context, R.layout.project_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectListView) {
        mProjectListView = projectListView;

        mSrlProjectList = (SwipeRefreshLayout) mProjectListView.findViewById(R.id.projectListSwipeRefresh);
        mSrlProjectList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mProjectListListener != null)
                    mProjectListListener.onProjectListRequest();
            }
        });

        mRvProjectList = (RecyclerView) mProjectListView.findViewById(R.id.projectList);
        mRvProjectList.setItemAnimator(new DefaultItemAnimator());
        mRvProjectList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvProjectList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectModel projectModel = mProjectListAdapter.getItem(position);
                if (projectModel != null) {
                    if (mProjectListListener != null)
                        mProjectListListener.onProjectItemClick(projectModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvProjectList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvProjectList.addItemDecoration(dividerItemDecoration);

        mProjectListAdapter = new ProjectListAdapter();
        mRvProjectList.setAdapter(mProjectListAdapter);

        if (mProjectListListener != null)
            mProjectListListener.onProjectListRequest();
    }

    public void setProjectModels(final ProjectModel[] projectModels) {
        mProjectListAdapter.setProjectModels(projectModels);
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlProjectList.isRefreshing())
            mSrlProjectList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlProjectList.isRefreshing())
            mSrlProjectList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mProjectListView;
    }

    public void setProjectListListener(final ProjectListListener projectListListener) {
        mProjectListListener = projectListListener;
    }

    public interface ProjectListListener {
        void onProjectListRequest();
        void onProjectItemClick(ProjectModel projectModel);
    }

    protected class ProjectListAdapter extends RecyclerView.Adapter<ProjectListViewHolder> {

        protected ProjectModel[] mProjectModels;

        public ProjectListAdapter() {

        }

        public ProjectListAdapter(final ProjectModel[] projectModels) {
            this();
            mProjectModels = projectModels;
        }

        public void setProjectModels(final ProjectModel[] projectModels) {
            mProjectModels = projectModels;

            notifyDataSetChanged();
        }

        public ProjectModel getItem(final int position) {
            if (mProjectModels == null)
                return null;
            if ((position + 1) > mProjectModels.length)
                return null;

            return mProjectModels[position];
        }

        @Override
        public ProjectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item_view, parent, false);
            return new ProjectListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectListViewHolder holder, int position) {
            if (mProjectModels == null)
                return;
            if ((position + 1) > mProjectModels.length)
                return;

            ProjectModel projectModel = mProjectModels[position];
            holder.setProjectModel(projectModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectModels == null)
                return 0;

            return mProjectModels.length;
        }
    }

    protected class ProjectListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtContractNo;
        protected AppCompatTextView mEtProjectName;
        protected AppCompatTextView mEtStageCode;

        public ProjectListViewHolder(View view) {
            super(view);

            mEtContractNo = (AppCompatTextView) view.findViewById(R.id.contractNo);
            mEtProjectName = (AppCompatTextView) view.findViewById(R.id.projectName);
            mEtStageCode = (AppCompatTextView) view.findViewById(R.id.stageCode);
        }

        public void setProjectModel(final ProjectModel projectModel) {
            mEtContractNo.setText(projectModel.getContractNo());
            mEtProjectName.setText(projectModel.getProjectName());
            mEtStageCode.setText(projectModel.getStageCode());
        }
    }
}
