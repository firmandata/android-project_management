package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.asynctask.InspectorProjectActivityListAsyncTask;
import com.construction.pm.asynctask.param.InspectorProjectActivityListAsyncTaskParam;
import com.construction.pm.asynctask.result.InspectorProjectActivityListAsyncTaskResult;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.networks.InspectorNetwork;
import com.construction.pm.networks.webapi.WebApiError;
import com.construction.pm.persistence.InspectorCachePersistent;
import com.construction.pm.persistence.PersistenceError;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.inspector.InspectorLayout;

import java.util.ArrayList;
import java.util.List;

public class InspectorFragment extends Fragment implements InspectorLayout.InspectorLayoutListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected InspectorLayout mInspectorLayout;

    public static InspectorFragment newInstance() {
        return new InspectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare InspectorLayout --
        mInspectorLayout = InspectorLayout.buildInspectorLayout(getContext(), null);
        mInspectorLayout.setInspectorLayoutListener(this);

        // -- Load to Fragment --
        mInspectorLayout.loadLayoutToFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load InspectorLayout to fragment --
        return mInspectorLayout.getLayout();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onInspectorRequest() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare InspectorProjectActivityListAsyncTask --
        InspectorProjectActivityListAsyncTask inspectorProjectActivityListAsyncTask = new InspectorProjectActivityListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(InspectorProjectActivityListAsyncTaskResult projectActivityListHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectActivityListHandleTaskResult != null) {
                    ProjectActivityModel[] projectActivityModels = projectActivityListHandleTaskResult.getProjectActivityModels();
                    if (projectActivityModels != null)
                        onProjectActivityListRequestSuccess(projectActivityModels);
                    else
                        onProjectActivityListRequestFailed(projectActivityListHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onProjectActivityListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do InspectorProjectActivityListAsyncTask --
        inspectorProjectActivityListAsyncTask.execute(new InspectorProjectActivityListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onProjectActivityListRequestProgress(final String progressMessage) {

    }

    protected void onProjectActivityListRequestSuccess(final ProjectActivityModel[] projectActivityModels) {
        mInspectorLayout.setLayoutData(projectActivityModels);
    }

    protected void onProjectActivityListRequestFailed(final String errorMessage) {

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
}
