package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.FileModel;

public class FileCachePersistent extends NetworkCachePersistent {

    public FileCachePersistent(Context context) {
        super(context);
    }

    public long setFileModel(final FileModel fileModel) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = null;
        String content = null;

        // -- Get contentKey --
        if (fileModel.getFileId() != null)
            contentKey = String.valueOf(fileModel.getFileId());

        // -- Get FileModel content --
        try {
            content = fileModel.build().toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.FILE, contentKey, content, 0);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public FileModel getFileModel(final Integer fileId) throws PersistenceError {
        FileModel fileModel = null;

        String contentKey = String.valueOf(fileId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.FILE, contentKey, 0);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate FileModel from content
        if (content != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                fileModel = FileModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        return fileModel;
    }
}
