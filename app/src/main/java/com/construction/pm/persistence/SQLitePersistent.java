package com.construction.pm.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLitePersistent extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AppPersistent.db";

    public SQLitePersistent(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate;

        sqlCreate =
            "CREATE TABLE [network_pending] (" +
            "  'id'                 INTEGER     NOT NULL    PRIMARY KEY AUTOINCREMENT" +
            ", 'type'               INTEGER     NOT NULL" +
            ", 'command_key'        TEXT        NULL" +
            ", 'command'            TEXT        NULL" +
            ", 'is_sent'            INTEGER     NOT NULL    DEFAULT 0" +
            ", 'project_member_id'  INTEGER     NOT NULL" +
            ", 'created_date'       DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP" +
            ", 'updated_date'       DATETIME    NULL" +
            ")";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_pending_idx01 ON network_pending(project_member_id)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_pending_idx02 ON network_pending(type)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_pending_idx03 ON network_pending(is_sent)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_pending_idx04 ON network_pending(command_key)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate =
            "CREATE TABLE [network_cache] (" +
            "  'id'                 INTEGER     NOT NULL    PRIMARY KEY AUTOINCREMENT" +
            ", 'type'               INTEGER     NOT NULL" +
            ", 'content_key'        TEXT        NOT NULL" +
            ", 'content'            TEXT        NULL" +
            ", 'project_member_id'  INTEGER     NOT NULL" +
            ", 'created_date'       DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP" +
            ", 'updated_date'       DATETIME" +
            ")";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_cache_idx01 ON network_cache(project_member_id)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_cache_idx02 ON network_cache(type)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_cache_idx03 ON network_cache(content_key)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate =
            "CREATE TABLE [network_notification] (" +
            "  'id'                         INTEGER     NOT NULL    PRIMARY KEY AUTOINCREMENT" +
            ", 'project_notification_id'    INTEGER     NULL" +
            ", 'content'                    TEXT        NULL" +
            ", 'is_read'                    INTEGER     NOT NULL    DEFAULT 0" +
            ", 'project_member_id'          INTEGER     NOT NULL" +
            ", 'created_date'               DATETIME    NOT NULL    DEFAULT CURRENT_TIMESTAMP" +
            ", 'updated_date'               DATETIME" +
            ")";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_notification_idx01 ON network_notification(project_notification_id)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_notification_idx02 ON network_notification(project_member_id)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX network_notification_idx03 ON network_notification(is_read)";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sqlUpgrade;

        for (int version = oldVersion + 1; version <= newVersion; version++) {
            if (version == 2) {
                sqlUpgrade = "";
                sqLiteDatabase.execSQL(sqlUpgrade);
            }
        }
    }
}
