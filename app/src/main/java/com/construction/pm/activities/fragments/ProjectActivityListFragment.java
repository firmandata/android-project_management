package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project_activity.ProjectActivityListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityListFragment extends Fragment implements ProjectActivityListView.ProjectActivityListListener {
    public static final String PARAM_PROJECT_ACTIVITY_MODELS = "ProjectActivityModels";
    public static final String PARAM_STATUS_TASK_ENUM = "StatusTaskEnum";

    protected ProjectActivityListView mProjectActivityListView;

    public static ProjectActivityListFragment newInstance() {
        return newInstance(null, null);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityModel[] projectActivityModels) {
        return newInstance(projectActivityModels, null);
    }

    public static ProjectActivityListFragment newInstance(final StatusTaskEnum statusTaskEnum) {
        return newInstance(null, statusTaskEnum);
    }

    public static ProjectActivityListFragment newInstance(final ProjectActivityModel[] projectActivityModels, final StatusTaskEnum statusTaskEnum) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
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
        if (statusTaskEnum != null)
            bundle.putString(PARAM_STATUS_TASK_ENUM, statusTaskEnum.getValue());

        // -- Create ProjectActivityListFragment --
        ProjectActivityListFragment projectActivityListFragment = new ProjectActivityListFragment();
        projectActivityListFragment.setArguments(bundle);
        return projectActivityListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectActivityModel[] projectActivityModels = null;
        StatusTaskEnum statusTaskEnum = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
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

            // -- Get StatusTaskEnum parameter --
            String statusTaskEnumValue = bundle.getString(PARAM_STATUS_TASK_ENUM);
            if (statusTaskEnumValue != null)
                statusTaskEnum = StatusTaskEnum.fromString(statusTaskEnumValue);
        }

        // -- Prepare ProjectActivityListView --
        mProjectActivityListView = ProjectActivityListView.buildProjectActivityListView(getContext(), null);
        mProjectActivityListView.setProjectActivityListListener(this);
        mProjectActivityListView.setStatusTaskEnum(statusTaskEnum);
        if (projectActivityModels != null)
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
    public void onProjectActivityListRequest(StatusTaskEnum statusTaskEnum) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectActivityListHandleTask --
        ProjectActivityListHandleTask projectActivityListHandleTask = new ProjectActivityListHandleTask() {
            @Override
            public void onPostExecute(ProjectActivityListHandleTaskResult projectActivityListHandleTaskResult) {
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

        // -- Do ProjectActivityListHandleTask --
        projectActivityListHandleTask.execute(new ProjectActivityListHandleTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel(), statusTaskEnum));
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

    protected class ProjectActivityListHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectMemberModel mProjectMemberModel;
        protected StatusTaskEnum mStatusTaskEnum;

        public ProjectActivityListHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel, final StatusTaskEnum statusTaskEnum) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectMemberModel = projectMemberModel;
            mStatusTaskEnum = statusTaskEnum;
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

        public StatusTaskEnum getStatusTaskEnum() {
            return mStatusTaskEnum;
        }
    }

    protected class ProjectActivityListHandleTaskResult {

        protected ProjectActivityModel[] mProjectActivityModels;
        protected String mMessage;

        public ProjectActivityListHandleTaskResult() {

        }

        public void setProjectActivityModels(final ProjectActivityModel[] projectPlanModels) {
            mProjectActivityModels = projectPlanModels;
        }

        public ProjectActivityModel[] getProjectActivityModels() {
            return mProjectActivityModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class ProjectActivityListHandleTask extends AsyncTask<ProjectActivityListHandleTaskParam, String, ProjectActivityListHandleTaskResult> {
        protected ProjectActivityListHandleTaskParam mProjectActivityListHandleTaskParam;
        protected Context mContext;

        @Override
        protected ProjectActivityListHandleTaskResult doInBackground(ProjectActivityListHandleTaskParam... projectActivityListHandleTaskParams) {
            // Get ProjectActivityListHandleTaskParam
            mProjectActivityListHandleTaskParam = projectActivityListHandleTaskParams[0];
            mContext = mProjectActivityListHandleTaskParam.getContext();

            // -- Prepare ProjectActivityListHandleTaskResult --
            ProjectActivityListHandleTaskResult projectActivityListHandleTaskResult = new ProjectActivityListHandleTaskResult();

            // -- Get ProjectActivityModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_list_handle_task_begin));

            // -- Prepare InspectorCachePersistent --
            InspectorCachePersistent inspectorCachePersistent = new InspectorCachePersistent(mContext);

            // -- Prepare InspectorNetwork --
            InspectorNetwork inspectorNetwork = new InspectorNetwork(mContext, mProjectActivityListHandleTaskParam.getSettingUserModel());

            ProjectActivityModel[] projectActivityModels = null;
            try {
                ProjectMemberModel projectMemberModel = mProjectActivityListHandleTaskParam.getProjectMemberModel();
                if (projectMemberModel != null) {
                    try {
                        // -- Invalidate Access Token --
                        inspectorNetwork.invalidateAccessToken();

                        // -- Invalidate Login --
                        inspectorNetwork.invalidateLogin();

                        // -- Get ProjectActivityModels from server --
                        projectActivityModels = inspectorNetwork.getProjectActivities(projectMemberModel.getProjectMemberId(), mProjectActivityListHandleTaskParam.getStatusTaskEnum());

                        // -- Save to InspectorCachePersistent --
                        try {
                            inspectorCachePersistent.setProjectActivityModels(projectActivityModels, mProjectActivityListHandleTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectActivityModels from InspectorCachePersistent --
                            try {
                                projectActivityModels = inspectorCachePersistent.getProjectActivityModels(mProjectActivityListHandleTaskParam.getStatusTaskEnum(), projectMemberModel.getProjectMemberId());
                            } catch (PersistenceError ex) {
                            }
                        } else
                            throw webApiError;
                    }
                }
            } catch (WebApiError webApiError) {
                projectActivityListHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (projectActivityModels != null) {
                // -- Set result --
                projectActivityListHandleTaskResult.setProjectActivityModels(projectActivityModels);

                // -- Get ProjectActivityModels progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.inspector_activity_list_handle_task_success));
            }

            return projectActivityListHandleTaskResult;
        }
    }

    @Override
    public void onProjectActivityItemClick(ProjectActivityModel projectActivityModel) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
