package com.construction.pm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.utils.ConstantUtil;
import com.construction.pm.views.GalleryLayout;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryLayout.GalleryListener {

    protected List<AsyncTask> mAsyncTaskList;

    protected GalleryLayout mGalleryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle AsyncTask --
        mAsyncTaskList = new ArrayList<AsyncTask>();

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare GalleryLayout --
        mGalleryLayout = GalleryLayout.buildGalleryLayout(this, null);
        mGalleryLayout.setGalleryListener(this);

        // -- Load to Activity --
        mGalleryLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());

        onGalleryRequest();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // -- Handle intent request parameters --
        newIntentHandle(intent.getExtras());

        // -- Handle page request by parameters --
        requestPageHandle(intent.getExtras());
    }

    protected void newIntentHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onGalleryRequest() {

    }

    @Override
    public void onImageClick(String filePath) {
        Intent intent = new Intent();
        intent.putExtra(ConstantUtil.INTENT_RESULT_FILE_PATH, filePath);
        setResult(ConstantUtil.INTENT_REQUEST_GALLERY_ACTIVITY_RESULT_FILE, intent);

        finish();
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
