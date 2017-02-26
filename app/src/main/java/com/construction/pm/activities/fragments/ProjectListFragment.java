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
import com.construction.pm.asynctask.ProjectListAsyncTask;
import com.construction.pm.asynctask.param.ProjectListAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectListAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project.ProjectListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectListFragment extends Fragment implements ProjectListView.ProjectListListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectListView mProjectListView;

    public static ProjectListFragment newInstance() {
        return new ProjectListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

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

        // -- Prepare ProjectListAsyncTask --
        ProjectListAsyncTask projectListAsyncTask = new ProjectListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectListAsyncTaskResult projectListHandleTaskResult) {
                mAsyncTaskList.remove(this);

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

        // -- Do ProjectListAsyncTask --
        projectListAsyncTask.execute(new ProjectListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectItemClick(ProjectModel projectModel) {
        // -- Redirect to ProjectActivity --
        Intent intent = new Intent(this.getContext(), ProjectActivity.class);

        try {
            org.json.JSONObject projectModelJsonObject = projectModel.build();
            String projectModelJson = projectModelJsonObject.toString(0);

            intent.putExtra(ProjectActivity.INTENT_PARAM_PROJECT_MODEL, projectModelJson);
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
