package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.network.ProjectResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectPersistent extends NetworkCachePersistent {

    public ProjectPersistent(Context context) {
        super(context);
    }

    public long setProjectModels(final ProjectModel[] projectModels, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        // -- Get ProjectModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectModel projectModel : projectModels) {
                org.json.JSONObject jsonObject = projectModel.build();
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
                networkCacheId = saveNetworkCache(sqLiteDatabase, PersistentNetworkType.PROJECT_LIST, contentKey, content, projectMemberId);

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

    public ProjectModel[] getProjectModels(final Integer projectMemberId) throws PersistenceError {
        List<ProjectModel> projectModelList = new ArrayList<ProjectModel>();

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCache(sqLiteDatabase, PersistentNetworkType.PROJECT_LIST, contentKey, projectMemberId);

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

        // -- Generate ProjectModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    projectModelList.add(ProjectModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ProjectModel[] projectModels = new ProjectModel[projectModelList.size()];
        projectModelList.toArray(projectModels);
        return projectModels;
    }

    public long setProjectResponseModel(final ProjectResponseModel projectResponseModel, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = null;
        String content = null;

        // -- Get contentKey --
        if (projectResponseModel.getProjectModel() != null)
            contentKey = String.valueOf(projectResponseModel.getProjectModel().getProjectId());

        // -- Get ProjectResponseModel content --
        try {
            content = projectResponseModel.build().toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            SQLiteDatabase sqLiteDatabase = null;

            try {
                sqLiteDatabase = getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCache(sqLiteDatabase, PersistentNetworkType.PROJECT_DEPENDENCIES, contentKey, content, projectMemberId);

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

    public ProjectResponseModel getProjectResponseModel(final Integer projectId, final Integer projectMemberId) throws PersistenceError {
        ProjectResponseModel projectResponseModel = null;

        String contentKey = String.valueOf(projectId);
        String content = null;

        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCache(sqLiteDatabase, PersistentNetworkType.PROJECT_DEPENDENCIES, contentKey, projectMemberId);

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

        // -- Generate ProjectResponseModel from content
        if (content != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                projectResponseModel = ProjectResponseModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        return projectResponseModel;
    }
}
