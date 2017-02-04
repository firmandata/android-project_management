package com.construction.pm.views.system;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.ViewUtil;

public class AuthenticationLoginView {

    protected Context mContext;

    protected RelativeLayout mAuthenticationLoginView;

    protected AppCompatEditText mEtLogin;
    protected AppCompatEditText mEtPassword;

    protected LoginListener mLoginListener;

    protected AuthenticationLoginView(final Context context) {
        mContext = context;
    }

    public AuthenticationLoginView(final Context context, final RelativeLayout authenticationLoginView) {
        this(context);

        initializeView(authenticationLoginView);
    }

    public static AuthenticationLoginView buildAuthenticationLoginView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationLoginView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationLoginView buildAuthenticationLoginView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationLoginView.buildAuthenticationLoginView(context, R.layout.system_authentication_login_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationLoginView) {
        mAuthenticationLoginView = authenticationLoginView;

        mEtLogin = (AppCompatEditText) mAuthenticationLoginView.findViewById(R.id.login);
        mEtPassword = (AppCompatEditText) mAuthenticationLoginView.findViewById(R.id.password);

        AppCompatButton btnLogin = (AppCompatButton) mAuthenticationLoginView.findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateLogin();
                    if (mLoginListener != null)
                        mLoginListener.onLoginRequest(getLogin(), getPassword());
                } catch (Exception ex) {
                }
            }
        });

    }

    public void setLogin(final String login) {
        mEtLogin.setText(login);
    }

    public String getLogin() {
        return mEtLogin.getText().toString();
    }

    public void setPassword(final String password) {
        mEtPassword.setText(password);
    }

    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    public void validateLogin() throws Exception {
        mEtLogin.setError(null);
        mEtPassword.setError(null);

        String username = getLogin();
        String password = getPassword();

        if (TextUtils.isEmpty(username)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_username_required);

            mEtLogin.setError(invalidError);
            mEtLogin.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(password)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_password_required);

            mEtPassword.setError(invalidError);
            mEtPassword.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationLoginView;
    }

    public void setLoginListener(final LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public interface LoginListener {
        void onLoginRequest(String login, String password);
    }
}
