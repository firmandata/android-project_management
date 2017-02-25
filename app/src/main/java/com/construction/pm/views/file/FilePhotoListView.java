package com.construction.pm.views.file;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.FileModel;
import com.construction.pm.views.ImageRequestListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilePhotoListView {
    protected Context mContext;

    protected RelativeLayout mFilePhotoListView;

    protected RecyclerView mFilePhotoList;
    protected FilePhotoListAdapter mFilePhotoListAdapter;

    public FilePhotoListView(final Context context) {
        mContext = context;
    }

    public FilePhotoListView(final Context context, final RelativeLayout filePhotoListView) {
        this(context);

        initializeView(filePhotoListView);
    }

    public static FilePhotoListView buildFilePhotoListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new FilePhotoListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static FilePhotoListView buildFilePhotoListView(final Context context, final ViewGroup viewGroup) {
        return buildFilePhotoListView(context, R.layout.file_photo_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout filePhotoListView) {
        mFilePhotoListView = filePhotoListView;

        mFilePhotoList = (RecyclerView) mFilePhotoListView.findViewById(R.id.filePhotoList);
        mFilePhotoList.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mFilePhotoList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        mFilePhotoList.addItemDecoration(dividerItemDecoration);

        mFilePhotoListAdapter = new FilePhotoListAdapter();
        mFilePhotoList.setAdapter(mFilePhotoListAdapter);
    }

    public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
        mFilePhotoListAdapter.setImageRequestListener(imageRequestListener);
    }

    public void setFileIds(final Integer[] fileIds) {
        mFilePhotoListAdapter.setFileIds(fileIds);
    }

    public void addFileIds(final Integer[] fileIds) {
        mFilePhotoListAdapter.addFileIds(fileIds);
    }

    public void addFileId(final Integer fileId) {
        mFilePhotoListAdapter.addFileId(fileId);
    }

    public void setFileId(final Integer fileId) {
        int position = mFilePhotoListAdapter.getPosition(fileId);
        if (position >= 0) {
            mFilePhotoListAdapter.setFileId(position, fileId);
        }
    }

    public void removeFileIds(final Integer[] fileIds) {
        mFilePhotoListAdapter.removeFileIds(fileIds);
    }

    public void removeFileId(final Integer fileId) {
        mFilePhotoListAdapter.removeFileId(fileId);
    }

    public RelativeLayout getView() {
        return mFilePhotoListView;
    }

    protected class FilePhotoListAdapter extends RecyclerView.Adapter<FilePhotoListViewHolder> {

        protected List<Integer> mFileIdList;

        protected ImageRequestListener mImageRequestListener;

        public FilePhotoListAdapter() {
            mFileIdList = new ArrayList<Integer>();
        }

        public FilePhotoListAdapter(final Integer[] fileIds) {
            this();
            mFileIdList = new ArrayList<Integer>(Arrays.asList(fileIds));
        }

        public void setFileIds(final Integer[] fileIds) {
            mFileIdList = new ArrayList<Integer>(Arrays.asList(fileIds));
            notifyDataSetChanged();
        }

        public void addFileIds(final Integer[] fileIds) {
            List<Integer> newFileIdList = new ArrayList<Integer>();
            for (Integer newFileId : fileIds) {
                int position = getPosition(newFileId);
                if (position >= 0) {
                    // -- replace item --
                    setFileId(position, newFileId);
                } else {
                    // -- new items --
                    newFileIdList.add(newFileId);
                }
            }
            if (newFileIdList.size() > 0) {
                mFileIdList.addAll(0, newFileIdList);
                notifyItemRangeInserted(0, newFileIdList.size());
            }
        }

        public void addFileId(final Integer fileId) {
            addFileIds(new Integer[] { fileId });
        }

        public void setFileId(final int position, final Integer fileId) {
            if ((position + 1) > mFileIdList.size())
                return;

            mFileIdList.set(position, fileId);
            notifyItemChanged(position);
        }

        public void removeFileIds(final Integer[] fileIds) {
            for (Integer removeFileId : fileIds) {
                removeFileId(removeFileId);
            }
        }

        public void removeFileId(final Integer fileId) {
            int position = getPosition(fileId);
            if (position >= 0) {
                mFileIdList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Integer getFileId(final int position) {
            if ((position + 1) > mFileIdList.size())
                return null;

            return mFileIdList.get(position);
        }

        public int getPosition(final Integer fileId) {
            if (fileId == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (Integer fileIdExist : mFileIdList) {
                if (fileIdExist.equals(fileId)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            isPositionFound = false;
            position = 0;
            for (Integer fileIdExist : mFileIdList) {
                if (fileIdExist != null) {
                    if (fileIdExist.equals(fileId)) {
                        isPositionFound = true;
                        break;
                    }
                }
                position++;
            }

            if (isPositionFound)
                return position;

            return -1;
        }

        @Override
        public FilePhotoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_photo_list_item_view, parent, false);
            return new FilePhotoListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FilePhotoListViewHolder holder, int position) {
            if ((position + 1) > mFileIdList.size())
                return;

            Integer fileId = mFileIdList.get(position);
            holder.setImageRequestListener(mImageRequestListener);
            holder.setFileId(fileId);
        }

        @Override
        public int getItemCount() {
            return mFileIdList.size();
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }

    protected class FilePhotoListViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPhotoId;

        protected ImageRequestListener mImageRequestListener;

        public FilePhotoListViewHolder(View view) {
            super(view);

            mPhotoId = (ImageView) view.findViewById(R.id.photoId);
        }

        public void setFileId(final Integer fileId) {
            if (fileId != null) {
                if (mImageRequestListener != null)
                    mImageRequestListener.onImageRequest(mPhotoId, fileId);
            }
        }

        public void setImageRequestListener(final ImageRequestListener imageRequestListener) {
            mImageRequestListener = imageRequestListener;
        }
    }
}
