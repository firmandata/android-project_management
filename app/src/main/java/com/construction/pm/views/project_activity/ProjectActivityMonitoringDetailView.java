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
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.file.FilePhotoListView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestDuplicateListener;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectActivityMonitoringDetailView;

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected TabLayout mTabLayout;

    protected AppCompatTextView mMonitoringDate;
    protected AppCompatTextView mActualStartDate;
    protected AppCompatTextView mActualEndDate;
    protected AppCompatTextView mActivityStatus;
    protected AppCompatTextView mPercentComplete;
    protected AppCompatTextView mComment;

    protected ImageRequestDuplicateListener mImageRequestDuplicateListener;
    protected ImageRequestClickListener mImageRequestClickListener;

    public ProjectActivityMonitoringDetailView(final Context context) {
        mContext = context;
    }

    public ProjectActivityMonitoringDetailView(final Context context, final RelativeLayout projectActivityMonitoringDetailView) {
        this(context);

        initializeView(projectActivityMonitoringDetailView);
    }

    public static ProjectActivityMonitoringDetailView buildProjectActivityMonitoringDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectActivityMonitoringDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectActivityMonitoringDetailView buildProjectActivityMonitoringDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectActivityMonitoringDetailView(context, R.layout.project_activity_monitoring_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityMonitoringDetailView) {
        mProjectActivityMonitoringDetailView = projectActivityMonitoringDetailView;

        mViewPager = (ViewPager) mProjectActivityMonitoringDetailView.findViewById(R.id.photoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectActivityMonitoringDetailView.findViewById(R.id.photoTab);

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

        mMonitoringDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.monitoringDate);
        mActualStartDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.actualStartDate);
        mActualEndDate = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.actualEndDate);
        mActivityStatus = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.activityStatus);
        mPercentComplete = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.percentComplete);
        mComment = (AppCompatTextView) mProjectActivityMonitoringDetailView.findViewById(R.id.comment);
    }

    public RelativeLayout getView() {
        return mProjectActivityMonitoringDetailView;
    }

    public void setImageRequestDuplicateListener(final ImageRequestDuplicateListener imageRequestDuplicateListener) {
        mImageRequestDuplicateListener = imageRequestDuplicateListener;
    }

    public void setImageRequestClickListener(final ImageRequestClickListener imageRequestClickListener) {
        mImageRequestClickListener = imageRequestClickListener;
    }

    public void setProjectActivityMonitoringModel(final ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        if (projectActivityMonitoringModel == null)
            return;

        mMonitoringDate.setText(DateTimeUtil.ToDateTimeDisplayString(projectActivityMonitoringModel.getMonitoringDate()));
        mActualStartDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualStartDate()));
        mActualEndDate.setText(DateTimeUtil.ToDateDisplayString(projectActivityMonitoringModel.getActualEndDate()));
        mActivityStatus.setText(projectActivityMonitoringModel.getActivityStatus());
        mPercentComplete.setText(StringUtil.numberPercentFormat(projectActivityMonitoringModel.getPercentComplete()));
        mComment.setText(projectActivityMonitoringModel.getComment());

        if (projectActivityMonitoringModel.getPhotoId() != null)
            requestImagePosition(0, projectActivityMonitoringModel.getPhotoId());
        if (projectActivityMonitoringModel.getPhotoAdditional1Id() != null)
            requestImagePosition(1, projectActivityMonitoringModel.getPhotoAdditional1Id());
        if (projectActivityMonitoringModel.getPhotoAdditional2Id() != null)
            requestImagePosition(2, projectActivityMonitoringModel.getPhotoAdditional2Id());
        if (projectActivityMonitoringModel.getPhotoAdditional3Id() != null)
            requestImagePosition(3, projectActivityMonitoringModel.getPhotoAdditional3Id());
        if (projectActivityMonitoringModel.getPhotoAdditional4Id() != null)
            requestImagePosition(4, projectActivityMonitoringModel.getPhotoAdditional4Id());
        if (projectActivityMonitoringModel.getPhotoAdditional5Id() != null)
            requestImagePosition(5, projectActivityMonitoringModel.getPhotoAdditional5Id());
    }

    protected void requestImagePosition(final int position, final Integer fileId) {
        if (mImageRequestDuplicateListener != null) {
            AppCompatImageView contentImageView = mViewPagerAdapter.getItem(position);
            if (mImageRequestClickListener != null) {
                contentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mImageRequestClickListener.onImageRequestClick(fileId);
                    }
                });
            }
            AppCompatImageView tabImageView = null;
            TabLayout.Tab tab = mTabLayout.getTabAt(position);
            if (tab != null)
                tabImageView = (AppCompatImageView) tab.getCustomView();

            mImageRequestDuplicateListener.onImageRequestDuplicate(contentImageView, tabImageView, fileId);
        }
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
