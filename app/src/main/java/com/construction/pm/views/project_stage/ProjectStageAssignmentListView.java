package com.construction.pm.views.project_stage;

import android.content.Context;
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
import com.construction.pm.models.ProjectStageAssignmentModel;

public class ProjectStageAssignmentListView {

    protected Context mContext;

    protected RelativeLayout mProjectStageAssignmentListView;

    protected RecyclerView mProjectStageAssignmentList;
    protected ProjectStageAssignmentListAdapter mProjectStageAssignmentListAdapter;

    public ProjectStageAssignmentListView(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignmentListView(final Context context, final RelativeLayout projectStageAssignmentListView) {
        this(context);

        initializeView(projectStageAssignmentListView);
    }

    public static ProjectStageAssignmentListView buildProjectStageAssignmentListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignmentListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignmentListView buildProjectStageAssignmentListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignmentListView(context, R.layout.project_stage_assignment_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageAssignmentListView) {
        mProjectStageAssignmentListView = projectStageAssignmentListView;

        mProjectStageAssignmentList = (RecyclerView) mProjectStageAssignmentListView.findViewById(R.id.projectStageAssignmentList);
        mProjectStageAssignmentList.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mProjectStageAssignmentList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        mProjectStageAssignmentList.addItemDecoration(dividerItemDecoration);

        mProjectStageAssignmentListAdapter = new ProjectStageAssignmentListAdapter();
        mProjectStageAssignmentList.setAdapter(mProjectStageAssignmentListAdapter);
    }

    public void setProjectStageAssignmentModels(final ProjectStageAssignmentModel[] projectStageAssignmentModels) {
        mProjectStageAssignmentListAdapter.setProjectStageAssignmentModels(projectStageAssignmentModels);
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        return mProjectStageAssignmentListAdapter.getProjectStageAssignmentModels();
    }

    public RelativeLayout getView() {
        return mProjectStageAssignmentListView;
    }

    protected class ProjectStageAssignmentListAdapter extends RecyclerView.Adapter<ProjectStageAssignmentListViewHolder> {

        protected ProjectStageAssignmentModel[] mProjectStageAssignmentModels;

        public ProjectStageAssignmentListAdapter() {

        }

        public ProjectStageAssignmentListAdapter(final ProjectStageAssignmentModel[] projectStageAssignmentModels) {
            this();
            mProjectStageAssignmentModels = projectStageAssignmentModels;
        }

        public void setProjectStageAssignmentModels(final ProjectStageAssignmentModel[] projectStageAssignmentModels) {
            mProjectStageAssignmentModels = projectStageAssignmentModels;

            notifyDataSetChanged();
        }

        public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
            return mProjectStageAssignmentModels;
        }

        public ProjectStageAssignmentModel getItem(final int position) {
            if (mProjectStageAssignmentModels == null)
                return null;
            if ((position + 1) > mProjectStageAssignmentModels.length)
                return null;

            return mProjectStageAssignmentModels[position];
        }

        @Override
        public ProjectStageAssignmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_assignment_list_item_view, parent, false);
            return new ProjectStageAssignmentListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageAssignmentListViewHolder holder, int position) {
            if (mProjectStageAssignmentModels == null)
                return;
            if ((position + 1) > mProjectStageAssignmentModels.length)
                return;

            ProjectStageAssignmentModel projectStageAssignmentModel = mProjectStageAssignmentModels[position];
            holder.setProjectStageAssignmentModel(projectStageAssignmentModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectStageAssignmentModels == null)
                return 0;

            return mProjectStageAssignmentModels.length;
        }
    }

    protected class ProjectStageAssignmentListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mMemberCode;
        protected AppCompatTextView mMemberName;

        public ProjectStageAssignmentListViewHolder(View view) {
            super(view);

            mMemberCode = (AppCompatTextView) view.findViewById(R.id.memberCode);
            mMemberName = (AppCompatTextView) view.findViewById(R.id.memberName);
        }

        public void setProjectStageAssignmentModel(final ProjectStageAssignmentModel projectStageAssignmentModel) {
            mMemberCode.setText(projectStageAssignmentModel.getMemberCode());
            mMemberName.setText(projectStageAssignmentModel.getMemberName());
        }
    }
}
