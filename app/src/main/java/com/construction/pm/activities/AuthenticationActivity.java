package com.construction.pm.activities;

import android.app.Activity;
import android.os.Bundle;

import com.construction.pm.views.system.AuthenticationLayout;

public class AuthenticationActivity extends Activity {

    protected AuthenticationLayout mAuthenticationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthenticationLayout = AuthenticationLayout.buildAuthenticationLayout(this, null);
        mAuthenticationLayout.setLoginListener(new AuthenticationLayout.LoginListener() {
            @Override
            public void onLoginRequest(String username, String password) {

            }
        });
        mAuthenticationLayout.setSettingListener(new AuthenticationLayout.SettingListener() {
            @Override
            public void onSettingRequest() {

            }
        });
        mAuthenticationLayout.loadLayoutToActivity(this);
    }

}
