package com.construction.pm.models;

import android.util.Base64;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class FileModel {
    protected Integer mFileId;
    protected String mVirtualPath;
    protected String mFileName;
    protected String mFileType;
    protected String mFileExt;
    protected Integer mFileSize;
    protected String mThumbName;
    protected String mOriginalFileName;
    protected String mTypeName;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;
    protected byte[] mFileData;

    public FileModel() {

    }

    public void setFileId(final Integer fileId) {
        mFileId = fileId;
    }

    public Integer getFileId() {
        return mFileId;
    }

    public void setVirtualPath(final String virtualPath) {
        mVirtualPath = virtualPath;
    }

    public String getVirtualPath() {
        return mVirtualPath;
    }

    public void setFileName(final String fileName) {
        mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileType(final String fileType) {
        mFileType = fileType;
    }

    public String getFileType() {
        return mFileType;
    }

    public void setFileExt(final String fileExt) {
        mFileExt = fileExt;
    }

    public String getFileExt() {
        return mFileExt;
    }

    public void setFileSize(final Integer fileSize) {
        mFileSize = fileSize;
    }

    public Integer getFileSize() {
        return mFileSize;
    }

    public void setThumbName(final String thumbName) {
        mThumbName = thumbName;
    }

    public String getThumbName() {
        return mThumbName;
    }

    public void setOriginalFileName(final String originalFileName) {
        mOriginalFileName = originalFileName;
    }

    public String getOriginalFileName() {
        return mOriginalFileName;
    }

    public void setTypeName(final String typeName) {
        mTypeName = typeName;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setCreatorId(final Integer creatorId) {
        mCreatorId = creatorId;
    }

    public Integer getCreatorId() {
        return mCreatorId;
    }

    public void setCreateDate(final Calendar createDate) {
        mCreateDate = createDate;
    }

    public Calendar getCreateDate() {
        return mCreateDate;
    }

    public void setLastUserId(final Integer lastUserId) {
        mLastUserId = lastUserId;
    }

    public Integer getLastUserId() {
        return mLastUserId;
    }

    public void setLastUpdate(final Calendar lastUpdate) {
        mLastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return mLastUpdate;
    }

    public void setFileData(final byte[] fileData) {
        mFileData = fileData;
    }

    public byte[] getFileData() {
        return mFileData;
    }

    public static FileModel build(final org.json.JSONObject jsonObject) throws JSONException {
        FileModel fileModel = new FileModel();

        if (!jsonObject.isNull("file_id"))
            fileModel.setFileId(jsonObject.getInt("file_id"));
        if (!jsonObject.isNull("virtual_path"))
            fileModel.setVirtualPath(jsonObject.getString("virtual_path"));
        if (!jsonObject.isNull("file_name"))
            fileModel.setFileName(jsonObject.getString("file_name"));
        if (!jsonObject.isNull("file_type"))
            fileModel.setFileType(jsonObject.getString("file_type"));
        if (!jsonObject.isNull("file_ext"))
            fileModel.setFileExt(jsonObject.getString("file_ext"));
        if (!jsonObject.isNull("file_size"))
            fileModel.setFileSize(jsonObject.getInt("file_size"));
        if (!jsonObject.isNull("thumb_name"))
            fileModel.setThumbName(jsonObject.getString("thumb_name"));
        if (!jsonObject.isNull("original_file_name"))
            fileModel.setOriginalFileName(jsonObject.getString("original_file_name"));
        if (!jsonObject.isNull("type_name"))
            fileModel.setTypeName(jsonObject.getString("type_name"));
        if (!jsonObject.isNull("creator_id"))
            fileModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            fileModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            fileModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            fileModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));
        if (!jsonObject.isNull("binaryData")) {
            String base64Encode = jsonObject.getString("binaryData");
            byte[] fileData = null;
            try {
                String base64EncodeBytes = base64Encode.substring(base64Encode.indexOf(",") + 1);
                fileData = Base64.decode(base64EncodeBytes, Base64.DEFAULT);
            } catch (Exception ex) {
            }
            if (fileData != null)
                fileModel.setFileData(fileData);
        }

        return fileModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getFileId() != null)
            jsonObject.put("file_id", getFileId());
        if (getVirtualPath() != null)
            jsonObject.put("virtual_path", getVirtualPath());
        if (getFileName() != null)
            jsonObject.put("file_name", getFileName());
        if (getFileType() != null)
            jsonObject.put("file_type", getFileType());
        if (getFileExt() != null)
            jsonObject.put("file_ext", getFileExt());
        if (getFileSize() != null)
            jsonObject.put("file_size", getFileSize());
        if (getThumbName() != null)
            jsonObject.put("thumb_name", getThumbName());
        if (getOriginalFileName() != null)
            jsonObject.put("original_file_name", getOriginalFileName());
        if (getTypeName() != null)
            jsonObject.put("type_name", getTypeName());
        if (getCreatorId() != null)
            jsonObject.put("creator_id", getCreatorId());
        if (getCreateDate() != null)
            jsonObject.put("create_date", DateTimeUtil.ToDateTimeString(getCreateDate()));
        if (getLastUserId() != null)
            jsonObject.put("last_user_id", getLastUserId());
        if (getLastUpdate() != null)
            jsonObject.put("last_update", DateTimeUtil.ToDateTimeString(getLastUpdate()));
        if (getFileData() != null) {
            String fileData = null;
            try {
                fileData = Base64.encodeToString(getFileData(), Base64.DEFAULT);
            } catch (Exception ex) {
            }
            if (fileData != null)
                jsonObject.put("binaryData", "data:" + (getFileType() != null ? getFileType() : "") + ";base64," + fileData);
        }

        return jsonObject;
    }
}
