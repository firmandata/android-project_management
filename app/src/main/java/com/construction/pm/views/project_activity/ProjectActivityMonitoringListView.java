package com.construction.pm.views.project_activity;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.views.listeners.ImageRequestListener;

public class ProjectActivityMonitoringListView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityMonitoringListView;

    protected RecyclerView mProjectActivityMonitoringList;
    protected ProjectActivityMonitoringListAdapter mProjectActivityMonitoringListAdapter;

    protected ProjectActivityModel mProjectActivityModel;

    protected ProjectActivityMonitoringListListener mProjectActivityMonitoringListListener;

    public ProjectActivityMonitoringListView(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringListView(final Context context, final RelativeLayout projectActivityMonitoringListView) {
        this(context);

        initializeView(projectActivityMonitoringListView);
    }

    public static ProjectActivityMonitoringListView buildProjectActivityMonitoringListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringListView buildProjectActivityMonitoringListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringListView(context, R.layout.project_activity_monitoring_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityMonitoringListView) {
        mProjectActivityMonitoringListView = projectActivityMonitoringListView;

        mProjectActivityMonitoringList = (RecyclerView) mProjectActivityMonitoringListView.findViewById(R.id.projectActivityMonitoringList);
        mProjectActivityMonitoringList.setItemAnimator(new DefaultItemAnimator());
        mProjectActivityMonitoringList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectActivityMonitoringList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectActivityMonitoringModel projectActivityMonitoringModel = mProjectActivityMonitoringListAdapter.getItem(position);
                if (projectActivityMonitoringModel != null) {
                    if (mProjectActivityMonitoringListListener != null)
                        mProjectActivityMonitoringListListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mProjectActivityMonitoringList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mProjectActivityMonitoringList.addItemDecoration(dividerItemDecoration);

        mProjectActivityMonitoringListAdapter = new ProjectActivityMonitoringListAdapter();
        mProjectActivityMonitoringList.setAdapter(mProjectActivityMonitoringListAdapter);

        if (mProjectActivityMonitoringListListener != null)
            mProjectActivityMonitoringListListener.onProjectActivityMonitoringListRequest(mProjectActivityModel);
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        mProjectActivityModel = projectActivityModel;
    }

    public void setProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
        mProjectActivityMonitoringListAdapter.setProjectActivityMonitoringModels(projectActivityMonitoringModels);
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mProjectActivityMonitoringListAdapter.setImageRequestListener(imageRequestListener);
    }

    public RelativeLayout getView() {
        return mProjectActivityMonitoringListView;
    }

    public void setProjectActivityMonitoringListListener(final ProjectActivityMonitoringListListener projectActivityMonitoringListListener) {
        mProjectActivityMonitoringListListener = projectActivityMonitoringListListener;
    }

    public interface ProjectActivityMonitoringListListener {
        void onProjectActivityMonitoringListRequest(ProjectActivityModel projectActivityModel);
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }

    protected class ProjectActivityMonitoringListAdapter extends RecyclerView.Adapter<ProjectActivityMonitoringListViewHolder> {

        protected ProjectActivityMonitoringModel[] mProjectActivityMonitoringModels;

        protected ImageRequestListener mImageRequestListener;

        public ProjectActivityMonitoringListAdapter() {

        }

        public ProjectActivityMonitoringListAdapter(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
            this();
            mProjectActivityMonitoringModels = projectActivityMonitoringModels;
        }

        public void setProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
            mProjectActivityMonitoringModels = projectActivityMonitoringModels;

            notifyDataSetChanged();
        }

        public ProjectActivityMonitoringModel getItem(final int position) {
            if (mProjectActivityMonitoringModels == null)
                return null;
            if ((position + 1) > mProjectActivityMonitoringModels.length)
                return null;

            return mProjectActivityMonitoringModels[position];
        }

        @Override
        public ProjectActivityMonitoringListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_monitoring_list_item_view, parent, false);
            return new ProjectActivityMonitoringListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityMonitoringListViewHolder holder, int position) {
            if (mProjectActivityMonitoringModels == null)
                return;
            if ((position + 1) > mProjectActivityMonitoringModels.length)
                return;

            ProjectActivityMonitoringModel projectActivityMonitoringModel = mProjectActivityMonitoringModels[position];
            holder.setImageRequestListener(mImageRequestListener);
            holder.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectActivityMonitoringModels == null)
                return 0;

            return mProjectActivityMonitoringModels.length;
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }

    protected class ProjectActivityMonitoringListViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPhotoId;
        protected AppCompatTextView mMonitoringDate;
        protected AppCompatTextView mActualStartDate;
        protected AppCompatTextView mActualEndDate;
        protected AppCompatTextView mActivityStatus;
        protected AppCompatTextView mPercentComplete;
        protected AppCompatTextView mComment;

        protected ImageRequestListener mImageRequestListener;

        public ProjectActivityMonitoringListViewHolder(View view) {
            super(view);

            mPhotoId = (ImageView) view.findViewById(R.id.photoId);
            mMonitoringDate = (AppCompatTextView) view.findViewById(R.id.monitoringDate);
            mActualStartDate = (AppCompatTextView) view.findViewById(R.id.actualStartDate);
            mActualEndDate = (AppCompatTextView) view.findViewById(R.id.actualEndDate);
            mActivityStatus = (AppCompatTextView) view.findViewById(R.id.activityStatus);
            mPercentComplete = (AppCompatTextView) view.findViewById(R.id.percentComplete);
            mComment = (AppCompatTextView) view.findViewById(R.id.comment);
        }

        public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
            if (projectActivityMonitoringModel.getPhotoId() != null) {
                if (mImageRequestListener != null)
                    mImageRequestListener.onImageRequest(mPhotoId, projectActivityMonitoringModel.getPhotoId());
            }
            mMonitoringDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate()));
            mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualStartDate()));
            mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualEndDate()));
            mActivityStatus.setText(projectActivityMonitoringModel.getActivityStatus());
            mPercentComplete.setText(StringUtil.numberFormat(projectActivityMonitoringModel.getPercentComplete()));
            mComment.setText(projectActivityMonitoringModel.getComment());
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }
}
