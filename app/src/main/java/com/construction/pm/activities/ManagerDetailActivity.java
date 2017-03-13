package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityModel;
import com.construction.pm.models.ProjectActivityMonitoringModel;
import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.views.manager.ManagerDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailActivity extends AppCompatActivity implements
        ManagerDetailLayout.ManagerDetailLayoutListener {
    public static final String INTENT_PARAM_PROJECT_ACTIVITY_MODEL = "PROJECT_ACTIVITY_MODEL";

    protected ProjectActivityModel mProjectActivityModel;
    protected List<AsyncTask> mAsyncTaskList;

    protected ManagerDetailLayout mManagerDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ManagerDetailLayout --
        mManagerDetailLayout = ManagerDetailLayout.buildManagerDetailLayout(this, null);
        mManagerDetailLayout.setManagerDetailLayoutListener(this);

        // -- Load to Activity --
        mManagerDetailLayout.loadLayoutToActivity(this, mProjectActivityModel);
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
                if (!mManagerDetailLayout.isManagerFragmentShow())
                    mManagerDetailLayout.showManagerDetailFragment(mProjectActivityModel);
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onManagerDetailRequest(ProjectActivityModel projectActivityModel) {
        mManagerDetailLayout.showManagerDetailFragment(projectActivityModel);
    }

    @Override
    public void onProjectActivityUpdateListItemClick(ProjectActivityUpdateModel projectActivityUpdateModel) {

    }

    @Override
    public void onProjectActivityMonitoringListItemClick(ProjectActivityMonitoringModel projectActivityMonitoringModel) {
        mManagerDetailLayout.showProjectActivityMonitoringDetailFragment(projectActivityMonitoringModel);
    }

    @Override
    public void onBackPressed() {
        if (!mManagerDetailLayout.isManagerFragmentShow()) {
            mManagerDetailLayout.showManagerDetailFragment(mProjectActivityModel);
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
