package com.construction.pm.asynctask.result;

import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;

public class ProjectStageGetAsyncTaskResult {

    protected ProjectStageModel mProjectStageModel;
    protected ProjectStageAssignmentModel[] mProjectStageAssignmentModels;
    protected ProjectStageDocumentModel[] mProjectStageDocumentModels;
    protected ProjectStageAssignCommentModel[] mProjectStageAssignCommentModels;
    protected String mMessage;

    public ProjectStageGetAsyncTaskResult() {

    }

    public void setProjectStageModel(final ProjectStageModel projectStageModel) {
        mProjectStageModel = projectStageModel;
    }

    public ProjectStageModel getProjectStageModel() {
        return mProjectStageModel;
    }

    public void setProjectStageAssignmentModels(final ProjectStageAssignmentModel[] projectStageAssignmentModels) {
        mProjectStageAssignmentModels = projectStageAssignmentModels;
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        return mProjectStageAssignmentModels;
    }

    public void setProjectStageDocumentModels(final ProjectStageDocumentModel[] projectStageDocumentModels) {
        mProjectStageDocumentModels = projectStageDocumentModels;
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        return mProjectStageDocumentModels;
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