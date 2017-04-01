package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.models.ProjectStageAssignCommentModel;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_stage.ProjectStageAssignCommentDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectStageAssignCommentDetailActivity extends AppCompatActivity implements
        ProjectStageAssignCommentDetailLayout.ProjectStageAssignCommentDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL = "PROJECT_STAGE_ASSIGN_COMMENT_MODEL";

    protected ProjectStageAssignCommentModel mProjectStageAssignCommentModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectStageAssignCommentDetailLayout mProjectStageAssignCommentDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectStageAssignCommentDetailLayout --
        mProjectStageAssignCommentDetailLayout = ProjectStageAssignCommentDetailLayout.buildProjectStageAssignCommentDetailLayout(this, null);
        mProjectStageAssignCommentDetailLayout.setProjectStageAssignCommentDetailLayoutListener(this);

        // -- Load to Activity --
        mProjectStageAssignCommentDetailLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectStageAssignCommentModel parameter --
            String projectStageAssignCommentModelJson = bundle.getString(INTENT_PARAM_PROJECT_STAGE_ASSIGN_COMMENT_MODEL);
            if (projectStageAssignCommentModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectStageAssignCommentModelJson);
                    mProjectStageAssignCommentModel = ProjectStageAssignCommentModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        if (mProjectStageAssignCommentModel != null) {
            // -- Load ProjectStageAssignCommentDetailFragment --
            mProjectStageAssignCommentDetailLayout.showProjectStageAssignCommentDetailFragment(mProjectStageAssignCommentModel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mProjectStageAssignCommentDetailLayout.createProjectStageAssignCommentEditMenu(menu);
        return true;
    }

    @Override
    public void onProjectStageAssignCommentDetailEditClick() {
        String projectStageAssignCommentModelJson = null;
        if (mProjectStageAssignCommentModel != null) {
            try {
                org.json.JSONObject jsonObject = mProjectStageAssignCommentModel.build();
                projectStageAssignCommentModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }
        }

        Intent intent = new Intent();
        intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_STAGE_ASSIGN_COMMENT_MODEL, projectStageAssignCommentModelJson);
        setResult(ConstantUtil.INTENT_REQUEST_PROJECT_STAGE_ASSIGN_COMMENT_DETAIL_RESULT_EDIT, intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onDestroy();
    }
}
