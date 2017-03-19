package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.inspector.InspectorDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class InspectorDetailActivity extends AppCompatActivity implements
        InspectorDetailLayout.InspectorDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected ProjectActivityModel mProjectActivityModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected InspectorDetailLayout mInspectorDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare InspectorDetailLayout --
        mInspectorDetailLayout = InspectorDetailLayout.buildInspectorDetailLayout(this, null);
        mInspectorDetailLayout.setInspectorDetailLayoutListener(this);

        // -- Load to Activity --
        mInspectorDetailLayout.loadLayoutToActivity(this, mProjectActivityModel);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        if (bundle != null) {
            // -- Get ProjectActivityModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MODEL));
                mProjectActivityModel = ProjectActivityModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mInspectorDetailLayout.createProjectActivityMonitoringAddMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (!mInspectorDetailLayout.isProjectActivityMonitoringListFragmentShow())
                    mInspectorDetailLayout.showProjectActivityMonitoringListFragment(mProjectActivityModel);
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onInspectorDetailRequest(ProjectActivityModel projectActivityModel) {
        mInspectorDetailLayout.setLayoutData(projectActivityModel);
        mInspectorDetailLayout.showProjectActivityMonitoringListFragment(projectActivityModel);
    }

    @Override
    public void onProjectActivityMonitoringAddMenuClick() {
        // -- Redirect to ProjectActivityMonitoringFormActivity --
        Intent intent = new Intent(this, ProjectActivityMonitoringFormActivity.class);

        try {
            org.json.JSONObject projectActivityModelJsonObject = mProjectActivityModel.build();
            String projectActivityModelJson = projectActivityModelJsonObject.toString(0);
            intent.putExtra(ProjectActivityMonitoringFormActivity.INTENT_PARAM_PROJECT_ACTIVITY_MODEL, projectActivityModelJson);
        } catch (org.json.JSONException ex) {
        }

        startActivityForResult(intent, ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();

        if (requestCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM) {
            if (resultCode == ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_FORM_RESULT_SAVED) {
                if (bundle != null) {
                    if (bundle.containsKey(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL)) {
                        String projectActivityMonitoringModelJson = bundle.getString(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL);
                        if (projectActivityMonitoringModelJson != null) {
                            ProjectActivityMonitoringModel projectActivityMonitoringModel = null;
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityMonitoringModelJson);
                                projectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                            } catch (org.json.JSONException ex) {
                            }
                            if (projectActivityMonitoringModel != null)
                                mInspectorDetailLayout.addProjectActivityMonitoringModel(projectActivityMonitoringModel);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        // -- Redirect to ProjectActivityMonitoringDetailActivity --
        Intent intent = new Intent(this, ProjectActivityMonitoringDetailActivity.class);

        try {
            org.json.JSONObject projectActivityMonitoringModelJsonObject = projectActivityMonitoringModel.build();
            String projectActivityMonitoringModelJson = projectActivityMonitoringModelJsonObject.toString(0);

            intent.putExtra(ProjectActivityMonitoringDetailActivity.INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
        } catch (org.json.JSONException ex) {

        }

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (!mInspectorDetailLayout.isProjectActivityMonitoringListFragmentShow()) {
            mInspectorDetailLayout.showProjectActivityMonitoringListFragment(mProjectActivityModel);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
