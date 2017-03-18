package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_activity.ProjectActivityMonitoringDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityMonitoringDetailActivity extends AppCompatActivity implements
        ProjectActivityMonitoringDetailLayout.ProjectActivityMonitoringDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL = "PROJECT_ACTIVITY_MONITORING_MODEL";
    public static final String INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE = "SHOW_MENU_PROJECT_ACTIVITY_UPDATE";

    protected ProjectActivityMonitoringModel mProjectActivityMonitoringModel;
    protected boolean mShowMenuProjectActivityUpdate;
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
            // -- Get ShowMenuProjectActivityUpdate parameter --
            mShowMenuProjectActivityUpdate = bundle.getBoolean(INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE);

            // -- Get ProjectActivityMonitoringModel parameter --
            String projectActivityMonitoringModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_MONITORING_MODEL);
            if (projectActivityMonitoringModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityMonitoringModelJson);
                    mProjectActivityMonitoringModel = ProjectActivityMonitoringModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mShowMenuProjectActivityUpdate)
            mProjectActivityMonitoringDetailLayout.createProjectActivityUpdateMenu(menu);
        return true;
    }

    @Override
    public void onProjectActivityMonitoringDetailActivityUpdateClick() {
        String projectActivityMonitoringModelJson = null;
        if (mProjectActivityMonitoringModel != null) {
            try {
                org.json.JSONObject jsonObject = mProjectActivityMonitoringModel.build();
                projectActivityMonitoringModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }
        }

        Intent intent = new Intent();
        intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_MONITORING_MODEL, projectActivityMonitoringModelJson);
        setResult(ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_MONITORING_DETAIL_RESULT_UPDATE, intent);

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
