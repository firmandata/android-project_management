package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.activities.NotificationActivity;
import com.construction.pm.asynctask.NotificationListAsyncTask;
import com.construction.pm.asynctask.param.NotificationListAsyncTaskParam;
import com.construction.pm.asynctask.result.NotificationListAsyncTaskResult;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.notification.NotificationListView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NotificationListFragment extends Fragment implements NotificationListView.NotificationListListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected NotificationListView mNotificationListView;

    protected NotificationListFragmentListener mNotificationListFragmentListener;

    public static NotificationListFragment newInstance() {
        return new NotificationListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare NotificationListView --
        mNotificationListView = NotificationListView.buildNotificationListView(getContext(), null);
        mNotificationListView.setNotificationListListener(this);

        // -- Load NotificationList --
        onNotificationListRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load NotificationListView to fragment --
        return mNotificationListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onNotificationListRequest() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare NotificationListAsyncTask --
        NotificationListAsyncTask notificationListAsyncTask = new NotificationListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(NotificationListAsyncTaskResult notificationListHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (notificationListHandleTaskResult != null) {
                    NotificationModel[] notificationModels = notificationListHandleTaskResult.getNotificationModels();
                    if (notificationModels != null)
                        onNotificationListRequestSuccess(notificationModels);
                    else
                        onNotificationListRequestFailed(notificationListHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onNotificationListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do NotificationListAsyncTask --
        notificationListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new NotificationListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_NOTIFICATION_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                // -- Get NotificationModel --
                NotificationModel notificationModel = null;
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL)) {
                        String notificationModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_NOTIFICATION_MODEL);
                        if (notificationModelJson != null) {
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(notificationModelJson);
                                notificationModel = NotificationModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {

                            }
                        }
                    }
                }

                if (notificationModel != null) {
                    // -- Mark NotificationModel view as read --
                    mNotificationListView.updateNotificationModel(notificationModel);

                    if (mNotificationListFragmentListener != null)
                        mNotificationListFragmentListener.onNotificationListRead(notificationModel);
                }
            }
        }
    }

    @Override
    public void onNotificationItemClick(NotificationModel notificationModel, int position) {
        // -- Redirect to NotificationActivity --
        Intent intent = new Intent(this.getContext(), NotificationActivity.class);

        try {
            org.json.JSONObject notificationModelJsonObject = notificationModel.build();
            String notificationModelJson = notificationModelJsonObject.toString(0);

            intent.putExtra(ConstantUtil.INTENT_PARAM_NOTIFICATION_MODEL, notificationModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_NOTIFICATION_ACTIVITY);
    }

    protected void onNotificationListRequestProgress(final String progressMessage) {
        mNotificationListView.startRefreshAnimation();
    }

    protected void onNotificationListRequestSuccess(final NotificationModel[] notificationModels) {
        mNotificationListView.setNotificationModels(notificationModels);
        mNotificationListView.stopRefreshAnimation();
    }

    protected void onNotificationListRequestFailed(final String errorMessage) {
        mNotificationListView.stopRefreshAnimation();
    }

    public void addNotificationModels(final NotificationModel[] notificationModels) {
        if (mNotificationListView != null)
            mNotificationListView.addNotificationModels(notificationModels);
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

    public void setNotificationListFragmentListener(final NotificationListFragmentListener notificationListFragmentListener) {
        mNotificationListFragmentListener = notificationListFragmentListener;
    }

    public interface NotificationListFragmentListener {
        void onNotificationListRead(NotificationModel notificationModel);
    }
}
