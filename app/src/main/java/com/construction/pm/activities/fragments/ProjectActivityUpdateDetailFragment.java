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

import com.construction.pm.activities.FileViewActivity;
import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.InspectorProjectActivityMonitoringDetailAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringDetailAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringDetailAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.project_activity.ProjectActivityUpdateDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateDetailFragment extends Fragment implements
        ProjectActivityUpdateDetailView.ProjectActivityUpdateDetailListener,
        ImageRequestDuplicateListener,
        ImageRequestClickListener {

    public static final String PARAM_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateDetailView mProjectActivityUpdateDetailView;

    public static ProjectActivityUpdateDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityUpdateDetailFragment newInstance(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityUpdateModel != null) {
            try {
                String projectActivityUpdateModelJson = projectActivityUpdateModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityUpdateDetailFragment --
        ProjectActivityUpdateDetailFragment projectActivityUpdateDetailFragment = new ProjectActivityUpdateDetailFragment();
        projectActivityUpdateDetailFragment.setArguments(bundle);
        return projectActivityUpdateDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityUpdateModel projectActivityUpdateModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityUpdateModel parameter --
            String projectActivityModelJson = bundle.getString(PARAM_PROJECT_ACTIVITY_UPDATE_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    projectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityUpdateDetailView --
        mProjectActivityUpdateDetailView = ProjectActivityUpdateDetailView.buildProjectActivityUpdateDetailView(getContext(), null);
        mProjectActivityUpdateDetailView.setProjectActivityUpdateDetailListener(this);
        mProjectActivityUpdateDetailView.setImageRequestDuplicateListener(this);
        mProjectActivityUpdateDetailView.setImageRequestClickListener(this);
        mProjectActivityUpdateDetailView.setProjectActivityUpdateModel(projectActivityUpdateModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityUpdateDetail to fragment --
        return mProjectActivityUpdateDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectActivityMonitoringDetailRequest(Integer projectActivityMonitoringId, Integer projectActivityId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare InspectorProjectActivityMonitoringDetailAsyncTask --
        InspectorProjectActivityMonitoringDetailAsyncTask inspectorProjectActivityMonitoringDetailAsyncTask = new InspectorProjectActivityMonitoringDetailAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityMonitoringDetailAsyncTaskResult inspectorProjectActivityMonitoringDetailAsyncTask) {
                mAsyncTaskList.remove(this);

                if (inspectorProjectActivityMonitoringDetailAsyncTask != null) {
                    onProjectActivityMonitoringDetailRequestSuccess(inspectorProjectActivityMonitoringDetailAsyncTask.getProjectActivityMonitoringModel());
                    if (inspectorProjectActivityMonitoringDetailAsyncTask.getMessage() != null)
                        onProjectActivityMonitoringDetailRequestMessage(inspectorProjectActivityMonitoringDetailAsyncTask.getMessage());
                } else {
                    onProjectActivityMonitoringDetailRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityMonitoringDetailRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do InspectorProjectActivityMonitoringDetailAsyncTask --
        inspectorProjectActivityMonitoringDetailAsyncTask.execute(new InspectorProjectActivityMonitoringDetailAsyncTaskParam(getContext(), settingUserModel, projectActivityMonitoringId, projectActivityId, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectActivityMonitoringDetailRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityMonitoringDetailRequestSuccess(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mProjectActivityUpdateDetailView.setProjectActivityMonitoringModel(projectActivityMonitoringModel);
    }

    protected void onProjectActivityMonitoringDetailRequestMessage(final String message) {

    }

    @Override
    public void onImageRequestDuplicate(final ImageView imageView, final ImageView duplicateImageView, final Integer fileId) {
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
                        File file = fileModel.getFile(getContext());
                        if (file != null) {
                            ImageUtil.setImageThumbnailView(getContext(), imageView, file);
                            if (duplicateImageView != null)
                                ImageUtil.setImageThumbnailView(getContext(), duplicateImageView, file);
                        }
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
                FileModel fileModel = null;
                if (fileRequestAsyncTaskResult != null) {
                    fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        File file = fileModel.getFile(getContext());
                        if (file != null) {
                            ImageUtil.setImageThumbnailView(getContext(), imageView, file);
                            if (duplicateImageView != null)
                                ImageUtil.setImageThumbnailView(getContext(), duplicateImageView, file);
                        }
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, fileModel));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, null));
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
