package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.ManagerProjectActivityUpdateListAsyncTask;
import com.construction.pm.asynctask.param.ManagerProjectActivityUpdateListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityUpdateListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.project_activity.ProjectActivityUpdateListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateListFragment extends Fragment implements
        ProjectActivityUpdateListView.ProjectActivityUpdateListListener {

    public static final String PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateListView mProjectActivityUpdateListView;

    protected ProjectActivityUpdateListFragmentListener mProjectActivityUpdateListFragmentListener;

    public static ProjectActivityUpdateListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityUpdateListFragment newInstance(final ProjectActivityModel projectActivityModel) {
        return newInstance(projectActivityModel, null);
    }

    public static ProjectActivityUpdateListFragment newInstance(final ProjectActivityModel projectActivityModel, final ProjectActivityUpdateListFragmentListener projectActivityUpdateListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                String projectActivityModelJson = projectActivityModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityUpdateListFragment --
        ProjectActivityUpdateListFragment projectActivityUpdateListFragment = new ProjectActivityUpdateListFragment();
        projectActivityUpdateListFragment.setArguments(bundle);
        projectActivityUpdateListFragment.setProjectActivityUpdateListFragmentListener(projectActivityUpdateListFragmentListener);
        return projectActivityUpdateListFragment;
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

        // -- Prepare ProjectActivityUpdateListView --
        mProjectActivityUpdateListView = ProjectActivityUpdateListView.buildProjectActivityUpdateListView(getContext(), null);
        mProjectActivityUpdateListView.setProjectActivityModel(projectActivityModel);
        mProjectActivityUpdateListView.setProjectActivityUpdateListListener(this);

        // -- Load ProjectActivityUpdateList --
        onProjectActivityUpdateListRequest(projectActivityModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityUpdateList to fragment --
        return mProjectActivityUpdateListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectActivityUpdateListRequest(ProjectActivityModel projectActivityModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ManagerProjectActivityUpdateListAsyncTask --
        ManagerProjectActivityUpdateListAsyncTask managerProjectActivityUpdateListAsyncTask = new ManagerProjectActivityUpdateListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ManagerProjectActivityUpdateListAsyncTaskResult managerProjectActivityUpdateListAsyncTask) {
                mAsyncTaskList.remove(this);

                if (managerProjectActivityUpdateListAsyncTask != null) {
                    onProjectListRequestSuccess(managerProjectActivityUpdateListAsyncTask.getProjectActivityUpdateModels());
                    if (managerProjectActivityUpdateListAsyncTask.getMessage() != null)
                        onProjectListRequestMessage(managerProjectActivityUpdateListAsyncTask.getMessage());
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

        // -- Do ManagerProjectActivityUpdateListAsyncTask --
        managerProjectActivityUpdateListAsyncTask.execute(new ManagerProjectActivityUpdateListAsyncTaskParam(getContext(), settingUserModel, projectActivityModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (mProjectActivityUpdateListFragmentListener != null)
            mProjectActivityUpdateListFragmentListener.onProjectActivityUpdateListItemClick(projectActivityUpdateModel);
    }

    protected void onProjectListRequestProgress(final String progressMessage) {

    }

    protected void onProjectListRequestSuccess(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectActivityUpdateListView.setProjectActivityUpdateModels(projectActivityUpdateModels);
    }

    protected void onProjectListRequestMessage(final String message) {

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

    public void setProjectActivityUpdateListFragmentListener(final ProjectActivityUpdateListFragmentListener projectActivityUpdateListFragmentListener) {
        mProjectActivityUpdateListFragmentListener = projectActivityUpdateListFragmentListener;
    }

    public interface ProjectActivityUpdateListFragmentListener {
        void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel);
    }
}