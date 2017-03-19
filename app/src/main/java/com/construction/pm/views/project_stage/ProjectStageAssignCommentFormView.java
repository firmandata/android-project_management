package com.construction.pm.views.project_stage;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.models.ProjectStageAssignmentModel;

public class ProjectStageAssignCommentFormView {
    protected Context mContext;

    protected RelativeLayout mProjectStageAssignCommentFormView;

    protected AppCompatEditText mComment;

    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;

    public ProjectStageAssignCommentFormView(final Context context) {
        mContext = context;
    }

    public ProjectStageAssignCommentFormView(final Context context, final RelativeLayout projectStageAssignCommentFormView) {
        this(context);

        initializeView(projectStageAssignCommentFormView);
    }

    public static ProjectStageAssignCommentFormView buildProjectStageAssignCommentFormView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageAssignCommentFormView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageAssignCommentFormView buildProjectStageAssignCommentFormView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageAssignCommentFormView(context, R.layout.project_stage_assign_comment_form_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectActivityDetailView) {
        mProjectStageAssignCommentFormView = projectActivityDetailView;

        mComment = (AppCompatEditText) mProjectStageAssignCommentFormView.findViewById(R.id.comment);
    }

    public RelativeLayout getView() {
        return mProjectStageAssignCommentFormView;
    }

    public void setProjectStageAssignmentModel(final ProjectStageAssignmentModel projectStageAssignmentModel) {
        if (projectStageAssignmentModel == null)
            return;

        mProjectStageAssignCommentModel = new ProjectStageAssignCommentModel();
        mProjectStageAssignCommentModel.setProjectStageAssignmentId(projectStageAssignmentModel.getProjectStageAssignmentId());
    }

    public void setProjectStageAssignCommentModel(final ProjectStageAssignCommentModel projectStageAssignCommentModel) {
        if (projectStageAssignCommentModel == null)
            return;

        mProjectStageAssignCommentModel = projectStageAssignCommentModel.duplicate();

        mComment.setText(projectStageAssignCommentModel.getComment());
    }

    public ProjectStageAssignCommentModel getProjectStageAssignCommentModel() {
        if (mProjectStageAssignCommentModel == null)
            mProjectStageAssignCommentModel = new ProjectStageAssignCommentModel();

        mProjectStageAssignCommentModel.setComment(mComment.getText().toString());

        return mProjectStageAssignCommentModel;
    }
}
