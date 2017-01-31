package com.construction.pm.views.system;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class AuthenticationLayout {

    protected Context mContext;

    protected RelativeLayout mAuthenticationLayout;

    protected AuthenticationView mAuthenticationView;
    protected LoginListener mLoginListener;
    protected SettingListener mSettingListener;

    protected AuthenticationLayout(final Context context) {
        mContext = context;
    }

    public AuthenticationLayout(final Context context, final RelativeLayout authenticationView) {
        this(context);

        initializeView(authenticationView);
    }

    public static AuthenticationLayout buildAuthenticationLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationLayout buildAuthenticationLayout(final Context context, final ViewGroup viewGroup) {
        return buildAuthenticationLayout(context, R.layout.system_authentication_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationLayout) {
        mAuthenticationLayout = authenticationLayout;
        mAuthenticationView = new AuthenticationView(mContext, (RelativeLayout) mAuthenticationLayout.findViewById(R.id.authenticationView));

        AppCompatButton btnLogin = (AppCompatButton) mAuthenticationLayout.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuthenticationView.validateLogin();
                    if (mLoginListener != null)
                        mLoginListener.onLoginRequest(mAuthenticationView.getUsername(), mAuthenticationView.getPassword());
                } catch (Exception ex) {

                }
            }
        });

        AppCompatButton btnSetting = (AppCompatButton) mAuthenticationLayout.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSettingListener != null)
                    mSettingListener.onSettingRequest();
            }
        });
    }

    public AuthenticationView getAuthenticationView() {
        return mAuthenticationView;
    }

    public RelativeLayout getView() {
        return mAuthenticationLayout;
    }

    public void setLoginListener(final LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public void setSettingListener(final SettingListener settingListener) {
        mSettingListener = settingListener;
    }

    public interface LoginListener {
        void onLoginRequest(String username, String password);
    }

    public interface SettingListener {
        void onSettingRequest();
    }
}
