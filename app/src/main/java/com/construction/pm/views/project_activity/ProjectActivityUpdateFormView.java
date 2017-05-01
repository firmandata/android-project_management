package com.construction.pm.views.project_activity;

import android.content.Context;
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
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.adapter.SpinnerActivityStatusAdapter;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateFormView {

    protected Context mContext;

    protected RelativeLayout mProjectActivityUpdateFormView;

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

    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;

    protected ImageRequestListener mImageRequestListener;
    protected ImageRequestClickListener mImageRequestClickListener;

    public ProjectActivityUpdateFormView(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateFormView(final Context context, final RelativeLayout projectActivityUpdateFormView) {
        this(context);

        initializeView(projectActivityUpdateFormView);
    }

    public static ProjectActivityUpdateFormView buildProjectActivityUpdateFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateFormView buildProjectActivityUpdateFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateFormView(context, R.layout.project_activity_update_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectActivityUpdateFormView = projectActivityDetailView;

        mViewPager = (ViewPager) mProjectActivityUpdateFormView.findViewById(R.id.photoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectActivityUpdateFormView.findViewById(R.id.photoTab);

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

        mActualStartDate = (DatePickerView) mProjectActivityUpdateFormView.findViewById(R.id.actualStartDate);
        mActualEndDate = (DatePickerView) mProjectActivityUpdateFormView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatSpinner) mProjectActivityUpdateFormView.findViewById(R.id.activityStatus);
        mPercentCompleteLabel = (AppCompatTextView) mProjectActivityUpdateFormView.findViewById(R.id.percentCompleteLabel);
        mPercentComplete = (SeekBar) mProjectActivityUpdateFormView.findViewById(R.id.percentComplete);
        mComment = (AppCompatEditText) mProjectActivityUpdateFormView.findViewById(R.id.comment);
        mSpinnerActivityStatusAdapter = new SpinnerActivityStatusAdapter(mContext);
        mActivityStatus.setAdapter(mSpinnerActivityStatusAdapter);

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
        return mProjectActivityUpdateFormView;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mActualStartDate.setDate(projectActivityMonitoringModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityMonitoringModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityMonitoringModel.getActivityStatus())));
        if (projectActivityMonitoringModel.getPercentComplete() != null) {
            Double percentComplete = projectActivityMonitoringModel.getPercentComplete() * 10;
            mPercentComplete.setProgress(percentComplete.intValue());
        }
        mComment.setText(projectActivityMonitoringModel.getComment());

        if (projectActivityMonitoringModel.getPhotoId() != null)
            setPhotoPosition(0, projectActivityMonitoringModel.getPhotoId());
        if (projectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            setPhotoPosition(1, projectActivityMonitoringModel.getPhotoAdditional1Id());
        if (projectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            setPhotoPosition(2, projectActivityMonitoringModel.getPhotoAdditional2Id());
        if (projectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            setPhotoPosition(3, projectActivityMonitoringModel.getPhotoAdditional3Id());
        if (projectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            setPhotoPosition(4, projectActivityMonitoringModel.getPhotoAdditional4Id());
        if (projectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            setPhotoPosition(5, projectActivityMonitoringModel.getPhotoAdditional5Id());

        updatePercentCompleteLabel();

        mPercentCompleteBeforeActivityStatusComplete = mPercentComplete.getProgress();

        mProjectActivityUpdateModel = new ProjectActivityUpdateModel();
        mProjectActivityUpdateModel.setProjectActivityMonitoringId(projectActivityMonitoringModel.getProjectActivityMonitoringId());
    }

    public void showImageFromProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        if (projectActivityMonitoringModel.getPhotoId() != null)
            setPhotoPosition(0, projectActivityMonitoringModel.getPhotoId());
        if (projectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            setPhotoPosition(1, projectActivityMonitoringModel.getPhotoAdditional1Id());
        if (projectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            setPhotoPosition(2, projectActivityMonitoringModel.getPhotoAdditional2Id());
        if (projectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            setPhotoPosition(3, projectActivityMonitoringModel.getPhotoAdditional3Id());
        if (projectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            setPhotoPosition(4, projectActivityMonitoringModel.getPhotoAdditional4Id());
        if (projectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            setPhotoPosition(5, projectActivityMonitoringModel.getPhotoAdditional5Id());
    }

    public void setProjectActivityUpdateModel(final ProjectActivityUpdateModel projectActivityUpdateModel) {
        if (projectActivityUpdateModel == null)
            return;

        mProjectActivityUpdateModel = projectActivityUpdateModel.duplicate();

        mActualStartDate.setDate(projectActivityUpdateModel.getActualStartDate());
        mActualEndDate.setDate(projectActivityUpdateModel.getActualEndDate());
        mActivityStatus.setSelection(mSpinnerActivityStatusAdapter.getPositionByItem(ActivityStatusEnum.fromString(projectActivityUpdateModel.getActivityStatus())));
        if (projectActivityUpdateModel.getPercentComplete() != null) {
            Double percentComplete = projectActivityUpdateModel.getPercentComplete() * 10;
            mPercentComplete.setProgress(percentComplete.intValue());
        }
        mComment.setText(projectActivityUpdateModel.getComment());

        mPercentCompleteBeforeActivityStatusComplete = mPercentComplete.getProgress();

        updatePercentCompleteLabel();
    }

    public ProjectActivityUpdateModel getProjectActivityUpdateModel() {
        if (mProjectActivityUpdateModel == null)
            mProjectActivityUpdateModel = new ProjectActivityUpdateModel();

        mProjectActivityUpdateModel.setActualStartDate(mActualStartDate.getCalendar());
        mProjectActivityUpdateModel.setActualEndDate(mActualEndDate.getCalendar());
        if (mActivityStatus.getSelectedItem() != null)
            mProjectActivityUpdateModel.setActivityStatus(((ActivityStatusEnum) mActivityStatus.getSelectedItem()).getValue());
        else
            mProjectActivityUpdateModel.setActivityStatus(null);
        mProjectActivityUpdateModel.setPercentComplete((double) mPercentComplete.getProgress() / (double) 10);
        mProjectActivityUpdateModel.setComment(mComment.getText().toString());

        return mProjectActivityUpdateModel;
    }

    protected void setPhotoPosition(final int position, final Integer fileId) {
        mViewPagerAdapter.setItemFileId(position, fileId);

        if (mImageRequestListener != null) {
            FilePhotoItemView filePhotoItemView = mViewPagerAdapter.getItem(position);
            if (mImageRequestClickListener != null) {
                filePhotoItemView.setFilePhotoItemViewClickListener(new FilePhotoItemView.FilePhotoItemViewClickListener() {
                    @Override
                    public void onFilePhotoItemClick() {
                        mImageRequestClickListener.onImageRequestClick(fileId);
                    }
                });
            }
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
        mPercentCompleteLabel.setText(ViewUtil.getResourceString(mContext, R.string.project_activity_update_percent_complete, StringUtil.numberPercentFormat(percentComplete)));
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
    }

    public void setImageRequestClickListener(final ImageRequestClickListener imageRequestClickListener) {
        mImageRequestClickListener = imageRequestClickListener;
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
