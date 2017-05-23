package com.construction.pm.views.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.AppDrawableCompatButton;
import com.construction.pm.utils.ButtonUtil;
import com.construction.pm.utils.ViewUtil;

public class AuthenticationLoginFirstView {
    protected Context mContext;

    protected RelativeLayout mAuthenticationLoginFirstView;
    protected ProgressDialog mProgressDialog;

    protected TextInputEditText mEtPasswordNew;
    protected TextInputEditText mEtPasswordNewConfirm;

    protected AuthenticationLoginFirstListener mAuthenticationLoginFirstListener;

    protected AuthenticationLoginFirstView(final Context context) {
        mContext = context;
    }

    public AuthenticationLoginFirstView(final Context context, final RelativeLayout authenticationFirstView) {
        this(context);

        initializeView(authenticationFirstView);
    }

    public static AuthenticationLoginFirstView buildAuthenticationLoginFirstView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationLoginFirstView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationLoginFirstView buildAuthenticationLoginFirstView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationLoginFirstView.buildAuthenticationLoginFirstView(context, R.layout.system_authentication_login_first_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationLoginFirstView) {
        mAuthenticationLoginFirstView = authenticationLoginFirstView;

        mEtPasswordNew = (TextInputEditText) mAuthenticationLoginFirstView.findViewById(R.id.passwordNew);
        mEtPasswordNewConfirm = (TextInputEditText) mAuthenticationLoginFirstView.findViewById(R.id.passwordNewConfirm);

        AppDrawableCompatButton btnFirstPassword = (AppDrawableCompatButton) mAuthenticationLoginFirstView.findViewById(R.id.newPasswordButton);
        btnFirstPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateAuthenticationLoginFirst();
                    if (mAuthenticationLoginFirstListener != null)
                        mAuthenticationLoginFirstListener.onLoginFirstRequest(getPasswordNew());
                } catch (Exception ex) {
                }
            }
        });

        ButtonUtil.setButtonPrimary(mContext, btnFirstPassword);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setPasswordNew(final String passwordNew) {
        mEtPasswordNew.setText(passwordNew);
    }

    public String getPasswordNew() {
        return mEtPasswordNew.getText().toString();
    }

    public void setPasswordNewConfirm(final String passwordNewConfirm) {
        mEtPasswordNewConfirm.setText(passwordNewConfirm);
    }

    public String getPasswordNewConfirm() {
        return mEtPasswordNewConfirm.getText().toString();
    }

    public void validateAuthenticationLoginFirst() throws Exception {
        mEtPasswordNew.setError(null);
        mEtPasswordNewConfirm.setError(null);

        String passwordNew = getPasswordNew();
        String passwordNewConfirm = getPasswordNewConfirm();

        if (TextUtils.isEmpty(passwordNew)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_password_new_required);

            mEtPasswordNew.setError(invalidError);
            mEtPasswordNew.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_password_new_required);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }

        if (!passwordNew.equals(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_password_new_confirm_not_match);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationLoginFirstView;
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
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_alert_title_error), errorMessage, R.drawable.ic_error_dark_24, null);
    }

    public void alertDialogFirstSuccess(final String successMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.system_authentication_login_first_view_alert_title_success), successMessage, R.drawable.ic_checked_user_dark_24, null);
    }

    public void setAuthenticationLoginFirstListener(final AuthenticationLoginFirstListener authenticationLoginFirstListener) {
        mAuthenticationLoginFirstListener = authenticationLoginFirstListener;
    }

    public interface AuthenticationLoginFirstListener {
        void onLoginFirstRequest(String passwordNew);
    }
}
