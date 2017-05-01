package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.construction.pm.R;
import com.construction.pm.asynctask.FileGetCacheAsyncTask;
import com.construction.pm.asynctask.FileGetNetworkAsyncTask;
import com.construction.pm.asynctask.ProjectStageGetAsyncTask;
import com.construction.pm.asynctask.param.FileGetAsyncTaskParam;
import com.construction.pm.asynctask.param.ProjectStageGetAsyncTaskParam;
import com.construction.pm.asynctask.result.FileGetAsyncTaskResult;
import com.construction.pm.asynctask.result.ProjectStageGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.FileUtil;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageActivity extends AppCompatActivity implements
        ProjectStageLayout.ProjectStageLayoutListener {

    public static final String INTENT_PARAM_PROJECT_STAGE_MODEL = "PROJECT_STAGE_MODEL";

    protected ProjectStageModel mProjectStageModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageLayout mProjectStageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectStageLayout --
        mProjectStageLayout = ProjectStageLayout.buildProjectStageLayout(this, null);
        mProjectStageLayout.setProjectStageLayoutListener(this);

        // -- Load to Activity --
        mProjectStageLayout.loadLayoutToActivity(this, mProjectStageModel);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectStageModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_STAGE_MODEL));
                mProjectStageModel = ProjectStageModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mProjectStageLayout != null) {
            ProjectStageAssignmentModel projectStageAssignmentModel = getProjectStageAssignmentModel(mProjectStageLayout.getProjectStageModel(), mProjectStageLayout.getProjectStageAssignmentModels());
            if (projectStageAssignmentModel != null && mProjectStageLayout.isTabShowProjectStageAssignCommentList())
                mProjectStageLayout.createProjectStageAssignCommentAddMenu(menu);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProjectStageRequest(ProjectStageModel projectStageModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectStageGetAsyncTask --
        ProjectStageGetAsyncTask projectStageGetAsyncTask = new ProjectStageGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectStageGetAsyncTaskResult projectHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectHandleTaskResult != null) {
                    onProjectStageRequestSuccess(projectHandleTaskResult.getProjectStageModel(), projectHandleTaskResult.getProjectStageAssignmentModels(), projectHandleTaskResult.getProjectStageDocumentModels(), projectHandleTaskResult.getProjectStageAssignCommentModels());
                    if (projectHandleTaskResult.getMessage() != null)
                        onProjectStageRequestMessage(projectHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectStageRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectStageGetAsyncTask --
        projectStageGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ProjectStageGetAsyncTaskParam(this, settingUserModel, projectStageModel.getProjectStageId(), sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectStageAssignCommentAddMenuClick() {
        ProjectStageAssignmentModel projectStageAssignmentModel = getProjectStageAssignmentModel(mProjectStageLayout.getProjectStageModel(), mProjectStageLayout.getProjectStageAssignmentModels());
        if (projectStageAssignmentModel != null)
            mProjectStageLayout.showProjectStageAssignCommentForm(projectStageAssignmentModel);
    }

    @Override
    public void onProjectStageTabChanged(int position) {
        supportInvalidateOptionsMenu();
    }

    protected void onProjectStageRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageRequestSuccess(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageLayout.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageDocumentModels, projectStageAssignCommentModels);
    }

    protected void onProjectStageRequestMessage(final String message) {

    }

    protected ProjectStageAssignmentModel getProjectStageAssignmentModel(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels) {
        int projectMemberId = 0;
        int projectStageId = 0;

        // -- Get ProjectMemberModel form SessionLoginModel in SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();
        ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
        if (projectMemberModel != null) {
            if (projectMemberModel.getProjectMemberId() != null)
                projectMemberId = projectMemberModel.getProjectMemberId();
        }

        // -- Get ProjectStageModel --
        if (projectStageModel != null) {
            if (projectStageModel.getProjectStageId() != null)
                projectStageId = projectStageModel.getProjectStageId();
        }

        // -- Get ProjectStageAssignmentModel --
        ProjectStageAssignmentModel projectStageAssignmentModel = null;
        if (projectStageAssignmentModels != null) {
            for (ProjectStageAssignmentModel existProjectStageAssignmentModel : projectStageAssignmentModels) {
                int existProjectMemberId = 0;
                if (existProjectStageAssignmentModel.getProjectMemberId() != null)
                    existProjectMemberId = existProjectStageAssignmentModel.getProjectMemberId();
                int existProjectStageId = 0;
                if (existProjectStageAssignmentModel.getProjectStageId() != null)
                    existProjectStageId = existProjectStageAssignmentModel.getProjectStageId();

                if (projectMemberId == existProjectMemberId && projectStageId == existProjectStageId) {
                    projectStageAssignmentModel = existProjectStageAssignmentModel;
                    break;
                }
            }
        }

        return projectStageAssignmentModel;
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
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                asyncTask.cancel(true);
            }
        }

        super.onDestroy();
    }
}
