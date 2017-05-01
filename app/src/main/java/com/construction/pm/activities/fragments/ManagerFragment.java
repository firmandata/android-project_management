package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.ManagerDetailActivity;
import com.construction.pm.asynctask.ManagerProjectActivityListAsyncTask;
import com.construction.pm.asynctask.param.ManagerProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.manager.ManagerLayout;

import java.util.ArrayList;
import java.util.List;

public class ManagerFragment extends Fragment implements ManagerLayout.ManagerLayoutListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected ManagerLayout mManagerLayout;

    public static ManagerFragment newInstance() {
        return new ManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare ManagerLayout --
        mManagerLayout = ManagerLayout.buildManagerLayout(getContext(), null);
        mManagerLayout.setManagerLayoutListener(this);

        // -- Load to Fragment --
        mManagerLayout.loadLayoutToFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ManagerLayout to fragment --
        return mManagerLayout.getLayout();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY_RESULT_CHANGED) {
                // -- Get ProjectActivityModel --
                ProjectActivityModel projectActivityModel = null;
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MODEL)) {
                        String projectActivityModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MODEL);
                        if (projectActivityModelJson != null) {
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                                projectActivityModel = ProjectActivityModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                        }
                    }
                }
                if (projectActivityModel != null) {
                    mManagerLayout.replaceLayoutData(new ProjectActivityModel[] { projectActivityModel });
                }
            }
        }
    }

    @Override
    public void onManagerRequest() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ManagerProjectActivityListAsyncTask --
        ManagerProjectActivityListAsyncTask managerProjectActivityListAsyncTask = new ManagerProjectActivityListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ManagerProjectActivityListAsyncTaskResult projectActivityListHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectActivityListHandleTaskResult != null) {
                    ProjectActivityModel[] projectActivityModels = projectActivityListHandleTaskResult.getProjectActivityModels();
                    if (projectActivityModels != null)
                        onProjectActivityListRequestSuccess(projectActivityModels);
                    else
                        onProjectActivityListRequestFailed(projectActivityListHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ManagerProjectActivityListAsyncTask --
        managerProjectActivityListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ManagerProjectActivityListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectActivityListItemClick(ProjectActivityModel projectActivityModel) {
        // -- Redirect to ManagerDetailActivity --
        Intent intent = new Intent(this.getContext(), ManagerDetailActivity.class);

        try {
            org.json.JSONObject projectActivityModelJsonObject = projectActivityModel.build();
            String projectActivityModelJson = projectActivityModelJsonObject.toString(0);

            intent.putExtra(ManagerDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY);
    }

    protected void onProjectActivityListRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityListRequestSuccess(final ProjectActivityModel[] projectActivityModels) {
        mManagerLayout.setLayoutData(projectActivityModels);
    }

    protected void onProjectActivityListRequestFailed(final String errorMessage) {

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
