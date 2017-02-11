package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.ProjectActivity;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectPersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project.ProjectListView;

public class ProjectListFragment extends Fragment implements ProjectListView.ProjectListListener {
    protected ProjectListView mProjectListView;

    public static ProjectListFragment newInstance() {
        return new ProjectListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare ProjectListView --
        mProjectListView = ProjectListView.buildProjectListView(getContext(), null);
        mProjectListView.setProjectListListener(this);

        // -- Load ProjectList --
        onProjectListRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectListView to fragment --
        return mProjectListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectListRequest() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectListHandleTask --
        ProjectListHandleTask projectListHandleTask = new ProjectListHandleTask() {
            @Override
            public void onPostExecute(ProjectListHandleTaskResult projectListHandleTaskResult) {
                if (projectListHandleTaskResult != null) {
                    ProjectModel[] projectModels = projectListHandleTaskResult.getProjectModels();
                    if (projectModels != null)
                        onProjectListRequestSuccess(projectModels);
                    else
                        onProjectListRequestFailed(projectListHandleTaskResult.getMessage());
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

        // -- Do ProjectListHandleTask --
        projectListHandleTask.execute(new ProjectListHandleTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectItemClick(ProjectModel projectModel) {
        // -- Redirect to ProjectActivity --
        Intent intent = new Intent(this.getContext(), ProjectActivity.class);

        try {
            org.json.JSONObject projectModelJsonObject = projectModel.build();
            String projectModelJson = projectModelJsonObject.toString(0);

            intent.putExtra(ProjectActivity.PARAM_PROJECT_MODEL, projectModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivity(intent);
    }

    protected void onProjectListRequestProgress(final String progressMessage) {
        mProjectListView.startRefreshAnimation();
    }

    protected void onProjectListRequestSuccess(final ProjectModel[] projectModels) {
        mProjectListView.setProjectModels(projectModels);
        mProjectListView.stopRefreshAnimation();
    }

    protected void onProjectListRequestFailed(final String errorMessage) {
        mProjectListView.stopRefreshAnimation();
    }

    protected class ProjectListHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectMemberModel mProjectMemberModel;

        public ProjectListHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class ProjectListHandleTaskResult {

        protected ProjectModel[] mProjectModels;
        protected String mMessage;

        public ProjectListHandleTaskResult() {

        }

        public void setProjectModels(final ProjectModel[] projectModels) {
            mProjectModels = projectModels;
        }

        public ProjectModel[] getProjectModels() {
            return mProjectModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class ProjectListHandleTask extends AsyncTask<ProjectListHandleTaskParam, String, ProjectListHandleTaskResult> {
        protected ProjectListHandleTaskParam mProjectListHandleTaskParam;
        protected Context mContext;

        @Override
        protected ProjectListHandleTaskResult doInBackground(ProjectListHandleTaskParam... projectListHandleTaskParams) {
            // Get ProjectListHandleTaskParam
            mProjectListHandleTaskParam = projectListHandleTaskParams[0];
            mContext = mProjectListHandleTaskParam.getContext();

            // -- Prepare ProjectListHandleTaskResult --
            ProjectListHandleTaskResult projectListHandleTaskResult = new ProjectListHandleTaskResult();

            // -- Get ProjectModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_list_handle_task_begin));

            // -- Prepare ProjectPersistent --
            ProjectPersistent projectPersistent = new ProjectPersistent(mContext);

            // -- Prepare ProjectNetwork --
            ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectListHandleTaskParam.getSettingUserModel());

            ProjectModel[] projectModels = null;
            try {
                ProjectMemberModel projectMemberModel = mProjectListHandleTaskParam.getProjectMemberModel();
                if (projectMemberModel != null) {
                    try {
                        // -- Invalidate Access Token --
                        projectNetwork.invalidateAccessToken();

                        // -- Invalidate Login --
                        projectNetwork.invalidateLogin();

                        // -- Get projects from server --
                        projectModels = projectNetwork.getProjects(projectMemberModel.getProjectMemberId());

                        // -- Save to ProjectPersistent --
                        try {
                            projectPersistent.setProjectModels(projectModels, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectModels from ProjectPersistent --
                            try {
                                projectModels = projectPersistent.getProjectModels(projectMemberModel.getProjectMemberId());
                            } catch (PersistenceError ex) {
                            }
                        } else
                            throw webApiError;
                    }
                }
            } catch (WebApiError webApiError) {
                projectListHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (projectModels != null) {
                // -- Set ProjectModels to result --
                projectListHandleTaskResult.setProjectModels(projectModels);

                // -- Get ProjectModels progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.project_list_handle_task_success));
            }

            return projectListHandleTaskResult;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
