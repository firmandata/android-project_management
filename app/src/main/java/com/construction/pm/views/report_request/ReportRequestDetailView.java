package com.construction.pm.views.report_request;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.DateTimeUtil;

public class ReportRequestDetailView {
    protected Context mContext;

    protected RelativeLayout mReportRequestDetailView;
    protected AppCompatTextView mRequestDate;
    protected AppCompatTextView mRequestStatus;
    protected AppCompatTextView mProjectName;
    protected AppCompatTextView mComment;
    protected AppCompatTextView mLastReportSentToEmailDate;
    protected AppCompatButton mResendButton;

    protected ReportRequestModel mReportRequestModel;

    protected ReportRequestDetailListener mReportRequestDetailListener;

    public ReportRequestDetailView(final Context context) {
        mContext = context;
    }

    public ReportRequestDetailView(final Context context, final RelativeLayout reportRequestDetailView) {
        this(context);

        initializeView(reportRequestDetailView);
    }

    public static ReportRequestDetailView buildReportRequestDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ReportRequestDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ReportRequestDetailView buildReportRequestDetailView(final Context context, final ViewGroup viewGroup) {
        return buildReportRequestDetailView(context, R.layout.report_request_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout reportRequestDetailView) {
        mReportRequestDetailView = reportRequestDetailView;

        mRequestDate = (AppCompatTextView) mReportRequestDetailView.findViewById(R.id.requestDate);
        mRequestStatus = (AppCompatTextView) mReportRequestDetailView.findViewById(R.id.requestStatus);
        mProjectName = (AppCompatTextView) mReportRequestDetailView.findViewById(R.id.projectName);
        mComment = (AppCompatTextView) mReportRequestDetailView.findViewById(R.id.comment);
        mLastReportSentToEmailDate = (AppCompatTextView) mReportRequestDetailView.findViewById(R.id.lastReportSentToEmailDate);

        mResendButton = (AppCompatButton) mReportRequestDetailView.findViewById(R.id.resendButton);
        mResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mReportRequestDetailListener != null)
                    mReportRequestDetailListener.onReportRequestDetailResend(mReportRequestModel);
            }
        });

        ButtonUtil.setButtonPrimary(mContext, mResendButton);
    }

    public RelativeLayout getView() {
        return mReportRequestDetailView;
    }

    public void setReportRequestModel(final ReportRequestModel reportRequestModel) {
        mReportRequestModel = reportRequestModel;

        mRequestDate.setText(DateTimeUtil.ToDateTimeDisplayString(mReportRequestModel.getRequestDate()));
        mRequestStatus.setText(mReportRequestModel.getRequestStatus());
        mProjectName.setText(mReportRequestModel.getProjectName());
        mComment.setText(mReportRequestModel.getComment());
        mLastReportSentToEmailDate.setText(DateTimeUtil.ToDateTimeString(mReportRequestModel.getLastReportSentToEmailDate()));
    }

    public void setReportRequestDetailListener(final ReportRequestDetailListener reportRequestDetailListener) {
        mReportRequestDetailListener = reportRequestDetailListener;
    }

    public interface ReportRequestDetailListener {
        void onReportRequestDetailResend(ReportRequestModel reportRequestModel);
    }
}
