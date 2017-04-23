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
        ProjectStageLayout.ProjectStageLayoutListener,
        ImageRequestListener {

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
        projectStageGetAsyncTask.execute(new ProjectStageGetAsyncTaskParam(this, settingUserModel, projectStageModel.getProjectStageId(), sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel) {

    }

    @Override
    public void onProjectStageDocumentItemClick(final FileModel fileModel) {
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
                    FileModel fileModelCache = fileRequestAsyncTaskResult.getFileModelCache();
                    File file = null;
                    if (fileModel != null)
                        file = fileModel.getFile(ProjectStageActivity.this);
                    else if (fileModelCache != null)
                        file = fileModelCache.getFile(ProjectStageActivity.this);
                    onProjectStageDocumentDownloaded(file);
                }
            }

            @Override
            protected void onProgressUpdate(String... progress) {
                if (progress != null) {
                    if (progress.length > 0) {
                        onProjectStageDocumentDownloadingProgress(progress[0]);
                    }
                }
            }
        };

        // -- Prepare FileGetCacheAsyncTask --
        FileGetCacheAsyncTask fileGetCacheAsyncTask = new FileGetCacheAsyncTask() {
            @Override
            public void onPreExecute() {
                onProjectStageDocumentDownloadBegin();
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(FileGetAsyncTaskResult fileRequestAsyncTaskResult) {
                FileModel cacheFileModel = null;
                if (fileRequestAsyncTaskResult != null)
                    cacheFileModel = fileRequestAsyncTaskResult.getFileModel();

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(ProjectStageActivity.this, settingUserModel, fileModel.getFileId(), cacheFileModel));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(this, settingUserModel, fileModel.getFileId(), null));
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
    public void onProjectStageAssignCommentItemClick(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(this, ProjectStageAssignCommentDetailActivity.class);

        try {
            org.json.JSONObject projectStageAssignCommentModelJsonObject = projectStageAssignCommentModel.build();
            String projectStageAssignCommentModelJson = projectStageAssignCommentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentDetailActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL);
    }

    protected void showProjectStageAssignCommentForm(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(this, ProjectStageAssignCommentFormActivity.class);

        try {
            org.json.JSONObject projectStageAssignCommentModelJsonObject = projectStageAssignCommentModel.build();
            String projectStageAssignCommentModelJson = projectStageAssignCommentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentFormActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
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
        } else if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL_RESULT_EDIT) {
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
                                showProjectStageAssignCommentForm(projectStageAssignCommentModel);
                        }
                    }
                }
            }
        }
    }

    protected void onProjectStageRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageRequestSuccess(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageLayout.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageDocumentModels, projectStageAssignCommentModels);
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
                        File file = fileModel.getFile(ProjectStageActivity.this);
                        if (file != null)
                            ImageUtil.setImageThumbnailView(ProjectStageActivity.this, imageView, file);
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
                FileModel fileModel = null;
                if (fileRequestAsyncTaskResult != null) {
                    fileModel = fileRequestAsyncTaskResult.getFileModel();
                    if (fileModel != null) {
                        File file = fileModel.getFile(ProjectStageActivity.this);
                        if (file != null)
                            ImageUtil.setImageThumbnailView(ProjectStageActivity.this, imageView, file);
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(ProjectStageActivity.this, settingUserModel, fileId, fileModel));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(this, settingUserModel, fileId, null));
    }

    protected void onProjectStageDocumentDownloadBegin() {
        mProjectStageLayout.progressDialogShow(ViewUtil.getResourceString(this, R.string.project_stage_layout_download_begin));
    }

    protected void onProjectStageDocumentDownloadingProgress(String progress) {
        mProjectStageLayout.progressDialogShow(ViewUtil.getResourceString(this, R.string.project_stage_layout_download_progress, progress));
    }

    protected void onProjectStageDocumentDownloaded(final File file) {
        mProjectStageLayout.progressDialogDismiss();
        if (file != null)
            FileUtil.openFile(this, file);
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
