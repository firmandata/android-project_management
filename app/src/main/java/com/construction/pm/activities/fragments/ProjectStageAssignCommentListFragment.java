package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.construction.pm.activities.ProjectStageAssignCommentDetailActivity;
import com.construction.pm.activities.ProjectStageAssignCommentFormActivity;
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
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.listeners.ImageRequestListener;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentListFragment extends Fragment implements
        ProjectStageAssignCommentListView.ProjectStageAssignCommentListListener,
        ImageRequestListener {

    public static final String PARAM_PROJECT_STAGE_MODEL = "PROJECT_STAGE_MODEL";
    public static final String PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODELS = "PROJECT_STAGE_ASSIGN_COMMENT_MODELS";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageAssignCommentListView mProjectStageAssignCommentListView;

    protected ProjectStageAssignCommentListFragmentListener mProjectStageAssignCommentListFragmentListener;

    public static ProjectStageAssignCommentListFragment newInstance(ProjectStageAssignCommentListFragmentListener projectStageAssignCommentListFragmentListener) {
        return newInstance(null, projectStageAssignCommentListFragmentListener);
    }

    public static ProjectStageAssignCommentListFragment newInstance(final ProjectStageModel projectStageModel, final ProjectStageAssignCommentListFragmentListener projectStageAssignCommentListFragmentListener) {
        return newInstance(projectStageModel, null, projectStageAssignCommentListFragmentListener);
    }

    public static ProjectStageAssignCommentListFragment newInstance(final ProjectStageModel projectStageModel, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels, final ProjectStageAssignCommentListFragmentListener projectStageAssignCommentListFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectStageModel != null) {
            try {
                String projectStageModelJson = projectStageModel.build().toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_MODEL, projectStageModelJson);
            } catch (org.json.JSONException ex) {
            }
        }
        if (projectStageAssignCommentModels != null) {
            try {
                org.json.JSONArray projectStageAssignCommentModelJson = new org.json.JSONArray();
                for (ProjectStageAssignCommentModel projectStageAssignCommentModel : projectStageAssignCommentModels)
                    projectStageAssignCommentModelJson.put(projectStageAssignCommentModel.build());
                bundle.putString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODELS, projectStageAssignCommentModelJson.toString(0));
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectStageAssignCommentListFragment --
        ProjectStageAssignCommentListFragment projectStageAssignCommentListFragment = new ProjectStageAssignCommentListFragment();
        projectStageAssignCommentListFragment.setArguments(bundle);
        projectStageAssignCommentListFragment.setProjectStageAssignCommentListFragmentListener(projectStageAssignCommentListFragmentListener);
        return projectStageAssignCommentListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectStageModel projectStageModel = null;
        ProjectStageAssignCommentModel[] projectStageAssignCommentModels = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ProjectStageModel parameter --
            String projectStageModelJson = bundle.getString(PARAM_PROJECT_STAGE_MODEL);
            if (projectStageModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageModelJson);
                    projectStageModel = ProjectStageModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }

            // -- Get ProjectStageAssignCommentModels parameter --
            String projectStageAssignCommentModelJson = bundle.getString(PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODELS);
            if (projectStageAssignCommentModelJson != null) {
                List<ProjectStageAssignCommentModel> projectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();
                try {
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectStageAssignCommentModelJson);
                    for (int projectStageAssignCommentModelCounter = 0; projectStageAssignCommentModelCounter < jsonArray.length(); projectStageAssignCommentModelCounter++) {
                        projectStageAssignCommentModelList.add(ProjectStageAssignCommentModel.build(jsonArray.getJSONObject(projectStageAssignCommentModelCounter)));
                    }
                } catch (org.json.JSONException ex) {
                }
                projectStageAssignCommentModels = new ProjectStageAssignCommentModel[projectStageAssignCommentModelList.size()];
                projectStageAssignCommentModelList.toArray(projectStageAssignCommentModels);
            }
        }

        // -- Prepare ProjectStageAssignCommentListView --
        mProjectStageAssignCommentListView = ProjectStageAssignCommentListView.buildProjectStageAssignCommentListView(getContext(), null);
        mProjectStageAssignCommentListView.setProjectStageAssignCommentListListener(this);
        mProjectStageAssignCommentListView.setImageRequestListener(this);
        mProjectStageAssignCommentListView.setProjectStageModel(projectStageModel);
        mProjectStageAssignCommentListView.setProjectStageAssignCommentModels(projectStageAssignCommentModels);

        if (projectStageModel != null && projectStageAssignCommentModels == null) {
            // -- Load ProjectStageAssignCommentList --
            onProjectStageAssignCommentListRequest(projectStageModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectStageAssignCommentList to fragment --
        return mProjectStageAssignCommentListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
                                mProjectStageAssignCommentListView.addProjectStageAssignCommentModel(projectStageAssignCommentModel);
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

    public void showProjectStageAssignCommentForm(final ProjectStageAssignmentModel projectStageAssignmentModel) {
        if (projectStageAssignmentModel == null)
            return;

        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(getContext(), ProjectStageAssignCommentFormActivity.class);

        try {
            org.json.JSONObject projectStageAssignmentModelJsonObject = projectStageAssignmentModel.build();
            String projectStageAssignmentModelJson = projectStageAssignmentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentFormActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGNMENT_MODEL, projectStageAssignmentModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM);
    }

    public void showProjectStageAssignCommentForm(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(getContext(), ProjectStageAssignCommentFormActivity.class);

        try {
            org.json.JSONObject projectStageAssignCommentModelJsonObject = projectStageAssignCommentModel.build();
            String projectStageAssignCommentModelJson = projectStageAssignCommentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentFormActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_FORM);
    }

    @Override
    public void onProjectStageAssignCommentListRequest(ProjectStageModel projectStageModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
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
                    onProjectStageAssignCommentListRequestSuccess(projectHandleTaskResult.getProjectStageAssignCommentModels());
                    if (projectHandleTaskResult.getMessage() != null)
                        onProjectStageAssignCommentListRequestMessage(projectHandleTaskResult.getMessage());
                } else {
                    onProjectStageAssignCommentListRequestMessage(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectStageAssignCommentListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectStageGetAsyncTask --
        projectStageGetAsyncTask.execute(new ProjectStageGetAsyncTaskParam(getContext(), settingUserModel, projectStageModel.getProjectStageId(), sessionLoginModel.getProjectMemberModel()));
    }
    
    @Override
    public void onProjectStageAssignCommentItemClick(ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        // -- Redirect to ProjectStageAssignCommentFormActivity --
        Intent intent = new Intent(getContext(), ProjectStageAssignCommentDetailActivity.class);

        try {
            org.json.JSONObject projectStageAssignCommentModelJsonObject = projectStageAssignCommentModel.build();
            String projectStageAssignCommentModelJson = projectStageAssignCommentModelJsonObject.toString(0);
            intent.putExtra(ProjectStageAssignCommentDetailActivity.INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL);
    }

    @Override
    public void onImageRequest(final ImageView imageView, final Integer fileId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
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
                        File file = fileModel.getFile(getContext());
                        if (file != null)
                            ImageUtil.setImageThumbnailView(getContext(), imageView, file);
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
                        File file = fileModel.getFile(getContext());
                        if (file != null)
                            ImageUtil.setImageThumbnailView(getContext(), imageView, file);
                    }
                }

                // -- Do FileGetNetworkAsyncTask --
                fileGetNetworkAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, fileModel));

                mAsyncTaskList.remove(this);
            }
        };

        // -- Do FileGetCacheAsyncTask --
        fileGetCacheAsyncTask.execute(new FileGetAsyncTaskParam(getContext(), settingUserModel, fileId, null));
    }

    protected void onProjectStageAssignCommentListRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageAssignCommentListRequestSuccess(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageAssignCommentListView.setProjectStageAssignCommentModels(projectStageAssignCommentModels);
    }

    protected void onProjectStageAssignCommentListRequestMessage(final String message) {

    }
    
    public void reloadProjectStageAssignCommentList(final ProjectStageModel projectStageModel) {
        // -- Load ProjectStageAssignCommentList --
        onProjectStageAssignCommentListRequest(projectStageModel);
    }

    public void addProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentListView.addProjectStageAssignCommentModel(projectStageAssignCommentModel);
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
        if (mProjectStageAssignCommentListView != null)
            return mProjectStageAssignCommentListView.getProjectStageAssignCommentModels();
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }

    public void setProjectStageAssignCommentListFragmentListener(final ProjectStageAssignCommentListFragmentListener projectStageAssignCommentListFragmentListener) {
        mProjectStageAssignCommentListFragmentListener = projectStageAssignCommentListFragmentListener;
    }

    public interface ProjectStageAssignCommentListFragmentListener {
        void onProjectStageAssignCommentListRequest(ProjectStageModel projectStageModel);
        void onProjectStageAssignCommentItemClick(ProjectStageAssignCommentModel projectStageAssignCommentModel);
    }
}