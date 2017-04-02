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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        mProjectActivityUpdateList.setHasFixedSize(true);
        mProjectActivityUpdateList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectActivityUpdateList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectActivityUpdateModel projectActivityUpdateModel = mProjectActivityUpdateListAdapter.getProjectActivityUpdateModel(position);
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

    public void addProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        mProjectActivityUpdateListAdapter.addProjectActivityUpdateModels(new ProjectActivityUpdateModel[] { projectActivityUpdateModel });
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

        protected List<ProjectActivityUpdateModel> mProjectActivityUpdateModelList;

        public ProjectActivityUpdateListAdapter() {
            mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>();
        }

        public ProjectActivityUpdateListAdapter(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            this();
            mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>(Arrays.asList(projectActivityUpdateModels));
        }

        public void setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            if (projectActivityUpdateModels != null)
                mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>(Arrays.asList(projectActivityUpdateModels));
            else
                mProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>();
            notifyDataSetChanged();
        }

        public void addProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            List<ProjectActivityUpdateModel> newProjectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>();
            for (ProjectActivityUpdateModel newProjectActivityUpdateModel : projectActivityUpdateModels) {
                int position = getPosition(newProjectActivityUpdateModel);
                if (position >= 0) {
                    // -- replace item --
                    setProjectActivityUpdateModel(position, newProjectActivityUpdateModel);
                } else {
                    // -- new items --
                    newProjectActivityUpdateModelList.add(newProjectActivityUpdateModel);
                }
            }
            if (newProjectActivityUpdateModelList.size() > 0) {
                mProjectActivityUpdateModelList.addAll(0, newProjectActivityUpdateModelList);
                notifyItemRangeInserted(0, newProjectActivityUpdateModelList.size());
            }
        }

        public void setProjectActivityUpdateModel(final int position, final ProjectActivityUpdateModel projectActivityUpdateModel) {
            if ((position + 1) > mProjectActivityUpdateModelList.size())
                return;

            mProjectActivityUpdateModelList.set(position, projectActivityUpdateModel);
            notifyItemChanged(position);
        }

        public ProjectActivityUpdateModel getProjectActivityUpdateModel(final int position) {
            if ((position + 1) > mProjectActivityUpdateModelList.size())
                return null;

            return mProjectActivityUpdateModelList.get(position);
        }

        public int getPosition(final ProjectActivityUpdateModel projectActivityUpdateModel) {
            if (projectActivityUpdateModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (ProjectActivityUpdateModel projectActivityUpdateModelExist : mProjectActivityUpdateModelList) {
                if (projectActivityUpdateModelExist.equals(projectActivityUpdateModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchProjectActivityUpdateId = projectActivityUpdateModel.getProjectActivityUpdateId();
            if (searchProjectActivityUpdateId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (ProjectActivityUpdateModel projectActivityUpdateModelExist : mProjectActivityUpdateModelList) {
                Integer existProjectActivityUpdateId = projectActivityUpdateModelExist.getProjectActivityUpdateId();
                if (existProjectActivityUpdateId != null) {
                    if (existProjectActivityUpdateId.equals(searchProjectActivityUpdateId)) {
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
        public ProjectActivityUpdateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_activity_update_list_item_view, parent, false);
            return new ProjectActivityUpdateListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectActivityUpdateListViewHolder holder, int position) {
            if ((position + 1) > mProjectActivityUpdateModelList.size())
                return;

            ProjectActivityUpdateModel projectActivityUpdateModel = mProjectActivityUpdateModelList.get(position);
            holder.setProjectActivityUpdateModel(projectActivityUpdateModel);
        }

        @Override
        public int getItemCount() {
            return mProjectActivityUpdateModelList.size();
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
            mPercentComplete.setText(StringUtil.numberPercentFormat(projectActivityUpdateModel.getPercentComplete()));
            mComment.setText(projectActivityUpdateModel.getComment());
        }
    }
}
