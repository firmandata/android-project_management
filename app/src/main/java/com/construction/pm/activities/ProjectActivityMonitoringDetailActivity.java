package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringDetailActivity extends AppCompatActivity implements
        ProjectActivityMonitoringDetailLayout.ProjectActivityMonitoringDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";

    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityMonitoringDetailLayout mProjectActivityMonitoringDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectActivityMonitoringDetailLayout --
        mProjectActivityMonitoringDetailLayout = ProjectActivityMonitoringDetailLayout.buildProjectActivityMonitoringDetailLayout(this, null);
        mProjectActivityMonitoringDetailLayout.setProjectActivityMonitoringDetailLayoutListener(this);

        // -- Load to Activity --
        mProjectActivityMonitoringDetailLayout.loadLayoutToActivity(this);

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
            // -- Get ProjectActivityMonitoringModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL));
                mProjectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        if (mProjectActivityMonitoringModel != null) {
            // -- Load ProjectActivityMonitoringDetailFragment --
            mProjectActivityMonitoringDetailLayout.showProjectActivityMonitoringDetailFragment(mProjectActivityMonitoringModel);
        }
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
