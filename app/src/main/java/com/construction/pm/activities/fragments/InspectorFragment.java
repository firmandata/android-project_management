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
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.inspector.InspectorLayout;

public class InspectorFragment extends Fragment implements InspectorLayout.InspectorLayoutListener {
    protected InspectorLayout mInspectorLayout;

    public static InspectorFragment newInstance() {
        return new InspectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare InspectorLayout --
        mInspectorLayout = InspectorLayout.buildInspectorLayout(getContext(), null);
        mInspectorLayout.setInspectorLayoutListener(this);

        // -- Load to Fragment --
        mInspectorLayout.loadLayoutToFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load InspectorLayout to fragment --
        return mInspectorLayout.getLayout();
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
    public void onInspectorRequest() {
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
        projectActivityListHandleTask.execute(new ProjectActivityListHandleTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectActivityListRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityListRequestSuccess(final ProjectActivityModel[] projectActivityModels) {
        mInspectorLayout.setLayoutData(projectActivityModels);
    }

    protected void onProjectActivityListRequestFailed(final String errorMessage) {

    }

    protected class ProjectActivityListHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectMemberModel mProjectMemberModel;

        public ProjectActivityListHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel projectMemberModel) {
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
                        projectActivityModels = inspectorNetwork.getProjectActivities(projectMemberModel.getProjectMemberId());

                        // -- Save to InspectorCachePersistent --
                        try {
                            inspectorCachePersistent.setProjectActivityModels(projectActivityModels, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectActivityModels from InspectorCachePersistent --
                            try {
                                projectActivityModels = inspectorCachePersistent.getProjectActivityModels(projectMemberModel.getProjectMemberId());
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
    public void onDetach() {
        super.onDetach();
    }
}
