package com.construction.pm.views.project_stage;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragmentdialogs.ProjectStageDocumentListDialogFragment;
import com.construction.pm.models.FileModel;
import com.construction.pm.models.ProjectStageAssignmentModel;
import com.construction.pm.models.ProjectStageDocumentModel;
import com.construction.pm.models.ProjectStageModel;

public class ProjectStageView implements
        ProjectStageDocumentListView.ProjectStageDocumentListListener,
        ProjectStageDocumentListDialogFragment.ProjectStageDocumentListListener {

    protected Context mContext;
    protected FragmentManager mFragmentManager;

    protected static final String FRAGMENT_TAG_PROJECT_STAGE_DOCUMENT_LIST = "FRAGMENT_PROJECT_STAGE_DOCUMENT_LIST";

    protected RelativeLayout mProjectStageView;

    protected ProgressDialog mProgressDialog;

    protected ProjectStageDetailView mProjectStageDetailView;
    protected ProjectStageAssignmentListView mProjectStageAssignmentListView;
    protected ProjectStageDocumentListView mProjectStageDocumentListView;

    protected ProjectStageViewListener mProjectStageViewListener;

    protected ProjectStageView(final Context context) {
        mContext = context;
    }

    public ProjectStageView(final Context context, final RelativeLayout projectStageView) {
        this(context);

        initializeView(projectStageView);
    }

    public static ProjectStageView buildProjectStageView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ProjectStageView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ProjectStageView buildProjectStageView(final Context context, final ViewGroup viewGroup) {
        return buildProjectStageView(context, R.layout.project_stage_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout projectView) {
        mProjectStageView = projectView;

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mProjectStageDetailView = new ProjectStageDetailView(mContext, (RelativeLayout) mProjectStageView.findViewById(R.id.project_stage_detail_view));
        mProjectStageAssignmentListView = new ProjectStageAssignmentListView(mContext, (RelativeLayout) mProjectStageView.findViewById(R.id.project_stage_assignment_list_view));
        mProjectStageDocumentListView = new ProjectStageDocumentListView(mContext, (RelativeLayout) mProjectStageView.findViewById(R.id.project_stage_document_list_view));
        mProjectStageDocumentListView.setProjectStageDocumentListListener(this);
    }

    @Override
    public void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel) {
        ProjectStageDocumentListDialogFragment projectStageDocumentListDialogFragment = ProjectStageDocumentListDialogFragment.newInstance(projectStageDocumentModel, this);
        if (mFragmentManager != null)
            projectStageDocumentListDialogFragment.show(mFragmentManager, FRAGMENT_TAG_PROJECT_STAGE_DOCUMENT_LIST);

        if (mProjectStageViewListener != null)
            mProjectStageViewListener.onProjectStageDocumentItemClick(projectStageDocumentModel);
    }

    @Override
    public void onProjectStageDocumentItemClick(FileModel fileModel) {
        if (mProjectStageViewListener != null)
            mProjectStageViewListener.onProjectStageDocumentItemClick(fileModel);
    }

    public RelativeLayout getView() {
        return mProjectStageView;
    }

    public void progressDialogShow(final String progressMessage, final boolean cancelable) {
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogShow(final String progressMessage) {
        progressDialogShow(progressMessage, false);
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog.setCancelable(false);
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentManager = fragment.getChildFragmentManager();
    }

    public void setLayoutData(final ProjectStageModel projectStageModel, final ProjectStageAssignmentModel[] projectStageAssignmentModels, final ProjectStageDocumentModel[] projectStageDocumentModels) {
        mProjectStageDetailView.setProjectStageModel(projectStageModel);
        mProjectStageAssignmentListView.setProjectStageAssignmentModels(projectStageAssignmentModels);
        mProjectStageDocumentListView.setProjectStageDocumentModels(projectStageDocumentModels);
    }

    public ProjectStageModel getProjectStageModel() {
        return mProjectStageDetailView.getProjectStageModel();
    }

    public ProjectStageAssignmentModel[] getProjectStageAssignmentModels() {
        return mProjectStageAssignmentListView.getProjectStageAssignmentModels();
    }

    public ProjectStageDocumentModel[] getProjectStageDocumentModels() {
        return mProjectStageDocumentListView.getProjectStageDocumentModels();
    }

    public void setProjectStageViewListener(final ProjectStageViewListener projectStageViewListener) {
        mProjectStageViewListener = projectStageViewListener;
    }

    public interface ProjectStageViewListener {
        void onProjectStageRequest(ProjectStageModel projectStageModel);
        void onProjectStageDocumentItemClick(ProjectStageDocumentModel projectStageDocumentModel);
        void onProjectStageDocumentItemClick(FileModel fileModel);
    }
}
