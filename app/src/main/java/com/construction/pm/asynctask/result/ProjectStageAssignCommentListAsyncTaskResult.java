package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectStageAssignCommentModel;

public class ProjectStageAssignCommentListAsyncTaskResult {

    protected ProjectStageAssignCommentModel[] mProjectStageAssignCommentModels;
    protected String mMessage;

    public ProjectStageAssignCommentListAsyncTaskResult() {

    }

    public void setProjectStageAssignCommentModels(final ProjectStageAssignCommentModel[] projectStageAssignCommentModels) {
        mProjectStageAssignCommentModels = projectStageAssignCommentModels;
    }

    public ProjectStageAssignCommentModel[] getProjectStageAssignCommentModels() {
        return mProjectStageAssignCommentModels;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}