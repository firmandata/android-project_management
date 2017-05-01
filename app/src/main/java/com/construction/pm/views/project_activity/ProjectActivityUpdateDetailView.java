package com.construction.pm.views.project_activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityUpdateDetailView;

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected TabLayout mTabLayout;

    protected ImageRequestListener mImageRequestListener;
    protected ImageRequestClickListener mImageRequestClickListener;

    protected AppCompatTextView mMonitoringMonitoringDate;
    protected AppCompatTextView mMonitoringActualStartDate;
    protected AppCompatTextView mMonitoringActualEndDate;
    protected AppCompatTextView mMonitoringActivityStatus;
    protected AppCompatTextView mMonitoringPercentComplete;
    protected AppCompatTextView mMonitoringComment;

    protected AppCompatTextView mUpdateUpdateDate;
    protected AppCompatTextView mUpdateActualStartDate;
    protected AppCompatTextView mUpdateActualEndDate;
    protected AppCompatTextView mUpdateActivityStatus;
    protected AppCompatTextView mUpdatePercentComplete;
    protected AppCompatTextView mUpdateComment;

    protected ProjectActivityUpdateDetailListener mProjectActivityUpdateDetailListener;

    public ProjectActivityUpdateDetailView(final Context context) {
        mContext = context;
    }

    public ProjectActivityUpdateDetailView(final Context context, final RelativeLayout projectActivityUpdateDetailView) {
        this(context);

        initializeView(projectActivityUpdateDetailView);
    }

    public static ProjectActivityUpdateDetailView buildProjectActivityUpdateDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityUpdateDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityUpdateDetailView buildProjectActivityUpdateDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityUpdateDetailView(context, R.layout.project_activity_update_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityUpdateDetailView) {
        mProjectActivityUpdateDetailView = projectActivityUpdateDetailView;

        mViewPager = (ViewPager) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringPhotoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringPhotoTab);

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

        mMonitoringMonitoringDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringMonitoringDate);
        mMonitoringActualStartDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringActualStartDate);
        mMonitoringActualEndDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringActualEndDate);
        mMonitoringActivityStatus = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringActivityStatus);
        mMonitoringPercentComplete = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringPercentComplete);
        mMonitoringComment = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.monitoringComment);

        mUpdateUpdateDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateUpdateDate);
        mUpdateActualStartDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateActualStartDate);
        mUpdateActualEndDate = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateActualEndDate);
        mUpdateActivityStatus = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateActivityStatus);
        mUpdatePercentComplete = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updatePercentComplete);
        mUpdateComment = (AppCompatTextView) mProjectActivityUpdateDetailView.findViewById(R.id.updateComment);
    }

    public RelativeLayout getView() {
        return mProjectActivityUpdateDetailView;
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
    }

    public void setImageRequestClickListener(final ImageRequestClickListener imageRequestClickListener) {
        mImageRequestClickListener = imageRequestClickListener;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mMonitoringMonitoringDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate()));
        mMonitoringActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualStartDate()));
        mMonitoringActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualEndDate()));
        mMonitoringActivityStatus.setText(projectActivityMonitoringModel.getActivityStatus());
        mMonitoringPercentComplete.setText(StringUtil.numberPercentFormat(projectActivityMonitoringModel.getPercentComplete()));
        mMonitoringComment.setText(projectActivityMonitoringModel.getComment());

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

        mUpdateUpdateDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityUpdateModel.getUpdateDate()));
        mUpdateActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualStartDate()));
        mUpdateActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityUpdateModel.getActualEndDate()));
        mUpdateActivityStatus.setText(projectActivityUpdateModel.getActivityStatus());
        mUpdatePercentComplete.setText(StringUtil.numberPercentFormat(projectActivityUpdateModel.getPercentComplete()));
        mUpdateComment.setText(projectActivityUpdateModel.getComment());

        if (mProjectActivityUpdateDetailListener != null)
            mProjectActivityUpdateDetailListener.onProjectActivityMonitoringDetailRequest(projectActivityUpdateModel.getProjectActivityMonitoringId(), projectActivityUpdateModel.getProjectActivityId());
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

    public void setProjectActivityUpdateDetailListener(final ProjectActivityUpdateDetailListener projectActivityUpdateDetailListener) {
        mProjectActivityUpdateDetailListener = projectActivityUpdateDetailListener;
    }

    public interface ProjectActivityUpdateDetailListener {
        void onProjectActivityMonitoringDetailRequest(Integer projectActivityMonitoringId, Integer projectActivityId);
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
