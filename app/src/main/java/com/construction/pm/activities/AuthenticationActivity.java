package com.construction.pm.activities;

import android.app.Activity;
import android.os.Bundle;

import com.construction.pm.views.system.AuthenticationLayout;

public class AuthenticationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthenticationLayout authenticationLayout = AuthenticationLayout.buildAuthenticationLayout(this, null);
        authenticationLayout.setLoginListener(new AuthenticationLayout.LoginListener() {
            @Override
            public void onLoginRequest(String username, String password) {

            }
        });
        authenticationLayout.setSettingListener(new AuthenticationLayout.SettingListener() {
            @Override
            public void onSettingRequest() {

            }
        });
        setContentView(authenticationLayout.getView());
    }

}
