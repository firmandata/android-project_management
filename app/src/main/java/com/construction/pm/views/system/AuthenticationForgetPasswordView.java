package com.construction.pm.views.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.utils.ViewUtil;

public class AuthenticationForgetPasswordView {

    protected Context mContext;

    protected RelativeLayout mAuthenticationForgetPasswordView;
    protected ProgressDialog mProgressDialog;

    protected AppCompatEditText mEtLogin;

    protected ForgetPasswordListener mForgetPasswordListener;

    protected AuthenticationForgetPasswordView(final Context context) {
        mContext = context;
    }

    public AuthenticationForgetPasswordView(final Context context, final RelativeLayout authenticationForgetPasswordView) {
        this(context);

        initializeView(authenticationForgetPasswordView);
    }

    public static AuthenticationForgetPasswordView buildAuthenticationForgetPasswordView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationForgetPasswordView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationForgetPasswordView buildAuthenticationForgetPasswordView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationForgetPasswordView.buildAuthenticationForgetPasswordView(context, R.layout.system_authentication_forget_password_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationLoginView) {
        mAuthenticationForgetPasswordView = authenticationLoginView;

        mEtLogin = (AppCompatEditText) mAuthenticationForgetPasswordView.findViewById(R.id.login);

        AppCompatButton btnReset = (AppCompatButton) mAuthenticationForgetPasswordView.findViewById(R.id.resetButton);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateForgetPassword();
                    if (mForgetPasswordListener != null)
                        mForgetPasswordListener.onForgetPasswordRequest(getLogin());
                } catch (Exception ex) {
                }
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

    public void validateForgetPassword() throws Exception {
        mEtLogin.setError(null);

        String username = getLogin();

        if (TextUtils.isEmpty(username)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_forget_password_view_login_required);

            mEtLogin.setError(invalidError);
            mEtLogin.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationForgetPasswordView;
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
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.system_authentication_forget_password_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogSuccessShow(final String successMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_forget_password_view_alert_title_success), successMessage, R.drawable.checkmark_24, null);
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_forget_password_view_alert_title_error), errorMessage, R.drawable.cancel_2_24, null);
    }

    public void setForgetPasswordListener(final ForgetPasswordListener forgetPasswordListener) {
        mForgetPasswordListener = forgetPasswordListener;
    }

    public interface ForgetPasswordListener {
        void onForgetPasswordRequest(String login);
    }
}
