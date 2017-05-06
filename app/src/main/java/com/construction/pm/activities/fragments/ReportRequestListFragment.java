package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.asynctask.ReportRequestListAsyncTask;
import com.construction.pm.asynctask.param.ReportRequestListAsyncTaskParam;
import com.construction.pm.asynctask.result.ReportRequestListAsyncTaskResult;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.report_request.ReportRequestListView;

import java.util.ArrayList;
import java.util.List;

public class ReportRequestListFragment extends Fragment implements ReportRequestListView.ReportRequestListListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected ReportRequestListView mReportRequestListView;

    public static ReportRequestListFragment newInstance() {
        return new ReportRequestListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Prepare ReportRequestListView --
        mReportRequestListView = ReportRequestListView.buildReportRequestListView(getContext(), null);
        mReportRequestListView.setReportRequestListListener(this);
        mReportRequestListView.loadLayoutToFragment(this);

        // -- Load ReportRequestList --
        onReportRequestListRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ReportRequestListView to fragment --
        return mReportRequestListView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onReportRequestListRequest() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ReportRequestListAsyncTask --
        ReportRequestListAsyncTask reportRequestListAsyncTask = new ReportRequestListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ReportRequestListAsyncTaskResult reportRequestListHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (reportRequestListHandleTaskResult != null) {
                    ReportRequestModel[] reportRequestModels = reportRequestListHandleTaskResult.getReportRequestModels();
                    if (reportRequestModels != null)
                        onReportRequestListRequestSuccess(reportRequestModels);
                    else
                        onReportRequestListRequestFailed(reportRequestListHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onReportRequestListRequestProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ReportRequestListAsyncTask --
        reportRequestListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ReportRequestListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    @Override
    public void onReportRequestItemClick(ReportRequestModel reportRequestModel) {
        if (mReportRequestListView != null)
            mReportRequestListView.showReportRequestDetail(reportRequestModel);
    }

    protected void onReportRequestListRequestProgress(final String progressMessage) {
        mReportRequestListView.startRefreshAnimation();
    }

    protected void onReportRequestListRequestSuccess(final ReportRequestModel[] reportRequestModels) {
        mReportRequestListView.setReportRequestModels(reportRequestModels);
        mReportRequestListView.stopRefreshAnimation();
    }

    protected void onReportRequestListRequestFailed(final String errorMessage) {
        mReportRequestListView.stopRefreshAnimation();
    }

    public void addReportRequest() {
        if (mReportRequestListView != null)
            mReportRequestListView.showReportRequestAdd();
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
