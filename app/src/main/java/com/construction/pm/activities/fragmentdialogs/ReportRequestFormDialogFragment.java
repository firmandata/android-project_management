package com.construction.pm.activities.fragmentdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.construction.pm.asynctask.ProjectListAsyncTask;
import com.construction.pm.asynctask.ReportRequestSendAsyncTask;
import com.construction.pm.asynctask.param.ProjectListAsyncTaskParam;
import com.construction.pm.asynctask.param.ReportRequestSendAsyncTaskParam;
import com.construction.pm.asynctask.result.ProjectListAsyncTaskResult;
import com.construction.pm.asynctask.result.ReportRequestSendAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.report_request.ReportRequestFormView;

import java.util.ArrayList;
import java.util.List;

public class ReportRequestFormDialogFragment extends DialogFragment implements ReportRequestFormView.ReportRequestFormListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected ReportRequestFormView mReportRequestFormView;

    protected ReportRequestFormListener mReportRequestFormListener;

    public static ReportRequestFormDialogFragment newInstance(final ReportRequestFormListener reportRequestFormListener) {
        final Bundle bundle = new Bundle();

        final ReportRequestFormDialogFragment reportRequestFormDialogFragment = new ReportRequestFormDialogFragment();
        reportRequestFormDialogFragment.setArguments(bundle);
        reportRequestFormDialogFragment.setReportRequestFormListener(reportRequestFormListener);
        return reportRequestFormDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        Bundle bundle = getArguments();
        if (bundle != null) {

        }

        mReportRequestFormView = ReportRequestFormView.buildReportRequestFormView(getContext(), null);
        mReportRequestFormView.setReportRequestFormListener(this);

        requestProjectList();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(mReportRequestFormView.getView());
        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void requestProjectList() {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare ProjectListAsyncTask --
        ProjectListAsyncTask projectListAsyncTask = new ProjectListAsyncTask() {
            @Override
            public void onPreExecute() {
                mAsyncTaskList.add(this);
            }

            @Override
            public void onPostExecute(ProjectListAsyncTaskResult projectListHandleTaskResult) {
                mAsyncTaskList.remove(this);

                if (projectListHandleTaskResult != null) {
                    ProjectModel[] projectModels = projectListHandleTaskResult.getProjectModels();
                    if (projectModels != null)
                        onRequestProjectListSuccess(projectModels);
                    else
                        onRequestProjectListFailed(projectListHandleTaskResult.getMessage());
                }
            }

            @Override
            protected void onProgressUpdate(String... messages) {
                if (messages != null) {
                    if (messages.length > 0) {
                        onRequestProjectListProgress(messages[0]);
                    }
                }
            }
        };

        // -- Do ProjectListAsyncTask --
        projectListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ProjectListAsyncTaskParam(getContext(), settingUserModel, sessionLoginModel.getProjectMemberModel()));
    }

    protected void onRequestProjectListProgress(final String progressMessage) {
        if (mReportRequestFormListener != null)
            mReportRequestFormListener.onReportRequestFormProjectListProgress(progressMessage);
    }

    protected void onRequestProjectListSuccess(final ProjectModel[] projectModels) {
        if (mReportRequestFormView != null)
            mReportRequestFormView.setProjectModels(projectModels);
        if (mReportRequestFormListener != null)
            mReportRequestFormListener.onReportRequestFormProjectListFinish(null);
    }

    protected void onRequestProjectListFailed(final String errorMessage) {
        if (mReportRequestFormListener != null)
            mReportRequestFormListener.onReportRequestFormProjectListFinish(errorMessage);
    }

    @Override
    public void onReportRequestFormInvalid(String invalidMessage) {
        if (mReportRequestFormListener != null)
            mReportRequestFormListener.onReportRequestFormSendInvalid(invalidMessage);
    }

    @Override
    public void onReportRequestFormSend(ReportRequestModel reportRequestModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Set projectMemberId --
            reportRequestModel.setProjectMemberId(projectMemberModel.getProjectMemberId());

            // -- Prepare ReportRequestSendAsyncTask --
            ReportRequestSendAsyncTask reportRequestSendAsyncTask = new ReportRequestSendAsyncTask() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(ReportRequestSendAsyncTaskResult reportRequestSendAsyncTaskResult) {
                    if (reportRequestSendAsyncTaskResult != null) {
                        if (mReportRequestFormListener != null)
                            mReportRequestFormListener.onReportRequestFormSent(reportRequestSendAsyncTaskResult.getReportRequestModel(), reportRequestSendAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            if (mReportRequestFormListener != null)
                                mReportRequestFormListener.onReportRequestFormSendProgress(messages[0]);
                        }
                    }
                }
            };

            // -- Do ReportRequestSendAsyncTask --
            reportRequestSendAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ReportRequestSendAsyncTaskParam(getContext(), settingUserModel, reportRequestModel, projectMemberModel));
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

    public void setReportRequestFormListener(final ReportRequestFormListener reportRequestFormListener) {
        mReportRequestFormListener = reportRequestFormListener;
    }

    public interface ReportRequestFormListener {
        void onReportRequestFormProjectListProgress(String progressMessage);
        void onReportRequestFormProjectListFinish(String errorMessage);
        void onReportRequestFormSendInvalid(String invalidMessage);
        void onReportRequestFormSendProgress(String progressMessage);
        void onReportRequestFormSent(ReportRequestModel reportRequestModel, String message);
    }
}
