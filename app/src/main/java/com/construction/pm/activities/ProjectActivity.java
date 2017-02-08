package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.models.ProjectModel;
import com.construction.pm.views.project.ProjectDetailLayout;

public class ProjectActivity extends AppCompatActivity {

    public static final String PARAM_PROJECT_MODEL = "ProjectModel";

    protected ProjectModel mProjectModel;

    protected ProjectDetailLayout mProjectDetailLayout;

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

        // -- Prepare ProjectDetailLayout --
        mProjectDetailLayout = ProjectDetailLayout.buildProjectDetailLayout(this, null);
        mProjectDetailLayout.setProjectModel(mProjectModel);

        // -- Load to Activity --
        mProjectDetailLayout.loadLayoutToActivity(this);
    }
}
