package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.InspectorDetailActivity;
import com.construction.pm.activities.ManagerDetailActivity;
import com.construction.pm.activities.ProjectStageActivity;
import com.construction.pm.asynctask.InspectorProjectActivityGetAsyncTask;
import com.construction.pm.asynctask.ManagerProjectActivityGetAsyncTask;
import com.construction.pm.asynctask.ProjectStageGetAsyncTask;
import com.construction.pm.asynctask.param.InspectorProjectActivityGetAsyncTaskParam;
import com.construction.pm.asynctask.param.ManagerProjectActivityGetAsyncTaskParam;
import com.construction.pm.asynctask.param.ProjectStageGetAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityGetAsyncTaskResult;
import com.construction.pm.asynctask.result.ManagerProjectActivityGetAsyncTaskResult;
import com.construction.pm.asynctask.result.ProjectStageGetAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.notification.NotificationDetailView;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailFragment extends Fragment implements NotificationDetailView.NotificationDetailListener {
    public static final String PARAM_NOTIFICATION_MODEL = "NotificationModel";

    protected List<AsyncTask> mAsyncTaskList;

    protected NotificationDetailView mNotificationDetailView;

    public static NotificationDetailFragment newInstance() {
        return newInstance(null);
    }

    public static NotificationDetailFragment newInstance(final NotificationModel notificationModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (notificationModel != null) {
            try {
                org.json.JSONObject notificationModelJsonObject = notificationModel.build();
                String notificationModelJson = notificationModelJsonObject.toString(0);
                bundle.putString(PARAM_NOTIFICATION_MODEL, notificationModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create NotificationDetailFragment --
        NotificationDetailFragment notificationDetailFragment = new NotificationDetailFragment();
        notificationDetailFragment.setArguments(bundle);
        return notificationDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        NotificationModel notificationModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get NotificationModel parameter --
            String notificationModelJson = bundle.getString(PARAM_NOTIFICATION_MODEL);
            if (notificationModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(notificationModelJson);
                    notificationModel = NotificationModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare NotificationDetailView --
        mNotificationDetailView = NotificationDetailView.buildNotificationDetailView(getContext(), null);
        mNotificationDetailView.setNotificationDetailListener(this);
        if (notificationModel != null)
            mNotificationDetailView.setNotificationModel(notificationModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load NotificationDetailView to fragment --
        return mNotificationDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setNotificationModel(final NotificationModel notificationModel) {
        if (mNotificationDetailView != null)
            mNotificationDetailView.setNotificationModel(notificationModel);
    }

    @Override
    public void onRequestProjectStage(Integer projectStageId) {
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
                    mNotificationDetailView.setProjectStageModel(projectHandleTaskResult.getProjectStageModel());
                } else {
                    mNotificationDetailView.setProjectStageModel(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {

            }
        };

        // -- Do ProjectStageGetAsyncTask --
        projectStageGetAsyncTask.execute(new ProjectStageGetAsyncTaskParam(getContext(), settingUserModel, projectStageId, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onRequestProjectActivity(Integer projectActivityId) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
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
                    mNotificationDetailView.setManagerProjectActivityModel(projectActivityGetHandleTaskResult.getProjectActivityModel());
                } else {
                    mNotificationDetailView.setManagerProjectActivityModel(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {

            }
        };

        // -- Do ManagerProjectActivityGetAsyncTask --
        managerProjectActivityGetAsyncTask.execute(new ManagerProjectActivityGetAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel(), projectActivityId));

        // -- Prepare InspectorProjectActivityGetAsyncTask --
        InspectorProjectActivityGetAsyncTask inspectorProjectActivityGetAsyncTask = new InspectorProjectActivityGetAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityGetAsyncTaskResult projectActivityGetHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectActivityGetHandleTaskResult != null) {
                    mNotificationDetailView.setInspectorProjectActivityModel(projectActivityGetHandleTaskResult.getProjectActivityModel());
                } else {
                    mNotificationDetailView.setInspectorProjectActivityModel(null);
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {

            }
        };

        // -- Do InspectorProjectActivityGetAsyncTask --
        inspectorProjectActivityGetAsyncTask.execute(new InspectorProjectActivityGetAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel(), projectActivityId));
    }

    @Override
    public void onProjectStageClick(ProjectStageModel projectStageModel) {
        // -- Redirect to ProjectStageActivity --
        Intent intent = new Intent(this.getContext(), ProjectStageActivity.class);

        try {
            org.json.JSONObject projectStageModelJsonObject = projectStageModel.build();
            String projectStageModelJson = projectStageModelJsonObject.toString(0);

            intent.putExtra(ProjectStageActivity.INTENT_PARAM_PROJECT_STAGE_MODEL, projectStageModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivity(intent);
    }

    @Override
    public void onManagerProjectActivityClick(ProjectActivityModel projectActivityModel) {
        // -- Redirect to ManagerDetailActivity --
        Intent intent = new Intent(this.getContext(), ManagerDetailActivity.class);

        try {
            org.json.JSONObject projectActivityModelJsonObject = projectActivityModel.build();
            String projectActivityModelJson = projectActivityModelJsonObject.toString(0);

            intent.putExtra(ManagerDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivity(intent);
    }

    @Override
    public void onInspectorProjectActivityClick(ProjectActivityModel projectActivityModel) {
        // -- Redirect to InspectorDetailActivity --
        Intent intent = new Intent(this.getContext(), InspectorDetailActivity.class);

        try {
            org.json.JSONObject projectActivityModelJsonObject = projectActivityModel.build();
            String projectActivityModelJson = projectActivityModelJsonObject.toString(0);

            intent.putExtra(InspectorDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivity(intent);
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
}
