package com.construction.pm.views.project_activity;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.DatePickerView;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.FileUtil;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.adapter.SpinnerActivityStatusAdapter;
import com.construction.pm.views.file.FilePhotoItemView;
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

    protected int mPercentCompleteBeforeActivityStatusComplete;

    protected ProjectActivityMonitoringFormListener mProjectActivityMonitoringFormListener;
    protected ImageRequestListener mImageRequestListener;

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

        int photoSize = ViewUtil.getPxFromDp(mContext, 48);

        mTabLayout.setupWithViewPager(mViewPager);
        for (int photoIdx = 0; photoIdx < 6; photoIdx++) {
            if (mTabLayout != null) {
                TabLayout.Tab tab = mTabLayout.getTabAt(photoIdx);
                if (tab != null) {
                    FilePhotoItemView filePhotoItemView = FilePhotoItemView.buildFilePhotoItemView(mContext, null);

                    RelativeLayout filePhotoItemViewLayout = filePhotoItemView.getView();
                    filePhotoItemViewLayout.setLayoutParams(new ViewGroup.LayoutParams(photoSize, photoSize));

                    tab.setTag(filePhotoItemView);
                    tab.setCustomView(filePhotoItemViewLayout);
                }
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

        mActivityStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityStatusEnum activityStatusEnum = mSpinnerActivityStatusAdapter.getItem(i);
                if (activityStatusEnum != null) {
                    if (activityStatusEnum == ActivityStatusEnum.COMPLETED) {
                        mPercentCompleteBeforeActivityStatusComplete = mPercentComplete.getProgress();
                        mPercentComplete.setProgress(mPercentComplete.getMax());
                    } else {
                        mPercentComplete.setProgress(mPercentCompleteBeforeActivityStatusComplete);
                    }
                } else {
                    mPercentComplete.setProgress(mPercentCompleteBeforeActivityStatusComplete);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        mActualStartDate.setDate(projectActivityModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityModel.getActivityStatus())));
        if (projectActivityModel.getPercentComplete() != null) {
            Double percentComplete = projectActivityModel.getPercentComplete() * 10;
            mPercentComplete.setProgress(percentComplete.intValue());
        }

        mPercentCompleteBeforeActivityStatusComplete = mPercentComplete.getProgress();

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
            setPhotoPosition(0, mProjectActivityMonitoringModel.getPhotoId());
        if (mProjectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            setPhotoPosition(1, mProjectActivityMonitoringModel.getPhotoAdditional1Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            setPhotoPosition(2, mProjectActivityMonitoringModel.getPhotoAdditional2Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            setPhotoPosition(3, mProjectActivityMonitoringModel.getPhotoAdditional3Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            setPhotoPosition(4, mProjectActivityMonitoringModel.getPhotoAdditional4Id());
        if (mProjectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            setPhotoPosition(5, mProjectActivityMonitoringModel.getPhotoAdditional5Id());
        mComment.setText(projectActivityMonitoringModel.getComment());

        mPercentCompleteBeforeActivityStatusComplete = mPercentComplete.getProgress();

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

    protected void setPhotoPosition(final int position, final Integer fileId) {
        mViewPagerAdapter.setItemFileId(position, fileId);

        if (mImageRequestListener != null) {
            FilePhotoItemView filePhotoItemView = mViewPagerAdapter.getItem(position);
            mImageRequestListener.onImageRequest(filePhotoItemView, fileId);
        }
    }

    public FilePhotoItemView getFilePhotoItemView(final Integer fileId) {
        int position = mViewPagerAdapter.getPositionByFileId(fileId);
        if (position >= 0)
            return mViewPagerAdapter.getItem(position);
        return null;
    }

    public FilePhotoItemView getFilePhotoItemTabView(final Integer fileId) {
        int position = mViewPagerAdapter.getPositionByFileId(fileId);
        if (position >= 0) {
            TabLayout.Tab tab = mTabLayout.getTabAt(position);
            if (tab != null)
                return (FilePhotoItemView) tab.getTag();
        }
        return null;
    }

    protected void updatePercentCompleteLabel() {
        double percentComplete = Double.valueOf(String.valueOf((double) mPercentComplete.getProgress() / (double) 10));
        mPercentCompleteLabel.setText(ViewUtil.getResourceString(mContext, R.string.project_activity_monitoring_percent_complete, StringUtil.numberPercentFormat(percentComplete)));
    }

    public void setPhotoId(final File file) {
        int position = mViewPager.getCurrentItem();

        mViewPagerAdapter.getItem(position).setFilePhotoThumbnail(file);

        TabLayout.Tab tab = mTabLayout.getTabAt(position);
        if (tab != null) {
            FilePhotoItemView tabFilePhotoItemView = (FilePhotoItemView) tab.getTag();
            if (tabFilePhotoItemView != null)
                tabFilePhotoItemView.setFilePhotoThumbnail(file);
        }

        String fileName = file.getName();
        String fileCacheName = "MONITORING_PICTURE_" + String.valueOf(position) + "." + fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        File fileNew = ImageUtil.copyImageFileToCache(mContext, file, fileCacheName, 500, 500);
        if (fileNew == null)
            fileNew = file;

        mViewPagerAdapter.setItemFile(position, fileNew);
    }

    public WebApiParam.WebApiParamFile getPhoto(final int position) {
        File file = mViewPagerAdapter.getItemFile(position);
        if (file == null)
            return null;
        if (!file.exists())
            return null;

        WebApiParam.WebApiParamFile webApiParamFile = new WebApiParam.WebApiParamFile();
        webApiParamFile.setMimeType("image/jpeg");
        webApiParamFile.setFileName(file.getName());
        webApiParamFile.setFileData(FileUtil.toByteArray(file));
        return webApiParamFile;
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
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
        protected List<Integer> mFileIdList;
        protected List<FilePhotoItemView> mFilePhotoItemViewList;
        protected List<File> mFileList;

        public ViewPagerAdapter(final Context context) {
            super();
            mContext = context;

            mFilePhotoItemViewList = new ArrayList<FilePhotoItemView>();
            mFilePhotoItemViewList.add(generateFilePhotoItemView());
            mFilePhotoItemViewList.add(generateFilePhotoItemView());
            mFilePhotoItemViewList.add(generateFilePhotoItemView());
            mFilePhotoItemViewList.add(generateFilePhotoItemView());
            mFilePhotoItemViewList.add(generateFilePhotoItemView());
            mFilePhotoItemViewList.add(generateFilePhotoItemView());

            mFileIdList = new ArrayList<Integer>();
            mFileList = new ArrayList<File>();
            for (int imageIdx = 0; imageIdx < mFilePhotoItemViewList.size(); imageIdx++) {
                mFileIdList.add(null);
                mFileList.add(null);
            }
        }

        protected FilePhotoItemView generateFilePhotoItemView() {
            FilePhotoItemView filePhotoItemView = FilePhotoItemView.buildFilePhotoItemView(mContext, null);
            filePhotoItemView.setFilePhotoScaleType(AppCompatImageView.ScaleType.CENTER);

            RelativeLayout filePhotoItemViewLayout = filePhotoItemView.getView();
            filePhotoItemViewLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return filePhotoItemView;
        }

        public void setItemFile(final int position, final File file) {
            if ((position + 1) > mFileList.size())
                return;

            mFileList.set(position, file);
        }

        public File getItemFile(final int position) {
            if ((position + 1) > mFileList.size())
                return null;

            return mFileList.get(position);
        }

        public void setItemFileId(final int position, final Integer fileId) {
            if ((position + 1) > mFileIdList.size())
                return;

            mFileIdList.set(position, fileId);
        }

        public Integer getItemFileId(final int position) {
            if ((position + 1) > mFileIdList.size())
                return null;

            return mFileIdList.get(position);
        }

        public FilePhotoItemView getItem(final int position) {
            if ((position + 1) > mFilePhotoItemViewList.size())
                return null;

            return mFilePhotoItemViewList.get(position);
        }

        public int getPositionByFileId(final Integer fileId) {
            for (int position = 0; position < mFileIdList.size(); position++) {
                Integer existFileId = mFileIdList.get(position);
                if (existFileId != null) {
                    if (existFileId.equals(fileId)) {
                        return position;
                    }
                }
            }
            return -1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            collection.addView(mFilePhotoItemViewList.get(position).getView());

            return mFilePhotoItemViewList.get(position).getView();
        }

        @Override
        public int getCount() {
            return mFilePhotoItemViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView(mFilePhotoItemViewList.get(position).getView());
        }
    }
}
