package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.asynctask.ManagerProjectActivityGetAsyncTask;
import com.construction.pm.asynctask.param.ManagerProjectActivityGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityGetAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.manager.ManagerDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailActivity extends AppCompatActivity implements
        ManagerDetailLayout.ManagerDetailLayoutListener {
    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected ProjectActivityModel mProjectActivityModel;
    protected boolean mProjectActivityModelChanged;
    protected List<AsyncTask> mAsyncTaskList;

    protected ManagerDetailLayout mManagerDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ManagerDetailLayout --
        mManagerDetailLayout = ManagerDetailLayout.buildManagerDetailLayout(this, null);
        mManagerDetailLayout.setManagerDetailLayoutListener(this);

        // -- Load to Activity --
        mManagerDetailLayout.loadLayoutToActivity(this, mProjectActivityModel);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectActivityModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MODEL));
                mProjectActivityModel = ProjectActivityModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (!mManagerDetailLayout.isManagerFragmentShow())
                    mManagerDetailLayout.showManagerDetailFragment(mProjectActivityModel);
                else {
                    handleFinish();
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onManagerDetailRequest(ProjectActivityModel projectActivityModel) {
        mManagerDetailLayout.showManagerDetailFragment(projectActivityModel);
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {
        showProjectActivityUpdateDetailActivity(projectActivityUpdateModel);
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Redirect to ProjectActivityMonitoringDetailActivity --
        Intent intent = new Intent(this, ProjectActivityMonitoringDetailActivity.class);
        intent.putExtra(ProjectActivityMonitoringDetailActivity.INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE, true);

        try {
            org.json.JSONObject projectActivityMonitoringModelJsonObject = projectActivityMonitoringModel.build();
            String projectActivityMonitoringModelJson = projectActivityMonitoringModelJsonObject.toString(0);

            intent.putExtra(ProjectActivityMonitoringDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL);
    }

    protected void showProjectActivityUpdateFormActivity(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Redirect to ProjectActivityUpdateFormActivity --
        Intent intent = new Intent(this, ProjectActivityUpdateFormActivity.class);

        try {
            org.json.JSONObject projectActivityMonitoringModelJsonObject = projectActivityMonitoringModel.build();
            String projectActivityMonitoringModelJson = projectActivityMonitoringModelJsonObject.toString(0);
            intent.putExtra(ProjectActivityUpdateFormActivity.INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM);
    }

    protected void showProjectActivityUpdateDetailActivity(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        // -- Redirect to ProjectActivityUpdateDetailActivity --
        Intent intent = new Intent(this, ProjectActivityUpdateDetailActivity.class);

        try {
            org.json.JSONObject projectActivityUpdateModelJsonObject = projectActivityUpdateModel.build();
            String projectActivityUpdateModelJson = projectActivityUpdateModelJsonObject.toString(0);
            intent.putExtra(ProjectActivityUpdateDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            intent.putExtra(ProjectActivityUpdateDetailActivity.INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE_EDIT, true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL_RESULT_UPDATE) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL)) {
                        String projectActivityMonitoringModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL);
                        if (projectActivityMonitoringModelJson != null) {
                            ProjectActivityMonitoringModel projectActivityMonitoringModel = null;
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityMonitoringModelJson);
                                projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                            if (projectActivityMonitoringModel != null)
                                showProjectActivityUpdateFormActivity(projectActivityMonitoringModel);
                        }
                    }
                }
            }
        } else if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_DETAIL) {
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
                                reloadProjectActivityGet(mProjectActivityModel);
                                mManagerDetailLayout.addProjectActivityUpdateModel(projectActivityUpdateModel);
                            }
                        }
                    }
                }
            }
        }
    }

    public void reloadProjectActivityGet(final ProjectActivityModel projectActivityModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ManagerProjectActivityGetAsyncTask --
        ManagerProjectActivityGetAsyncTask managerProjectActivityGetAsyncTask = new ManagerProjectActivityGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ManagerProjectActivityGetAsyncTaskResult projectActivityGetHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectActivityGetHandleTaskResult != null) {
                    ProjectActivityModel projectActivityModelNew = projectActivityGetHandleTaskResult.getProjectActivityModel();
                    if (projectActivityModelNew != null) {
                        onProjectActivityGetReloadSuccess(projectActivityModelNew);
                    } else
                        onProjectActivityGetReloadFailed(projectActivityModel, projectActivityGetHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityGetReloadProgress(projectActivityModel, messages[0]);
                    }
                }
            }
        };

        // -- Do ManagerProjectActivityGetAsyncTask --
        managerProjectActivityGetAsyncTask.execute(new ManagerProjectActivityGetAsyncTaskParam(this, settingUserModel, sessionLoginModel.getProjectMemberModel(), projectActivityModel.getProjectActivityId()));
    }

    public void onProjectActivityGetReloadProgress(final ProjectActivityModel projectActivityModel, final String progressMessage) {

    }

    public void onProjectActivityGetReloadSuccess(final ProjectActivityModel projectActivityModel) {
        mProjectActivityModel = projectActivityModel;
        mProjectActivityModelChanged = true;

        mManagerDetailLayout.setProjectActivityModel(mProjectActivityModel);
    }

    public void onProjectActivityGetReloadFailed(final ProjectActivityModel projectActivityModel, final String errorMessage) {

    }

    @Override
    public void onBackPressed() {
        if (!mManagerDetailLayout.isManagerFragmentShow()) {
            mManagerDetailLayout.showManagerDetailFragment(mProjectActivityModel);
            return;
        }

        handleFinish();
        super.onBackPressed();
    }

    protected void handleFinish() {
        // -- Set result callback --
        Intent intent = new Intent();
        if (mProjectActivityModelChanged) {
            String projectActivityModelJson = null;
            if (mProjectActivityModel != null) {
                try {
                    org.json.JSONObject jsonObject = mProjectActivityModel.build();
                    projectActivityModelJson = jsonObject.toString(0);
                } catch (org.json.JSONException e) {
                }
            }
            intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
            setResult(ConstantUtil.INTENT_REQUEST_MANAGER_DETAIL_ACTIVITY_RESULT_CHANGED, intent);
        } else
            setResult(RESULT_OK, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
