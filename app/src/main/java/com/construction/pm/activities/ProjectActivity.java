package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.asynctask.ProjectGetAsyncTask;
import com.construction.pm.asynctask.param.ProjectGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectGetAsyncTaskResult;
import com.construction.pm.models.ContractModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.project.ProjectLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity implements ProjectLayout.ProjectLayoutListener {

    public static final String INTENT_PARAM_PROJECT_MODEL = "PROJECT_MODEL";

    protected ProjectModel mProjectModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectLayout mProjectLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

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

        // -- Prepare ProjectGetAsyncTask --
        ProjectGetAsyncTask projectGetAsyncTask = new ProjectGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectGetAsyncTaskResult projectHandleTaskResult) {
                mAsyncTaskList.remove(this);

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

        // -- Do ProjectGetAsyncTask --
        projectGetAsyncTask.execute(new ProjectGetAsyncTaskParam(this, settingUserModel, projectModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectRequestProgress(final String progressMessage) {

    }

    protected void onProjectRequestSuccess(final ContractModel contractModel, final ProjectModel projectModel, final ProjectStageModel[] projectStageModels, final ProjectPlanModel[] projectPlanModels) {
        mProjectLayout.setLayoutData(contractModel, projectModel, projectStageModels, projectPlanModels);
    }

    protected void onProjectRequestMessage(final String message) {

    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
