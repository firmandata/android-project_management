package com.construction.pm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.NotificationModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationPersistent extends SQLitePersistent {

    public NotificationPersistent(Context context) {
        super(context);
    }

    public long[] saveNotificationModels(final NotificationModel[] notificationModels) throws PersistenceError {
        List<Long> notificationIdList = new ArrayList<Long>();

        SQLiteDatabase sqLiteDatabase = null;

        try {
            sqLiteDatabase = getWritableDatabase();

            // -- Start transaction --
            sqLiteDatabase.beginTransaction();

            try {
                for (NotificationModel notificationModel : notificationModels) {
                    // -- Get NotificationModel content --
                    String content = null;
                    try {
                        org.json.JSONObject jsonObject = notificationModel.build();
                        content = jsonObject.toString(0);
                    } catch (org.json.JSONException ex) {
                    } catch (Exception ex) {
                    }

                    // -- Check existing --
                    long networkNotificationId = 0;
                    Cursor cursor = sqLiteDatabase.rawQuery(
                        "SELECT id " +
                        "  FROM network_notification " +
                        " WHERE project_notification_id = ? " +
                        "   AND project_member_id = ?",
                        new String[] {
                            String.valueOf(notificationModel.getProjectNotificationId()),
                            String.valueOf(notificationModel.getProjectMemberId())
                        }
                    );
                    if (cursor.moveToFirst())
                        networkNotificationId = cursor.getLong(cursor.getColumnIndex("id"));
                    cursor.close();

                    // -- Set NotificationModel --
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("content", content);
                    contentValues.put("is_read", notificationModel.isRead() ? 1 : 0);
                    if (networkNotificationId > 0) {
                        // -- Update old NotificationModel --
                        contentValues.put("updated_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                        int affectedRow = sqLiteDatabase.update("network_notification", contentValues, "id = ?",
                            new String[] {
                                String.valueOf(networkNotificationId)
                            }
                        );
                        if (affectedRow > 0)
                            notificationIdList.add(networkNotificationId);
                    } else {
                        // -- Create new NotificationModel --
                        contentValues.put("project_notification_id", notificationModel.getProjectNotificationId());
                        contentValues.put("project_member_id", notificationModel.getProjectMemberId());
                        contentValues.put("created_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                        long notificationId = sqLiteDatabase.insertOrThrow("network_notification", null, contentValues);
                        notificationIdList.add(notificationId);
                    }
                }

                // -- Commit transaction --
                sqLiteDatabase.setTransactionSuccessful();
            } catch (SQLException ex) {
                // -- End transaction --
                sqLiteDatabase.endTransaction();
                throw ex;
            } catch (Exception ex) {
                // -- End transaction --
                sqLiteDatabase.endTransaction();
                throw ex;
            }

            // -- End transaction --
            sqLiteDatabase.endTransaction();

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        int notificationIdSize = notificationIdList.size();
        long notificationIds[] = new long[notificationIdSize];
        for (int notificationIdIdx = 0; notificationIdIdx < notificationIdSize; notificationIdIdx++) {
            notificationIds[notificationIdIdx] = notificationIdList.get(notificationIdIdx);
        }
        return notificationIds;
    }

    public NotificationModel[] getNotificationModels(final Integer projectMemberId) throws PersistenceError {
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT content " +
                "  FROM network_notification " +
                " WHERE project_member_id = ? " +
                " ORDER BY project_notification_id DESC",
                new String[] {
                    String.valueOf(projectMemberId)
                }
            );
            if (cursor.moveToFirst()) {
                do {
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    if (content != null) {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                            notificationModelList.add(NotificationModel.build(jsonObject));
                        } catch (org.json.JSONException ex) {
                        } catch (Exception ex) {
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        NotificationModel[] notificationModels = new NotificationModel[notificationModelList.size()];
        notificationModelList.toArray(notificationModels);
        return notificationModels;
    }

    public NotificationModel[] getUnreadNotificationModels(final Integer projectMemberId) throws PersistenceError {
        return getUnreadNotificationModels(projectMemberId, 0);
    }

    public NotificationModel[] getUnreadNotificationModels(final Integer projectMemberId, final int limit) throws PersistenceError {
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT content " +
                "  FROM network_notification " +
                " WHERE project_member_id = ? " +
                "   AND is_read = 0 " +
                " ORDER BY project_notification_id DESC " +
                (limit > 0 ? " LIMIT " + String.valueOf(limit) : ""),
                new String[] {
                    String.valueOf(projectMemberId)
                }
            );
            if (cursor.moveToFirst()) {
                do {
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    if (content != null) {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                            notificationModelList.add(NotificationModel.build(jsonObject));
                        } catch (org.json.JSONException ex) {
                        } catch (Exception ex) {
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        NotificationModel[] notificationModels = new NotificationModel[notificationModelList.size()];
        notificationModelList.toArray(notificationModels);
        return notificationModels;
    }

    public int getUnreadNotificationModelCount(final Integer projectMemberId) throws PersistenceError {
        int unreadNotificationModelCount = 0;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT COUNT(id) num_rows " +
                "  FROM network_notification " +
                " WHERE project_member_id = ?" +
                "   AND is_read = 0",
                new String[] {
                    String.valueOf(projectMemberId)
                }
            );
            if (cursor.moveToFirst()) {
                unreadNotificationModelCount = cursor.getInt(cursor.getColumnIndex("num_rows"));
            }
            cursor.close();

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        return unreadNotificationModelCount;
    }

    public boolean saveNotificationModel(final NotificationModel notificationModel) throws PersistenceError {
        int affectedRow = 0;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getWritableDatabase();

            // -- Get NotificationModel json content --
            String content = null;
            try {
                org.json.JSONObject jsonObject = notificationModel.build();
                content = jsonObject.toString(0);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }

            // -- Update table record --
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", content);
            contentValues.put("is_read", notificationModel.isRead() ? 1 : 0);
            contentValues.put("updated_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
            affectedRow = sqLiteDatabase.update("network_notification", contentValues, "project_notification_id = ? AND project_member_id = ?",
                new String[] {
                    String.valueOf(notificationModel.getProjectNotificationId()),
                    String.valueOf(notificationModel.getProjectMemberId())
                }
            );

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        return (affectedRow > 0);
    }

    public NotificationModel getLastNotificationModel(final Integer projectMemberId) throws PersistenceError {
        NotificationModel notificationModel = null;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT content, is_read " +
                "  FROM network_notification " +
                " WHERE project_member_id = ? " +
                " ORDER BY project_notification_id DESC " +
                " LIMIT 1",
                new String[] {
                    String.valueOf(projectMemberId)
                }
            );
            if (cursor.moveToFirst()) {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                if (content != null) {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                        notificationModel = NotificationModel.build(jsonObject);
                    } catch (org.json.JSONException ex) {
                    } catch (Exception ex) {
                    }
                }
            }
            cursor.close();

            // -- Close database --
            sqLiteDatabase.close();
        } catch (SQLException ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            // -- Close database --
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        return notificationModel;
    }
}
