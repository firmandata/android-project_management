package com.construction.pm.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.models.ProjectActivityUpdateModel;
import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.project_activity.ProjectActivityUpdateDetailLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivityUpdateDetailActivity extends AppCompatActivity implements
        ProjectActivityUpdateDetailLayout.ProjectActivityUpdateDetailLayoutListener {

    public static final String INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL = "PROJECT_ACTIVITY_UPDATE_MODEL";
    public static final String INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE_EDIT = "SHOW_MENU_PROJECT_ACTIVITY_UPDATE_EDIT";

    protected ProjectActivityUpdateModel mProjectActivityUpdateModel;
    protected boolean mShowMenuProjectActivityUpdateEdit;
    protected List<AsyncTask> mAsyncTaskList;

    protected ProjectActivityUpdateDetailLayout mProjectActivityUpdateDetailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare ProjectActivityUpdateDetailLayout --
        mProjectActivityUpdateDetailLayout = ProjectActivityUpdateDetailLayout.buildProjectActivityUpdateDetailLayout(this, null);
        mProjectActivityUpdateDetailLayout.setProjectActivityUpdateDetailLayoutListener(this);

        // -- Load to Activity --
        mProjectActivityUpdateDetailLayout.loadLayoutToActivity(this);

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
            // -- Get ShowMenuProjectActivityUpdateEdit parameter --
            mShowMenuProjectActivityUpdateEdit = bundle.getBoolean(INTENT_PARAM_SHOW_MENU_PROJECT_ACTIVITY_UPDATE_EDIT);

            // -- Get ProjectActivityUpdateModel parameter --
            String projectActivityUpdateModelJson = bundle.getString(INTENT_PARAM_PROJECT_ACTIVITY_UPDATE_MODEL);
            if (projectActivityUpdateModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(projectActivityUpdateModelJson);
                    mProjectActivityUpdateModel = ProjectActivityUpdateModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        if (mProjectActivityUpdateModel != null) {
            // -- Load ProjectActivityUpdateDetailFragment --
            mProjectActivityUpdateDetailLayout.showProjectActivityUpdateDetailFragment(mProjectActivityUpdateModel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mShowMenuProjectActivityUpdateEdit)
            mProjectActivityUpdateDetailLayout.createProjectActivityUpdateEditMenu(menu);
        return true;
    }

    @Override
    public void onProjectActivityUpdateDetailEditClick() {
        String projectActivityUpdateModelJson = null;
        if (mProjectActivityUpdateModel != null) {
            try {
                org.json.JSONObject jsonObject = mProjectActivityUpdateModel.build();
                projectActivityUpdateModelJson = jsonObject.toString(0);
            } catch (org.json.JSONException e) {
            }
        }

        Intent intent = new Intent();
        intent.putExtra(ConstantUtil.INTENT_RESULT_PROJECT_ACTIVITY_UPDATE_MODEL, projectActivityUpdateModelJson);
        setResult(ConstantUtil.INTENT_REQUEST_PROJECT_ACTIVITY_UPDATE_DETAIL_RESULT_EDIT, intent);

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
