package com.construction.pm.views.project_stage;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.FileUtil;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.views.file.FilePhotoItemView;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentFormView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentFormView;

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected TabLayout mTabLayout;
    protected TextInputEditText mComment;

    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;

    protected ProjectStageAssignCommentFormListener mProjectStageAssignCommentFormListener;
    protected ImageRequestListener mImageRequestListener;

    public ProjectStageAssignCommentFormView(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentFormView(final Context context, final RelativeLayout projectStageAssignCommentFormView) {
        this(context);

        initializeView(projectStageAssignCommentFormView);
    }

    public static ProjectStageAssignCommentFormView buildProjectStageAssignCommentFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentFormView buildProjectStageAssignCommentFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentFormView(context, R.layout.project_stage_assign_comment_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectStageAssignCommentFormView = projectActivityDetailView;

        mComment = (TextInputEditText) mProjectStageAssignCommentFormView.findViewById(R.id.comment);
        mViewPager = (ViewPager) mProjectStageAssignCommentFormView.findViewById(R.id.photoPager);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout = (TabLayout) mProjectStageAssignCommentFormView.findViewById(R.id.photoTab);

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

        FloatingActionButton takeCamera = (FloatingActionButton) mProjectStageAssignCommentFormView.findViewById(R.id.takeCamera);
        takeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjectStageAssignCommentFormListener != null)
                    mProjectStageAssignCommentFormListener.onRequestCamera();
            }
        });

        FloatingActionButton takeGallery = (FloatingActionButton) mProjectStageAssignCommentFormView.findViewById(R.id.takeGallery);
        takeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjectStageAssignCommentFormListener != null)
                    mProjectStageAssignCommentFormListener.onRequestGallery();
            }
        });
    }

    public RelativeLayout getView() {
        return mProjectStageAssignCommentFormView;
    }

    public void setProjectStageAssignmentModel(final ProjectStageAssignmentModel projectStageAssignmentModel) {
        if (projectStageAssignmentModel == null)
            return;

        mProjectStageAssignCommentModel = new ProjectStageAssignCommentModel();
        mProjectStageAssignCommentModel.setProjectStageAssignmentId(projectStageAssignmentModel.getProjectStageAssignmentId());
    }

    public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        if (projectStageAssignCommentModel == null)
            return;

        mProjectStageAssignCommentModel = projectStageAssignCommentModel.duplicate();
        if (mProjectStageAssignCommentModel.getPhotoId() != null)
            setPhotoPosition(0, mProjectStageAssignCommentModel.getPhotoId());
        if (mProjectStageAssignCommentModel.getPhotoAdditional1Id() != null)
            setPhotoPosition(1, mProjectStageAssignCommentModel.getPhotoAdditional1Id());
        if (mProjectStageAssignCommentModel.getPhotoAdditional2Id() != null)
            setPhotoPosition(2, mProjectStageAssignCommentModel.getPhotoAdditional2Id());
        if (mProjectStageAssignCommentModel.getPhotoAdditional3Id() != null)
            setPhotoPosition(3, mProjectStageAssignCommentModel.getPhotoAdditional3Id());
        if (mProjectStageAssignCommentModel.getPhotoAdditional4Id() != null)
            setPhotoPosition(4, mProjectStageAssignCommentModel.getPhotoAdditional4Id());
        if (mProjectStageAssignCommentModel.getPhotoAdditional5Id() != null)
            setPhotoPosition(5, mProjectStageAssignCommentModel.getPhotoAdditional5Id());

        mComment.setText(projectStageAssignCommentModel.getComment());
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
        String fileCacheName = "COMMENT_PICTURE_" + String.valueOf(position) + "." + fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
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

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        if (mProjectStageAssignCommentModel == null)
            mProjectStageAssignCommentModel = new ProjectStageAssignCommentModel();

        mProjectStageAssignCommentModel.setComment(mComment.getText().toString());

        return mProjectStageAssignCommentModel;
    }

    public void setProjectStageAssignCommentFormListener(final ProjectStageAssignCommentFormListener projectStageAssignCommentFormListener) {
        mProjectStageAssignCommentFormListener = projectStageAssignCommentFormListener;
    }

    public interface ProjectStageAssignCommentFormListener {
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
