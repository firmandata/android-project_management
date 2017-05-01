package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.asynctask.InspectorProjectActivityMonitoringSaveAsyncTask;
import com.construction.pm.asynctask.param.InspectorProjectActivityMonitoringSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityMonitoringSaveAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringFormLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectActivityMonitoringFormActivity extends AppCompatActivity implements
        ProjectActivityMonitoringFormLayout.ProjectActivityMonitoringFormLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";
    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";

    protected ProjectActivityModel mProjectActivityModel;
    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringFormLayout mProjectActivityMonitoringFormLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectActivityMonitoringFormLayout --
        mProjectActivityMonitoringFormLayout = ProjectActivityMonitoringFormLayout.buildProjectActivityMonitoringFormLayout(this, null);
        mProjectActivityMonitoringFormLayout.setProjectActivityMonitoringFormLayoutListener(this);

        // -- Load to Activity --
        mProjectActivityMonitoringFormLayout.loadLayoutToActivity(this);

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
            // -- Get ProjectActivityModel parameter --
            String projectActivityModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MODEL);
            if (projectActivityModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityModelJson);
                    mProjectActivityModel = ProjectActivityModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }

            // -- Get ProjectActivityMonitoringModel parameter --
            String projectActivityMonitoringModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (projectActivityMonitoringModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityMonitoringModelJson);
                    mProjectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        // -- Load ProjectActivityMonitoringFormFragment --
        mProjectActivityMonitoringFormLayout.showProjectActivityMonitoringFormFragment(mProjectActivityModel, mProjectActivityMonitoringModel);
    }

    @Override
    public void onProjectActivityMonitoringFormLayoutSaveMenuClick() {
        ProjectActivityMonitoringModel projectActivityMonitoringModel = mProjectActivityMonitoringFormLayout.getProjectActivityMonitoringModel();
        if (projectActivityMonitoringModel == null)
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
            // -- Set ProjectActivityMonitoringModel additional data --
            projectActivityMonitoringModel.setProjectMemberId(projectMemberModel.getProjectMemberId());
            projectActivityMonitoringModel.setMonitoringDate(Calendar.getInstance());

            // -- Prepare InspectorProjectActivityMonitoringSaveAsyncTask --
            InspectorProjectActivityMonitoringSaveAsyncTask inspectorProjectActivityMonitoringSaveAsyncTask = new InspectorProjectActivityMonitoringSaveAsyncTask() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(InspectorProjectActivityMonitoringSaveAsyncTaskResult inspectorProjectActivityMonitoringSaveAsyncTaskResult) {
                    if (inspectorProjectActivityMonitoringSaveAsyncTaskResult != null) {
                        onProjectActivityMonitoringSaveFinish(inspectorProjectActivityMonitoringSaveAsyncTaskResult.getProjectActivityMonitoringModel(), inspectorProjectActivityMonitoringSaveAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            onProjectActivityMonitoringSaveProgress(messages[0]);
                        }
                    }
                }
            };

            // -- Do InspectorProjectActivityMonitoringSaveAsyncTask --
            InspectorProjectActivityMonitoringSaveAsyncTaskParam inspectorProjectActivityMonitoringSaveAsyncTaskParam = new InspectorProjectActivityMonitoringSaveAsyncTaskParam(this, settingUserModel, projectActivityMonitoringModel, sessionLoginModel.getProjectMemberModel());
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhoto(mProjectActivityMonitoringFormLayout.getPhoto(0));
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhotoAdditional1(mProjectActivityMonitoringFormLayout.getPhoto(1));
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhotoAdditional2(mProjectActivityMonitoringFormLayout.getPhoto(2));
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhotoAdditional3(mProjectActivityMonitoringFormLayout.getPhoto(3));
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhotoAdditional4(mProjectActivityMonitoringFormLayout.getPhoto(4));
            inspectorProjectActivityMonitoringSaveAsyncTaskParam.setPhotoAdditional5(mProjectActivityMonitoringFormLayout.getPhoto(5));
            inspectorProjectActivityMonitoringSaveAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, inspectorProjectActivityMonitoringSaveAsyncTaskParam);
        }
    }

    protected void onProjectActivityMonitoringSaveProgress(final String progressMessage) {
        // -- Show progress dialog --
        mProjectActivityMonitoringFormLayout.progressDialogShow(progressMessage);
    }

    protected void onProjectActivityMonitoringSaveFinish(final ProjectActivityMonitoringModel projectActivityMonitoringModel, final String message) {
        // -- Hide progress dialog --
        mProjectActivityMonitoringFormLayout.progressDialogDismiss();

        if (projectActivityMonitoringModel != null) {
            String projectActivityMonitoringModelJson = null;

            try {
                org.json.JSONObject jsonObject = projectActivityMonitoringModel.build();
                projectActivityMonitoringModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }

            Intent intent = new Intent();
            intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
            setResult(ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM_RESULT_SAVED, intent);

            finish();
        } else {
            if (message != null) {
                // -- Show message dialog --
                mProjectActivityMonitoringFormLayout.alertDialogErrorShow(message);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mProjectActivityMonitoringFormLayout.createProjectActivityMonitoringSaveMenu(menu);
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