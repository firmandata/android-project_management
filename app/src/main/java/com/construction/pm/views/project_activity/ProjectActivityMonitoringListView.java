package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        mProjectActivityMonitoringList.setNestedScrollingEnabled(false);
        mProjectActivityMonitoringList.setHasFixedSize(true);
        mProjectActivityMonitoringList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectActivityMonitoringList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectActivityMonitoringModel projectActivityMonitoringModel = mProjectActivityMonitoringListAdapter.getProjectActivityMonitoringModel(position);
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

    public void addProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityMonitoringListAdapter.addProjectActivityMonitoringModels(new ProjectActivityMonitoringModel[] { projectActivityMonitoringModel });
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

        protected List<ProjectActivityMonitoringModel> mProjectActivityMonitoringModelList;

        protected ImageRequestListener mImageRequestListener;

        public ProjectActivityMonitoringListAdapter() {
            mProjectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>();
        }

        public ProjectActivityMonitoringListAdapter(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
            this();
            mProjectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>(Arrays.asList(projectActivityMonitoringModels));
        }

        public void setProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
            if (projectActivityMonitoringModels != null)
                mProjectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>(Arrays.asList(projectActivityMonitoringModels));
            else
                mProjectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>();
            notifyDataSetChanged();
        }

        public void addProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
            List<ProjectActivityMonitoringModel> newProjectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>();
            for (ProjectActivityMonitoringModel newProjectActivityMonitoringModel : projectActivityMonitoringModels) {
                int position = getPosition(newProjectActivityMonitoringModel);
                if (position >= 0) {
                    // -- replace item --
                    setProjectActivityMonitoringModel(position, newProjectActivityMonitoringModel);
                } else {
                    // -- new items --
                    newProjectActivityMonitoringModelList.add(newProjectActivityMonitoringModel);
                }
            }
            if (newProjectActivityMonitoringModelList.size() > 0) {
                mProjectActivityMonitoringModelList.addAll(0, newProjectActivityMonitoringModelList);
                notifyItemRangeInserted(0, newProjectActivityMonitoringModelList.size());
            }
        }

        public void setProjectActivityMonitoringModel(final int position, final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
            if ((position + 1) > mProjectActivityMonitoringModelList.size())
                return;

            mProjectActivityMonitoringModelList.set(position, projectActivityMonitoringModel);
            notifyItemChanged(position);
        }

        public ProjectActivityMonitoringModel getProjectActivityMonitoringModel(final int position) {
            if ((position + 1) > mProjectActivityMonitoringModelList.size())
                return null;

            return mProjectActivityMonitoringModelList.get(position);
        }

        public int getPosition(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
            if (projectActivityMonitoringModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (ProjectActivityMonitoringModel projectActivityMonitoringModelExist : mProjectActivityMonitoringModelList) {
                if (projectActivityMonitoringModelExist.equals(projectActivityMonitoringModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchProjectActivityMonitoringId = projectActivityMonitoringModel.getProjectActivityMonitoringId();
            if (searchProjectActivityMonitoringId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (ProjectActivityMonitoringModel projectActivityMonitoringModelExist : mProjectActivityMonitoringModelList) {
                Integer existProjectActivityMonitoringId = projectActivityMonitoringModelExist.getProjectActivityMonitoringId();
                if (existProjectActivityMonitoringId != null) {
                    if (existProjectActivityMonitoringId.equals(searchProjectActivityMonitoringId)) {
                        isPositionFound = true;
                        break;
                    }
                }
                position++;
            }

            if (isPositionFound)
                return position;

            return -1;
        }

        @Override
        public ProjectActivityMonitoringListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_monitoring_list_item_view, parent, false);
            return new ProjectActivityMonitoringListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityMonitoringListViewHolder holder, int position) {
            if ((position + 1) > mProjectActivityMonitoringModelList.size())
                return;

            ProjectActivityMonitoringModel projectActivityMonitoringModel = mProjectActivityMonitoringModelList.get(position);
            holder.setImageRequestListener(mImageRequestListener);
            holder.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
        }

        @Override
        public int getItemCount() {
            return mProjectActivityMonitoringModelList.size();
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }

    protected class ProjectActivityMonitoringListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatImageView mPhotoId;
        protected AppCompatTextView mMonitoringDate;
        protected AppCompatTextView mActualStartDate;
        protected AppCompatTextView mActualEndDate;
        protected AppCompatTextView mActivityStatus;
        protected AppCompatTextView mPercentComplete;
        protected AppCompatTextView mComment;

        protected ImageRequestListener mImageRequestListener;

        public ProjectActivityMonitoringListViewHolder(View view) {
            super(view);

            mPhotoId = (AppCompatImageView) view.findViewById(R.id.photoId);
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
            mPercentComplete.setText(StringUtil.numberPercentFormat(projectActivityMonitoringModel.getPercentComplete()));
            mComment.setText(projectActivityMonitoringModel.getComment());
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }
}
