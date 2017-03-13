package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.construction.pm.R;
import com.construction.pm.activities.FileViewActivity;
import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringDetailView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringDetailFragment extends Fragment implements
        ImageRequestListener,
        ImageRequestClickListener {

    public static final String PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringDetailView mProjectActivityMonitoringDetailView;

    public static ProjectActivityMonitoringDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityMonitoringDetailFragment newInstance(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityMonitoringModel != null) {
            try {
                String projectActivityMonitoringModelJson = projectActivityMonitoringModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityMonitoringDetailFragment --
        ProjectActivityMonitoringDetailFragment projectActivityMonitoringDetailFragment = new ProjectActivityMonitoringDetailFragment();
        projectActivityMonitoringDetailFragment.setArguments(bundle);
        return projectActivityMonitoringDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityMonitoringModel projectActivityMonitoringModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityMonitoringModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityMonitoringDetailView --
        mProjectActivityMonitoringDetailView = ProjectActivityMonitoringDetailView.buildProjectActivityMonitoringDetailView(getContext(), null);
        mProjectActivityMonitoringDetailView.setImageRequestListener(this);
        mProjectActivityMonitoringDetailView.setImageRequestClickListener(this);
        mProjectActivityMonitoringDetailView.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityMonitoringDetail to fragment --
        return mProjectActivityMonitoringDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onImageRequest(final ImageView imageView, final Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        final SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileGetNetworkAsyncTask --
        final FileGetNetworkAsyncTask fileGetNetworkAsyncTask = new FileGetNetworkAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                mAsyncTaskList.remove(this);

                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        if (fileModel.getFileData() != null)
                            ViewUtil.setImageThumbnailView(getContext(), imageView, fileModel.getFileData());
                    }
                }
            }
        };

        // -- Prepare FileGetCacheAsyncTask --
        FileGetCacheAsyncTask fileGetCacheAsyncTask = new FileGetCacheAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        if (fileModel.getFileData() != null)
                            ViewUtil.setImageThumbnailView(getContext(), imageView, fileModel.getFileData());
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId));
    }

    @Override
    public void onImageRequestClick(Integer fileId) {
        // -- Redirect to FileViewActivity --
        Intent intent = new Intent(getContext(), FileViewActivity.class);
        intent.putExtra(FileViewActivity.INTENT_PARAM_FILE_ID, fileId);
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
