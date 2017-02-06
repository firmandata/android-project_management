package com.construction.pm.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.construction.pm.R;
import com.construction.pm.views.MainLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected MainLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare MainLayout --
        mMainLayout = MainLayout.buildMainLayout(this, null);
        mMainLayout.getNavigationView().setNavigationItemSelectedListener(this);

        // -- Load MainLayout to activity --
        mMainLayout.loadLayoutToActivity(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigator_menu_inbox:
                break;
            case R.id.navigator_menu_project_task:
                break;
            case R.id.navigator_menu_monitoring:
                break;
            case R.id.navigator_menu_update_task_progress:
                break;
            case R.id.navigator_menu_request_report:
                break;
            case R.id.navigator_menu_profile:
                mMainLayout.showUserChangeProfile();
                break;
            case R.id.navigator_menu_change_password:
                mMainLayout.showUserChangePassword();
                break;
            case R.id.navigator_menu_logout:
                break;
        }

        if (menuItem.isCheckable()) {
            if (!menuItem.isChecked())
                menuItem.setChecked(true);
        }

        mMainLayout.getLayout().closeDrawers();

        return true;
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
