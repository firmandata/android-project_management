package com.construction.pm.activities.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import com.construction.pm.asynctask.ManagerProjectActivityMonitoringListAsyncTask;
import com.construction.pm.asynctask.param.ManagerProjectActivityMonitoringListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityMonitoringListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;

public class ProjectActivityUpdateMonitoringListFragment extends ProjectActivityMonitoringListFragment {

    public static ProjectActivityUpdateMonitoringListFragment newInstance() {
        return newInstance(null);
    }

    public static ProjectActivityUpdateMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel) {
        return newInstance(projectActivityModel, null);
    }

    public static ProjectActivityUpdateMonitoringListFragment newInstance(final ProjectActivityModel projectActivityModel, final ProjectActivityMonitoringListFragmentListener projectActivityMonitoringListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityModel != null) {
            try {
                String projectActivityModelJson = projectActivityModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityUpdateMonitoringListFragment --
        ProjectActivityUpdateMonitoringListFragment projectActivityUpdateMonitoringListFragment = new ProjectActivityUpdateMonitoringListFragment();
        projectActivityUpdateMonitoringListFragment.setArguments(bundle);
        projectActivityUpdateMonitoringListFragment.setProjectActivityMonitoringListFragmentListener(projectActivityMonitoringListFragmentListener);
        return projectActivityUpdateMonitoringListFragment;
    }

    @Override
    public void onProjectActivityMonitoringListRequest(ProjectActivityModel projectActivityModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ManagerProjectActivityMonitoringListAsyncTask --
        ManagerProjectActivityMonitoringListAsyncTask managerProjectActivityMonitoringListAsyncTask = new ManagerProjectActivityMonitoringListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ManagerProjectActivityMonitoringListAsyncTaskResult managerProjectActivityMonitoringListAsyncTask) {
                mAsyncTaskList.remove(this);

                if (managerProjectActivityMonitoringListAsyncTask != null) {
                    onProjectActivityMonitoringListRequestSuccess(managerProjectActivityMonitoringListAsyncTask.getProjectActivityMonitoringModels());
                    if (managerProjectActivityMonitoringListAsyncTask.getMessage() != null)
                        onProjectActivityMonitoringListRequestMessage(managerProjectActivityMonitoringListAsyncTask.getMessage());
                } else {
                    onProjectActivityMonitoringListRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityMonitoringListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ManagerProjectActivityMonitoringListAsyncTask --
        managerProjectActivityMonitoringListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ManagerProjectActivityMonitoringListAsyncTaskParam(getContext(), settingUserModel, projectActivityModel, sessionLoginModel.getProjectMemberModel()));
    }
}
