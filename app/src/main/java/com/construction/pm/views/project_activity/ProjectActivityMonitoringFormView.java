package com.construction.pm.views.project_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.DatePickerView;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.adapter.SpinnerActivityStatusAdapter;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringFormView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityMonitoringFormView;

    protected DatePickerView mActualStartDate;
    protected DatePickerView mActualEndDate;
    protected AppCompatSpinner mActivityStatus;
    protected SpinnerActivityStatusAdapter mSpinnerActivityStatusAdapter;
    protected AppCompatTextView mPercentCompleteLabel;
    protected SeekBar mPercentComplete;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected TabLayout mTabLayout;
    protected AppCompatEditText mComment;

    protected ProjectActivityMonitoringFormListener mProjectActivityMonitoringFormListener;
    protected ImageRequestDuplicateListener mImageRequestDuplicateListener;

    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;

    public ProjectActivityMonitoringFormView(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringFormView(final Context context, final RelativeLayout projectActivityMonitoringFormView) {
        this(context);

        initializeView(projectActivityMonitoringFormView);
    }

    public static ProjectActivityMonitoringFormView buildProjectActivityMonitoringFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringFormView buildProjectActivityMonitoringFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringFormView(context, R.layout.project_activity_monitoring_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectActivityMonitoringFormView = projectActivityDetailView;

        mViewPager = (ViewPager) mProjectActivityMonitoringFormView.findViewById(R.id.photoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectActivityMonitoringFormView.findViewById(R.id.photoTab);

        mViewPagerAdapter = new ViewPagerAdapter(mContext);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        for (int photoIdx = 0; photoIdx < 6; photoIdx++) {
            int photoSize = ViewUtil.getPxFromDp(mContext, 48);

            AppCompatImageView photoImage = new AppCompatImageView(mContext);
            photoImage.setImageResource(R.drawable.ic_image_dark_24);
            photoImage.setLayoutParams(new ViewGroup.LayoutParams(photoSize, photoSize));

            if (mTabLayout != null) {
                TabLayout.Tab tab = mTabLayout.getTabAt(photoIdx);
                if (tab != null)
                    tab.setCustomView(photoImage);
            }
        }

        mActualStartDate = (DatePickerView) mProjectActivityMonitoringFormView.findViewById(R.id.actualStartDate);
        mActualEndDate = (DatePickerView) mProjectActivityMonitoringFormView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatSpinner) mProjectActivityMonitoringFormView.findViewById(R.id.activityStatus);
        mPercentCompleteLabel = (AppCompatTextView) mProjectActivityMonitoringFormView.findViewById(R.id.percentCompleteLabel);
        mPercentComplete = (SeekBar) mProjectActivityMonitoringFormView.findViewById(R.id.percentComplete);
        mComment = (AppCompatEditText) mProjectActivityMonitoringFormView.findViewById(R.id.comment);
        mSpinnerActivityStatusAdapter = new SpinnerActivityStatusAdapter(mContext);
        mActivityStatus.setAdapter(mSpinnerActivityStatusAdapter);

        FloatingActionButton takeCamera = (FloatingActionButton) mProjectActivityMonitoringFormView.findViewById(R.id.takeCamera);
        takeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjectActivityMonitoringFormListener != null)
                    mProjectActivityMonitoringFormListener.onRequestCamera();
            }
        });

        FloatingActionButton takeGallery = (FloatingActionButton) mProjectActivityMonitoringFormView.findViewById(R.id.takeGallery);
        takeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjectActivityMonitoringFormListener != null)
                    mProjectActivityMonitoringFormListener.onRequestGallery();
            }
        });

        updatePercentCompleteLabel();
        mPercentComplete.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updatePercentCompleteLabel();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public RelativeLayout getView() {
        return mProjectActivityMonitoringFormView;
    }

    public void setProjectActivityModel(final ProjectActivityModel projectActivityModel) {
        if (projectActivityModel == null)
            return;

        mProjectActivityMonitoringModel = new ProjectActivityMonitoringModel();
        mProjectActivityMonitoringModel.setProjectActivityId(projectActivityModel.getProjectActivityId());
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mProjectActivityMonitoringModel = projectActivityMonitoringModel.duplicate();

        mActualStartDate.setDate(projectActivityMonitoringModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityMonitoringModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityMonitoringModel.getActivityStatus())));
        if (projectActivityMonitoringModel.getPercentComplete() != null) {
            Double percentComplete = projectActivityMonitoringModel.getPercentComplete() * 10;
            mPercentComplete.setProgress(percentComplete.intValue());
        }
        if (mProjectActivityMonitoringModel.getPhotoId() != null)
            requestImagePosition(0, mProjectActivityMonitoringModel.getPhotoId());
        if (mProjectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            requestImagePosition(1, mProjectActivityMonitoringModel.getPhotoAdditional1Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            requestImagePosition(2, mProjectActivityMonitoringModel.getPhotoAdditional2Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            requestImagePosition(3, mProjectActivityMonitoringModel.getPhotoAdditional3Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            requestImagePosition(4, mProjectActivityMonitoringModel.getPhotoAdditional4Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            requestImagePosition(5, mProjectActivityMonitoringModel.getPhotoAdditional5Id());
        mComment.setText(projectActivityMonitoringModel.getComment());

        updatePercentCompleteLabel();
    }

    public ProjectActivityMonitoringModel getProjectActivityMonitoringModel() {
        if (mProjectActivityMonitoringModel == null)
            mProjectActivityMonitoringModel = new ProjectActivityMonitoringModel();

        mProjectActivityMonitoringModel.setActualStartDate(mActualStartDate.getCalendar());
        mProjectActivityMonitoringModel.setActualEndDate(mActualEndDate.getCalendar());
        if (mActivityStatus.getSelectedItem() != null)
            mProjectActivityMonitoringModel.setActivityStatus(((ActivityStatusEnum) mActivityStatus.getSelectedItem()).getValue());
        else
            mProjectActivityMonitoringModel.setActivityStatus(null);
        mProjectActivityMonitoringModel.setPercentComplete((double) mPercentComplete.getProgress() / (double) 10);
        mProjectActivityMonitoringModel.setComment(mComment.getText().toString());

        return mProjectActivityMonitoringModel;
    }

    protected void requestImagePosition(final int position, final Integer fileId) {
        if (mImageRequestDuplicateListener != null) {
            AppCompatImageView contentImageView = mViewPagerAdapter.getItem(position);

            AppCompatImageView tabImageView = null;
            TabLayout.Tab tab = mTabLayout.getTabAt(position);
            if (tab != null)
                tabImageView = (AppCompatImageView) tab.getCustomView();

            mImageRequestDuplicateListener.onImageRequestDuplicate(contentImageView, tabImageView, fileId);
        }
    }

    protected void updatePercentCompleteLabel() {
        double percentComplete = Double.valueOf(String.valueOf((double) mPercentComplete.getProgress() / (double) 10));
        mPercentCompleteLabel.setText(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_percent_complete, StringUtil.numberPercentFormat(percentComplete)));
    }

    public void setPhotoId(final File file) {
        int position = mViewPager.getCurrentItem();

        ImageUtil.setImageThumbnailView(mContext, mViewPagerAdapter.getItem(position), file.getAbsolutePath());

        mViewPagerAdapter.setItemFile(position, file);
    }

    public WebApiParam.WebApiParamFile getPhoto(final int position) {
        File file = mViewPagerAdapter.getItemFile(position);
        if (file == null)
            return null;

        WebApiParam.WebApiParamFile webApiParamFile = new WebApiParam.WebApiParamFile();
        webApiParamFile.setMimeType("image/jpeg");
        webApiParamFile.setFileName(file.getName());
        webApiParamFile.setFileData(ImageUtil.getImageData(file, Bitmap.CompressFormat.JPEG, 30));
        return webApiParamFile;
    }

    public void setImageRequestDuplicateListener(final ImageRequestDuplicateListener imageRequestDuplicateListener) {
        mImageRequestDuplicateListener = imageRequestDuplicateListener;
    }

    public void setProjectActivityMonitoringFormListener(final ProjectActivityMonitoringFormListener projectActivityMonitoringFormListener) {
        mProjectActivityMonitoringFormListener = projectActivityMonitoringFormListener;
    }

    public interface ProjectActivityMonitoringFormListener {
        void onRequestCamera();
        void onRequestGallery();
    }

    public class ViewPagerAdapter extends PagerAdapter {
        protected Context mContext;
        protected List<AppCompatImageView> mImageViewList;
        protected List<File> mFileList;

        public ViewPagerAdapter(final Context context) {
            super();
            mContext = context;

            mImageViewList = new ArrayList<AppCompatImageView>();
            mImageViewList.add(generateImageView());
            mImageViewList.add(generateImageView());
            mImageViewList.add(generateImageView());
            mImageViewList.add(generateImageView());
            mImageViewList.add(generateImageView());
            mImageViewList.add(generateImageView());

            mFileList = new ArrayList<File>();
            for (int imageIdx = 0; imageIdx < mImageViewList.size(); imageIdx++) {
                mFileList.add(null);
            }
        }

        protected AppCompatImageView generateImageView() {
            AppCompatImageView imageView = new AppCompatImageView(mContext);
            imageView.setScaleType(AppCompatImageView.ScaleType.CENTER);
            imageView.setImageResource(R.drawable.ic_gallery_dark_24);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        public void setItemFile(final int position, final File file) {
            if ((position + 1) > mImageViewList.size())
                return;

            mFileList.set(position, file);
        }

        public File getItemFile(final int position) {
            if ((position + 1) > mFileList.size())
                return null;

            return mFileList.get(position);
        }

        public AppCompatImageView getItem(final int position) {
            if ((position + 1) > mImageViewList.size())
                return null;

            return mImageViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            collection.addView(mImageViewList.get(position));

            return mImageViewList.get(position);
        }

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView(mImageViewList.get(position));
        }
    }
}
