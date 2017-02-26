package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.asynctask.ProjectPlanGetAsyncTask;
import com.construction.pm.asynctask.param.ProjectPlanGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectPlanGetAsyncTaskResult;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectPlanAssignmentModel;
import com.construction.pm.models.ProjectPlanModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.project_plan.ProjectPlanLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectPlanActivity extends AppCompatActivity implements ProjectPlanLayout.ProjectPlanLayoutListener {

    public static final String INTENT_PARAM_PROJECT_PLAN_MODEL = "PROJECT_PLAN_MODEL";

    protected ProjectPlanModel mProjectPlanModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectPlanLayout mProjectPlanLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

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

        // -- Prepare ProjectPlanGetAsyncTask --
        ProjectPlanGetAsyncTask projectPlanGetAsyncTask = new ProjectPlanGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectPlanGetAsyncTaskResult projectHandleTaskResult) {
                mAsyncTaskList.remove(this);

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

        // -- Do ProjectPlanGetAsyncTask --
        projectPlanGetAsyncTask.execute(new ProjectPlanGetAsyncTaskParam(this, settingUserModel, projectPlanModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectPlanRequestProgress(final String progressMessage) {

    }

    protected void onProjectPlanRequestSuccess(final ProjectPlanModel projectPlanModel, final ProjectPlanAssignmentModel[] projectPlanAssignmentModels, final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectPlanLayout.setLayoutData(projectPlanModel, projectPlanAssignmentModels, projectActivityUpdateModels);
    }

    protected void onProjectPlanRequestMessage(final String message) {

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
