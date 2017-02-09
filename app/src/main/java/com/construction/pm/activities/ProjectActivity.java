package com.construction.pm.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.project.ProjectLayout;

public class ProjectActivity extends AppCompatActivity implements ProjectLayout.ProjectLayoutListener {

    public static final String PARAM_PROJECT_MODEL = "ProjectModel";

    protected ProjectModel mProjectModel;

    protected ProjectLayout mProjectLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Get parameters --
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // -- Get ProjectModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(PARAM_PROJECT_MODEL));
                mProjectModel = ProjectModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Prepare ProjectLayout --
        mProjectLayout = ProjectLayout.buildProjectDetailLayout(this, null);
        mProjectLayout.setProjectModel(mProjectModel);
        mProjectLayout.setProjectLayoutListener(this);

        // -- Load to Activity --
        mProjectLayout.loadLayoutToActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onProjectRequest(ProjectModel projectModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare ProjectHandleTask --
        ProjectHandleTask projectHandleTask = new ProjectHandleTask() {
            @Override
            public void onPostExecute(ProjectHandleTaskResult projectHandleTaskResult) {
                if (projectHandleTaskResult != null) {
                    if (projectHandleTaskResult.getContractModel() != null)
                        onProjectRequestContract(projectHandleTaskResult.getContractModel());
                    if (projectHandleTaskResult.getProjectModel() != null)
                        onProjectRequestProject(projectHandleTaskResult.getProjectModel());
                    if (projectHandleTaskResult.getProjectStageModels() != null)
                        onProjectRequestProjectStages(projectHandleTaskResult.getProjectStageModels());
                    if (projectHandleTaskResult.getProjectPlanModels() != null)
                        onProjectRequestProjectPlans(projectHandleTaskResult.getProjectPlanModels());
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
        projectHandleTask.execute(new ProjectHandleTaskParam(this, settingUserModel, projectModel));
    }

    protected void onProjectRequestProgress(final String progressMessage) {

    }

    protected void onProjectRequestContract(final ContractModel contractModel) {
        mProjectLayout.setContract(contractModel);
    }

    protected void onProjectRequestProject(final ProjectModel projectModel) {
        mProjectLayout.setProject(projectModel);
    }

    protected void onProjectRequestProjectStages(final ProjectStageModel[] projectStageModels) {
        mProjectLayout.setProjectStages(projectStageModels);
    }

    protected void onProjectRequestProjectPlans(final ProjectPlanModel[] projectPlanModels) {
        mProjectLayout.setProjectPlans(projectPlanModels);
    }

    protected void onProjectRequestMessage(final String message) {

    }

    protected class ProjectHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectModel mProjectModel;

        public ProjectHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectModel projectModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectModel = projectModel;
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

            // -- Get ProjectModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_begin));

            // -- Prepare ProjectNetwork --
            ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectHandleTaskParam.getSettingUserModel());

            ProjectResponseModel projectResponseModel = null;
            try {
                // -- Invalidate Access Token --
                projectNetwork.invalidateAccessToken();

                // -- Invalidate Login --
                projectNetwork.invalidateLogin();

                // -- Get project from server --
                ProjectModel projectModel = mProjectHandleTaskParam.getProjectModel();
                if (projectModel != null) {
                    projectResponseModel = projectNetwork.getProject(projectModel.getProjectId());
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

                // -- Get ProjectModels progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.project_handle_task_success));
            }

            return projectHandleTaskResult;
        }
    }
}
