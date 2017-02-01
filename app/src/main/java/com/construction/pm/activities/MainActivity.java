package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.views.MainLayout;

public class MainActivity extends AppCompatActivity {

    protected MainLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainLayout = MainLayout.buildMainLayout(this, null);

        setSupportActionBar(mMainLayout.getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        setContentView(mMainLayout.getView());

        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mMainLayout.getView().openDrawer(GravityCompat.START);  // OPEN DRAWER
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMainLayout.getView().isDrawerOpen(GravityCompat.START)) {
            mMainLayout.getView().closeDrawers();
            return;
        }

        super.onBackPressed();
    }

}
