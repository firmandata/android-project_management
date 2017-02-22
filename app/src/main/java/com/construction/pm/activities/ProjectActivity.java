package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project.ProjectLayout;

public class ProjectActivity extends AppCompatActivity implements ProjectLayout.ProjectLayoutListener {

    public static final String INTENT_PARAM_PROJECT_MODEL = "PROJECT_MODEL";

    protected ProjectModel mProjectModel;

    protected ProjectLayout mProjectLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectLayout --
        mProjectLayout = ProjectLayout.buildProjectLayout(this, null);
        mProjectLayout.setProjectLayoutListener(this);

        // -- Load to Activity --
        mProjectLayout.loadLayoutToActivity(this, mProjectModel);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_MODEL));
                mProjectModel = ProjectModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onProjectRequest(ProjectModel projectModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectHandleTask --
        ProjectHandleTask projectHandleTask = new ProjectHandleTask() {
            @Override
            public void onPostExecute(ProjectHandleTaskResult projectHandleTaskResult) {
                if (projectHandleTaskResult != null) {
                    onProjectRequestSuccess(projectHandleTaskResult.getContractModel(), projectHandleTaskResult.getProjectModel(), projectHandleTaskResult.getProjectStageModels(), projectHandleTaskResult.getProjectPlanModels());
                    if (projectHandleTaskResult.getMessage() != null)
                        onProjectRequestMessage(projectHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectHandleTask --
        projectHandleTask.execute(new ProjectHandleTaskParam(this, settingUserModel, projectModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectRequestProgress(final String progressMessage) {

    }

    protected void onProjectRequestSuccess(final ContractModel contractModel, final ProjectModel projectModel, final ProjectStageModel[] projectStageModels, final ProjectPlanModel[] projectPlanModels) {
        mProjectLayout.setLayoutData(contractModel, projectModel, projectStageModels, projectPlanModels);
    }

    protected void onProjectRequestMessage(final String message) {

    }

    protected class ProjectHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectModel mProjectModel;
        protected ProjectMemberModel mProjectMemberModel;

        public ProjectHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectModel projectModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectModel = projectModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectModel getProjectModel() {
            return mProjectModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class ProjectHandleTaskResult {

        protected ContractModel mContractModel;
        protected ProjectModel mProjectModel;
        protected ProjectStageModel[] mProjectStageModels;
        protected ProjectPlanModel[] mProjectPlanModels;
        protected String mMessage;

        public ProjectHandleTaskResult() {

        }

        public void setContractModel(final ContractModel contractModel) {
            mContractModel = contractModel;
        }

        public ContractModel getContractModel() {
            return mContractModel;
        }

        public void setProjectModel(final ProjectModel projectModel) {
            mProjectModel = projectModel;
        }

        public ProjectModel getProjectModel() {
            return mProjectModel;
        }

        public void setProjectStageModels(final ProjectStageModel[] projectStageModels) {
            mProjectStageModels = projectStageModels;
        }

        public ProjectStageModel[] getProjectStageModels() {
            return mProjectStageModels;
        }

        public void setProjectPlanModels(final ProjectPlanModel[] projectPlanModels) {
            mProjectPlanModels = projectPlanModels;
        }

        public ProjectPlanModel[] getProjectPlanModels() {
            return mProjectPlanModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class ProjectHandleTask extends AsyncTask<ProjectHandleTaskParam, String, ProjectHandleTaskResult> {
        protected ProjectHandleTaskParam mProjectHandleTaskParam;
        protected Context mContext;

        @Override
        protected ProjectHandleTaskResult doInBackground(ProjectHandleTaskParam... projectHandleTaskParams) {
            // Get ProjectHandleTaskParam
            mProjectHandleTaskParam = projectHandleTaskParams[0];
            mContext = mProjectHandleTaskParam.getContext();

            // -- Prepare ProjectHandleTaskResult --
            ProjectHandleTaskResult projectHandleTaskResult = new ProjectHandleTaskResult();

            // -- Get ProjectResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_begin));

            // -- Prepare ProjectCachePersistent --
            ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

            // -- Prepare ProjectNetwork --
            ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectHandleTaskParam.getSettingUserModel());

            ProjectResponseModel projectResponseModel = null;
            try {
                ProjectModel projectModel = mProjectHandleTaskParam.getProjectModel();
                ProjectMemberModel projectMemberModel = mProjectHandleTaskParam.getProjectMemberModel();
                if (projectModel != null && projectMemberModel != null) {
                    try {
                        // -- Invalidate Access Token --
                        projectNetwork.invalidateAccessToken();

                        // -- Invalidate Login --
                        projectNetwork.invalidateLogin();

                        // -- Get ProjectResponseModel from server --
                        projectResponseModel = projectNetwork.getProject(projectModel.getProjectId());

                        // -- Save to ProjectCachePersistent --
                        try {
                            projectCachePersistent.setProjectResponseModel(projectResponseModel, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectResponseModel from ProjectCachePersistent --
                            try {
                                projectResponseModel = projectCachePersistent.getProjectResponseModel(projectModel.getProjectId(), projectMemberModel.getProjectMemberId());
                            } catch (PersistenceError ex) {
                            }
                        } else
                            throw webApiError;
                    }
                }
            } catch (WebApiError webApiError) {
                projectHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (projectResponseModel != null) {
                // -- Set result --
                projectHandleTaskResult.setContractModel(projectResponseModel.getContractModel());
                projectHandleTaskResult.setProjectModel(projectResponseModel.getProjectModel());
                projectHandleTaskResult.setProjectStageModels(projectResponseModel.getProjectStageModels());
                projectHandleTaskResult.setProjectPlanModels(projectResponseModel.getProjectPlanModels());

                // -- Get ProjectResponseModel progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_success));
            }

            return projectHandleTaskResult;
        }
    }
}
