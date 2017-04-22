package com.construction.pm.views.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.construction.pm.R;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectStageDocumentListAdapter extends BaseAdapter {

    protected final List<Integer> mFileIdList;
    protected final SparseArray<FileModel> mFileModelList;
    protected ProjectStageDocumentListAdapterListener mProjectStageDocumentListAdapterListener;

    public ProjectStageDocumentListAdapter(final ProjectStageDocumentModel projectStageDocumentModel) {
        mFileModelList = new SparseArray<FileModel>();

        mFileIdList = new ArrayList<Integer>();
        if (projectStageDocumentModel.getFileId() != null)
            mFileIdList.add(projectStageDocumentModel.getFileId());
        if (projectStageDocumentModel.getFileAdditional1Id() != null)
            mFileIdList.add(projectStageDocumentModel.getFileAdditional1Id());
        if (projectStageDocumentModel.getFileAdditional2Id() != null)
            mFileIdList.add(projectStageDocumentModel.getFileAdditional2Id());
        if (projectStageDocumentModel.getFileAdditional3Id() != null)
            mFileIdList.add(projectStageDocumentModel.getFileAdditional3Id());
        if (projectStageDocumentModel.getFileAdditional4Id() != null)
            mFileIdList.add(projectStageDocumentModel.getFileAdditional4Id());
        if (projectStageDocumentModel.getFileAdditional5Id() != null)
            mFileIdList.add(projectStageDocumentModel.getFileAdditional5Id());
    }

    @Override
    public int getCount() {
        return mFileIdList.size();
    }

    @Override
    public FileModel getItem(int position) {
        return mFileModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFileIdList.get(position);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ProjectStageDocumentListAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_document_list_item_view, parent, false);

            holder = new ProjectStageDocumentListAdapter.ViewHolder(view);

            Integer fileId = mFileIdList.get(position);
            if (fileId != null) {
                holder.setFileId(fileId);
                if (mProjectStageDocumentListAdapterListener != null)
                    mProjectStageDocumentListAdapterListener.onProjectStageDocumentListAdapterRequestFileInfo(position, fileId);
            }

            view.setTag(holder);
        } else {
            holder = (ProjectStageDocumentListAdapter.ViewHolder) view.getTag();
        }

        final FileModel fileModel = getItem(position);
        if (fileModel != null) {
            if (holder.getFileModel() == null) {
                holder.setFileModel(fileModel);
                if (mProjectStageDocumentListAdapterListener != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mProjectStageDocumentListAdapterListener.onProjectStageDocumentListAdapterClick(fileModel);
                        }
                    });
                }
            }
        }

        return view;
    }

    public void setItem(final int position, final FileModel fileModel) {
        mFileModelList.put(position, fileModel);
        notifyDataSetChanged();
    }

    public void setProjectStageDocumentListAdapterListener(final ProjectStageDocumentListAdapterListener projectStageDocumentListAdapterListener) {
        mProjectStageDocumentListAdapterListener = projectStageDocumentListAdapterListener;
    }

    public static class ViewHolder {
        protected Integer mFileId;
        protected FileModel mFileModel;

        protected AppCompatTextView mFileDocumentDate;
        protected AppCompatTextView mFileDocumentName;

        public ViewHolder(View view) {
            mFileDocumentDate = (AppCompatTextView) view.findViewById(R.id.documentDate);
            mFileDocumentName = (AppCompatTextView) view.findViewById(R.id.documentName);
        }

        public void setFileId(final Integer fileId) {
            mFileId = fileId;
        }

        public Integer getFileId() {
            return mFileId;
        }

        public void setFileModel(final FileModel fileModel) {
            mFileModel = fileModel;

            Calendar documentDate = null;
            if (mFileModel.getCreateDate() != null)
                documentDate = mFileModel.getCreateDate();
            else if (mFileModel.getLastUpdate() != null)
                documentDate = mFileModel.getLastUpdate();
            if (documentDate != null)
                mFileDocumentDate.setText(DateTimeUtil.ToDateTimeDisplayString(documentDate));

            mFileDocumentName.setText(mFileModel.getOriginalFileName());
        }

        public FileModel getFileModel() {
            return mFileModel;
        }
    }

    public interface ProjectStageDocumentListAdapterListener {
        void onProjectStageDocumentListAdapterRequestFileInfo(int position, Integer fileId);
        void onProjectStageDocumentListAdapterClick(FileModel fileModel);
    }
}