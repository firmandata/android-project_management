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
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.utils.DateTimeUtil;

public class ProjectStageDocumentListView {

    protected Context mContext;

    protected RelativeLayout mProjectStageDocumentListView;

    protected RecyclerView mProjectStageDocumentList;
    protected ProjectStageDocumentListAdapter mProjectStageDocumentListAdapter;

    protected ProjectStageDocumentListListener mProjectStageDocumentListListener;

    public ProjectStageDocumentListView(final Context context) {
        mContext = context;
    }

    public ProjectStageDocumentListView(final Context context, final RelativeLayout projectStageDocumentListView) {
        this(context);

        initializeView(projectStageDocumentListView);
    }

    public static ProjectStageDocumentListView buildProjectStageDocumentListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageDocumentListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageDocumentListView buildProjectStageDocumentListView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageDocumentListView(context, R.layout.project_stage_document_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageDocumentListView) {
        mProjectStageDocumentListView = projectStageDocumentListView;

        mProjectStageDocumentList = (RecyclerView) mProjectStageDocumentListView.findViewById(R.id.projectStageDocumentList);
        mProjectStageDocumentList.setItemAnimator(new DefaultItemAnimator());
        mProjectStageDocumentList.setNestedScrollingEnabled(false);
        mProjectStageDocumentList.setHasFixedSize(true);
        mProjectStageDocumentList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mProjectStageDocumentList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectStageDocumentModel projectStageDocumentModel = mProjectStageDocumentListAdapter.getProjectStageDocumentModel(position);
                if (projectStageDocumentModel != null) {
                    if (mProjectStageDocumentListListener != null)
                        mProjectStageDocumentListListener.onProjectStageDocumentItemClick(projectStageDocumentModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mProjectStageDocumentList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mProjectStageDocumentList.addItemDecoration(dividerItemDecoration);

        mProjectStageDocumentListAdapter = new ProjectStageDocumentListAdapter();
        mProjectStageDocumentList.setAdapter(mProjectStageDocumentListAdapter);
    }

    public void setProjectStageDocumentModels(final ProjectStageDocumentModel[] projectStageDocumentModels) {
        mProjectStageDocumentListAdapter.setProjectStageDocumentModels(projectStageDocumentModels);
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        return mProjectStageDocumentListAdapter.getProjectStageDocumentModels();
    }

    public RelativeLayout getView() {
        return mProjectStageDocumentListView;
    }

    public void setProjectStageDocumentListListener(final ProjectStageDocumentListListener projectStageDocumentListListener) {
        mProjectStageDocumentListListener = projectStageDocumentListListener;
    }

    public interface ProjectStageDocumentListListener {
        void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel);
    }

    protected class ProjectStageDocumentListAdapter extends RecyclerView.Adapter<ProjectStageDocumentListViewHolder> {

        protected ProjectStageDocumentModel[] mProjectStageDocumentModels;

        public ProjectStageDocumentListAdapter() {

        }

        public ProjectStageDocumentListAdapter(final ProjectStageDocumentModel[] projectStageDocumentModels) {
            this();
            mProjectStageDocumentModels = projectStageDocumentModels;
        }

        public void setProjectStageDocumentModels(final ProjectStageDocumentModel[] projectStageDocumentModels) {
            mProjectStageDocumentModels = projectStageDocumentModels;

            notifyDataSetChanged();
        }

        public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
            return mProjectStageDocumentModels;
        }

        public ProjectStageDocumentModel getProjectStageDocumentModel(final int position) {
            if (mProjectStageDocumentModels == null)
                return null;
            if ((position + 1) > mProjectStageDocumentModels.length)
                return null;

            return mProjectStageDocumentModels[position];
        }

        @Override
        public ProjectStageDocumentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_stage_document_list_item_view, parent, false);
            return new ProjectStageDocumentListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectStageDocumentListViewHolder holder, int position) {
            if (mProjectStageDocumentModels == null)
                return;
            if ((position + 1) > mProjectStageDocumentModels.length)
                return;

            ProjectStageDocumentModel projectStageDocumentModel = mProjectStageDocumentModels[position];
            holder.setProjectStageDocumentModel(projectStageDocumentModel);
        }

        @Override
        public int getItemCount() {
            if (mProjectStageDocumentModels == null)
                return 0;

            return mProjectStageDocumentModels.length;
        }
    }

    protected class ProjectStageDocumentListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mDocumentDate;
        protected AppCompatTextView mDocumentName;

        public ProjectStageDocumentListViewHolder(View view) {
            super(view);

            mDocumentDate = (AppCompatTextView) view.findViewById(R.id.documentDate);
            mDocumentName = (AppCompatTextView) view.findViewById(R.id.documentName);
        }

        public void setProjectStageDocumentModel(final ProjectStageDocumentModel projectStageDocumentModel) {
            mDocumentDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectStageDocumentModel.getDocumentDate()));
            mDocumentName.setText(projectStageDocumentModel.getDocumentName());
        }
    }
}
