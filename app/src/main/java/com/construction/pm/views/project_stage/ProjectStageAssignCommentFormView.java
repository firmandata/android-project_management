package com.construction.pm.views.project_stage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.networks.webapi.WebApiParam;
import com.construction.pm.utils.ImageUtil;
import com.construction.pm.views.listeners.ImageRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentFormView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentFormView;

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected AppCompatEditText mComment;

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

        mComment = (AppCompatEditText) mProjectStageAssignCommentFormView.findViewById(R.id.comment);
        mViewPager = (ViewPager) mProjectStageAssignCommentFormView.findViewById(R.id.photoPager);

        mViewPagerAdapter = new ViewPagerAdapter(mContext);
        mViewPager.setAdapter(mViewPagerAdapter);

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
        if (mImageRequestListener != null) {
            if (mProjectStageAssignCommentModel.getPhotoId() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(0), mProjectStageAssignCommentModel.getPhotoId());
            if (mProjectStageAssignCommentModel.getPhotoAdditional1Id() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(1), mProjectStageAssignCommentModel.getPhotoAdditional1Id());
            if (mProjectStageAssignCommentModel.getPhotoAdditional2Id() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(2), mProjectStageAssignCommentModel.getPhotoAdditional2Id());
            if (mProjectStageAssignCommentModel.getPhotoAdditional3Id() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(3), mProjectStageAssignCommentModel.getPhotoAdditional3Id());
            if (mProjectStageAssignCommentModel.getPhotoAdditional4Id() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(4), mProjectStageAssignCommentModel.getPhotoAdditional4Id());
            if (mProjectStageAssignCommentModel.getPhotoAdditional5Id() != null)
                mImageRequestListener.onImageRequest(mViewPagerAdapter.getItem(5), mProjectStageAssignCommentModel.getPhotoAdditional5Id());
        }

        mComment.setText(projectStageAssignCommentModel.getComment());
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
