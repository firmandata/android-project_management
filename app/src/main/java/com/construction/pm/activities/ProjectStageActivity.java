package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

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
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectStageActivity extends AppCompatActivity implements
        ProjectStageLayout.ProjectStageLayoutListener,
        ImageRequestListener,
        ImageRequestClickListener {

    public static final String INTENT_PARAM_PROJECT_STAGE_MODEL = "PROJECT_STAGE_MODEL";

    protected ProjectStageModel mProjectStageModel;
    protected List<AsyncTask> mAsyncTaskList;
    protected List<AsyncTask> mAsyncTaskPendingList;

    protected ProjectStageLayout mProjectStageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();
        mAsyncTaskPendingList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectStageLayout --
        mProjectStageLayout = ProjectStageLayout.buildProjectStageLayout(this, null);
        mProjectStageLayout.setProjectStageLayoutListener(this);
        mProjectStageLayout.setImageRequestListener(this);
        mProjectStageLayout.setImageRequestClickListener(this);

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
        mProjectStageLayout.createProjectStageAssignCommentAddMenu(menu);
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
                    onProjectStageRequestSuccess(projectHandleTaskResult.getProjectModel(), projectHandleTaskResult.getProjectStageAssignmentModels(), projectHandleTaskResult.getProjectStageAssignCommentModels());
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
        projectStageGetAsyncTask.execute(new ProjectStageGetAsyncTaskParam(this, settingUserModel, projectStageModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectStageAssignCommentAddMenuClick() {
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
        ProjectStageModel projectStageModel = mProjectStageLayout.getProjectStageModel();
        if (projectStageModel != null) {
            if (projectStageModel.getProjectStageId() != null)
                projectStageId = projectStageModel.getProjectStageId();
        }

        // -- Get ProjectStageAssignmentModel --
        ProjectStageAssignmentModel projectStageAssignmentModel = null;
        ProjectStageAssignmentModel[] projectStageAssignmentModels = mProjectStageLayout.getProjectStageAssignmentModels();
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

        if (projectStageAssignmentModel == null)
            return;

        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(this, ProjectStageAssignCommentFormActivity.class);

        try {
            org.json.JSONObject projectStageAssignmentModelJsonObject = projectStageAssignmentModel.build();
            String projectStageAssignmentModelJson = projectStageAssignmentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentFormActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL, projectStageAssignmentModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM_RESULT_SAVED) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_STAGE_ASSIGN_COMMENT_MODEL)) {
                        String projectStageAssignCommentModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_STAGE_ASSIGN_COMMENT_MODEL);
                        if (projectStageAssignCommentModelJson != null) {
                            ProjectStageAssignCommentModel projectStageAssignCommentModel = null;
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageAssignCommentModelJson);
                                projectStageAssignCommentModel = ProjectStageAssignCommentModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                            if (projectStageAssignCommentModel != null)
                                mProjectStageLayout.addProjectStageAssignCommentModel(projectStageAssignCommentModel);
                        }
                    }
                }
            }
        }
    }

    protected void onProjectStageRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageRequestSuccess(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageLayout.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageAssignCommentModels);
    }

    protected void onProjectStageRequestMessage(final String message) {

    }

    @Override
    public void onImageRequest(final ImageView imageView, final Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        final SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileGetNetworkAsyncTask --
        final FileGetNetworkAsyncTask fileGetNetworkAsyncTask = new FileGetNetworkAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                mAsyncTaskList.remove(this);

                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        if (fileModel.getFileData() != null)
                            ViewUtil.setImageThumbnailView(ProjectStageActivity.this, imageView, fileModel.getFileData());
                    }
                }
            }
        };

        // -- Prepare FileGetCacheAsyncTask --
        FileGetCacheAsyncTask fileGetCacheAsyncTask = new FileGetCacheAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                if (fileRequestAsyncTaskResult != null) {
                    FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        if (fileModel.getFileData() != null)
                            ViewUtil.setImageThumbnailView(ProjectStageActivity.this, imageView, fileModel.getFileData());
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(ProjectStageActivity.this, settingUserModel, fileId));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(this, settingUserModel, fileId));
    }

    @Override
    public void onImageRequestClick(Integer fileId) {
        // -- Redirect to FileViewActivity --
        Intent intent = new Intent(this, FileViewActivity.class);
        intent.putExtra(FileViewActivity.INTENT_PARAM_FILE_ID, fileId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (AsyncTask asyncTask : mAsyncTaskPendingList) {

        }
        mAsyncTaskPendingList.clear();
    }

    @Override
    protected void onPause() {
        for (AsyncTask asyncTask : mAsyncTaskPendingList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                asyncTask.cancel(true);
                mAsyncTaskPendingList.add(asyncTask);
            }
        }

        super.onDestroy();
    }
}
