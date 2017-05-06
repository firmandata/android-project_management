package com.construction.pm.views.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerProjectListAdapter extends BaseAdapter {

    protected Context mContext;
    protected List<ProjectModel> mProjectList;

    public SpinnerProjectListAdapter(final Context context) {
        super();

        mContext = context;

        mProjectList = new ArrayList<ProjectModel>();
        mProjectList.add(null);
    }

    public SpinnerProjectListAdapter(final Context context, final ProjectModel projectModel) {
        super();

        mContext = context;

        mProjectList = new ArrayList<ProjectModel>();
        mProjectList.add(projectModel);
    }

    public SpinnerProjectListAdapter(final Context context, final ProjectModel projectModel, final ProjectModel[] projectModels) {
        this(context, projectModel);

        if (projectModels != null) {
            mProjectList.addAll(Arrays.asList(projectModels));
        }
    }

    public void addProjectModels(final ProjectModel[] projectModels) {
        if (projectModels != null) {
            mProjectList.addAll(Arrays.asList(projectModels));
            notifyDataSetChanged();
        }
    }

    public int getPositionByItem(final ProjectModel projectModel) {
        int position = -1;
        if (projectModel != null) {
            Integer projectId = projectModel.getProjectId();
            for (int itemPosition = 0; itemPosition < getCount(); itemPosition++) {
                ProjectModel existProjectModel = getItem(itemPosition);
                if (existProjectModel != null) {
                    // -- Find by object --
                    if (existProjectModel.equals(projectModel)) {
                        position = itemPosition;
                        break;
                    }

                    // -- Find by projectId --
                    Integer existProjectId = existProjectModel.getProjectId();
                    if (existProjectId != null && projectId != null) {
                        if (existProjectId.equals(projectId)) {
                            position = itemPosition;
                            break;
                        }
                    }
                }
            }
        }
        return position;
    }

    @Override
    public ProjectModel getItem(int position) {
        return mProjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItem view = ViewItem.buildViewItem(mContext, null);
        view.setProjectName(mProjectList.get(position));
        return view.getView();
    }

    @Override
    public int getCount() {
        return mProjectList.size();
    }

    protected static class ViewItem {

        protected Context mContext;

        protected RelativeLayout mViewItem;

        protected AppCompatTextView mProjectName;

        public ViewItem(final Context context) {
            mContext = context;
        }

        public ViewItem(final Context context, final RelativeLayout itemView) {
            this(context);

            initializeView(itemView);
        }

        public static ViewItem buildViewItem(final Context context, final int layoutId, final ViewGroup viewGroup) {
            return new ViewItem(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
        }

        public static ViewItem buildViewItem(final Context context, final ViewGroup viewGroup) {
            return buildViewItem(context, R.layout.spinner_project_item_view, viewGroup);
        }

        protected void initializeView(final RelativeLayout itemView) {
            mViewItem = itemView;

            mProjectName = (AppCompatTextView) mViewItem.findViewById(R.id.projectName);
            mProjectName.setText(ViewUtil.getResourceString(mContext, R.string.spinner_project_list_blank));
        }

        public void setProjectName(final ProjectModel projectModel) {
            if (projectModel != null)
                mProjectName.setText(projectModel.getProjectName());
        }

        public RelativeLayout getView() {
            return mViewItem;
        }
    }
}