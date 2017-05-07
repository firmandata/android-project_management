package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.ProjectActivityDashboardAsyncTask;
import com.construction.pm.asynctask.param.ProjectActivityDashboardAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectActivityDashboardAsyncTaskResult;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.models.StatusTaskEnum;
import com.construction.pm.models.network.ProjectActivityDashboardResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.HomeView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView.HomeListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected HomeView mHomeView;

    protected HomeFragmentListener mHomeFragmentListener;

    public static HomeFragment newInstance(final HomeFragmentListener homeFragmentListener) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setHomeFragmentListener(homeFragmentListener);
        return homeFragment;
    }

    public static HomeFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare HomeView --
        mHomeView = HomeView.buildHomeView(getContext(), null);
        mHomeView.setHomeListener(this);

        requestProjectActivityDashboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load HomeView to fragment --
        return mHomeView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void requestProjectActivityDashboard() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectActivityDashboardAsyncTask --
        ProjectActivityDashboardAsyncTask projectActivityDashboardAsyncTask = new ProjectActivityDashboardAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectActivityDashboardAsyncTaskResult projectActivityDashboardAsyncTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectActivityDashboardAsyncTaskResult != null) {
                    ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel = projectActivityDashboardAsyncTaskResult.getProjectActivityDashboardResponseModel();
                    if (projectActivityDashboardResponseModel != null)
                        onRequestProjectActivityDashboardSuccess(projectActivityDashboardResponseModel);
                    else
                        onRequestProjectActivityDashboardFailed(projectActivityDashboardAsyncTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onRequestProjectActivityDashboardProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectActivityDashboardAsyncTask --
        projectActivityDashboardAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ProjectActivityDashboardAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onRequestProjectActivityDashboardProgress(final String progressMessage) {

    }

    protected void onRequestProjectActivityDashboardSuccess(final ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel) {
        mHomeView.setProjectActivityDashboardResponseModel(projectActivityDashboardResponseModel);
    }

    protected void onRequestProjectActivityDashboardFailed(final String errorMessage) {

    }

    @Override
    public void onDashboardInspectorItemClick(ProjectActivityDashboardModel projectActivityDashboardModel) {
        if (projectActivityDashboardModel != null) {
            if (mHomeFragmentListener != null)
                mHomeFragmentListener.onHomeRequestInspectorFragment(projectActivityDashboardModel.getStatusTask());
        }
    }

    @Override
    public void onDashboardManagerItemClick(ProjectActivityDashboardModel projectActivityDashboardModel) {
        if (projectActivityDashboardModel != null) {
            if (mHomeFragmentListener != null)
                mHomeFragmentListener.onHomeRequestManagerFragment(projectActivityDashboardModel.getStatusTask());
        }
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

    public void setHomeFragmentListener(final HomeFragmentListener homeFragmentListener) {
        mHomeFragmentListener = homeFragmentListener;
    }

    public interface HomeFragmentListener {
        void onHomeRequestInspectorFragment(StatusTaskEnum statusTaskEnum);
        void onHomeRequestManagerFragment(StatusTaskEnum statusTaskEnum);
    }
}
