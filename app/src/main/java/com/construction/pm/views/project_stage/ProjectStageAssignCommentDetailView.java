package com.construction.pm.views.project_stage;

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
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestClickListener;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentDetailView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentDetailView;

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected TabLayout mTabLayout;
    protected AppCompatTextView mComment;

    protected ImageRequestListener mImageRequestListener;
    protected ImageRequestClickListener mImageRequestClickListener;

    public ProjectStageAssignCommentDetailView(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentDetailView(final Context context, final RelativeLayout projectStageAssignCommentDetailView) {
        this(context);

        initializeView(projectStageAssignCommentDetailView);
    }

    public static ProjectStageAssignCommentDetailView buildProjectStageAssignCommentDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentDetailView buildProjectStageAssignCommentDetailView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentDetailView(context, R.layout.project_stage_assign_comment_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectStageAssignCommentDetailView) {
        mProjectStageAssignCommentDetailView = projectStageAssignCommentDetailView;

        mComment = (AppCompatTextView) mProjectStageAssignCommentDetailView.findViewById(R.id.comment);
        mViewPager = (ViewPager) mProjectStageAssignCommentDetailView.findViewById(R.id.photoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectStageAssignCommentDetailView.findViewById(R.id.photoTab);

        mViewPagerAdapter = new ViewPagerAdapter(mContext);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        int photoSize = ViewUtil.getPxFromDp(mContext, 48);

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
    }

    public RelativeLayout getView() {
        return mProjectStageAssignCommentDetailView;
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mImageRequestListener = imageRequestListener;
    }

    public void setImageRequestClickListener(final ImageRequestClickListener imageRequestClickListener) {
        mImageRequestClickListener = imageRequestClickListener;
    }

    public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        if (projectStageAssignCommentModel == null)
            return;

        mComment.setText(projectStageAssignCommentModel.getComment());

        if (projectStageAssignCommentModel.getPhotoId() != null)
            setPhotoPosition(0, projectStageAssignCommentModel.getPhotoId());
        if (projectStageAssignCommentModel.getPhotoAdditional1Id() != null)
            setPhotoPosition(1, projectStageAssignCommentModel.getPhotoAdditional1Id());
        if (projectStageAssignCommentModel.getPhotoAdditional2Id() != null)
            setPhotoPosition(2, projectStageAssignCommentModel.getPhotoAdditional2Id());
        if (projectStageAssignCommentModel.getPhotoAdditional3Id() != null)
            setPhotoPosition(3, projectStageAssignCommentModel.getPhotoAdditional3Id());
        if (projectStageAssignCommentModel.getPhotoAdditional4Id() != null)
            setPhotoPosition(4, projectStageAssignCommentModel.getPhotoAdditional4Id());
        if (projectStageAssignCommentModel.getPhotoAdditional5Id() != null)
            setPhotoPosition(5, projectStageAssignCommentModel.getPhotoAdditional5Id());
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

    public class ViewPagerAdapter extends PagerAdapter {
        protected Context mContext;
        protected List<Integer> mFileIdList;
        protected List<FilePhotoItemView> mFilePhotoItemViewList;
        protected List<File> mFileList;

        public ViewPagerAdapter(final Context context) {
            super();
            mContext = context;

            mFilePhotoItemViewList = new ArrayList<>();
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
