package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.models.ProjectModel;
import com.construction.pm.views.project.ProjectLayout;

public class ProjectActivity extends AppCompatActivity {

    public static final String PARAM_PROJECT_MODEL = "ProjectModel";

    protected ProjectModel mProjectModel;

    protected ProjectLayout mProjectLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Get parameters --
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            // -- Get ProjectModel parameter --
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(extra.getString(PARAM_PROJECT_MODEL));
                mProjectModel = ProjectModel.build(jsonObject);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Prepare ProjectLayout --
        mProjectLayout = ProjectLayout.buildProjectDetailLayout(this, null);
        mProjectLayout.setProjectModel(mProjectModel);

        // -- Load to Activity --
        mProjectLayout.loadLayoutToActivity(this);
    }
}
