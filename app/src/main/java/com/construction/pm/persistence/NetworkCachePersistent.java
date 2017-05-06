package com.construction.pm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.MainApplication;
import com.construction.pm.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class NetworkCachePersistent {

    protected Context mContext;
    protected SQLitePersistent mSQLitePersistent;

    protected enum NetworkCachePersistentType {
        PROJECT_LIST(0), PROJECT_DEPENDENCIES(1),
        PROJECT_STAGE_DEPENDENCIES(3), PROJECT_STAGE_ASSIGN_COMMENT_LIST(13),
        PROJECT_PLAN_DEPENDENCIES(4),
        INSPECTOR_PROJECT_ACTIVITY_LIST(2), INSPECTOR_PROJECT_ACTIVITY_DETAIL(11),
        INSPECTOR_PROJECT_ACTIVITY_MONITORING_LIST(6), INSPECTOR_PROJECT_ACTIVITY_MONITORING_DETAIL(12),
        MANAGER_PROJECT_ACTIVITY_LIST(7), MANAGER_PROJECT_ACTIVITY_DETAIL(10),
        MANAGER_PROJECT_ACTIVITY_MONITORING_LIST(8), MANAGER_PROJECT_ACTIVITY_UPDATE_LIST(9),
        FILE(5),
        REPORT_REQUEST_LIST(14);

        private final int mNetworkCachePersistentType;

        NetworkCachePersistentType(int value) {
            mNetworkCachePersistentType = value;
        }

        public int getValue() {
            return mNetworkCachePersistentType;
        }

        public static NetworkCachePersistentType fromInt(int value) {
            for (NetworkCachePersistentType networkCachePersistentType : NetworkCachePersistentType.values()) {
                if (value == networkCachePersistentType.getValue()) {
                    return networkCachePersistentType;
                }
            }
            return null;
        }
    }

    public NetworkCachePersistent(Context context) {
        mContext = context;
        mSQLitePersistent = ((MainApplication) mContext.getApplicationContext()).getSQLitePersistent();
    }

    protected long saveNetworkCacheContent(final SQLiteDatabase sqLiteDatabase, final NetworkCachePersistentType networkCachePersistentType, final String contentKey, final String content, final Integer projectMemberId) {
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
                String.valueOf(networkCachePersistentType.getValue()),
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
            // -- Update old record --
            contentValues.put("updated_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
            int affectedRow = sqLiteDatabase.update("network_cache", contentValues, "id = ?",
                new String[] {
                    String.valueOf(networkCacheId)
                }
            );
        } else {
            // -- Create new record --
            contentValues.put("type", networkCachePersistentType.getValue());
            contentValues.put("content_key", contentKey);
            contentValues.put("project_member_id", projectMemberId);
            contentValues.put("created_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
            networkCacheId = sqLiteDatabase.insertOrThrow("network_cache", null, contentValues);
        }

        return networkCacheId;
    }

    protected String getNetworkCacheContent(final SQLiteDatabase sqLiteDatabase, final NetworkCachePersistentType networkCachePersistentType, final String contentKey, final Integer projectMemberId) {
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
                String.valueOf(networkCachePersistentType.getValue()),
                String.valueOf(projectMemberId)
            }
        );
        if (cursor.moveToFirst()) {
            content = cursor.getString(cursor.getColumnIndex("content"));
        }
        cursor.close();

        return content;
    }

    protected String[] getNetworkCacheContents(final SQLiteDatabase sqLiteDatabase, final NetworkCachePersistentType networkCachePersistentType, final String contentKey, final Integer projectMemberId) {
        List<String> contentList = new ArrayList<String>();

        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT content " +
            "  FROM network_cache " +
            " WHERE content_key = ? " +
            "   AND type = ?" +
            "   AND project_member_id = ?",
            new String[] {
                contentKey,
                String.valueOf(networkCachePersistentType.getValue()),
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
