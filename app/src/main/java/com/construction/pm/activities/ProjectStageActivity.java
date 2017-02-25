package com.construction.pm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.construction.pm.R;
import com.construction.pm.asynctask.FileRequestAsyncTask;
import com.construction.pm.asynctask.FileRequestAsyncTaskParam;
import com.construction.pm.asynctask.FileRequestAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.network.ProjectStageResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.ProjectNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.FileCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.ProjectCachePersistent;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageLayout;

public class ProjectStageActivity extends AppCompatActivity implements
        ProjectStageLayout.ProjectStageLayoutListener,
        ImageRequestListener {

    public static final String INTENT_PARAM_PROJECT_STAGE_MODEL = "PROJECT_STAGE_MODEL";

    protected ProjectStageModel mProjectStageModel;

    protected ProjectStageLayout mProjectStageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectStageLayout --
        mProjectStageLayout = ProjectStageLayout.buildProjectStageLayout(this, null);
        mProjectStageLayout.setProjectStageLayoutListener(this);
        mProjectStageLayout.setProjectStageAssignCommentImageRequestListener(this);

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

        // -- Prepare ProjectStageHandleTask --
        ProjectStageHandleTask projectStageHandleTask = new ProjectStageHandleTask() {
            @Override
            public void onPostExecute(ProjectStageHandleTaskResult projectHandleTaskResult) {
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

        // -- Do ProjectStageHandleTask --
        projectStageHandleTask.execute(new ProjectStageHandleTaskParam(this, settingUserModel, projectStageModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectStageRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageRequestSuccess(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageLayout.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageAssignCommentModels);
    }

    protected void onProjectStageRequestMessage(final String message) {

    }

    @Override
    public void onImageRequest(final ImageView imageView, Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(this);
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Prepare FileRequestAsyncTask --
        FileRequestAsyncTask fileRequestAsyncTask = new FileRequestAsyncTask() {
            @Override
            public void onPostExecute(Boolean result) {

            }

            @Override
            protected void onProgressUpdate(FileRequestAsyncTaskResult... fileRequestAsyncTaskResults) {
                if (fileRequestAsyncTaskResults != null) {
                    if (fileRequestAsyncTaskResults.length > 0) {
                        FileRequestAsyncTaskResult fileRequestAsyncTaskResult = fileRequestAsyncTaskResults[0];
                        if (fileRequestAsyncTaskResult != null) {
                            FileModel fileModel = fileRequestAsyncTaskResult.getFileModel();
                            if (fileModel != null) {
                                if (fileModel.getFileData() != null)
                                    ViewUtil.setImageViewFromBytes(imageView, fileModel.getFileData());
                            }
                        }
                    }
                }
            }
        };

        // -- Do FileRequestAsyncTask --
        AsyncTaskCompat.executeParallel(fileRequestAsyncTask, new FileRequestAsyncTaskParam(this, settingUserModel, fileId));
    }

    protected class ProjectStageHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectStageModel mProjectStageModel;
        protected ProjectMemberModel mProjectMemberModel;

        public ProjectStageHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectStageModel projectStageModel, final ProjectMemberModel projectMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectStageModel = projectStageModel;
            mProjectMemberModel = projectMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectStageModel getProjectStageModel() {
            return mProjectStageModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class ProjectStageHandleTaskResult {

        protected ProjectStageModel mProjectStageModel;
        protected ProjectStageAssignmentModel[] mProjectStageAssignmentModels;
        protected ProjectStageAssignCommentModel[] mProjectStageAssignCommentModels;
        protected String mMessage;

        public ProjectStageHandleTaskResult() {

        }

        public void setProjectModel(final ProjectStageModel projectModel) {
            mProjectStageModel = projectModel;
        }

        public ProjectStageModel getProjectModel() {
            return mProjectStageModel;
        }

        public void setProjectStageAssignmentModels(final ProjectStageAssignmentModel[] projectStageModels) {
            mProjectStageAssignmentModels = projectStageModels;
        }

        public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
            return mProjectStageAssignmentModels;
        }

        public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
            mProjectStageAssignCommentModels = projectStageAssignCommentModels;
        }

        public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
            return mProjectStageAssignCommentModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class ProjectStageHandleTask extends AsyncTask<ProjectStageHandleTaskParam, String, ProjectStageHandleTaskResult> {
        protected ProjectStageHandleTaskParam mProjectStageHandleTaskParam;
        protected Context mContext;

        @Override
        protected ProjectStageHandleTaskResult doInBackground(ProjectStageHandleTaskParam... projectStageHandleTaskParams) {
            // Get ProjectStageHandleTaskParam
            mProjectStageHandleTaskParam = projectStageHandleTaskParams[0];
            mContext = mProjectStageHandleTaskParam.getContext();

            // -- Prepare ProjectStageHandleTaskResult --
            ProjectStageHandleTaskResult projectStageHandleTaskResult = new ProjectStageHandleTaskResult();

            // -- Get ProjectStageResponseModel progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_handle_task_begin));

            // -- Prepare ProjectCachePersistent --
            ProjectCachePersistent projectCachePersistent = new ProjectCachePersistent(mContext);

            // -- Prepare ProjectNetwork --
            ProjectNetwork projectNetwork = new ProjectNetwork(mContext, mProjectStageHandleTaskParam.getSettingUserModel());

            ProjectStageResponseModel projectStageResponseModel = null;
            try {
                ProjectStageModel projectStageModel = mProjectStageHandleTaskParam.getProjectStageModel();
                ProjectMemberModel projectMemberModel = mProjectStageHandleTaskParam.getProjectMemberModel();
                if (projectStageModel != null && projectMemberModel != null) {
                    try {
                        // -- Invalidate Access Token --
                        projectNetwork.invalidateAccessToken();

                        // -- Invalidate Login --
                        projectNetwork.invalidateLogin();

                        // -- Get project from server --
                        projectStageResponseModel = projectNetwork.getProjectStage(projectStageModel.getProjectStageId());

                        // -- Save to ProjectCachePersistent --
                        try {
                            projectCachePersistent.setProjectStageResponseModel(projectStageResponseModel, projectMemberModel.getProjectMemberId());
                        } catch (PersistenceError ex) {
                        }
                    } catch (WebApiError webApiError) {
                        if (webApiError.isErrorConnection()) {
                            // -- Get ProjectStageResponseModel from ProjectCachePersistent --
                            try {
                                projectStageResponseModel = projectCachePersistent.getProjectStageResponseModel(projectStageModel.getProjectStageId(), projectMemberModel.getProjectMemberId());
                            } catch (PersistenceError ex) {
                            }
                        } else
                            throw webApiError;
                    }
                }
            } catch (WebApiError webApiError) {
                projectStageHandleTaskResult.setMessage(webApiError.getMessage());
                publishProgress(webApiError.getMessage());
            }

            if (projectStageResponseModel != null) {
                // -- Set result --
                projectStageHandleTaskResult.setProjectModel(projectStageResponseModel.getProjectStageModel());
                projectStageHandleTaskResult.setProjectStageAssignmentModels(projectStageResponseModel.getProjectStageAssignmentModels());
                projectStageHandleTaskResult.setProjectStageAssignCommentModels(projectStageResponseModel.getProjectStageAssignCommentModels());

                // -- Get ProjectStageResponseModel progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.project_stage_handle_task_success));
            }

            return projectStageHandleTaskResult;
        }
    }
}
