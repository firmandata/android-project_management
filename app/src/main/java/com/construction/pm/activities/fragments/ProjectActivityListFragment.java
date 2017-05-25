package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.InspectorProjectActivityListAsyncTask;
import com.construction.pm.asynctask.ManagerProjectActivityListAsyncTask;
import com.construction.pm.asynctask.param.InspectorProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.param.ManagerProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityListAsyncTaskResult;
import com.construction.pm.asynctask.result.ManagerProjectActivityListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityListTypeEnum;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.project_activity.ProjectActivityListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityListFragment extends Fragment implements ProjectActivityListView.ProjectActivityListListener {
    public static final String PARAM_PROJECT_ACTIVITY_MODELS = "ProjectActivityModels";
    public static final String PARAM_STATUS_TASK_ENUM = "StatusTaskEnum";
    public static final String PARAM_PROJECT_ACTIVITY_LIST_TYPE_ENUM = "ProjectActivityListTypeEnum";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityListView mProjectActivityListView;

    protected ProjectActivityListFragmentListener mProjectActivityListFragmentListener;

    public static ProjectActivityListFragment newInstance(final ProjectActivityListTypeEnum projectActivityListTypeEnum) {
        return newInstance(projectActivityListTypeEnum, null, null);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityListTypeEnum projectActivityListTypeEnum, final ProjectActivityModel[] projectActivityModels) {
        return newInstance(projectActivityListTypeEnum, null, projectActivityModels);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityListTypeEnum projectActivityListTypeEnum, final StatusTaskEnum statusTaskEnum) {
        return newInstance(projectActivityListTypeEnum, statusTaskEnum, null);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityListTypeEnum projectActivityListTypeEnum, final StatusTaskEnum statusTaskEnum, final ProjectActivityModel[] projectActivityModels) {
        return newInstance(projectActivityListTypeEnum, statusTaskEnum, projectActivityModels, null);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityListTypeEnum projectActivityListTypeEnum, final StatusTaskEnum statusTaskEnum, final ProjectActivityModel[] projectActivityModels, final ProjectActivityListFragmentListener projectActivityListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectActivityListTypeEnum != null)
            bundle.putString(PARAM_PROJECT_ACTIVITY_LIST_TYPE_ENUM, projectActivityListTypeEnum.getValue());
        if (statusTaskEnum != null)
            bundle.putString(PARAM_STATUS_TASK_ENUM, statusTaskEnum.getValue());
        if (projectActivityModels != null) {
            try {
                org.json.JSONArray projectActivityModelsJsonArray = new org.json.JSONArray();
                for (ProjectActivityModel projectActivityModel : projectActivityModels) {
                    projectActivityModelsJsonArray.put(projectActivityModel.build());
                }
                String projectActivityModelsJson = projectActivityModelsJsonArray.toString(0);
                bundle.putString(PARAM_PROJECT_ACTIVITY_MODELS, projectActivityModelsJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectActivityListFragment --
        ProjectActivityListFragment projectActivityListFragment = new ProjectActivityListFragment();
        projectActivityListFragment.setProjectActivityListFragmentListener(projectActivityListFragmentListener);
        projectActivityListFragment.setArguments(bundle);
        return projectActivityListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectActivityListTypeEnum projectActivityListType = null;
        StatusTaskEnum statusTask = null;
        ProjectActivityModel[] projectActivityModels = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectActivityListTypeEnum parameter --
            String projectActivityListTypeEnumValue = bundle.getString(PARAM_PROJECT_ACTIVITY_LIST_TYPE_ENUM);
            if (projectActivityListTypeEnumValue != null)
                projectActivityListType = ProjectActivityListTypeEnum.fromString(projectActivityListTypeEnumValue);

            // -- Get StatusTaskEnum parameter --
            String statusTaskEnumValue = bundle.getString(PARAM_STATUS_TASK_ENUM);
            if (statusTaskEnumValue != null)
                statusTask = StatusTaskEnum.fromString(statusTaskEnumValue);

            // -- Get ProjectActivityModels parameter --
            String projectActivityModelsJson = bundle.getString(PARAM_PROJECT_ACTIVITY_MODELS);
            if (projectActivityModelsJson != null) {
                try {
                    List<ProjectActivityModel> projectActivityModelList = new ArrayList<ProjectActivityModel>();
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectActivityModelsJson);
                    for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                        ProjectActivityModel projectActivityModel = ProjectActivityModel.build(jsonArray.getJSONObject(jsonArrayIndex));
                        projectActivityModelList.add(projectActivityModel);
                    }
                    if (projectActivityModelList.size() > 0) {
                        projectActivityModels = new ProjectActivityModel[projectActivityModelList.size()];
                        projectActivityModelList.toArray(projectActivityModels);
                    }
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ProjectActivityListView --
        mProjectActivityListView = ProjectActivityListView.buildProjectActivityListView(getContext(), null);
        mProjectActivityListView.setProjectActivityListListener(this);
        mProjectActivityListView.setProjectActivityListType(projectActivityListType);
        mProjectActivityListView.setStatusTask(statusTask);
        mProjectActivityListView.setProjectActivityModels(projectActivityModels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectActivityListView to fragment --
        return mProjectActivityListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectActivityListRequest(ProjectActivityListTypeEnum projectActivityListType, StatusTaskEnum statusTask) {
        if (projectActivityListType == null)
            return;

        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        if (projectActivityListType == ProjectActivityListTypeEnum.INSPECTOR) {
            // -- Prepare InspectorProjectActivityListAsyncTask --
            InspectorProjectActivityListAsyncTask inspectorProjectActivityListAsyncTask = new InspectorProjectActivityListAsyncTask() {
                @Override
                public void onPreExecute() {
                    mAsyncTaskList.add(this);
                }

                @Override
                public void onPostExecute(InspectorProjectActivityListAsyncTaskResult inspectorProjectActivityListAsyncTaskResult) {
                    mAsyncTaskList.remove(this);

                    if (inspectorProjectActivityListAsyncTaskResult != null) {
                        ProjectActivityModel[] projectActivityModels = inspectorProjectActivityListAsyncTaskResult.getProjectActivityModels();
                        if (projectActivityModels != null)
                            onProjectActivityListRequestSuccess(projectActivityModels);
                        else
                            onProjectActivityListRequestFailed(inspectorProjectActivityListAsyncTaskResult.getMessage());
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

            // -- Do InspectorProjectActivityListAsyncTask --
            inspectorProjectActivityListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new InspectorProjectActivityListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel(), statusTask));
        } else if (projectActivityListType == ProjectActivityListTypeEnum.MANAGER) {
            // -- Prepare ManagerProjectActivityListAsyncTask --
            ManagerProjectActivityListAsyncTask managerProjectActivityListAsyncTask = new ManagerProjectActivityListAsyncTask() {
                @Override
                public void onPreExecute() {
                    mAsyncTaskList.add(this);
                }

                @Override
                public void onPostExecute(ManagerProjectActivityListAsyncTaskResult managerProjectActivityListAsyncTaskResult) {
                    mAsyncTaskList.remove(this);

                    if (managerProjectActivityListAsyncTaskResult != null) {
                        ProjectActivityModel[] projectActivityModels = managerProjectActivityListAsyncTaskResult.getProjectActivityModels();
                        if (projectActivityModels != null)
                            onProjectActivityListRequestSuccess(projectActivityModels);
                        else
                            onProjectActivityListRequestFailed(managerProjectActivityListAsyncTaskResult.getMessage());
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
            managerProjectActivityListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ManagerProjectActivityListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel(), statusTask));
        }
    }

    protected void onProjectActivityListRequestProgress(final String progressMessage) {
        mProjectActivityListView.startRefreshAnimation();
    }

    protected void onProjectActivityListRequestSuccess(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListView.setProjectActivityModels(projectActivityModels);
        mProjectActivityListView.stopRefreshAnimation();
    }

    protected void onProjectActivityListRequestFailed(final String errorMessage) {
        mProjectActivityListView.stopRefreshAnimation();
    }

    @Override
    public void onProjectActivityItemClick(ProjectActivityModel projectActivityModel) {
        if (mProjectActivityListFragmentListener != null)
            mProjectActivityListFragmentListener.onProjectActivityClick(projectActivityModel);
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

    public void addProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListView.addProjectActivityModels(projectActivityModels);
    }

    public void removeProjectActivityModels(final ProjectActivityModel[] projectActivityModels) {
        mProjectActivityListView.removeProjectActivityModels(projectActivityModels);
    }

    public ProjectActivityModel[] getProjectActivityModels() {
        return mProjectActivityListView.getProjectActivityModels();
    }

    public StatusTaskEnum getStatusTask() {
        return mProjectActivityListView.getStatusTask();
    }

    public void setProjectActivityListFragmentListener(final ProjectActivityListFragmentListener projectActivityListFragmentListener) {
        mProjectActivityListFragmentListener = projectActivityListFragmentListener;
    }

    public interface ProjectActivityListFragmentListener {
        void onProjectActivityClick(ProjectActivityModel projectActivityModel);
    }
}
