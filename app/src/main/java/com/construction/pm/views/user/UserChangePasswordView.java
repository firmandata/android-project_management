package com.construction.pm.views.user;

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

public class UserChangePasswordView {
    protected Context mContext;

    protected RelativeLayout mUserChangePasswordView;
    protected ProgressDialog mProgressDialog;

    protected AppCompatEditText mEtPasswordOld;
    protected AppCompatEditText mEtPasswordNew;
    protected AppCompatEditText mEtPasswordNewConfirm;

    protected UserChangePasswordListener mUserUserChangePasswordListener;

    protected UserChangePasswordView(final Context context) {
        mContext = context;
    }

    public UserChangePasswordView(final Context context, final RelativeLayout authenticationChangeView) {
        this(context);

        initializeView(authenticationChangeView);
    }

    public static UserChangePasswordView buildAuthenticationChangePasswordView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new UserChangePasswordView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static UserChangePasswordView buildAuthenticationChangePasswordView(final Context context, final ViewGroup viewGroup) {
        return UserChangePasswordView.buildAuthenticationChangePasswordView(context, R.layout.user_change_password_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout userChangePasswordView) {
        mUserChangePasswordView = userChangePasswordView;

        mEtPasswordOld = (AppCompatEditText) mUserChangePasswordView.findViewById(R.id.passwordOld);
        mEtPasswordNew = (AppCompatEditText) mUserChangePasswordView.findViewById(R.id.passwordNew);
        mEtPasswordNewConfirm = (AppCompatEditText) mUserChangePasswordView.findViewById(R.id.passwordNewConfirm);

        AppCompatButton btnChangePassword = (AppCompatButton) mUserChangePasswordView.findViewById(R.id.changePasswordButton);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateUserChangePassword();
                    if (mUserUserChangePasswordListener != null)
                        mUserUserChangePasswordListener.onUserChangePasswordRequest(getPasswordOld(), getPasswordNew());
                } catch (Exception ex) {
                }
            }
        });

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setPasswordOld(final String passwordOld) {
        mEtPasswordOld.setText(passwordOld);
    }

    public String getPasswordOld() {
        return mEtPasswordOld.getText().toString();
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

    public void validateUserChangePassword() throws Exception {
        mEtPasswordOld.setError(null);
        mEtPasswordNew.setError(null);
        mEtPasswordNewConfirm.setError(null);

        String passwordOld = getPasswordOld();
        String passwordNew = getPasswordNew();
        String passwordNewConfirm = getPasswordNewConfirm();

        if (TextUtils.isEmpty(passwordOld)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_password_view_password_old_required);

            mEtPasswordOld.setError(invalidError);
            mEtPasswordOld.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNew)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_password_view_password_new_required);

            mEtPasswordNew.setError(invalidError);
            mEtPasswordNew.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_password_view_password_new_required);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }

        if (!passwordNew.equals(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_password_view_password_new_confirm_not_match);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mUserChangePasswordView;
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
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.user_change_password_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.user_change_password_view_alert_title_error), errorMessage, R.drawable.cancel_2_24, null);
    }

    public void alertDialogFirstSuccess(final String successMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.user_change_password_view_alert_title_success), successMessage, R.drawable.checked_user_24, null);
    }

    public void setUserChangePasswordListener(final UserChangePasswordListener userChangePasswordListener) {
        mUserUserChangePasswordListener = userChangePasswordListener;
    }

    public interface UserChangePasswordListener {
        void onUserChangePasswordRequest(String passwordOld, String passwordNew);
    }
}
