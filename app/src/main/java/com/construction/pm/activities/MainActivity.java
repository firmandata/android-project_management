package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.views.MainLayout;

public class MainActivity extends AppCompatActivity {

    protected MainLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainLayout = MainLayout.buildMainLayout(this, null);
        mMainLayout.loadLayoutToActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMainLayout.getLayout().isDrawerOpen(GravityCompat.START)) {
            mMainLayout.getLayout().closeDrawers();
            return;
        }

        super.onBackPressed();
    }
}
