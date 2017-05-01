package com.construction.pm.views.file;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.utils.ViewUtil;

import java.io.File;

public class FilePhotoItemView implements IFileDownloadItemView {
    protected Context mContext;

    protected RelativeLayout mFilePhotoItemView;

    protected AppCompatImageView mPhoto;

    protected RelativeLayout mDownloadProgressContainer;
    protected ProgressBar mDownloadProgressBar;
    protected AppCompatTextView mDownloadProgressBarText;

    protected FilePhotoItemViewClickListener mFilePhotoItemViewClickListener;

    public FilePhotoItemView(final Context context) {
        mContext = context;
    }

    public FilePhotoItemView(final Context context, final RelativeLayout filePhotoItemView) {
        this(context);

        initializeView(filePhotoItemView);
    }

    public static FilePhotoItemView buildFilePhotoItemView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new FilePhotoItemView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static FilePhotoItemView buildFilePhotoItemView(final Context context, final ViewGroup viewGroup) {
        return buildFilePhotoItemView(context, R.layout.file_photo_item_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout filePhotoItemView) {
        mFilePhotoItemView = filePhotoItemView;

        mPhoto = (AppCompatImageView) mFilePhotoItemView.findViewById(R.id.photo);

        mDownloadProgressContainer = (RelativeLayout) mFilePhotoItemView.findViewById(R.id.downloadProgressContainer);
        mDownloadProgressBar = (ProgressBar) mDownloadProgressContainer.findViewById(R.id.downloadProgressBar);
        mDownloadProgressBarText = (AppCompatTextView) mDownloadProgressContainer.findViewById(R.id.downloadProgressBarText);
    }

    public void setFilePhotoDefault() {
        mPhoto.setImageResource(R.drawable.ic_image_dark_24);
    }

    public void setFilePhotoThumbnail(final File file) {
        ImageUtil.setImageThumbnailView(mContext, mPhoto, file);
    }

    public void setFilePhoto(final File file) {
        ImageUtil.setImageView(mContext, mPhoto, file);
    }

    public void setFilePhotoScaleType(AppCompatImageView.ScaleType scaleType) {
        mPhoto.setScaleType(scaleType);
    }

    @Override
    public void setFileId(final Integer fileId) {
        String tag = "PHOTO_ID_" + String.valueOf(fileId);
        mFilePhotoItemView.setTag(tag);
    }

    @Override
    public String getFileType() {
        return "PHOTO";
    }

    @Override
    public void startProgress() {
        mDownloadProgressContainer.setVisibility(View.VISIBLE);
        setProgress(0, 0);
        setProgressText(ViewUtil.getResourceString(mContext, R.string.file_photo_item_view_start));
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
        setProgressText(ViewUtil.getResourceString(mContext, R.string.file_photo_item_view_finish));
        mDownloadProgressContainer.setVisibility(View.INVISIBLE);
    }

    public RelativeLayout getView() {
        return mFilePhotoItemView;
    }

    public void setFilePhotoItemViewClickListener(final FilePhotoItemViewClickListener filePhotoItemViewClickListener) {
        mFilePhotoItemViewClickListener = filePhotoItemViewClickListener;

        if (mFilePhotoItemViewClickListener != null) {
            mFilePhotoItemView.setClickable(true);
            mFilePhotoItemView.setFocusable(true);
            mFilePhotoItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilePhotoItemViewClickListener.onFilePhotoItemClick();
                }
            });
        } else {
            mFilePhotoItemView.setClickable(false);
            mFilePhotoItemView.setFocusable(false);
            mFilePhotoItemView.setOnClickListener(null);
        }
    }

    public interface FilePhotoItemViewClickListener {
        void onFilePhotoItemClick();
    }
}