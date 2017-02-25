package com.construction.pm.views.project_plan;

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
import com.construction.pm.models.ProjectPlanAssignmentModel;

public class ProjectPlanAssignmentListView {

    protected Context mContext;

    protected RelativeLayout mProjectPlanAssignmentListView;

    protected RecyclerView mProjectPlanAssignmentList;
    protected ProjectPlanAssignmentListAdapter mProjectPlanAssignmentListAdapter;

    public ProjectPlanAssignmentListView(final Context context) {
        mContext = context;
    }

    public ProjectPlanAssignmentListView(final Context context, final RelativeLayout projectPlanAssignmentListView) {
        this(context);

        initializeView(projectPlanAssignmentListView);
    }

    public static ProjectPlanAssignmentListView buildProjectPlanAssignmentListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectPlanAssignmentListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectPlanAssignmentListView buildProjectPlanAssignmentListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectPlanAssignmentListView(context, R.layout.project_plan_assignment_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectPlanAssignmentListView) {
        mProjectPlanAssignmentListView = projectPlanAssignmentListView;

        mProjectPlanAssignmentList = (RecyclerView) mProjectPlanAssignmentListView.findViewById(R.id.projectPlanAssignmentList);
        mProjectPlanAssignmentList.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mProjectPlanAssignmentList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        mProjectPlanAssignmentList.addItemDecoration(dividerItemDecoration);

        mProjectPlanAssignmentListAdapter = new ProjectPlanAssignmentListAdapter();
        mProjectPlanAssignmentList.setAdapter(mProjectPlanAssignmentListAdapter);
    }

    public void setProjectPlanAssignmentModels(final ProjectPlanAssignmentModel[] projectPlanAssignmentModels) {
        mProjectPlanAssignmentListAdapter.setProjectPlanAssignmentModels(projectPlanAssignmentModels);
        mProjectPlanAssignmentListAdapter.notifyDataSetChanged();
    }

    public RelativeLayout getView() {
        return mProjectPlanAssignmentListView;
    }

    protected class ProjectPlanAssignmentListAdapter extends RecyclerView.Adapter<ProjectPlanAssignmentListViewHolder> {

        protected ProjectPlanAssignmentModel[] mProjectPlanAssignmentModels;

        public ProjectPlanAssignmentListAdapter() {

        }

        public ProjectPlanAssignmentListAdapter(final ProjectPlanAssignmentModel[] projectPlanAssignmentModels) {
            this();
            mProjectPlanAssignmentModels = projectPlanAssignmentModels;
        }

        public void setProjectPlanAssignmentModels(final ProjectPlanAssignmentModel[] projectPlanAssignmentModels) {
            mProjectPlanAssignmentModels = projectPlanAssignmentModels;

            notifyDataSetChanged();
        }

        public ProjectPlanAssignmentModel getItem(final int position) {
            if (mProjectPlanAssignmentModels == null)
                return null;
            if ((position + 1) > mProjectPlanAssignmentModels.length)
                return null;

            return mProjectPlanAssignmentModels[position];
        }

        @Override
        public ProjectPlanAssignmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_plan_assignment_list_item_view, parent, false);
            return new ProjectPlanAssignmentListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectPlanAssignmentListViewHolder holder, int position) {
            if (mProjectPlanAssignmentModels == null)
                return;
            if ((position + 1) > mProjectPlanAssignmentModels.length)
                return;

            ProjectPlanAssignmentModel projectModel = mProjectPlanAssignmentModels[position];
            holder.setProjectPlanAssignmentModel(projectModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectPlanAssignmentModels == null)
                return 0;

            return mProjectPlanAssignmentModels.length;
        }
    }

    protected class ProjectPlanAssignmentListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mMemberCode;
        protected AppCompatTextView mMemberName;

        public ProjectPlanAssignmentListViewHolder(View view) {
            super(view);

            mMemberCode = (AppCompatTextView) view.findViewById(R.id.memberCode);
            mMemberName = (AppCompatTextView) view.findViewById(R.id.memberName);
        }

        public void setProjectPlanAssignmentModel(final ProjectPlanAssignmentModel projectPlanAssignmentModel) {
            mMemberCode.setText(projectPlanAssignmentModel.getMemberCode());
            mMemberName.setText(projectPlanAssignmentModel.getMemberName());
        }
    }
}
