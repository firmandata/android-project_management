package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.ProjectStageActivity;
import com.construction.pm.asynctask.ProjectStageGetAsyncTask;
import com.construction.pm.asynctask.param.ProjectStageGetAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectStageGetAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectStageModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
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
                String projectModelJson = notificationModelJsonObject.toString(0);
                bundle.putString(PARAM_NOTIFICATION_MODEL, projectModelJson);
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
            String contractModelJson = bundle.getString(PARAM_NOTIFICATION_MODEL);
            if (contractModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(contractModelJson);
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
    public void onProjectActivityClick(ProjectActivityModel projectActivityModel) {

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
