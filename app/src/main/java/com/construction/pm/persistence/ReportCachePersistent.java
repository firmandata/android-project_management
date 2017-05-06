package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.network.ProjectPlanResponseModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.network.ProjectStageResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ReportCachePersistent extends NetworkCachePersistent {

    public ReportCachePersistent(Context context) {
        super(context);
    }

    public long setReportRequestModels(final ReportRequestModel[] reportRequestModels, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        // -- Get ReportRequestModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ReportRequestModel reportRequestModel : reportRequestModels) {
                org.json.JSONObject jsonObject = reportRequestModel.build();
                jsonArray.put(jsonObject);
            }
            content = jsonArray.toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.REPORT_REQUEST_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ReportRequestModel[] getReportRequestModels(final Integer projectMemberId) throws PersistenceError {
        List<ReportRequestModel> reportRequestModelList = new ArrayList<ReportRequestModel>();

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.REPORT_REQUEST_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ReportRequestModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    reportRequestModelList.add(ReportRequestModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ReportRequestModel[] reportRequestModels = new ReportRequestModel[reportRequestModelList.size()];
        reportRequestModelList.toArray(reportRequestModels);
        return reportRequestModels;
    }
}
