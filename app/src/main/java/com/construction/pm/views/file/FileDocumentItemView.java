package com.construction.pm.views.file;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.Calendar;

public class FileDocumentItemView implements IFileDownloadItemView {
    protected Context mContext;

    protected RelativeLayout mFileDocumentItemView;

    protected AppCompatImageView mDocumentIcon;
    protected AppCompatTextView mDocumentDate;
    protected AppCompatTextView mDocumentName;

    protected RelativeLayout mDownloadProgressContainer;
    protected ProgressBar mDownloadProgressBar;
    protected AppCompatTextView mDownloadProgressBarText;

    public FileDocumentItemView(final Context context) {
        mContext = context;
    }

    public FileDocumentItemView(final Context context, final RelativeLayout fileDocumentItemView) {
        this(context);

        initializeView(fileDocumentItemView);
    }

    public static FileDocumentItemView buildFileDocumentItemView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new FileDocumentItemView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static FileDocumentItemView buildFileDocumentItemView(final Context context, final ViewGroup viewGroup) {
        return buildFileDocumentItemView(context, R.layout.file_document_item_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout fileDocumentItemView) {
        mFileDocumentItemView = fileDocumentItemView;

        mDocumentIcon = (AppCompatImageView) mFileDocumentItemView.findViewById(R.id.documentIcon);
        mDocumentDate = (AppCompatTextView) mFileDocumentItemView.findViewById(R.id.documentDate);
        mDocumentName = (AppCompatTextView) mFileDocumentItemView.findViewById(R.id.documentName);

        mDownloadProgressContainer = (RelativeLayout) mFileDocumentItemView.findViewById(R.id.downloadProgressContainer);
        mDownloadProgressBar = (ProgressBar) mDownloadProgressContainer.findViewById(R.id.downloadProgressBar);
        mDownloadProgressBarText = (AppCompatTextView) mDownloadProgressContainer.findViewById(R.id.downloadProgressBarText);
    }

    public void setDocumentFileIcon(final int resourceId) {
        mDocumentIcon.setImageResource(resourceId);
    }

    public void setDocumentFileDatetime(final Calendar documentFileDatetime) {
        mDocumentDate.setText(DateTimeUtil.ToDateTimeDisplayString(documentFileDatetime));
    }

    public void setDocumentFileName(final String documentFileName) {
        mDocumentName.setText(documentFileName);
    }

    @Override
    public void setFileId(final Integer fileId) {
        String tag = "DOCUMENT_ID_" + String.valueOf(fileId);
        mFileDocumentItemView.setTag(tag);
    }

    @Override
    public String getFileType() {
        return "DOCUMENT";
    }

    @Override
    public void startProgress() {
        mDownloadProgressContainer.setVisibility(View.VISIBLE);
        setProgress(0, 0);
        setProgressText(ViewUtil.getResourceString(mContext, R.string.file_document_item_view_start));
    }

    @Override
    public void setProgress(final int progress, final int max) {
        if (mDownloadProgressContainer.getVisibility() == View.INVISIBLE)
            mDownloadProgressContainer.setVisibility(View.VISIBLE);
        mDownloadProgressBar.setProgress(progress);
        mDownloadProgressBar.setMax(max);
    }

    @Override
    public void setProgressText(final String progressText) {
        if (mDownloadProgressContainer.getVisibility() == View.INVISIBLE)
            mDownloadProgressContainer.setVisibility(View.VISIBLE);
        mDownloadProgressBarText.setText(progressText);
    }

    @Override
    public void stopProgress() {
        setProgress(0, 0);
        setProgressText(ViewUtil.getResourceString(mContext, R.string.file_document_item_view_finish));
        mDownloadProgressContainer.setVisibility(View.INVISIBLE);
    }

    public RelativeLayout getView() {
        return mFileDocumentItemView;
    }
}
