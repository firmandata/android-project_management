package com.construction.pm.views.project_activity;

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
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ProjectActivityUpdateListView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityUpdateListView;

    protected RecyclerView mProjectActivityUpdateList;
    protected ProjectActivityUpdateListAdapter mProjectActivityUpdateListAdapter;

    protected ProjectActivityModel mProjectActivityModel;

    protected ProjectActivityUpdateListListener mProjectActivityUpdateListListener;

    public ProjectActivityUpdateListView(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateListView(final Context context, final RelativeLayout projectActivityUpdateListView) {
        this(context);

        initializeView(projectActivityUpdateListView);
    }

    public static ProjectActivityUpdateListView buildProjectActivityUpdateListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateListView buildProjectActivityUpdateListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateListView(context, R.layout.project_activity_update_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityUpdateListView) {
        mProjectActivityUpdateListView = projectActivityUpdateListView;

        mProjectActivityUpdateList = (RecyclerView) mProjectActivityUpdateListView.findViewById(R.id.projectActivityUpdateList);
        mProjectActivityUpdateList.setItemAnimator(new DefaultItemAnimator());
        mProjectActivityUpdateList.setNestedScrollingEnabled(false);
        mProjectActivityUpdateList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectActivityUpdateList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectActivityUpdateModel projectActivityUpdateModel = mProjectActivityUpdateListAdapter.getItem(position);
                if (projectActivityUpdateModel != null) {
                    if (mProjectActivityUpdateListListener != null)
                        mProjectActivityUpdateListListener.onProjectActivityUpdateListItemClick(projectActivityUpdateModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mProjectActivityUpdateList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mProjectActivityUpdateList.addItemDecoration(dividerItemDecoration);

        mProjectActivityUpdateListAdapter = new ProjectActivityUpdateListAdapter();
        mProjectActivityUpdateList.setAdapter(mProjectActivityUpdateListAdapter);

        if (mProjectActivityUpdateListListener != null)
            mProjectActivityUpdateListListener.onProjectActivityUpdateListRequest(mProjectActivityModel);
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mProjectActivityModel = projectActivityModel;
    }

    public void setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectActivityUpdateListAdapter.setProjectActivityUpdateModels(projectActivityUpdateModels);
    }

    public RelativeLayout getView() {
        return mProjectActivityUpdateListView;
    }

    public void setProjectActivityUpdateListListener(final ProjectActivityUpdateListListener projectActivityUpdateListListener) {
        mProjectActivityUpdateListListener = projectActivityUpdateListListener;
    }

    public interface ProjectActivityUpdateListListener {
        void onProjectActivityUpdateListRequest(ProjectActivityModel projectActivityModel);
        void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel);
    }

    protected class ProjectActivityUpdateListAdapter extends RecyclerView.Adapter<ProjectActivityUpdateListViewHolder> {

        protected ProjectActivityUpdateModel[] mProjectActivityUpdateModels;

        public ProjectActivityUpdateListAdapter() {

        }

        public ProjectActivityUpdateListAdapter(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            this();
            mProjectActivityUpdateModels = projectActivityUpdateModels;
        }

        public void setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            mProjectActivityUpdateModels = projectActivityUpdateModels;

            notifyDataSetChanged();
        }

        public ProjectActivityUpdateModel getItem(final int position) {
            if (mProjectActivityUpdateModels == null)
                return null;
            if ((position + 1) > mProjectActivityUpdateModels.length)
                return null;

            return mProjectActivityUpdateModels[position];
        }

        @Override
        public ProjectActivityUpdateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_update_list_item_view, parent, false);
            return new ProjectActivityUpdateListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityUpdateListViewHolder holder, int position) {
            if (mProjectActivityUpdateModels == null)
                return;
            if ((position + 1) > mProjectActivityUpdateModels.length)
                return;

            ProjectActivityUpdateModel projectActivityUpdateModel = mProjectActivityUpdateModels[position];
            holder.setProjectActivityUpdateModel(projectActivityUpdateModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectActivityUpdateModels == null)
                return 0;

            return mProjectActivityUpdateModels.length;
        }
    }

    protected class ProjectActivityUpdateListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mUpdateDate;
        protected AppCompatTextView mActualStartDate;
        protected AppCompatTextView mActualEndDate;
        protected AppCompatTextView mActivityStatus;
        protected AppCompatTextView mPercentComplete;
        protected AppCompatTextView mComment;

        public ProjectActivityUpdateListViewHolder(View view) {
            super(view);

            mUpdateDate = (AppCompatTextView) view.findViewById(R.id.updateDate);
            mActualStartDate = (AppCompatTextView) view.findViewById(R.id.actualStartDate);
            mActualEndDate = (AppCompatTextView) view.findViewById(R.id.actualEndDate);
            mActivityStatus = (AppCompatTextView) view.findViewById(R.id.activityStatus);
            mPercentComplete = (AppCompatTextView) view.findViewById(R.id.percentComplete);
            mComment = (AppCompatTextView) view.findViewById(R.id.comment);
        }

        public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
            mUpdateDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityUpdateModel.getUpdateDate()));
            mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualStartDate()));
            mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualEndDate()));
            mActivityStatus.setText(projectActivityUpdateModel.getActivityStatus());
            mPercentComplete.setText(StringUtil.numberFormat(projectActivityUpdateModel.getPercentComplete()));
            mComment.setText(projectActivityUpdateModel.getComment());
        }
    }
}
