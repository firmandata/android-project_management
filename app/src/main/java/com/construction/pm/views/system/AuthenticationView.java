package com.construction.pm.views.system;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.ViewUtil;

public class AuthenticationView {

    protected Context mContext;

    protected RelativeLayout mAuthenticationView;

    protected AppCompatEditText mEtUsername;
    protected AppCompatEditText mEtPassword;

    protected AuthenticationView(final Context context) {
        mContext = context;
    }

    public AuthenticationView(final Context context, final RelativeLayout authenticationView) {
        this(context);

        initializeView(authenticationView);
    }

    public static AuthenticationView buildAuthenticationView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationView buildAuthenticationView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationView.buildAuthenticationView(context, R.layout.system_authentication_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationView) {
        mAuthenticationView = authenticationView;

        mEtUsername = (AppCompatEditText) mAuthenticationView.findViewById(R.id.userName);
        mEtPassword = (AppCompatEditText) mAuthenticationView.findViewById(R.id.password);
    }

    public void setUsername(final String username) {
        mEtUsername.setText(username);
    }

    public String getUsername() {
        return mEtUsername.getText().toString();
    }

    public void setPassword(final String password) {
        mEtPassword.setText(password);
    }

    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    public void validateLogin() throws Exception {
        String username = getUsername();
        mEtUsername.setError(null);
        if (TextUtils.isEmpty(username)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_view_username_required);

            mEtUsername.setError(invalidError);
            mEtUsername.requestFocus();

            throw new Exception(invalidError);
        }

        String password = getPassword();
        mEtPassword.setError(null);
        if (TextUtils.isEmpty(password)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_view_password_required);

            mEtPassword.setError(invalidError);
            mEtPassword.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationView;
    }
}
