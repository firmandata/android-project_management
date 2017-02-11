package com.construction.pm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class NetworkCachePersistent extends SQLitePersistent {
    protected enum PersistentNetworkType {
        PROJECT_LIST(0), PROJECT_DEPENDENCIES(1);

        private final int mPersistentNetworkType;

        PersistentNetworkType(int value) {
            mPersistentNetworkType = value;
        }

        public int getValue() {
            return mPersistentNetworkType;
        }

        public static PersistentNetworkType fromInt(int value) {
            for (PersistentNetworkType persistentNetworkType : PersistentNetworkType.values()) {
                if (value == persistentNetworkType.getValue()) {
                    return persistentNetworkType;
                }
            }
            return null;
        }
    }

    protected NetworkCachePersistent(Context context) {
        super(context);
    }

    protected long saveNetworkCache(final SQLiteDatabase sqLiteDatabase, final PersistentNetworkType persistentNetworkType, final String contentKey, final String content, final Integer projectMemberId) {
        long networkCacheId = 0;

        // -- Check existing --
        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT id " +
            "  FROM network_cache " +
            " WHERE content_key = ? " +
            "   AND type = ?" +
            "   AND project_member_id = ?",
            new String[] {
                contentKey,
                String.valueOf(persistentNetworkType.getValue()),
                String.valueOf(projectMemberId)
            }
        );
        if (cursor.moveToFirst())
            networkCacheId = cursor.getLong(cursor.getColumnIndex("id"));
        cursor.close();

        // -- Set data record --
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        if (networkCacheId > 0) {
            // -- Update old notification record --
            contentValues.put("updated_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
            int affectedRow = sqLiteDatabase.update("network_cache", contentValues, "id = ?",
                new String[] {
                    String.valueOf(networkCacheId)
                }
            );
        } else {
            // -- Create new notification record --
            contentValues.put("type", persistentNetworkType.getValue());
            contentValues.put("content_key", contentKey);
            contentValues.put("project_member_id", projectMemberId);
            contentValues.put("created_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
            networkCacheId = sqLiteDatabase.insertOrThrow("network_cache", null, contentValues);
        }

        return networkCacheId;
    }

    protected String getNetworkCache(final SQLiteDatabase sqLiteDatabase, final PersistentNetworkType persistentNetworkType, final String contentKey, final Integer projectMemberId) {
        String content = null;

        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT content " +
            "  FROM network_cache " +
            " WHERE content_key = ? " +
            "   AND type = ?" +
            "   AND project_member_id = ?" +
            " LIMIT 1",
            new String[] {
                contentKey,
                String.valueOf(persistentNetworkType.getValue()),
                String.valueOf(projectMemberId)
            }
        );
        if (cursor.moveToFirst()) {
            content = cursor.getString(cursor.getColumnIndex("content"));
        }
        cursor.close();

        return content;
    }

    protected String[] getNetworkCaches(final SQLiteDatabase sqLiteDatabase, final PersistentNetworkType persistentNetworkType, final String contentKey, final Integer projectMemberId) {
        List<String> contentList = new ArrayList<String>();

        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT content " +
            "  FROM network_cache " +
            " WHERE content_key = ? " +
            "   AND type = ?" +
            "   AND project_member_id = ?",
            new String[] {
                contentKey,
                String.valueOf(persistentNetworkType.getValue()),
                String.valueOf(projectMemberId)
            }
        );
        if (cursor.moveToFirst()) {
            do {
                contentList.add(cursor.getString(cursor.getColumnIndex("content")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        String[] contents = new String[contentList.size()];
        contentList.toArray(contents);
        return contents;
    }
}
