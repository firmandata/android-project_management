package com.construction.pm.activities.fragmentdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.construction.pm.asynctask.ReportRequestResendAsyncTask;
import com.construction.pm.asynctask.param.ReportRequestResendAsyncTaskParam;
import com.construction.pm.asynctask.result.ReportRequestResendAsyncTaskResult;
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.report_request.ReportRequestDetailView;

import java.util.ArrayList;
import java.util.List;

public class ReportRequestDetailDialogFragment extends DialogFragment implements ReportRequestDetailView.ReportRequestDetailListener {

    public static final String PARAM_REPORT_REQUEST_MODEL = "REPORT_REQUEST_MODEL";

    protected List<AsyncTask> mAsyncTaskList;

    protected ReportRequestDetailView mReportRequestDetailView;

    protected ReportRequestDetailListener mReportRequestDetailListener;

    public static ReportRequestDetailDialogFragment newInstance(ReportRequestModel reportRequestModel, final ReportRequestDetailListener reportRequestDetailListener) {
        final Bundle bundle = new Bundle();
        if (reportRequestModel != null) {
            try {
                bundle.putString(PARAM_REPORT_REQUEST_MODEL, reportRequestModel.build().toString(0));
            } catch (org.json.JSONException ex) {
            }
        }

        final ReportRequestDetailDialogFragment reportRequestDetailDialogFragment = new ReportRequestDetailDialogFragment();
        reportRequestDetailDialogFragment.setArguments(bundle);
        reportRequestDetailDialogFragment.setReportRequestDetailListener(reportRequestDetailListener);
        return reportRequestDetailDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        ReportRequestModel reportRequestModel = null;

        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ReportRequestModel parameter --
            String reportRequestModelJson = bundle.getString(PARAM_REPORT_REQUEST_MODEL);
            if (reportRequestModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(reportRequestModelJson);
                    reportRequestModel = ReportRequestModel.build(jsonObject);
                } catch (org.json.JSONException ex) {

                }
            }
        }

        mReportRequestDetailView = ReportRequestDetailView.buildReportRequestDetailView(getContext(), null);
        mReportRequestDetailView.setReportRequestDetailListener(this);
        mReportRequestDetailView.setReportRequestModel(reportRequestModel);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(mReportRequestDetailView.getView());
        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onReportRequestDetailResend(ReportRequestModel reportRequestModel) {
        // -- Get SettingUserModel from SettingPersistent --
        SettingPersistent settingPersistent = new SettingPersistent(getContext());
        SettingUserModel settingUserModel = settingPersistent.getSettingUserModel();

        // -- Get SessionLoginModel from SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(getContext());
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Get ProjectMemberModel --
        ProjectMemberModel projectMemberModel = sessionLoginModel.getProjectMemberModel();
        if (projectMemberModel != null) {
            // -- Prepare ReportRequestResendAsyncTask --
            ReportRequestResendAsyncTask reportRequestResendAsyncTask = new ReportRequestResendAsyncTask() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(ReportRequestResendAsyncTaskResult reportRequestResendAsyncTaskResult) {
                    if (reportRequestResendAsyncTaskResult != null) {
                        if (mReportRequestDetailListener != null)
                            mReportRequestDetailListener.onReportRequestDetailResent(reportRequestResendAsyncTaskResult.getReportRequestModel(), reportRequestResendAsyncTaskResult.getMessage());
                    }
                }

                @Override
                protected void onProgressUpdate(String... messages) {
                    if (messages != null) {
                        if (messages.length > 0) {
                            if (mReportRequestDetailListener != null)
                                mReportRequestDetailListener.onReportRequestDetailResendProgress(messages[0]);
                        }
                    }
                }
            };

            // -- Do ReportRequestResendAsyncTask --
            reportRequestResendAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ReportRequestResendAsyncTaskParam(getContext(), settingUserModel, reportRequestModel, sessionLoginModel.getProjectMemberModel()));
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

    public void setReportRequestDetailListener(final ReportRequestDetailListener reportRequestDetailListener) {
        mReportRequestDetailListener = reportRequestDetailListener;
    }

    public interface ReportRequestDetailListener {
        void onReportRequestDetailResendProgress(String progressMessage);
        void onReportRequestDetailResent(ReportRequestModel reportRequestModel, String message);
    }
}
