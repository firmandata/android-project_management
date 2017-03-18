package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectStageAssignCommentModel;

public class ProjectStageAssignCommentSaveAsyncTaskResult {
    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;
    protected String mMessage;

    public ProjectStageAssignCommentSaveAsyncTaskResult() {

    }

    public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        mProjectStageAssignCommentModel = projectStageAssignCommentModel;
    }

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        return mProjectStageAssignCommentModel;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
