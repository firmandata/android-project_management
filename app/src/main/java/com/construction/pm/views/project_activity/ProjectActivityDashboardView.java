package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.ExpandedGridView;
import com.construction.pm.models.ProjectActivityDashboardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectActivityDashboardView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityDashboardView;
    protected AppCompatTextView mProjectActivityDashboardListLabel;
    protected ExpandedGridView mProjectActivityDashboardList;
    protected ProjectActivityDashboardAdapter mProjectActivityDashboardAdapter;

    protected ProjectActivityDashboardItemListener mProjectActivityDashboardItemListener;

    public ProjectActivityDashboardView(final Context context) {
        mContext = context;
    }

    public ProjectActivityDashboardView(final Context context, final RelativeLayout projectActivityDashboardView) {
        this(context);

        initializeView(projectActivityDashboardView);
    }

    public static ProjectActivityDashboardView buildProjectActivityDashboardView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityDashboardView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityDashboardView buildProjectActivityDashboardView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityDashboardView(context, R.layout.project_activity_dashboard_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDashboardView) {
        mProjectActivityDashboardView = projectActivityDashboardView;

        mProjectActivityDashboardListLabel = (AppCompatTextView) mProjectActivityDashboardView.findViewById(R.id.projectActivityDashboardListLabel);

        mProjectActivityDashboardList = (ExpandedGridView) mProjectActivityDashboardView.findViewById(R.id.projectActivityDashboardList);
        mProjectActivityDashboardList.setExpanded(true);
        mProjectActivityDashboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ProjectActivityDashboardModel projectActivityDashboardModel = mProjectActivityDashboardAdapter.getItem(position);
                if (projectActivityDashboardModel != null) {
                    if (mProjectActivityDashboardItemListener != null)
                        mProjectActivityDashboardItemListener.onProjectActivityDashboardItemClick(projectActivityDashboardModel);
                }
            }
        });

        mProjectActivityDashboardAdapter = new ProjectActivityDashboardAdapter(mContext);
        mProjectActivityDashboardList.setAdapter(mProjectActivityDashboardAdapter);
    }

    public RelativeLayout getView() {
        return mProjectActivityDashboardView;
    }

    public void setLabel(final String label) {
        mProjectActivityDashboardListLabel.setText(label);
    }

    public void setProjectActivityDashboardModel(final ProjectActivityDashboardModel[] projectActivityDashboardModels) {
        mProjectActivityDashboardAdapter.setProjectActivityDashboardModels(projectActivityDashboardModels);
    }

    public class ProjectActivityDashboardAdapter extends BaseAdapter {

        protected Context mContext;
        protected List<ProjectActivityDashboardModel> mProjectActivityDashboardModelList;

        public ProjectActivityDashboardAdapter(final Context context) {
            mContext = context;
            mProjectActivityDashboardModelList = new ArrayList<ProjectActivityDashboardModel>();
        }

        public void setProjectActivityDashboardModels(final ProjectActivityDashboardModel[] projectActivityDashboardModels) {
            mProjectActivityDashboardModelList = new ArrayList<ProjectActivityDashboardModel>(Arrays.asList(projectActivityDashboardModels));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mProjectActivityDashboardModelList.size();
        }

        @Override
        public ProjectActivityDashboardModel getItem(int position) {
            return mProjectActivityDashboardModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            ProjectActivityDashboardModel projectActivityDashboardModel = getItem(position);
            if (projectActivityDashboardModel != null)
                return projectActivityDashboardModel.getStatusTask().ordinal();
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ProjectActivityDashboardItemView projectActivityDashboardItemView;
            if (view == null) {
                projectActivityDashboardItemView = ProjectActivityDashboardItemView.buildProjectActivityDashboardItemView(mContext, viewGroup, false);

                view = projectActivityDashboardItemView.getView();
                view.setTag(projectActivityDashboardItemView);
            } else {
                projectActivityDashboardItemView = (ProjectActivityDashboardItemView) view.getTag();
            }

            if (projectActivityDashboardItemView != null) {
                ProjectActivityDashboardModel projectActivityDashboardModel = getItem(position);
                if (projectActivityDashboardModel != null) {
                    projectActivityDashboardItemView.setProjectActivityDashboardModel(projectActivityDashboardModel);
                }
            }

            return view;
        }
    }

    public void setProjectActivityDashboardItemListener(final ProjectActivityDashboardItemListener projectActivityDashboardItemListener) {
        mProjectActivityDashboardItemListener = projectActivityDashboardItemListener;
    }

    public interface ProjectActivityDashboardItemListener {
        void onProjectActivityDashboardItemClick(ProjectActivityDashboardModel projectActivityDashboardModel);
    }
}
