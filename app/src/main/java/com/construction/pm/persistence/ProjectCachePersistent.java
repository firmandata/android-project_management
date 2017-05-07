package com.construction.pm.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.construction.pm.models.ProjectActivityDashboardModel;
import com.construction.pm.models.ProjectModel;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.network.ProjectActivityDashboardResponseModel;
import com.construction.pm.models.network.ProjectPlanResponseModel;
import com.construction.pm.models.network.ProjectResponseModel;
import com.construction.pm.models.network.ProjectStageResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectCachePersistent extends NetworkCachePersistent {

    public ProjectCachePersistent(Context context) {
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
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectModel[] getProjectModels(final Integer projectMemberId) throws PersistenceError {
        List<ProjectModel> projectModelList = new ArrayList<ProjectModel>();

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
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
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_DEPENDENCIES, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectResponseModel getProjectResponseModel(final Integer projectId, final Integer projectMemberId) throws PersistenceError {
        ProjectResponseModel projectResponseModel = null;

        String contentKey = String.valueOf(projectId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_DEPENDENCIES, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
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

    public long setProjectStageResponseModel(final ProjectStageResponseModel projectStageResponseModel, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = null;
        String content = null;

        // -- Get contentKey --
        if (projectStageResponseModel.getProjectStageModel() != null)
            contentKey = String.valueOf(projectStageResponseModel.getProjectStageModel().getProjectStageId());

        // -- Get ProjectStageResponseModel content --
        try {
            content = projectStageResponseModel.build().toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_STAGE_DEPENDENCIES, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectStageResponseModel getProjectStageResponseModel(final Integer projectStageId, final Integer projectMemberId) throws PersistenceError {
        ProjectStageResponseModel projectStageResponseModel = null;

        String contentKey = String.valueOf(projectStageId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_STAGE_DEPENDENCIES, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectStageResponseModel from content
        if (content != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                projectStageResponseModel = ProjectStageResponseModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        return projectStageResponseModel;
    }

    public long setProjectStageAssignCommentModels(final Integer projectStageId, final ProjectStageAssignCommentModel[] projectStageAssignCommentModels, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = null;
        String content = null;

        // -- Get contentKey --
        if (projectMemberId != null && projectStageId != null)
            contentKey = String.valueOf(projectMemberId) + "_" + String.valueOf(projectStageId);

        // -- Get ProjectStageAssignCommentModels content --
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (ProjectStageAssignCommentModel projectStageAssignCommentModel : projectStageAssignCommentModels) {
                org.json.JSONObject jsonObject = projectStageAssignCommentModel.build();
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
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_STAGE_ASSIGN_COMMENT_LIST, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels(final Integer projectStageId, final Integer projectMemberId) throws PersistenceError {
        List<ProjectStageAssignCommentModel> projectStageAssignCommentModelList = new ArrayList<ProjectStageAssignCommentModel>();

        String contentKey = null;
        String content = null;

        if (projectMemberId != null && projectStageId != null)
            contentKey = String.valueOf(projectMemberId) + "_" + String.valueOf(projectStageId);

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_STAGE_ASSIGN_COMMENT_LIST, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectStageAssignCommentModels from content
        if (content != null) {
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
                for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(jsonArrayIdx);
                    projectStageAssignCommentModelList.add(ProjectStageAssignCommentModel.build(jsonObject));
                }
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        ProjectStageAssignCommentModel[] projectStageAssignCommentModels = new ProjectStageAssignCommentModel[projectStageAssignCommentModelList.size()];
        projectStageAssignCommentModelList.toArray(projectStageAssignCommentModels);
        return projectStageAssignCommentModels;
    }

    public long setProjectPlanResponseModel(final ProjectPlanResponseModel projectPlanResponseModel, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = null;
        String content = null;

        // -- Get contentKey --
        if (projectPlanResponseModel.getProjectPlanModel() != null)
            contentKey = String.valueOf(projectPlanResponseModel.getProjectPlanModel().getProjectPlanId());

        // -- Get ProjectPlanResponseModel content --
        try {
            content = projectPlanResponseModel.build().toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_PLAN_DEPENDENCIES, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectPlanResponseModel getProjectPlanResponseModel(final Integer projectPlanId, final Integer projectMemberId) throws PersistenceError {
        ProjectPlanResponseModel projectPlanResponseModel = null;

        String contentKey = String.valueOf(projectPlanId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_PLAN_DEPENDENCIES, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectPlanResponseModel from content
        if (content != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                projectPlanResponseModel = ProjectPlanResponseModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        return projectPlanResponseModel;
    }

    public long setProjectActivityDashboardResponseModel(final ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel, final Integer projectMemberId) throws PersistenceError {
        long networkCacheId = 0;

        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        // -- Get ProjectActivityDashboardResponseModel content --
        try {
            org.json.JSONObject jsonObject = projectActivityDashboardResponseModel.build();
            content = jsonObject.toString(0);
        } catch (org.json.JSONException ex) {
        } catch (Exception ex) {
        }

        if (content != null) {
            try {
                SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getWritableDatabase();

                // -- Save content to cache --
                networkCacheId = saveNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_ACTIVITY_DASHBOARD, contentKey, content, projectMemberId);
            } catch (SQLException ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PersistenceError(0, ex.getMessage(), ex);
            }
        }

        return networkCacheId;
    }

    public ProjectActivityDashboardResponseModel getProjectActivityDashboardResponseModel(final Integer projectMemberId) throws PersistenceError {
        String contentKey = String.valueOf(projectMemberId);
        String content = null;

        try {
            SQLiteDatabase sqLiteDatabase = mSQLitePersistent.getReadableDatabase();

            // -- Get content from cache --
            content = getNetworkCacheContent(sqLiteDatabase, NetworkCachePersistentType.PROJECT_ACTIVITY_DASHBOARD, contentKey, projectMemberId);
        } catch (SQLException ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new PersistenceError(0, ex.getMessage(), ex);
        }

        // -- Generate ProjectActivityDashboardResponseModel from content --
        ProjectActivityDashboardResponseModel projectActivityDashboardResponseModel = null;
        if (content != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                projectActivityDashboardResponseModel = ProjectActivityDashboardResponseModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            } catch (Exception ex) {
            }
        }

        return projectActivityDashboardResponseModel;
    }
}
