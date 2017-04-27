package com.construction.pm.views.project_stage;

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
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.file.FilePhotoListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectStageAssignCommentListView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentListView;

    protected RecyclerView mProjectStageAssignCommentList;
    protected ProjectStageAssignCommentListAdapter mProjectStageAssignCommentListAdapter;

    protected ProjectStageModel mProjectStageModel;

    protected ProjectStageAssignCommentListListener mProjectStageAssignCommentListListener;

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
        mProjectStageAssignCommentList.setHasFixedSize(true);
        mProjectStageAssignCommentList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectStageAssignCommentList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectStageAssignCommentModel projectStageAssignCommentModel = mProjectStageAssignCommentListAdapter.getProjectStageAssignCommentModel(position);
                if (projectStageAssignCommentModel != null) {
                    if (mProjectStageAssignCommentListListener != null)
                        mProjectStageAssignCommentListListener.onProjectStageAssignCommentItemClick(projectStageAssignCommentModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mProjectStageAssignCommentList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mProjectStageAssignCommentList.addItemDecoration(dividerItemDecoration);

        mProjectStageAssignCommentListAdapter = new ProjectStageAssignCommentListAdapter();
        mProjectStageAssignCommentList.setAdapter(mProjectStageAssignCommentListAdapter);

        if (mProjectStageAssignCommentListListener != null)
            mProjectStageAssignCommentListListener.onProjectStageAssignCommentListRequest(mProjectStageModel);
    }

    public void setProjectStageModel(final ProjectStageModel projectStageModel) {
        mProjectStageModel = projectStageModel;
    }

    public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageAssignCommentListAdapter.setProjectStageAssignCommentModels(projectStageAssignCommentModels);
    }

    public void addProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentListAdapter.addProjectStageAssignCommentModels(new ProjectStageAssignCommentModel[] { projectStageAssignCommentModel });
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
        return mProjectStageAssignCommentListAdapter.getProjectStageAssignCommentModels();
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mProjectStageAssignCommentListAdapter.setImageRequestListener(imageRequestListener);
    }

    public RelativeLayout getView() {
        return mProjectStageAssignCommentListView;
    }

    public void setProjectStageAssignCommentListListener(final ProjectStageAssignCommentListListener projectStageAssignCommentListListener) {
        mProjectStageAssignCommentListListener = projectStageAssignCommentListListener;
    }

    public interface ProjectStageAssignCommentListListener {
        void onProjectStageAssignCommentListRequest(ProjectStageModel projectStageModel);
        void onProjectStageAssignCommentItemClick(ProjectStageAssignCommentModel projectStageAssignCommentModel);
    }

    protected class ProjectStageAssignCommentListAdapter extends RecyclerView.Adapter<ProjectStageAssignCommentListViewHolder> {

        protected List<ProjectStageAssignCommentModel> mProjectStageAssignCommentModelList;

        protected ImageRequestListener mImageRequestListener;

        public ProjectStageAssignCommentListAdapter() {
            mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();
        }

        public ProjectStageAssignCommentListAdapter(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            this();
            mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>(Arrays.asList(projectStageAssignCommentModels));
        }

        public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            if (projectStageAssignCommentModels != null)
                mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>(Arrays.asList(projectStageAssignCommentModels));
            else
                mProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();
            notifyDataSetChanged();
        }

        public void addProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            List<ProjectStageAssignCommentModel> newProjectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();
            for (ProjectStageAssignCommentModel newProjectStageAssignCommentModel : projectStageAssignCommentModels) {
                int position = getPosition(newProjectStageAssignCommentModel);
                if (position >= 0) {
                    // -- replace item --
                    setProjectStageAssignCommentModel(position, newProjectStageAssignCommentModel);
                } else {
                    // -- new items --
                    newProjectStageAssignCommentModelList.add(newProjectStageAssignCommentModel);
                }
            }
            if (newProjectStageAssignCommentModelList.size() > 0) {
                mProjectStageAssignCommentModelList.addAll(0, newProjectStageAssignCommentModelList);
                notifyItemRangeInserted(0, newProjectStageAssignCommentModelList.size());
            }
        }

        public void setProjectStageAssignCommentModel(final int position, final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
            if ((position + 1) > mProjectStageAssignCommentModelList.size())
                return;

            mProjectStageAssignCommentModelList.set(position, projectStageAssignCommentModel);
            notifyItemChanged(position);
        }

        public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
            ProjectStageAssignCommentModel[] projectStageAssignCommentModels = new ProjectStageAssignCommentModel[mProjectStageAssignCommentModelList.size()];
            mProjectStageAssignCommentModelList.toArray(projectStageAssignCommentModels);
            return projectStageAssignCommentModels;
        }

        public ProjectStageAssignCommentModel getProjectStageAssignCommentModel(final int position) {
            if ((position + 1) > mProjectStageAssignCommentModelList.size())
                return null;

            return mProjectStageAssignCommentModelList.get(position);
        }

        public int getPosition(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
            if (projectStageAssignCommentModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (ProjectStageAssignCommentModel projectStageAssignCommentModelExist : mProjectStageAssignCommentModelList) {
                if (projectStageAssignCommentModelExist.equals(projectStageAssignCommentModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchProjectStageAssignCommentId = projectStageAssignCommentModel.getProjectStageAssignCommentId();
            if (searchProjectStageAssignCommentId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (ProjectStageAssignCommentModel projectStageAssignCommentModelExist : mProjectStageAssignCommentModelList) {
                Integer existProjectStageAssignCommentId = projectStageAssignCommentModelExist.getProjectStageAssignCommentId();
                if (existProjectStageAssignCommentId != null) {
                    if (existProjectStageAssignCommentId.equals(searchProjectStageAssignCommentId)) {
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
        public ProjectStageAssignCommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_assign_comment_list_item_view, parent, false);
            return new ProjectStageAssignCommentListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageAssignCommentListViewHolder holder, int position) {
            if ((position + 1) > mProjectStageAssignCommentModelList.size())
                return;

            ProjectStageAssignCommentModel projectStageAssignCommentModel = mProjectStageAssignCommentModelList.get(position);
            holder.setImageRequestListener(mImageRequestListener);
            holder.setProjectStageAssignCommentModel(projectStageAssignCommentModel);
        }

        @Override
        public int getItemCount() {
            return mProjectStageAssignCommentModelList.size();
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }

    protected class ProjectStageAssignCommentListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mCommentDate;
        protected AppCompatTextView mComment;
        protected AppCompatImageView mPhotoId;

        protected ImageRequestListener mImageRequestListener;

        public ProjectStageAssignCommentListViewHolder(View view) {
            super(view);

            mCommentDate = (AppCompatTextView) view.findViewById(R.id.commentDate);
            mComment = (AppCompatTextView) view.findViewById(R.id.comment);
            mPhotoId = (AppCompatImageView) view.findViewById(R.id.photoId);
        }

        public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
            mCommentDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectStageAssignCommentModel.getCommentDate()));
            mComment.setText(projectStageAssignCommentModel.getComment());
            if (projectStageAssignCommentModel.getPhotoId() != null) {
                if (mImageRequestListener != null)
                    mImageRequestListener.onImageRequest(mPhotoId, projectStageAssignCommentModel.getPhotoId());
            } else {
                mPhotoId.setImageResource(R.drawable.ic_image_dark_24);
            }
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }
}
