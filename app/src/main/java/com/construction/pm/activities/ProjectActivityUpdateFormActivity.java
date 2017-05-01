package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.asynctask.ManagerProjectActivityUpdateSaveAsyncTask;
import com.construction.pm.asynctask.param.ManagerProjectActivityUpdateSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.ManagerProjectActivityUpdateSaveAsyncTaskResult;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_activity.ProjectActivityUpdateFormLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectActivityUpdateFormActivity extends AppCompatActivity implements
        ProjectActivityUpdateFormLayout.ProjectActivityUpdateFormLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";
    public static final String INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";

    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;
    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateFormLayout mProjectActivityUpdateFormLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectActivityUpdateFormLayout --
        mProjectActivityUpdateFormLayout = ProjectActivityUpdateFormLayout.buildProjectActivityUpdateFormLayout(this, null);
        mProjectActivityUpdateFormLayout.setProjectActivityMonitoringFormLayoutListener(this);

        // -- Load to Activity --
        mProjectActivityUpdateFormLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectActivityMonitoringModel parameter --
            String activityMonitoringModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (activityMonitoringModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(activityMonitoringModelJson);
                    mProjectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }

            // -- Get ProjectActivityUpdateModel parameter --
            String activityUpdateModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL);
            if (activityUpdateModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(activityUpdateModelJson);
                    mProjectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        // -- Load ProjectActivityUpdateFormFragment --
        mProjectActivityUpdateFormLayout.showProjectActivityUpdateFormFragment(mProjectActivityMonitoringModel, mProjectActivityUpdateModel);
    }

    @Override
    public void onProjectActivityUpdateFormLayoutSaveMenuClick() {
        ProjectActivityUpdateModel projectActivityUpdateModel = mProjectActivityUpdateFormLayout.getProjectActivityUpdateModel();
        if (projectActivityUpdateModel == null)
            return;

        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Set ProjectActivityUpdateModel additional data --
            projectActivityUpdateModel.setProjectMemberId(projectMemberModel.getProjectMemberId());
            projectActivityUpdateModel.setUpdateDate(Calendar.getInstance());

            // -- Prepare ManagerProjectActivityUpdateSaveAsyncTask --
            ManagerProjectActivityUpdateSaveAsyncTask managerProjectActivityUpdateSaveAsyncTask = new ManagerProjectActivityUpdateSaveAsyncTask() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(ManagerProjectActivityUpdateSaveAsyncTaskResult managerProjectActivityUpdateSaveAsyncTaskResult) {
                    if (managerProjectActivityUpdateSaveAsyncTaskResult != null) {
                        onProjectActivityUpdateSaveFinish(managerProjectActivityUpdateSaveAsyncTaskResult.getProjectActivityUpdateModel(), managerProjectActivityUpdateSaveAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            onProjectActivityUpdateSaveProgress(messages[0]);
                        }
                    }
                }
            };

            // -- Do ManagerProjectActivityUpdateSaveAsyncTask --
            managerProjectActivityUpdateSaveAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ManagerProjectActivityUpdateSaveAsyncTaskParam(this, settingUserModel, projectActivityUpdateModel, sessionLoginModel.getProjectMemberModel()));
        }
    }

    protected void onProjectActivityUpdateSaveProgress(final String progressMessage) {
        // -- Show progress dialog --
        mProjectActivityUpdateFormLayout.progressDialogShow(progressMessage);
    }

    protected void onProjectActivityUpdateSaveFinish(final ProjectActivityUpdateModel projectActivityUpdateModel, final String message) {
        // -- Hide progress dialog --
        mProjectActivityUpdateFormLayout.progressDialogDismiss();

        if (projectActivityUpdateModel != null) {
            String projectActivityUpdateModelJson = null;

            try {
                org.json.JSONObject jsonObject = projectActivityUpdateModel.build();
                projectActivityUpdateModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }

            Intent intent = new Intent();
            intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
            setResult(ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_FORM_RESULT_SAVED, intent);

            finish();
        } else {
            if (message != null) {
                // -- Show message dialog --
                mProjectActivityUpdateFormLayout.alertDialogErrorShow(message);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mProjectActivityUpdateFormLayout.createProjectActivityUpdateSaveMenu(menu);
        return true;
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
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
