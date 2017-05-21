package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.ProjectStageGetAsyncTask;
import com.construction.pm.asynctask.param.ProjectStageGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageGetAsyncTaskResult;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.project_stage.ProjectStageView;

import java.util.ArrayList;
import java.util.List;

public class ProjectStageFragment extends Fragment implements
        ProjectStageView.ProjectStageViewListener {

    public static final String PARAM_PROJECT_STAGE_MODEL = "PROJECT_STAGE_MODEL";
    public static final String PARAM_PROJECT_STAGE_ASSIGNMENT_MODELS = "PROJECT_STAGE_ASSIGNMENT_MODELS";
    public static final String PARAM_PROJECT_STAGE_DOCUMENT_MODELS = "PROJECT_STAGE_DOCUMENT_MODELS";

    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageView mProjectStageView;

    protected ProjectStageFragmentListener mProjectStageFragmentListener;

    public static ProjectStageFragment newInstance(final ProjectStageFragmentListener projectStageFragmentListener) {
        return newInstance(null, projectStageFragmentListener);
    }

    public static ProjectStageFragment newInstance(final ProjectStageModel projectStageModel, final ProjectStageFragmentListener projectStageFragmentListener) {
        return newInstance(projectStageModel, null, null, projectStageFragmentListener);
    }

    public static ProjectStageFragment newInstance(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels, final ProjectStageFragmentListener projectStageFragmentListener) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (projectStageModel != null) {
            try {
                org.json.JSONObject projectStageModelJsonObject = projectStageModel.build();
                String projectStageModelJson = projectStageModelJsonObject.toString(0);
                bundle.putString(PARAM_PROJECT_STAGE_MODEL, projectStageModelJson);
            } catch (org.json.JSONException ex) {
            }
        }
        if (projectStageAssignmentModels != null) {
            try {
                org.json.JSONArray projectStageAssignmentModelJson = new org.json.JSONArray();
                for (ProjectStageAssignmentModel projectStageAssignmentModel : projectStageAssignmentModels)
                    projectStageAssignmentModelJson.put(projectStageAssignmentModel.build());
                bundle.putString(PARAM_PROJECT_STAGE_ASSIGNMENT_MODELS, projectStageAssignmentModelJson.toString(0));
            } catch (org.json.JSONException ex) {
            }
        }
        if (projectStageDocumentModels != null) {
            try {
                org.json.JSONArray projectStageDocumentModelJson = new org.json.JSONArray();
                for (ProjectStageDocumentModel projectStageDocumentModel : projectStageDocumentModels)
                    projectStageDocumentModelJson.put(projectStageDocumentModel.build());
                bundle.putString(PARAM_PROJECT_STAGE_DOCUMENT_MODELS, projectStageDocumentModelJson.toString(0));
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ProjectStageFragment --
        ProjectStageFragment projectStageFragment = new ProjectStageFragment();
        projectStageFragment.setArguments(bundle);
        projectStageFragment.setProjectStageFragmentListener(projectStageFragmentListener);
        return projectStageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ProjectStageModel projectStageModel = null;
        ProjectStageAssignmentModel[] projectStageAssignmentModels = null;
        ProjectStageDocumentModel[] projectStageDocumentModels = null;

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

            // -- Get ProjectStageAssignmentModels parameter --
            String projectStageAssignmentModelJson = bundle.getString(PARAM_PROJECT_STAGE_ASSIGNMENT_MODELS);
            if (projectStageAssignmentModelJson != null) {
                List<ProjectStageAssignmentModel> projectStageAssignmentModelList = new ArrayList<ProjectStageAssignmentModel>();
                try {
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectStageAssignmentModelJson);
                    for (int projectStageAssignmentModelCounter = 0; projectStageAssignmentModelCounter < jsonArray.length(); projectStageAssignmentModelCounter++) {
                        projectStageAssignmentModelList.add(ProjectStageAssignmentModel.build(jsonArray.getJSONObject(projectStageAssignmentModelCounter)));
                    }
                } catch (org.json.JSONException ex) {
                }
                projectStageAssignmentModels = new ProjectStageAssignmentModel[projectStageAssignmentModelList.size()];
                projectStageAssignmentModelList.toArray(projectStageAssignmentModels);
            }

            // -- Get ProjectStageDocumentModels parameter --
            String projectStageDocumentModelJson = bundle.getString(PARAM_PROJECT_STAGE_DOCUMENT_MODELS);
            if (projectStageDocumentModelJson != null) {
                List<ProjectStageDocumentModel> projectStageDocumentModelList = new ArrayList<ProjectStageDocumentModel>();
                try {
                    org.json.JSONArray jsonArray = new org.json.JSONArray(projectStageDocumentModelJson);
                    for (int projectStageDocumentModelCounter = 0; projectStageDocumentModelCounter < jsonArray.length(); projectStageDocumentModelCounter++) {
                        projectStageDocumentModelList.add(ProjectStageDocumentModel.build(jsonArray.getJSONObject(projectStageDocumentModelCounter)));
                    }
                } catch (org.json.JSONException ex) {
                }
                projectStageDocumentModels = new ProjectStageDocumentModel[projectStageDocumentModelList.size()];
                projectStageDocumentModelList.toArray(projectStageDocumentModels);
            }
        }

        // -- Prepare ProjectStageView --
        mProjectStageView = ProjectStageView.buildProjectStageView(getContext(), null);
        mProjectStageView.setProjectStageViewListener(this);
        mProjectStageView.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageDocumentModels);
        mProjectStageView.loadLayoutToFragment(this);

        if (projectStageModel != null) {
            // -- Load ProjectStage --
            onProjectStageRequest(projectStageModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ProjectStageView to fragment --
        return mProjectStageView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onProjectStageRequest(ProjectStageModel projectStageModel) {
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
                    onProjectStageRequestSuccess(projectHandleTaskResult.getProjectStageModel(), projectHandleTaskResult.getProjectStageAssignmentModels(), projectHandleTaskResult.getProjectStageDocumentModels());
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
        projectStageGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ProjectStageGetAsyncTaskParam(getContext(), settingUserModel, projectStageModel.getProjectStageId(), sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel) {
        if (mProjectStageFragmentListener != null)
            mProjectStageFragmentListener.onProjectStageDocumentItemClick(projectStageDocumentModel);
    }

    @Override
    public void onProjectStageDocumentItemClick(final FileModel fileModel) {
        if (mProjectStageFragmentListener != null)
            mProjectStageFragmentListener.onProjectStageDocumentItemClick(fileModel);
    }

    @Override
    public void onProjectStageDocumentItemClick(String errorMessage) {
        if (mProjectStageFragmentListener != null)
            mProjectStageFragmentListener.onProjectStageDocumentItemClick(errorMessage);
    }

    protected void onProjectStageRequestProgress(final String progressMessage) {

    }

    protected void onProjectStageRequestSuccess(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels) {
        mProjectStageView.setLayoutData(projectStageModel, projectStageAssignmentModels, projectStageDocumentModels);
    }

    protected void onProjectStageRequestMessage(final String message) {

    }

    public ProjectStageModel getProjectStageModel() {
        if (mProjectStageView != null)
            return mProjectStageView.getProjectStageModel();
        return null;
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        if (mProjectStageView != null)
            return mProjectStageView.getProjectStageAssignmentModels();
        return null;
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        if (mProjectStageView != null)
            return mProjectStageView.getProjectStageDocumentModels();
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                asyncTask.cancel(true);
            }
        }

        super.onDestroy();
    }

    public void setProjectStageFragmentListener(final ProjectStageFragmentListener projectStageFragmentListener) {
        mProjectStageFragmentListener = projectStageFragmentListener;
    }

    public interface ProjectStageFragmentListener {
        void onProjectStageRequest(ProjectStageModel projectStageModel);
        void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel);
        void onProjectStageDocumentItemClick(FileModel fileModel);
        void onProjectStageDocumentItemClick(String errorMessage);
    }
}