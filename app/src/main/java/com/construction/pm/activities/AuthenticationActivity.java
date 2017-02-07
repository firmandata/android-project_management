package com.construction.pm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.activities.fragments.AuthenticationLoginFirstFragment;
import com.construction.pm.activities.fragments.AuthenticationLoginFragment;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.views.system.AuthenticationLayout;

public class AuthenticationActivity extends AppCompatActivity implements
        AuthenticationLoginFirstFragment.AuthenticationLoginFirstFragmentListener,
        AuthenticationLoginFragment.AuthenticationLoginFragmentListener {

    protected AuthenticationLayout mAuthenticationLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Prepare SessionPersistent --
        SessionPersistent sessionPersistent = new SessionPersistent(this);

        // -- Get SessionLoginModel from SessionPersistent --
        SessionLoginModel sessionLoginModel = sessionPersistent.getSessionLoginModel();

        // -- Prepare AuthenticationLayout --
        mAuthenticationLayout = AuthenticationLayout.buildAuthenticationLayout(this, null);

        // -- Load AuthenticationLayout to activity --
        mAuthenticationLayout.loadLayoutToActivity(this);

        // -- Show default view --
        if (sessionLoginModel != null) {
            if (sessionLoginModel.isFirstLogin()) {
                // -- Show AuthenticationLoginFirstFragment --
                AuthenticationLoginFirstFragment authenticationLoginFirstFragment = mAuthenticationLayout.showLoginFirst();
                authenticationLoginFirstFragment.setAuthenticationLoginFirstFragmentListener(this);
            } else {
                // -- Show AuthenticationLoginFragment --
                AuthenticationLoginFragment authenticationLoginFragment = mAuthenticationLayout.showLogin();
                authenticationLoginFragment.setAuthenticationLoginFragmentListener(this);
            }
        }
    }

    @Override
    public void onLoginSuccess(SessionLoginModel sessionLoginModel) {
        if (sessionLoginModel.isFirstLogin()) {
            // -- Show AuthenticationLoginFirstFragment --
            AuthenticationLoginFirstFragment authenticationLoginFirstFragment = mAuthenticationLayout.showLoginFirst();
            authenticationLoginFirstFragment.setAuthenticationLoginFirstFragmentListener(this);
        } else {
            // -- Redirect to MainActivity --
            Intent intent = new Intent(this, MainActivity.class);;
            startActivity(intent);

            // -- Close activity --
            finish();
        }
    }

    @Override
    public void onLoginFirstSuccess(SimpleResponseModel simpleResponseModel) {
        // -- Redirect to MainActivity --
        Intent intent = new Intent(this, MainActivity.class);;
        startActivity(intent);

        // -- Close activity --
        finish();
    }
}
