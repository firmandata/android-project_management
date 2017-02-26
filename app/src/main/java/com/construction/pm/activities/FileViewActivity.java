package com.construction.pm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.construction.pm.views.file.FileLayout;

public class FileViewActivity extends AppCompatActivity  implements FileLayout.FileLayoutListener {

    public static final String INTENT_PARAM_FILE_ID = "FILE_ID";

    protected Integer mFileId;

    protected FileLayout mFileLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Handle intent request parameters --
        newIntentHandle(getIntent().getExtras());

        // -- Prepare FileLayout --
        mFileLayout = FileLayout.buildFileLayout(this, null);
        mFileLayout.setFileLayoutListener(this);

        // -- Load FileLayout to activity --
        mFileLayout.loadLayoutToActivity(this);

        // -- Handle page request by parameters --
        requestPageHandle(getIntent().getExtras());
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
            // -- Get FileId parameter --
            if (bundle.containsKey(INTENT_PARAM_FILE_ID))
                mFileId = bundle.getInt(INTENT_PARAM_FILE_ID);
        }
    }

    protected void requestPageHandle(final Bundle bundle) {
        // -- Get parameters --
        if (bundle != null) {

        }

        if (mFileId != null) {
            // -- Load FilePhotoViewFragment --
            mFileLayout.showFilePhotoViewFragment(mFileId);
        }
    }

    @Override
    public void onFilePhotoRequest(Integer fileId) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (mFileLayout.isFilePhotoFragmentShow()) {
                    finish();
                    return true;
                } else {
                    // -- Load FilePhotoViewFragment --
                    mFileLayout.showFilePhotoViewFragment(mFileId);
                }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (mFileLayout.isFilePhotoFragmentShow())
            super.onBackPressed();
        else {
            // -- Load FilePhotoViewFragment --
            mFileLayout.showFilePhotoViewFragment(mFileId);
        }
    }
}
