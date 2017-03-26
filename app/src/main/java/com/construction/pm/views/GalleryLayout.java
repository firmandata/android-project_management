package com.construction.pm.views;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryLayout {

    protected Context mContext;

    protected RelativeLayout mGalleryLayout;

    protected RecyclerView mGalleryList;
    protected GalleryListAdapter mGalleryListAdapter;

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

        mGalleryList = (RecyclerView) mGalleryLayout.findViewById(R.id.galleryList);
        mGalleryList.setItemAnimator(new DefaultItemAnimator());
        mGalleryList.setHasFixedSize(true);
        mGalleryList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mGalleryList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String imagePath = mGalleryListAdapter.getImagePath(position);
                if (imagePath != null) {
                    if (mGalleryListener != null)
                        mGalleryListener.onImageClick(imagePath);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mGalleryList.setLayoutManager(layoutManager);

        if (mGalleryListener != null)
            mGalleryListener.onGalleryRequest();

        mGalleryListAdapter = new GalleryListAdapter(mContext);
        mGalleryList.setAdapter(mGalleryListAdapter);
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
        void onGalleryRequest();
        void onImageClick(String filePath);
    }

    protected class GalleryListAdapter extends RecyclerView.Adapter<GalleryListViewHolder> {

        protected Context mContext;
        protected List<Integer> mImageIdList;
        protected List<String> mImageDataList;

        public GalleryListAdapter(final Context context) {
            mContext = context;

            mImageIdList = new ArrayList<Integer>();
            mImageDataList = new ArrayList<String>();

            final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            if (cursor != null) {
                int rowCount = cursor.getCount();
                int idIdx = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int dataIdx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
                    cursor.moveToPosition(rowIdx);
                    mImageIdList.add(cursor.getInt(idIdx));
                    mImageDataList.add(cursor.getString(dataIdx));
                }
                cursor.close();
            }
        }

        public Bitmap getImageBitmap(final int position) {
            if ((position + 1) > mImageDataList.size())
                return null;

            Bitmap bitmap = null;

            String imageData = mImageDataList.get(position);
            if (imageData != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeFile(imageData, options);
            }

            return bitmap;
        }

        public String getImagePath(final int position) {
            if ((position + 1) > mImageDataList.size())
                return null;

            return mImageDataList.get(position);
        }

        public Bitmap getImageThumbnail(final int position) {
            if ((position + 1) > mImageDataList.size())
                return null;

            Bitmap bitmap = null;

            int imageId = mImageIdList.get(position);
            if (imageId > 0) {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                    mContext.getApplicationContext().getContentResolver(),
                    imageId,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null);
            }

            return bitmap;
        }

        @Override
        public GalleryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view, parent, false);
            return new GalleryListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GalleryListViewHolder holder, int position) {
            if ((position + 1) > mImageDataList.size())
                return;

            holder.setImageView(getImageThumbnail(position));
        }

        @Override
        public int getItemCount() {
            return mImageIdList.size();
        }
    }

    protected class GalleryListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatImageView mImageView;

        public GalleryListViewHolder(View view) {
            super(view);

            mImageView = (AppCompatImageView) view.findViewById(R.id.imageView);
        }

        public void setImageView(final Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
