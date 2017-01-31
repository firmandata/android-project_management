package com.construction.pm.libraries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteOpenerHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "constructionPM.db";

    public SqliteOpenerHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate;

        sqlCreate =
              "CREATE TABLE [submit_pending] ("
            + "  'id' INTEGER PRIMARY KEY AUTOINCREMENT"
            + ", 'send_command_type' INTEGER NOT NULL"
            + ", 'send_command' TEXT NULL"
            + ", 'is_sent' INTEGER"
            + ", 'created_date' DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ", 'updated_date' DATETIME"
            + ")";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX submit_pending_idx01 ON submit_pending(is_sent)";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate =
              "CREATE TABLE [data_cache] ("
            + "  'id' INTEGER PRIMARY KEY AUTOINCREMENT"
            + ", 'cache_type' INTEGER NOT NULL"
            + ", 'cache_data' TEXT NULL"
            + ", 'created_date' DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ", 'updated_date' DATETIME"
            + ")";
        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE INDEX data_cache_idx01 ON data_cache(cache_type)";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sqlUpgrade;

        for (int version = oldVersion + 1; version <= newVersion; version++) {
            if (version == 2) {

            }
        }
    }
}
