package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.InspectorProjectActivityMonitoringListAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringListAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringListAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringListFragment extends Fragment implements
        ProjectActivityMonitoringListView.ProjectActivityMonitoringListListener,
        ImageRequestListener {

    public static final String PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringListView mProjectActivityMonitoringListView;

    protected ProjectActivityMonitoringListFragmentListener mProjectActivityMonitoringListFragmentListener;

    public static ProjectActivityMonitoringListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel) {
        return newInstance(projectActivityModel, null);
    }

    public static ProjectActivityMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel, final ProjectActivityMonitoringListFragmentListener projectActivityMonitoringListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                String projectActivityModelJson = projectActivityModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityMonitoringListFragment --
        ProjectActivityMonitoringListFragment projectActivityMonitoringListFragment = new ProjectActivityMonitoringListFragment();
        projectActivityMonitoringListFragment.setArguments(bundle);
        projectActivityMonitoringListFragment.setProjectActivityMonitoringListFragmentListener(projectActivityMonitoringListFragmentListener);
        return projectActivityMonitoringListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityModel projectActivityModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityModel = ProjectActivityModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityMonitoringListView --
        mProjectActivityMonitoringListView = ProjectActivityMonitoringListView.buildProjectActivityMonitoringListView(getContext(), null);
        mProjectActivityMonitoringListView.setProjectActivityModel(projectActivityModel);
        mProjectActivityMonitoringListView.setProjectActivityMonitoringListListener(this);
        mProjectActivityMonitoringListView.setImageRequestListener(this);

        // -- Load ProjectActivityMonitoringList --
        onProjectActivityMonitoringListRequest(projectActivityModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityMonitoringList to fragment --
        return mProjectActivityMonitoringListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectActivityMonitoringListRequest(ProjectActivityModel projectActivityModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare InspectorProjectActivityMonitoringListAsyncTask --
        InspectorProjectActivityMonitoringListAsyncTask inspectorProjectActivityMonitoringListAsyncTask = new InspectorProjectActivityMonitoringListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityMonitoringListAsyncTaskResult inspectorProjectActivityMonitoringListAsyncTask) {
                mAsyncTaskList.remove(this);

                if (inspectorProjectActivityMonitoringListAsyncTask != null) {
                    onProjectListRequestSuccess(inspectorProjectActivityMonitoringListAsyncTask.getProjectActivityMonitoringModels());
                    if (inspectorProjectActivityMonitoringListAsyncTask.getMessage() != null)
                        onProjectListRequestMessage(inspectorProjectActivityMonitoringListAsyncTask.getMessage());
                } else {
                    onProjectListRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do InspectorProjectActivityMonitoringListAsyncTask --
        inspectorProjectActivityMonitoringListAsyncTask.execute(new InspectorProjectActivityMonitoringListAsyncTaskParam(getContext(), settingUserModel, projectActivityModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (mProjectActivityMonitoringListFragmentListener != null)
            mProjectActivityMonitoringListFragmentListener.onProjectActivityMonitoringListItemClick(projectActivityMonitoringModel);
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

    protected void onProjectListRequestProgress(final String progressMessage) {

    }

    protected void onProjectListRequestSuccess(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels) {
        mProjectActivityMonitoringListView.setProjectActivityMonitoringModels(projectActivityMonitoringModels);
    }

    protected void onProjectListRequestMessage(final String message) {

    }

    public void reloadProjectActivityMonitoringListRequest(final ProjectActivityModel projectActivityModel) {
        // -- Load ProjectActivityMonitoringList --
        onProjectActivityMonitoringListRequest(projectActivityModel);
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

    public void setProjectActivityMonitoringListFragmentListener(final ProjectActivityMonitoringListFragmentListener projectActivityMonitoringListFragmentListener) {
        mProjectActivityMonitoringListFragmentListener = projectActivityMonitoringListFragmentListener;
    }

    public interface ProjectActivityMonitoringListFragmentListener {
        void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel);
    }
}
