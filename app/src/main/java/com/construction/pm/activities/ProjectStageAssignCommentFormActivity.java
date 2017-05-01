package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.asynctask.ProjectStageAssignCommentSaveAsyncTask;
import com.construction.pm.asynctask.param.ProjectStageAssignCommentSaveAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageAssignCommentSaveAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentFormLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectStageAssignCommentFormActivity extends AppCompatActivity implements
        ProjectStageAssignCommentFormLayout.ProjectStageAssignCommentFormLayoutListener {

    public static final String INTENT_PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL = "PROJECT_STAGE_ASSIGNMENT_MODEL";
    public static final String INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL = "PROJECT_STAGE_ASSIGN_COMMENT_MODEL";

    protected ProjectStageAssignmentModel mProjectStageAssignmentModel;
    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageAssignCommentFormLayout mProjectStageAssignCommentFormLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectStageAssignCommentFormLayout --
        mProjectStageAssignCommentFormLayout = ProjectStageAssignCommentFormLayout.buildProjectStageAssignCommentFormLayout(this, null);
        mProjectStageAssignCommentFormLayout.setProjectStageAssignCommentFormLayoutListener(this);

        // -- Load to Activity --
        mProjectStageAssignCommentFormLayout.loadLayoutToActivity(this);

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
            // -- Get ProjectStageAssignmentModel parameter --
            String projectStageAssignmentModelJson = bundle.getString(INTENT_PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL);
            if (projectStageAssignmentModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageAssignmentModelJson);
                    mProjectStageAssignmentModel = ProjectStageAssignmentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }

            // -- Get ProjectStageAssignCommentModel parameter --
            String projectStageAssignCommentModelJson = bundle.getString(INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL);
            if (projectStageAssignCommentModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageAssignCommentModelJson);
                    mProjectStageAssignCommentModel = ProjectStageAssignCommentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        // -- Load ProjectStageAssignCommentFormFragment --
        mProjectStageAssignCommentFormLayout.showProjectStageAssignCommentFormFragment(mProjectStageAssignmentModel, mProjectStageAssignCommentModel);
    }

    @Override
    public void onProjectStageAssignCommentFormLayoutSaveMenuClick() {
        ProjectStageAssignCommentModel projectStageAssignCommentModel = mProjectStageAssignCommentFormLayout.getProjectStageAssignCommentModel();
        if (projectStageAssignCommentModel == null)
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
            // -- Set ProjectStageAssignCommentModel additional data --
            projectStageAssignCommentModel.setProjectMemberId(projectMemberModel.getProjectMemberId());
            projectStageAssignCommentModel.setCommentDate(Calendar.getInstance());

            // -- Prepare ProjectStageAssignCommentSaveAsyncTask --
            ProjectStageAssignCommentSaveAsyncTask projectStageAssignCommentSaveAsyncTask = new ProjectStageAssignCommentSaveAsyncTask() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(ProjectStageAssignCommentSaveAsyncTaskResult projectStageAssignCommentSaveAsyncTaskResult) {
                    if (projectStageAssignCommentSaveAsyncTaskResult != null) {
                        onProjectStageAssignCommentSaveFinish(projectStageAssignCommentSaveAsyncTaskResult.getProjectStageAssignCommentModel(), projectStageAssignCommentSaveAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            onProjectStageAssignCommentSaveProgress(messages[0]);
                        }
                    }
                }
            };

            // -- Do ProjectStageAssignCommentSaveAsyncTask --
            ProjectStageAssignCommentSaveAsyncTaskParam projectStageAssignCommentSaveAsyncTaskParam = new ProjectStageAssignCommentSaveAsyncTaskParam(this, settingUserModel, projectStageAssignCommentModel, sessionLoginModel.getProjectMemberModel());
            projectStageAssignCommentSaveAsyncTaskParam.setPhoto(mProjectStageAssignCommentFormLayout.getPhoto(0));
            projectStageAssignCommentSaveAsyncTaskParam.setPhotoAdditional1(mProjectStageAssignCommentFormLayout.getPhoto(1));
            projectStageAssignCommentSaveAsyncTaskParam.setPhotoAdditional2(mProjectStageAssignCommentFormLayout.getPhoto(2));
            projectStageAssignCommentSaveAsyncTaskParam.setPhotoAdditional3(mProjectStageAssignCommentFormLayout.getPhoto(3));
            projectStageAssignCommentSaveAsyncTaskParam.setPhotoAdditional4(mProjectStageAssignCommentFormLayout.getPhoto(4));
            projectStageAssignCommentSaveAsyncTaskParam.setPhotoAdditional5(mProjectStageAssignCommentFormLayout.getPhoto(5));
            projectStageAssignCommentSaveAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, projectStageAssignCommentSaveAsyncTaskParam);
        }
    }

    protected void onProjectStageAssignCommentSaveProgress(final String progressMessage) {
        // -- Show progress dialog --
        mProjectStageAssignCommentFormLayout.progressDialogShow(progressMessage);
    }

    protected void onProjectStageAssignCommentSaveFinish(final ProjectStageAssignCommentModel projectStageAssignCommentModel, final String message) {
        // -- Hide progress dialog --
        mProjectStageAssignCommentFormLayout.progressDialogDismiss();

        if (projectStageAssignCommentModel != null) {
            String projectStageAssignCommentModelJson = null;

            try {
                org.json.JSONObject jsonObject = projectStageAssignCommentModel.build();
                projectStageAssignCommentModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }

            Intent intent = new Intent();
            intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
            setResult(ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM_RESULT_SAVED, intent);

            finish();
        } else {
            if (message != null) {
                // -- Show message dialog --
                mProjectStageAssignCommentFormLayout.alertDialogErrorShow(message);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mProjectStageAssignCommentFormLayout.createProjectStageAssignCommentSaveMenu(menu);
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