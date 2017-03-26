package com.construction.pm.views;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class GalleryLayout {

    protected Context mContext;

    protected RelativeLayout mGalleryLayout;

    protected GalleryListener mGalleryListener;

    protected GalleryLayout(final Context context) {
        mContext = context;
    }

    public GalleryLayout(final Context context, final RelativeLayout galleryLayout) {
        this(context);

        initializeView(galleryLayout);
    }

    public static GalleryLayout buildGalleryLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new GalleryLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static GalleryLayout buildGalleryLayout(final Context context, final ViewGroup viewGroup) {
        return buildGalleryLayout(context, R.layout.gallery_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout galleryLayout) {
        mGalleryLayout = galleryLayout;
    }

    public RelativeLayout getLayout() {
        return mGalleryLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        activity.setContentView(mGalleryLayout);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {

        }
    }

    public void setGalleryListener(final GalleryListener galleryListener) {
        mGalleryListener = galleryListener;
    }

    public interface GalleryListener {

    }
}
