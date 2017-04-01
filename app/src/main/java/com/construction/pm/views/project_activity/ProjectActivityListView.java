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
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                ProjectActivityModel projectActivityModel = mProjectActivityListAdapter.getProjectActivityModel(position);
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
    }

    public void addProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListAdapter.addProjectActivityModels(projectActivityModels);
    }

    public void removeProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListAdapter.removeProjectActivityModels(projectActivityModels);
    }

    public ProjectActivityModel[] getProjectActivityModels() {
        return mProjectActivityListAdapter.getProjectActivityModels();
    }

    public void setStatusTaskEnum(final StatusTaskEnum statusTaskEnum) {
        mStatusTaskEnum = statusTaskEnum;
    }

    public StatusTaskEnum getStatusTask() {
        return mStatusTaskEnum;
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

        protected List<ProjectActivityModel> mProjectActivityModelList;

        public ProjectActivityListAdapter() {
            mProjectActivityModelList = new ArrayList<ProjectActivityModel>();
        }

        public ProjectActivityListAdapter(final ProjectActivityModel[] projectActivityModels) {
            this();
            mProjectActivityModelList = new ArrayList<ProjectActivityModel>(Arrays.asList(projectActivityModels));
        }

        public void setProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
            mProjectActivityModelList = new ArrayList<ProjectActivityModel>(Arrays.asList(projectActivityModels));
            notifyDataSetChanged();
        }

        public void addProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
            List<ProjectActivityModel> newProjectActivityModelList = new ArrayList<ProjectActivityModel>();
            for (ProjectActivityModel newProjectActivityModel : projectActivityModels) {
                int position = getPosition(newProjectActivityModel);
                if (position >= 0) {
                    // -- replace item --
                    setProjectActivityModel(position, newProjectActivityModel);
                } else {
                    // -- new items --
                    newProjectActivityModelList.add(newProjectActivityModel);
                }
            }
            if (newProjectActivityModelList.size() > 0) {
                mProjectActivityModelList.addAll(0, newProjectActivityModelList);
                notifyItemRangeInserted(0, newProjectActivityModelList.size());
            }
        }

        public void setProjectActivityModel(final int position, final ProjectActivityModel projectActivityModel) {
            if ((position + 1) > mProjectActivityModelList.size())
                return;

            mProjectActivityModelList.set(position, projectActivityModel);
            notifyItemChanged(position);
        }

        public void removeProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
            for (ProjectActivityModel projectActivityModel : projectActivityModels) {
                removeProjectActivityModel(projectActivityModel);
            }
        }

        public void removeProjectActivityModel(final ProjectActivityModel projectActivityModel) {
            int position = getPosition(projectActivityModel);
            if (position >= 0) {
                mProjectActivityModelList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public ProjectActivityModel[] getProjectActivityModels() {
            ProjectActivityModel[] projectActivityModels = new ProjectActivityModel[mProjectActivityModelList.size()];
            mProjectActivityModelList.toArray(projectActivityModels);
            return projectActivityModels;
        }

        public ProjectActivityModel getProjectActivityModel(final int position) {
            if ((position + 1) > mProjectActivityModelList.size())
                return null;

            return mProjectActivityModelList.get(position);
        }

        public int getPosition(final ProjectActivityModel projectActivityModel) {
            if (projectActivityModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (ProjectActivityModel projectActivityModelExist : mProjectActivityModelList) {
                if (projectActivityModelExist.equals(projectActivityModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchProjectActivityId = projectActivityModel.getProjectActivityId();
            if (searchProjectActivityId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (ProjectActivityModel projectActivityModelExist : mProjectActivityModelList) {
                Integer existProjectActivityId = projectActivityModelExist.getProjectActivityId();
                if (existProjectActivityId != null) {
                    if (existProjectActivityId.equals(searchProjectActivityId)) {
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
        public ProjectActivityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_list_item_view, parent, false);
            return new ProjectActivityListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityListViewHolder holder, int position) {
            if ((position + 1) > mProjectActivityModelList.size())
                return;

            ProjectActivityModel projectActivityModel = mProjectActivityModelList.get(position);
            holder.setProjectActivityModel(projectActivityModel);
        }

        @Override
        public int getItemCount() {
            return mProjectActivityModelList.size();
        }
    }

    protected class ProjectActivityListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtTaskName;
        protected AppCompatTextView mEtPlanStartDate;
        protected AppCompatTextView mEtPlanEndDate;
        protected AppCompatTextView mEtActualStartDate;
        protected AppCompatTextView mEtActualEndDate;
        protected AppCompatTextView mEtActivityStatus;
        protected AppCompatTextView mEtPercentComplete;

        public ProjectActivityListViewHolder(View view) {
            super(view);

            mEtTaskName = (AppCompatTextView) view.findViewById(R.id.taskName);
            mEtPlanStartDate = (AppCompatTextView) view.findViewById(R.id.planStartDate);
            mEtPlanEndDate = (AppCompatTextView) view.findViewById(R.id.planEndDate);
            mEtActualStartDate = (AppCompatTextView) view.findViewById(R.id.actualStartDate);
            mEtActualEndDate = (AppCompatTextView) view.findViewById(R.id.actualEndDate);
            mEtActivityStatus = (AppCompatTextView) view.findViewById(R.id.activityStatus);
            mEtPercentComplete = (AppCompatTextView) view.findViewById(R.id.percentComplete);
        }

        public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
            mEtTaskName.setText(projectActivityModel.getTaskName());
            mEtPlanStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getPlanStartDate()));
            mEtPlanEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getPlanEndDate()));
            mEtActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getActualStartDate()));
            mEtActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityModel.getActualEndDate()));
            mEtActivityStatus.setText(projectActivityModel.getActivityStatus());
            mEtPercentComplete.setText(StringUtil.numberPercentFormat(projectActivityModel.getPercentComplete()));
        }
    }
}
