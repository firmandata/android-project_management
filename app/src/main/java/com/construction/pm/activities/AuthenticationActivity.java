package com.construction.pm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.MainApplication;
import com.construction.pm.fragments.AuthenticationFirstFragment;
import com.construction.pm.fragments.AuthenticationLoginFragment;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.models.system.UserModel;
import com.construction.pm.views.system.AuthenticationLayout;

public class AuthenticationActivity extends AppCompatActivity implements
        AuthenticationFirstFragment.AuthenticationFirstFragmentListener,
        AuthenticationLoginFragment.AuthenticationLoginFragmentListener {

    protected SettingUserModel mSettingUserModel;
    protected SessionLoginModel mSessionLoginModel;

    protected AuthenticationLayout mAuthenticationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication mainApplication = (MainApplication) getApplicationContext();
        if (mainApplication != null) {
            mSettingUserModel = mainApplication.getSettingUserModel();
            mSessionLoginModel = mainApplication.getSessionLoginModel();
        }

        mAuthenticationLayout = AuthenticationLayout.buildAuthenticationLayout(this, null);
        mAuthenticationLayout.loadLayoutToActivity(this);

        boolean isFirstLogin = false;
        if (mSessionLoginModel != null) {
            UserProjectMemberModel userProjectMemberModel = mSessionLoginModel.getUserProjectMemberModel();
            if (userProjectMemberModel != null) {
                UserModel userModel = userProjectMemberModel.getUserModel();
                if (userModel != null) {
                    isFirstLogin = userModel.getFirstLogin();
                }
            }
        }

        if (isFirstLogin) {
            // -- Show first login --
            AuthenticationFirstFragment authenticationFirstFragment = mAuthenticationLayout.showFirstPassword();
            authenticationFirstFragment.setAuthenticationFirstFragmentListener(this);
        } else {
            // -- Show login --
            AuthenticationLoginFragment authenticationLoginFragment = mAuthenticationLayout.showLogin();
            authenticationLoginFragment.setAuthenticationLoginFragmentListener(this);
        }
    }

    @Override
    public void onLoggedIn(UserProjectMemberModel userProjectMemberModel) {
        UserModel userModel = userProjectMemberModel.getUserModel();
        if (userModel != null) {
            if (userModel.getFirstLogin()) {
                // -- Show first login --
                AuthenticationFirstFragment authenticationFirstFragment = mAuthenticationLayout.showFirstPassword();
                authenticationFirstFragment.setAuthenticationFirstFragmentListener(this);
            } else {
                // -- Redirect to MainActivity --
                Intent intent = new Intent(this, MainActivity.class);;
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onFirstPassword() {
        // -- Redirect to MainActivity --
        Intent intent = new Intent(this, MainActivity.class);;
        startActivity(intent);
        finish();
    }
}
