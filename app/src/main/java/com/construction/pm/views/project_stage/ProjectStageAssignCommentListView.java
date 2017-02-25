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
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectStageAssignCommentListView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentListView;

    protected RecyclerView mProjectStageAssignCommentList;
    protected ProjectStageAssignCommentListAdapter mProjectStageAssignCommentListAdapter;

    public ProjectStageAssignCommentListView(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentListView(final Context context, final RelativeLayout projectStageAssignCommentListView) {
        this(context);

        initializeView(projectStageAssignCommentListView);
    }

    public static ProjectStageAssignCommentListView buildProjectStageAssignCommentListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentListView buildProjectStageAssignCommentListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentListView(context, R.layout.project_stage_assign_comment_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageAssignCommentListView) {
        mProjectStageAssignCommentListView = projectStageAssignCommentListView;

        mProjectStageAssignCommentList = (RecyclerView) mProjectStageAssignCommentListView.findViewById(R.id.projectStageAssignCommentList);
        mProjectStageAssignCommentList.setItemAnimator(new DefaultItemAnimator());
        mProjectStageAssignCommentList.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mProjectStageAssignCommentList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mProjectStageAssignCommentList.addItemDecoration(dividerItemDecoration);

        mProjectStageAssignCommentListAdapter = new ProjectStageAssignCommentListAdapter();
        mProjectStageAssignCommentList.setAdapter(mProjectStageAssignCommentListAdapter);
    }

    public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageAssignCommentListAdapter.setProjectStageAssignCommentModels(projectStageAssignCommentModels);
        mProjectStageAssignCommentListAdapter.notifyDataSetChanged();
    }

    public RelativeLayout getView() {
        return mProjectStageAssignCommentListView;
    }

    protected class ProjectStageAssignCommentListAdapter extends RecyclerView.Adapter<ProjectStageAssignCommentListViewHolder> {

        protected ProjectStageAssignCommentModel[] mProjectStageAssignCommentModels;

        public ProjectStageAssignCommentListAdapter() {

        }

        public ProjectStageAssignCommentListAdapter(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            this();
            mProjectStageAssignCommentModels = projectStageAssignCommentModels;
        }

        public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            mProjectStageAssignCommentModels = projectStageAssignCommentModels;

            notifyDataSetChanged();
        }

        public ProjectStageAssignCommentModel getItem(final int position) {
            if (mProjectStageAssignCommentModels == null)
                return null;
            if ((position + 1) > mProjectStageAssignCommentModels.length)
                return null;

            return mProjectStageAssignCommentModels[position];
        }

        @Override
        public ProjectStageAssignCommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_assign_comment_list_item_view, parent, false);
            return new ProjectStageAssignCommentListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageAssignCommentListViewHolder holder, int position) {
            if (mProjectStageAssignCommentModels == null)
                return;
            if ((position + 1) > mProjectStageAssignCommentModels.length)
                return;

            ProjectStageAssignCommentModel projectModel = mProjectStageAssignCommentModels[position];
            holder.setProjectStageAssignCommentModel(projectModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectStageAssignCommentModels == null)
                return 0;

            return mProjectStageAssignCommentModels.length;
        }
    }

    protected class ProjectStageAssignCommentListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mCommentDate;
        protected AppCompatTextView mComment;

        public ProjectStageAssignCommentListViewHolder(View view) {
            super(view);

            mCommentDate = (AppCompatTextView) view.findViewById(R.id.commentDate);
            mComment = (AppCompatTextView) view.findViewById(R.id.comment);
        }

        public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
            mCommentDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectStageAssignCommentModel.getCommentDate()));
            mComment.setText(projectStageAssignCommentModel.getComment());
        }
    }
}
