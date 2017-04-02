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
import com.construction.pm.utils.ConstantUtil;
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
                    onProjectPlanRequestSuccess(projectHandleTaskResult.getProjectPlanModel(), projectHandleTaskResult.getProjectPlanAssignmentModels(), projectHandleTaskResult.getProjectActivityUpdateModels());
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

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        showProjectActivityUpdateDetailActivity(projectActivityUpdateModel);
    }

    protected void onProjectPlanRequestProgress(final String progressMessage) {

    }

    protected void onProjectPlanRequestSuccess(final ProjectPlanModel projectPlanModel, final ProjectPlanAssignmentModel[] projectPlanAssignmentModels, final ProjectActivityUpdateModel[] projectActivityUpdateModels) {
        mProjectPlanLayout.setLayoutData(projectPlanModel, projectPlanAssignmentModels, projectActivityUpdateModels);
    }

    protected void onProjectPlanRequestMessage(final String message) {

    }

    protected void showProjectActivityUpdateDetailActivity(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Redirect to ProjectActivityUpdateDetailActivity --
        Intent intent = new Intent(this, ProjectActivityUpdateDetailActivity.class);

        try {
            org.json.JSONObject projectActivityUpdateModelJsonObject = projectActivityUpdateModel.build();
            String projectActivityUpdateModelJson = projectActivityUpdateModelJsonObject.toString(0);
            intent.putExtra(ProjectActivityUpdateDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            intent.putExtra(ProjectActivityUpdateDetailActivity.INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE_EDIT, false);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_DETAIL);
    }

    protected void showProjectActivityUpdateFormActivity(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Redirect to ProjectActivityUpdateFormActivity --
        Intent intent = new Intent(this, ProjectActivityUpdateFormActivity.class);

        try {
            org.json.JSONObject projectActivityUpdateModelJsonObject = projectActivityUpdateModel.build();
            String projectActivityUpdateModelJson = projectActivityUpdateModelJsonObject.toString(0);
            intent.putExtra(ProjectActivityUpdateFormActivity.INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM);
    }

    public void reloadProjectPlanModel(final ProjectPlanModel projectPlanModel) {
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
                    ProjectPlanModel newProjectPlanModel = projectHandleTaskResult.getProjectPlanModel();
                    if (newProjectPlanModel != null)
                        mProjectPlanLayout.setProjectPlanModel(newProjectPlanModel);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {

            }
        };

        // -- Do ProjectPlanGetAsyncTask --
        projectPlanGetAsyncTask.execute(new ProjectPlanGetAsyncTaskParam(this, settingUserModel, projectPlanModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_DETAIL) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_DETAIL_RESULT_EDIT) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL)) {
                        String projectActivityUpdateModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL);
                        if (projectActivityUpdateModelJson != null) {
                            ProjectActivityUpdateModel projectActivityUpdateModel = null;
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityUpdateModelJson);
                                projectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                            if (projectActivityUpdateModel != null)
                                showProjectActivityUpdateFormActivity(projectActivityUpdateModel);
                        }
                    }
                }
            }
        } else if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM_RESULT_SAVED) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL)) {
                        String projectActivityUpdateModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL);
                        if (projectActivityUpdateModelJson != null) {
                            ProjectActivityUpdateModel projectActivityUpdateModel = null;
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityUpdateModelJson);
                                projectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                            if (projectActivityUpdateModel != null) {
                                reloadProjectPlanModel(mProjectPlanModel);
                                mProjectPlanLayout.addProjectActivityUpdateModel(projectActivityUpdateModel);
                            }
                        }
                    }
                }
            }
        }
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
