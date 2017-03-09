package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.models.StatusTaskEnum;

import java.util.ArrayList;
import java.util.List;

public class ManagerCachePersistent extends NetworkCachePersistent {
    public ManagerCachePersistent(Context context) {
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
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
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

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
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

    public long setProjectActivityMonitoringModels(final ProjectActivityMonitoringModel[] projectActivityMonitoringModels, final Integer projectActivityId, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        contentKey += "_" + String.valueOf(projectActivityId);

        String content = null;

        // -- Get ProjectActivityMonitoringModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityMonitoringModel projectActivityMonitoringModel : projectActivityMonitoringModels) {
                org.json.JSONObject jsonObject = projectActivityMonitoringModel.build();
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
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_MONITORING_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectActivityMonitoringModel[] getProjectActivityMonitoringModels(final Integer projectActivityId, final Integer projectMemberId) throws PersistenceError {
        List<ProjectActivityMonitoringModel> projectActivityMonitoringModelList = new ArrayList<ProjectActivityMonitoringModel>();

        String contentKey = String.valueOf(projectMemberId);
        contentKey += "_" + String.valueOf(projectActivityId);

        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_MONITORING_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectActivityMonitoringModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    projectActivityMonitoringModelList.add(ProjectActivityMonitoringModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ProjectActivityMonitoringModel[] projectActivityMonitoringModels = new ProjectActivityMonitoringModel[projectActivityMonitoringModelList.size()];
        projectActivityMonitoringModelList.toArray(projectActivityMonitoringModels);
        return projectActivityMonitoringModels;
    }

    public long setProjectActivityUpdateModels(final ProjectActivityUpdateModel[] projectActivityUpdateModels, final Integer projectActivityId, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        contentKey += "_" + String.valueOf(projectActivityId);

        String content = null;

        // -- Get ProjectActivityUpdateModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectActivityUpdateModel projectActivityUpdateModel : projectActivityUpdateModels) {
                org.json.JSONObject jsonObject = projectActivityUpdateModel.build();
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
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_UPDATE_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectActivityUpdateModel[] getProjectActivityUpdateModels(final Integer projectActivityId, final Integer projectMemberId) throws PersistenceError {
        List<ProjectActivityUpdateModel> projectActivityUpdateModelList = new ArrayList<ProjectActivityUpdateModel>();

        String contentKey = String.valueOf(projectMemberId);
        contentKey += "_" + String.valueOf(projectActivityId);

        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.MANAGER_PROJECT_ACTIVITY_UPDATE_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectActivityUpdateModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    projectActivityUpdateModelList.add(ProjectActivityUpdateModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ProjectActivityUpdateModel[] projectActivityUpdateModels = new ProjectActivityUpdateModel[projectActivityUpdateModelList.size()];
        projectActivityUpdateModelList.toArray(projectActivityUpdateModels);
        return projectActivityUpdateModels;
    }
}
