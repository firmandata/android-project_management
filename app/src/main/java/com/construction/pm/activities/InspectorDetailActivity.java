package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.views.inspector.InspectorDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class InspectorDetailActivity extends AppCompatActivity implements
        InspectorDetailLayout.InspectorDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected ProjectActivityModel mProjectActivityModel;
    protected List<AsyncTask> mAsyncTaskList;
    protected List<AsyncTask> mAsyncTaskPendingList;

    protected InspectorDetailLayout mInspectorDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();
        mAsyncTaskPendingList = new ArrayList<AsyncTask>();

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
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mInspectorDetailLayout.showProjectActivityMonitoringDetailFragment(projectActivityMonitoringModel);
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

        for (AsyncTask asyncTask : mAsyncTaskPendingList) {

        }
        mAsyncTaskPendingList.clear();
    }

    @Override
    protected void onPause() {
        for (AsyncTask asyncTask : mAsyncTaskPendingList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                asyncTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (AsyncTask asyncTask : mAsyncTaskList) {
            if (asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                asyncTask.cancel(true);
                mAsyncTaskPendingList.add(asyncTask);
            }
        }

        super.onDestroy();
    }
}
