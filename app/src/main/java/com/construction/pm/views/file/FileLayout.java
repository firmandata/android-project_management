package com.construction.pm.views.file;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.FilePhotoViewFragment;
import com.construction.pm.utils.ViewUtil;

public class FileLayout {
    protected Context mContext;
    protected Handler mFragmentHandler;
    protected FragmentManager mFragmentManager;

    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_TAG_FILE_PHOTO = "FRAGMENT_FILE_PHOTO";

    protected CoordinatorLayout mFileLayout;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected Toolbar mToolbar;

    protected FileLayoutListener mFileLayoutListener;

    protected FileLayout(final Context context) {
        mContext = context;
    }

    public FileLayout(final Context context, final CoordinatorLayout fileLayout) {
        this(context);

        initializeView(fileLayout);
    }

    public static FileLayout buildFileLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new FileLayout(context, (CoordinatorLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static FileLayout buildFileLayout(final Context context, final ViewGroup viewGroup) {
        return buildFileLayout(context, R.layout.file_layout, viewGroup);
    }

    protected void initializeView(final CoordinatorLayout fileLayout) {
        mFileLayout = fileLayout;
        mAppBarLayout = (AppBarLayout) mFileLayout.findViewById(R.id.contentAppBar);
        mToolbar = (Toolbar) mFileLayout.findViewById(R.id.contentToolbar);
    }

    public CoordinatorLayout getLayout() {
        return mFileLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mFragmentHandler = new Handler();
        mFragmentManager = activity.getSupportFragmentManager();

        activity.setContentView(mFileLayout);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.file_layout_title);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(false);
        }
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentHandler = new Handler();
        mFragmentManager = fragment.getChildFragmentManager();

        mAppBarLayout.removeView(mToolbar);
    }

    public boolean isFilePhotoFragmentShow() {
        return mFragmentTagSelected.equals(FRAGMENT_TAG_FILE_PHOTO);
    }

    protected void loadFragment(final Fragment fragment, final String title, final String subtitle, final String tag) {
        if (mFragmentHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
        }

        mFragmentTagSelected = tag;

        if (mActionBar != null) {
            mActionBar.setTitle(title);
            if (subtitle != null)
                mActionBar.setSubtitle(subtitle);
        }

        mFragmentHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFragmentManager == null)
                    return;

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentBody, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

    public FilePhotoViewFragment showFilePhotoViewFragment(final Integer fileId) {
        FilePhotoViewFragment filePhotoViewFragment = FilePhotoViewFragment.newInstance(fileId);

        loadFragment(filePhotoViewFragment, ViewUtil.getResourceString(mContext, R.string.file_photo_detail_title), null, FRAGMENT_TAG_FILE_PHOTO);

        if (mFileLayoutListener != null)
            mFileLayoutListener.onFilePhotoRequest(fileId);

        return filePhotoViewFragment;
    }

    public void setFileLayoutListener(final FileLayoutListener fileLayoutListener) {
        mFileLayoutListener = fileLayoutListener;
    }

    public interface FileLayoutListener {
        void onFilePhotoRequest(Integer fileId);
    }
}
