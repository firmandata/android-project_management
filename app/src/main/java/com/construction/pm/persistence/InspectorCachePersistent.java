package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.StatusTaskEnum;

import java.util.ArrayList;
import java.util.List;

public class InspectorCachePersistent extends NetworkCachePersistent {
    public InspectorCachePersistent(Context context) {
        super(context);
    }

    public long setProjectActivityModels(final ProjectActivityModel[] projectActivityModels, final Integer projectMemberId) throws PersistenceError {
        return setProjectActivityModels(projectActivityModels, null, projectMemberId);
    }

    public long setProjectActivityModels(final ProjectActivityModel[] projectActivityModels, final StatusTaskEnum statusTaskEnum, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        if (statusTaskEnum != null)
            contentKey += "_" + statusTaskEnum.getValue();
        String content = null;

        // -- Get ProjectActivityModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityModel projectActivityModel : projectActivityModels) {
                org.json.JSONObject jsonObject = projectActivityModel.build();
                jsonArray.put(jsonObject);
            }
            content = jsonArray.toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            SQLiteDatabase sqLiteDatabase = null;

            try {
                sqLiteDatabase = getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.INSPECTOR_PROJECT_ACTIVITY_LIST, contentKey, content, projectMemberId);

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
        }

        return networkCacheId;
    }

    public ProjectActivityModel[] getProjectActivityModels(final Integer projectMemberId) throws PersistenceError {
        return getProjectActivityModels(null, projectMemberId);
    }

    public ProjectActivityModel[] getProjectActivityModels(final StatusTaskEnum statusTaskEnum, final Integer projectMemberId) throws PersistenceError {
        List<ProjectActivityModel> projectActivityModelList = new ArrayList<ProjectActivityModel>();

        String contentKey = String.valueOf(projectMemberId);
        if (statusTaskEnum != null)
            contentKey += "_" + statusTaskEnum.getValue();
        String content = null;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.INSPECTOR_PROJECT_ACTIVITY_LIST, contentKey, projectMemberId);

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

        // -- Generate ProjectActivityModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    projectActivityModelList.add(ProjectActivityModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ProjectActivityModel[] projectActivityModels = new ProjectActivityModel[projectActivityModelList.size()];
        projectActivityModelList.toArray(projectActivityModels);
        return projectActivityModels;
    }
}
