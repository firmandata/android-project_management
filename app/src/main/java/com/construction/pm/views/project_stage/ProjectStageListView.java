package com.construction.pm.views.project_stage;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectStageListView {
    protected Context mContext;

    protected RelativeLayout mProjectStageListView;

    protected SwipeRefreshLayout mSrlProjectStageList;
    protected RecyclerView mRvProjectStageList;
    protected ProjectStageListAdapter mProjectStageListAdapter;

    protected ProjectStageListListener mProjectStageListListener;

    public ProjectStageListView(final Context context) {
        mContext = context;
    }

    public ProjectStageListView(final Context context, final RelativeLayout projectStageListView) {
        this(context);

        initializeView(projectStageListView);
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageListView buildProjectStageListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageListView(context, R.layout.project_stage_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageListView) {
        mProjectStageListView = projectStageListView;

        mSrlProjectStageList = (SwipeRefreshLayout) mProjectStageListView.findViewById(R.id.projectStageListSwipeRefresh);
        mSrlProjectStageList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mProjectStageListListener != null)
                    mProjectStageListListener.onProjectStageListRequest();
            }
        });

        mRvProjectStageList = (RecyclerView) mProjectStageListView.findViewById(R.id.projectStageList);
        mRvProjectStageList.setItemAnimator(new DefaultItemAnimator());
        mRvProjectStageList.setHasFixedSize(true);
        mRvProjectStageList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvProjectStageList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectStageModel projectStageModel = mProjectStageListAdapter.getItem(position);
                if (projectStageModel != null) {
                    if (mProjectStageListListener != null)
                        mProjectStageListListener.onProjectStageItemClick(projectStageModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvProjectStageList.setLayoutManager(layoutManager);

        mProjectStageListAdapter = new ProjectStageListAdapter();
        mRvProjectStageList.setAdapter(mProjectStageListAdapter);

        if (mProjectStageListListener != null)
            mProjectStageListListener.onProjectStageListRequest();
    }

    public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
        mProjectStageListAdapter.setProjectStageModels(projectStageModels);
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlProjectStageList.isRefreshing())
            mSrlProjectStageList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlProjectStageList.isRefreshing())
            mSrlProjectStageList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mProjectStageListView;
    }

    public void setProjectStageListListener(final ProjectStageListListener projectStageListListener) {
        mProjectStageListListener = projectStageListListener;
    }

    public interface ProjectStageListListener {
        void onProjectStageListRequest();
        void onProjectStageItemClick(ProjectStageModel projectStageModel);
    }

    protected class ProjectStageListAdapter extends RecyclerView.Adapter<ProjectStageListViewHolder> {

        protected ProjectStageModelView[] mProjectStageModelViews;

        public ProjectStageListAdapter() {

        }

        public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
            mProjectStageModelViews = getProjectStageModelViews(projectStageModels);
            notifyDataSetChanged();
        }

        public ProjectStageModel getItem(final int position) {
            if (mProjectStageModelViews == null)
                return null;
            if ((position + 1) > mProjectStageModelViews.length)
                return null;

            return mProjectStageModelViews[position].getProjectStageModel();
        }

        @Override
        public ProjectStageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_list_item_view, parent, false);
            return new ProjectStageListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageListViewHolder holder, int position) {
            if (mProjectStageModelViews == null)
                return;
            if ((position + 1) > mProjectStageModelViews.length)
                return;

            ProjectStageModelView projectStageModelView = mProjectStageModelViews[position];
            holder.setProjectStageModel(projectStageModelView);
        }

        @Override
        public int getItemCount() {
            if (mProjectStageModelViews == null)
                return 0;

            return mProjectStageModelViews.length;
        }

        protected ProjectStageModelView[] getProjectStageModelViews(final ProjectStageModel[] projectStageModels) {
            ProjectStageModelView[] projectStageModelViews = buildHierarchyProjectStageModelViews(projectStageModels, null);
            return extractProjectStageModelViews(projectStageModelViews);
        }

        protected ProjectStageModelView[] extractProjectStageModelViews(final ProjectStageModelView[] projectStageModelViews) {
            List<ProjectStageModelView> projectStageModelViewList = new ArrayList<ProjectStageModelView>();

            for (ProjectStageModelView projectStageModelView : projectStageModelViews) {
                projectStageModelViewList.add(projectStageModelView);
                if (projectStageModelView.getChilds() != null) {
                    ProjectStageModelView[] projectStageModelViewChilds = extractProjectStageModelViews(projectStageModelView.getChilds());
                    if (projectStageModelViewChilds != null) {
                        for (ProjectStageModelView projectStageModelViewChild : projectStageModelViewChilds) {
                            projectStageModelViewList.add(projectStageModelViewChild);
                        }
                    }
                }
            }

            ProjectStageModelView[] newProjectStageModelViews = new ProjectStageModelView[projectStageModelViewList.size()];
            projectStageModelViewList.toArray(newProjectStageModelViews);
            return newProjectStageModelViews;
        }

        protected ProjectStageModelView[] buildHierarchyProjectStageModelViews(final ProjectStageModel[] projectStageModels, final ProjectStageModelView projectStageModelViewParent) {
            List<ProjectStageModelView> projectStageModelViewList = new ArrayList<ProjectStageModelView>();

            String stageFromCodeRef = null;
            if (projectStageModelViewParent != null)
                stageFromCodeRef = projectStageModelViewParent.getProjectStageModel().getStageCode();

            for (ProjectStageModel projectStageModel : projectStageModels) {
                ProjectStageModelView projectStageModelView = null;

                String stageFromCode = projectStageModel.getStageFromCode();
                if (stageFromCodeRef == null && stageFromCode == null) {
                    projectStageModelView = new ProjectStageModelView(projectStageModel);
                } else if (stageFromCodeRef != null && stageFromCode != null) {
                    if (stageFromCode.equals(stageFromCodeRef)) {
                        projectStageModelView = new ProjectStageModelView(projectStageModel);
                    }
                }

                if (projectStageModelView != null) {
                    projectStageModelView.setParent(projectStageModelViewParent);
                    projectStageModelView.setChilds(buildHierarchyProjectStageModelViews(projectStageModels, projectStageModelView));
                    projectStageModelViewList.add(projectStageModelView);
                }
            }

            ProjectStageModelView[] projectStageModelViews = new ProjectStageModelView[projectStageModelViewList.size()];
            projectStageModelViewList.toArray(projectStageModelViews);
            return projectStageModelViews;
        }
    }

    protected class ProjectStageModelView {

        protected ProjectStageModelView mParent;
        protected ProjectStageModel mProjectStageModel;
        protected List<ProjectStageModelView> mChildList;

        public ProjectStageModelView(final ProjectStageModel projectStageModel) {
            mProjectStageModel = projectStageModel;
            mChildList = new ArrayList<ProjectStageModelView>();
        }

        public ProjectStageModel getProjectStageModel() {
            return mProjectStageModel;
        }

        public void setParent(final ProjectStageModelView parent) {
            mParent = parent;
        }

        public ProjectStageModelView getParent() {
            return mParent;
        }

        public int getParentSize() {
            if (mParent == null)
                return 0;
            return mParent.getParentSize() + 1;
        }

        public void setChilds(final ProjectStageModelView[] childs) {
            mChildList = new ArrayList<ProjectStageModelView>(Arrays.asList(childs));
        }

        public void addChild(final ProjectStageModelView child) {
            mChildList.add(child);
        }

        public ProjectStageModelView[] getChilds() {
            ProjectStageModelView[] childs = new ProjectStageModelView[mChildList.size()];
            mChildList.toArray(childs);
            return childs;
        }
    }

    protected class ProjectStageListViewHolder extends RecyclerView.ViewHolder {

        protected CardView mContentBody;
        protected AppCompatTextView mEtStageDate;
        protected AppCompatTextView mEtStageCode;
        protected AppCompatTextView mEtStageNextCode;
        protected AppCompatTextView mEtStageNextPlanDate;
        protected AppCompatTextView mEtStageNextLocation;
        protected AppCompatTextView mEtStageNextSubject;

        public ProjectStageListViewHolder(View view) {
            super(view);

            mContentBody = (CardView) view.findViewById(R.id.contentBody);
            mEtStageDate = (AppCompatTextView) view.findViewById(R.id.stageDate);
            mEtStageCode = (AppCompatTextView) view.findViewById(R.id.stageCode);
            mEtStageNextCode = (AppCompatTextView) view.findViewById(R.id.stageNextCode);
            mEtStageNextPlanDate = (AppCompatTextView) view.findViewById(R.id.stageNextPlanDate);
            mEtStageNextLocation = (AppCompatTextView) view.findViewById(R.id.stageNextLocation);
            mEtStageNextSubject = (AppCompatTextView) view.findViewById(R.id.stageNextSubject);

            int margin = (int) mContentBody.getCardElevation();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentBody.getLayoutParams();
            layoutParams.setMargins(margin, margin, margin, margin);
        }

        public void setProjectStageModel(final ProjectStageModelView projectStageModelView) {
            ProjectStageModel projectStageModel = projectStageModelView.getProjectStageModel();
            if (projectStageModel == null)
                return;

            mEtStageDate.setText(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageDate()));
            mEtStageCode.setText(projectStageModel.getStageCode());
            mEtStageNextCode.setText(projectStageModel.getStageNextCode());
            mEtStageNextPlanDate.setText(DateTimeUtil.ToDateDisplayString(projectStageModel.getStageNextPlanDate()));
            mEtStageNextLocation.setText(projectStageModel.getStageNextLocation());
            mEtStageNextSubject.setText(projectStageModel.getStageNextSubject());

            Context context = getView().getContext();

            int margin = (int) mContentBody.getCardElevation(); //(int) context.getResources().getDimension(R.dimen.gap);
            int marginLeft = ViewUtil.getPxFromDp(context, projectStageModelView.getParentSize() * 10) + margin;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentBody.getLayoutParams();
            layoutParams.setMargins(marginLeft, margin, margin, margin);
        }
    }
}
