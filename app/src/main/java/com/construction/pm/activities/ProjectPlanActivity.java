package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectPlanAssignmentModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.network.ProjectPlanResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project_plan.ProjectPlanLayout;

public class ProjectPlanActivity extends AppCompatActivity implements ProjectPlanLayout.ProjectPlanLayoutListener {

    public static final String INTENT_PARAM_PROJECT_PLAN_MODEL = "PROJECT_PLAN_MODEL";

    protected ProjectPlanModel mProjectPlanModel;

    protected ProjectPlanLayout mProjectPlanLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectPlanLayout --
        mProjectPlanLayout = ProjectPlanLayout.buildProjectPlanLayout(this, null);
        mProjectPlanLayout.setProjectPlanLayoutListener(this);

        // -- Load to Activity --
        mProjectPlanLayout.loadLayoutToActivity(this, mProjectPlanModel);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectPlanModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_PLAN_MODEL));
                mProjectPlanModel = ProjectPlanModel.build(jsonObject);
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
    public void onProjectPlanRequest(ProjectPlanModel projectPlanModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectPlanHandleTask --
        ProjectPlanHandleTask projectPlanHandleTask = new ProjectPlanHandleTask() {
            @Override
            public void onPostExecute(ProjectPlanHandleTaskResult projectHandleTaskResult) {
                if (projectHandleTaskResult != null) {
                    onProjectPlanRequestSuccess(projectHandleTaskResult.getProjectModel(), projectHandleTaskResult.getProjectPlanAssignmentModels(), projectHandleTaskResult.getProjectActivityUpdateModels());
                    if (projectHandleTaskResult.getMessage() != null)
                        onProjectPlanRequestMessage(projectHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectPlanRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectPlanHandleTask --
        projectPlanHandleTask.execute(new ProjectPlanHandleTaskParam(this, settingUserModel, projectPlanModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectPlanRequestProgress(final String progressMessage) {

    }

    protected void onProjectPlanRequestSuccess(final ProjectPlanModel projectPlanModel, final ProjectPlanAssignmentModel[] projectPlanAssignmentModels, final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectPlanLayout.setLayoutData(projectPlanModel, projectPlanAssignmentModels, projectActivityUpdateModels);
    }

    protected void onProjectPlanRequestMessage(final String message) {

    }

    protected class ProjectPlanHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectPlanModel mProjectPlanModel;
        protected ProjectMemberModel mProjectMemberModel;

        public ProjectPlanHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectPlanModel projectPlanModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectPlanModel = projectPlanModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectPlanModel getProjectPlanModel() {
            return mProjectPlanModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class ProjectPlanHandleTaskResult {

        protected ProjectPlanModel mProjectPlanModel;
        protected ProjectPlanAssignmentModel[] mProjectPlanAssignmentModels;
        protected ProjectActivityUpdateModel[] mProjectActivityUpdateModels;
        protected String mMessage;

        public ProjectPlanHandleTaskResult() {

        }

        public void setProjectModel(final ProjectPlanModel projectModel) {
            mProjectPlanModel = projectModel;
        }

        public ProjectPlanModel getProjectModel() {
            return mProjectPlanModel;
        }

        public void setProjectPlanAssignmentModels(final ProjectPlanAssignmentModel[] projectPlanModels) {
            mProjectPlanAssignmentModels = projectPlanModels;
        }

        public ProjectPlanAssignmentModel[] getProjectPlanAssignmentModels() {
            return mProjectPlanAssignmentModels;
        }

        public void setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
            mProjectActivityUpdateModels = projectActivityUpdateModels;
        }

        public ProjectActivityUpdateModel[] getProjectActivityUpdateModels() {
            return mProjectActivityUpdateModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class ProjectPlanHandleTask extends AsyncTask<ProjectPlanHandleTaskParam, String, ProjectPlanHandleTaskResult> {
        protected ProjectPlanHandleTaskParam mProjectPlanHandleTaskParam;
        protected Context mContext;

        @Override
        protected ProjectPlanHandleTaskResult doInBackground(ProjectPlanHandleTaskParam... projectPlanHandleTaskParams) {
            // Get ProjectPlanHandleTaskParam
            mProjectPlanHandleTaskParam = projectPlanHandleTaskParams[0];
            mContext = mProjectPlanHandleTaskParam.getContext();

            // -- Prepare ProjectPlanHandleTaskResult --
            ProjectPlanHandleTaskResult projectPlanHandleTaskResult = new ProjectPlanHandleTaskResult();

            // -- Get ProjectPlanResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_plan_handle_task_begin));

            // -- Prepare ProjectCachePersistent --
            ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

            // -- Prepare ProjectNetwork --
            ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectPlanHandleTaskParam.getSettingUserModel());

            ProjectPlanResponseModel projectPlanResponseModel = null;
            try {
                ProjectPlanModel projectPlanModel = mProjectPlanHandleTaskParam.getProjectPlanModel();
                ProjectMemberModel projectMemberModel = mProjectPlanHandleTaskParam.getProjectMemberModel();
                if (projectPlanModel != null && projectMemberModel != null) {
                    try {
                        // -- Invalidate Access Token --
                        projectNetwork.invalidateAccessToken();

                        // -- Invalidate Login --
                        projectNetwork.invalidateLogin();

                        // -- Get project from server --
                        projectPlanResponseModel = projectNetwork.getProjectPlan(projectPlanModel.getProjectPlanId());

                        // -- Save to ProjectCachePersistent --
                        try {
                            projectCachePersistent.setProjectPlanResponseModel(projectPlanResponseModel, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectPlanResponseModel from ProjectCachePersistent --
                            try {
                                projectPlanResponseModel = projectCachePersistent.getProjectPlanResponseModel(projectPlanModel.getProjectPlanId(), projectMemberModel.getProjectMemberId());
                            } catch (PersistenceError ex) {
                            }
                        } else
                            throw webApiError;
                    }
                }
            } catch (WebApiError webApiError) {
                projectPlanHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (projectPlanResponseModel != null) {
                // -- Set result --
                projectPlanHandleTaskResult.setProjectModel(projectPlanResponseModel.getProjectPlanModel());
                projectPlanHandleTaskResult.setProjectPlanAssignmentModels(projectPlanResponseModel.getProjectPlanAssignmentModels());
                projectPlanHandleTaskResult.setProjectActivityUpdateModels(projectPlanResponseModel.getProjectActivityUpdateModels());

                // -- Get ProjectPlanResponseModel progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.project_plan_handle_task_success));
            }

            return projectPlanHandleTaskResult;
        }
    }
}
