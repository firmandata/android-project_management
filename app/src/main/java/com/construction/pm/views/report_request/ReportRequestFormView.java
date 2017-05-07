package com.construction.pm.views.report_request;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.adapter.SpinnerProjectListAdapter;

public class ReportRequestFormView {
    protected Context mContext;

    protected RelativeLayout mReportRequestFormView;
    protected AppCompatSpinner mSpinnerProjectList;
    protected SpinnerProjectListAdapter mSpinnerProjectListAdapter;
    protected TextInputEditText mComment;
    protected AppCompatButton mSendButton;

    protected ReportRequestFormListener mReportRequestFormListener;

    public ReportRequestFormView(final Context context) {
        mContext = context;
    }

    public ReportRequestFormView(final Context context, final RelativeLayout reportRequestFormView) {
        this(context);

        initializeView(reportRequestFormView);
    }

    public static ReportRequestFormView buildReportRequestFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ReportRequestFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ReportRequestFormView buildReportRequestFormView(final Context context, final ViewGroup viewGroup) {
        return buildReportRequestFormView(context, R.layout.report_request_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout reportRequestFormView) {
        mReportRequestFormView = reportRequestFormView;

        mSpinnerProjectList = (AppCompatSpinner) mReportRequestFormView.findViewById(R.id.projectList);
        mSpinnerProjectListAdapter = new SpinnerProjectListAdapter(mContext);
        mSpinnerProjectList.setAdapter(mSpinnerProjectListAdapter);

        mComment = (TextInputEditText) mReportRequestFormView.findViewById(R.id.comment);

        mSendButton = (AppCompatButton) mReportRequestFormView.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportRequestModel reportRequestModel = getReportRequestModel();
                if (reportRequestModel == null) {
                    if (mReportRequestFormListener != null)
                        mReportRequestFormListener.onReportRequestFormInvalid(ViewUtil.getResourceString(mContext, R.string.report_request_form_view_error_empty_data));
                    return;
                }
                if (reportRequestModel.getProjectId() == null) {
                    if (mReportRequestFormListener != null)
                        mReportRequestFormListener.onReportRequestFormInvalid(ViewUtil.getResourceString(mContext, R.string.report_request_form_view_error_require, ViewUtil.getResourceString(mContext, R.string.report_request_form_view_project_name)));
                    return;
                }
                if (reportRequestModel.getComment() == null) {
                    if (mReportRequestFormListener != null)
                        mReportRequestFormListener.onReportRequestFormInvalid(ViewUtil.getResourceString(mContext, R.string.report_request_form_view_error_require, ViewUtil.getResourceString(mContext, R.string.report_request_form_view_comment)));
                    return;
                }

                if (mReportRequestFormListener != null)
                    mReportRequestFormListener.onReportRequestFormSend(reportRequestModel);
            }
        });

        ButtonUtil.setButtonPrimary(mContext, mSendButton);
    }

    public RelativeLayout getView() {
        return mReportRequestFormView;
    }

    public void setProjectModels(final ProjectModel[] projectModels) {
        mSpinnerProjectListAdapter.addProjectModels(projectModels);
    }

    protected ReportRequestModel getReportRequestModel() {
        ReportRequestModel reportRequestModel = new ReportRequestModel();

        if (mSpinnerProjectList.getSelectedItem() != null)
            reportRequestModel.setProjectId(((ProjectModel) mSpinnerProjectList.getSelectedItem()).getProjectId());
        else
            reportRequestModel.setProjectId(null);

        String comment = mComment.getText().toString();
        if (!comment.isEmpty())
            reportRequestModel.setComment(comment);

        return reportRequestModel;
    }

    public void setReportRequestFormListener(final ReportRequestFormListener reportRequestFormListener) {
        mReportRequestFormListener = reportRequestFormListener;
    }

    public interface ReportRequestFormListener {
        void onReportRequestFormInvalid(String invalidMessage);
        void onReportRequestFormSend(ReportRequestModel reportRequestModel);
    }
}
