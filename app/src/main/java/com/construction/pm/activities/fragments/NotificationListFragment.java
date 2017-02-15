package com.construction.pm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.NotificationActivity;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.NotificationPersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.notification.NotificationListView;

import static android.app.Activity.RESULT_OK;

public class NotificationListFragment extends Fragment implements NotificationListView.NotificationListListener {

    protected NotificationListView mNotificationListView;

    protected NotificationListFragmentListener mNotificationListFragmentListener;

    public static NotificationListFragment newInstance() {
        return new NotificationListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // -- Prepare NotificationListHandleTask --
        NotificationListHandleTask notificationListHandleTask = new NotificationListHandleTask() {
            @Override
            public void onPostExecute(NotificationListHandleTaskResult notificationListHandleTaskResult) {
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

        // -- Do NotificationListHandleTask --
        notificationListHandleTask.execute(new NotificationListHandleTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantUtil.INTENT_REQUEST_NOTIFICATION_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                // -- Get NotificationModel --
                NotificationModel notificationModel = null;
                Bundle bundle = data.getExtras();
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

            intent.putExtra(NotificationActivity.INTENT_PARAM_NOTIFICATION_MODEL, notificationModelJson);
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

    protected class NotificationListHandleTaskParam {

        protected Context mContext;
        protected SettingUserModel mSettingUserModel;
        protected ProjectMemberModel mProjectMemberModel;

        public NotificationListHandleTaskParam(final Context context, final SettingUserModel settingUserModel, final ProjectMemberModel notificationMemberModel) {
            mContext = context;
            mSettingUserModel = settingUserModel;
            mProjectMemberModel = notificationMemberModel;
        }

        public Context getContext() {
            return mContext;
        }

        public SettingUserModel getSettingUserModel() {
            return mSettingUserModel;
        }

        public ProjectMemberModel getProjectMemberModel() {
            return mProjectMemberModel;
        }
    }

    protected class NotificationListHandleTaskResult {

        protected NotificationModel[] mNotificationModels;
        protected String mMessage;

        public NotificationListHandleTaskResult() {

        }

        public void setNotificationModels(final NotificationModel[] notificationModels) {
            mNotificationModels = notificationModels;
        }

        public NotificationModel[] getNotificationModels() {
            return mNotificationModels;
        }

        public void setMessage(final String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    protected class NotificationListHandleTask extends AsyncTask<NotificationListHandleTaskParam, String, NotificationListHandleTaskResult> {
        protected NotificationListHandleTaskParam mNotificationListHandleTaskParam;
        protected Context mContext;

        @Override
        protected NotificationListHandleTaskResult doInBackground(NotificationListHandleTaskParam... notificationListHandleTaskParams) {
            // Get NotificationListHandleTaskParam
            mNotificationListHandleTaskParam = notificationListHandleTaskParams[0];
            mContext = mNotificationListHandleTaskParam.getContext();
            ProjectMemberModel projectMemberModel = mNotificationListHandleTaskParam.getProjectMemberModel();

            // -- Prepare NotificationListHandleTaskResult --
            NotificationListHandleTaskResult notificationListHandleTaskResult = new NotificationListHandleTaskResult();

            // -- Get NotificationModels progress --
            publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_list_handle_task_begin));

            // -- Prepare NotificationPersistent --
            NotificationPersistent notificationPersistent = new NotificationPersistent(mContext);

            // -- Get NotificationModels from NotificationPersistent --
            NotificationModel[] notificationModels = null;
            try {
                notificationModels = notificationPersistent.getNotificationModels(projectMemberModel.getProjectMemberId());
            } catch (PersistenceError ex) {
                notificationListHandleTaskResult.setMessage(ex.getMessage());
                publishProgress(ex.getMessage());
            }

            if (notificationModels != null) {
                // -- Set NotificationModels to result --
                notificationListHandleTaskResult.setNotificationModels(notificationModels);

                // -- Get NotificationModels progress --
                publishProgress(ViewUtil.getResourceString(mContext, R.string.notification_list_handle_task_success));
            }

            return notificationListHandleTaskResult;
        }
    }

    public void addNotificationModels(final NotificationModel[] notificationModels) {
        if (mNotificationListView != null)
            mNotificationListView.addNotificationModels(notificationModels);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setNotificationListFragmentListener(final NotificationListFragmentListener notificationListFragmentListener) {
        mNotificationListFragmentListener = notificationListFragmentListener;
    }

    public interface NotificationListFragmentListener {
        void onNotificationListRead(NotificationModel notificationModel);
    }
}
