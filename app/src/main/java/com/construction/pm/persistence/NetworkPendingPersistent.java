package com.construction.pm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.MainApplication;
import com.construction.pm.models.network.NetworkPendingModel;
import com.construction.pm.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NetworkPendingPersistent {

    protected Context mContext;
    protected SQLitePersistent mSQLitePersistent;

    public NetworkPendingPersistent(Context context) {
        mContext = context;
        mSQLitePersistent = ((MainApplication) mContext.getApplicationContext()).getSQLitePersistent();
    }

    public long createNetworkPending(final NetworkPendingModel networkPendingModel) throws PersistenceError {
        long networkPendingId = 0;

        if (networkPendingModel.getCommand() != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save command to pending --
                networkPendingId = createNetworkPending(sqLiteDatabase, networkPendingModel);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkPendingId;
    }

    public boolean setNetworkPendingSent(final NetworkPendingModel networkPendingModel) throws PersistenceError {
        boolean isSetMarked = false;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

            // -- Save NetworkPendingCommand sent --
            isSetMarked = setNetworkPendingSent(sqLiteDatabase, networkPendingModel);

        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        return isSetMarked;
    }

    public NetworkPendingModel[] getUnSentNetworkPendingModels(final Integer projectMemberId) throws PersistenceError {
        NetworkPendingModel[] networkPendingModels = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

            // -- Get unsent NetworkPendingModel --
            networkPendingModels = getUnSentNetworkPendingModels(sqLiteDatabase, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        return networkPendingModels;
    }

    protected long createNetworkPending(final SQLiteDatabase sqLiteDatabase, final NetworkPendingModel networkPendingModel) {
        long networkPendingId = 0;

        // -- Check existing --
        if (networkPendingModel.getCommandKey() != null) {
            Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT id " +
                "  FROM network_pending " +
                " WHERE type = ? " +
                "   AND command_key = ?" +
                "   AND project_member_id = ?" +
                "   AND is_sent = 0",
                new String[] {
                    String.valueOf(networkPendingModel.getType().getValue()),
                    networkPendingModel.getCommandKey(),
                    String.valueOf(networkPendingModel.getProjectMemberId())
                }
            );
            if (cursor.moveToFirst())
                networkPendingId = cursor.getLong(cursor.getColumnIndex("id"));
            cursor.close();
        }

        if (networkPendingModel.getCommand() != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("command", networkPendingModel.getCommand());
            if (networkPendingId > 0) {
                // -- Update old record --
                contentValues.put("updated_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                int affectedRow = sqLiteDatabase.update("network_pending", contentValues, "id = ?",
                    new String[] {
                        String.valueOf(networkPendingId)
                    }
                );
            } else {
                // -- Create new record --
                contentValues.put("type", networkPendingModel.getType().getValue());
                contentValues.put("command_key", networkPendingModel.getCommandKey());
                contentValues.put("is_sent", 0);
                contentValues.put("project_member_id", networkPendingModel.getProjectMemberId());
                contentValues.put("created_date", DateTimeUtil.ToDateTimeString(Calendar.getInstance()));
                networkPendingId = sqLiteDatabase.insertOrThrow("network_pending", null, contentValues);
            }
        }

        return networkPendingId;
    }

    protected boolean setNetworkPendingSent(final SQLiteDatabase sqLiteDatabase, final NetworkPendingModel networkPendingModel) {
        // -- Delete sent record --
        int affectedRow = sqLiteDatabase.delete("network_pending", "id = ?",
            new String[] {
                String.valueOf(networkPendingModel.getId())
            }
        );

        return (affectedRow > 0);
    }

    protected NetworkPendingModel getNetworkPendingModel(final SQLiteDatabase sqLiteDatabase, final long id) {
        NetworkPendingModel networkPendingModel = null;

        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT * " +
            "  FROM network_pending " +
            " WHERE id = ? ",
            new String[] {
                String.valueOf(id)
            }
        );
        if (cursor.moveToFirst()) {
            networkPendingModel = getNetworkPendingModel(cursor);
        }
        cursor.close();

        return networkPendingModel;
    }

    protected NetworkPendingModel[] getUnSentNetworkPendingModels(final SQLiteDatabase sqLiteDatabase, final Integer projectMemberId) {
        List<NetworkPendingModel> networkPendingModelList = new ArrayList<NetworkPendingModel>();

        Cursor cursor = sqLiteDatabase.rawQuery(
            "SELECT * " +
            "  FROM network_pending " +
            " WHERE is_sent = 0 " +
            "   AND project_member_id = ?",
            new String[] {
                String.valueOf(projectMemberId)
            }
        );
        if (cursor.moveToFirst()) {
            do {
                networkPendingModelList.add(getNetworkPendingModel(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        NetworkPendingModel[] networkPendingModels = new NetworkPendingModel[networkPendingModelList.size()];
        networkPendingModelList.toArray(networkPendingModels);
        return networkPendingModels;
    }

    private NetworkPendingModel getNetworkPendingModel(final Cursor cursor) {
        NetworkPendingModel networkPendingModel = new NetworkPendingModel();

        networkPendingModel.setId(cursor.getLong(cursor.getColumnIndex("id")));
        networkPendingModel.setType(NetworkPendingModel.ECommandType.fromInt(cursor.getInt(cursor.getColumnIndex("type"))));
        networkPendingModel.setCommandKey(cursor.getString(cursor.getColumnIndex("command_key")));
        networkPendingModel.setCommand(cursor.getString(cursor.getColumnIndex("command")));
        networkPendingModel.setSent(cursor.getInt(cursor.getColumnIndex("is_sent")) > 0);
        networkPendingModel.setProjectMemberId(cursor.getInt(cursor.getColumnIndex("project_member_id")));
        networkPendingModel.setCreatedDate(DateTimeUtil.FromDateTimeString(cursor.getString(cursor.getColumnIndex("created_date"))));
        networkPendingModel.setUpdatedDate(DateTimeUtil.FromDateTimeString(cursor.getString(cursor.getColumnIndex("updated_date"))));

        return networkPendingModel;
    }
}
