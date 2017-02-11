package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.views.notification.NotificationDetailView;

public class NotificationDetailFragment extends Fragment {
    public static final String PARAM_NOTIFICATION_MODEL = "NotificationModel";

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
    public void onDetach() {
        super.onDetach();
    }
}
