package com.construction.pm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.views.home.HomeLayout;

public class HomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeLayout homeLayout = new HomeLayout(this, null);

        setContentView(homeLayout.getView());
    }
}
