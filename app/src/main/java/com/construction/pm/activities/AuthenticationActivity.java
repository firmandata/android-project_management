package com.construction.pm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.construction.pm.MainApplication;
import com.construction.pm.fragments.AuthenticationFirstFragment;
import com.construction.pm.fragments.AuthenticationLoginFragment;
import com.construction.pm.models.network.SimpleResponseModel;
import com.construction.pm.models.network.UserProjectMemberModel;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;
import com.construction.pm.models.system.UserModel;
import com.construction.pm.persistence.SessionPersistent;
import com.construction.pm.persistence.SettingPersistent;
import com.construction.pm.views.system.AuthenticationLayout;

public class AuthenticationActivity extends AppCompatActivity implements
        AuthenticationFirstFragment.AuthenticationFirstFragmentListener,
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
                // -- Show AuthenticationFirstFragment --
                AuthenticationFirstFragment authenticationFirstFragment = mAuthenticationLayout.showFirstPassword();
                authenticationFirstFragment.setAuthenticationFirstFragmentListener(this);
            } else {
                // -- Show AuthenticationLoginFragment --
                AuthenticationLoginFragment authenticationLoginFragment = mAuthenticationLayout.showLogin();
                authenticationLoginFragment.setAuthenticationLoginFragmentListener(this);
            }
        }
    }

    @Override
    public void onLoggedIn(SessionLoginModel sessionLoginModel) {
        if (sessionLoginModel.isFirstLogin()) {
            // -- Show AuthenticationFirstFragment --
            AuthenticationFirstFragment authenticationFirstFragment = mAuthenticationLayout.showFirstPassword();
            authenticationFirstFragment.setAuthenticationFirstFragmentListener(this);
        } else {
            // -- Redirect to MainActivity --
            Intent intent = new Intent(this, MainActivity.class);;
            startActivity(intent);

            // -- Close activity --
            finish();
        }
    }

    @Override
    public void onFirstPasswordFinish(SimpleResponseModel simpleResponseModel) {
        // -- Redirect to MainActivity --
        Intent intent = new Intent(this, MainActivity.class);;
        startActivity(intent);

        // -- Close activity --
        finish();
    }
}
