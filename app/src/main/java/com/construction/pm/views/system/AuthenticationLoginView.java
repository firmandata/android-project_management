package com.construction.pm.views.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.AppDrawableCompatButton;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.ViewUtil;

public class AuthenticationLoginView {

    protected Context mContext;

    protected RelativeLayout mAuthenticationLoginView;
    protected ProgressDialog mProgressDialog;

    protected TextInputEditText mEtLogin;
    protected TextInputEditText mEtPassword;

    protected LoginListener mLoginListener;
    protected LoginForgetPasswordListener mLoginForgetPasswordListener;

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

        mEtLogin = (TextInputEditText) mAuthenticationLoginView.findViewById(R.id.login);
        mEtPassword = (TextInputEditText) mAuthenticationLoginView.findViewById(R.id.password);

        AppDrawableCompatButton btnLogin = (AppDrawableCompatButton) mAuthenticationLoginView.findViewById(R.id.loginButton);
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

        ButtonUtil.setButtonPrimary(mContext, btnLogin);

        AppCompatTextView btnForgetPassword = (AppCompatTextView) mAuthenticationLoginView.findViewById(R.id.forgetPasswordButton);
        ViewUtil.setTextViewHyperlink(btnForgetPassword);
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginForgetPasswordListener != null)
                    mLoginForgetPasswordListener.onLoginForgetPasswordRequest();
            }
        });

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
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
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_login_required);

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

    public void progressDialogShow(final String progressMessage) {
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void alertDialogShow(final String alertTitle, final String alertMessage, final int iconId, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setIcon(iconId);
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_alert_title_error), errorMessage, R.drawable.ic_error_dark_24, null);
    }

    public void alertDialogFirstLoginShow() {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_alert_first_login_title), ViewUtil.getResourceString(mContext, R.string.system_authentication_login_view_alert_first_login_message), R.drawable.ic_checked_user_dark_24, null);
    }

    public void setLoginListener(final LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public interface LoginListener {
        void onLoginRequest(String login, String password);
    }

    public void setLoginForgetPasswordListener(final LoginForgetPasswordListener loginForgetPasswordListener) {
        mLoginForgetPasswordListener = loginForgetPasswordListener;
    }

    public interface LoginForgetPasswordListener {
        void onLoginForgetPasswordRequest();
    }
}
