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

public class AuthenticationChangeView {
    protected Context mContext;

    protected RelativeLayout mAuthenticationChangeView;

    protected AppCompatEditText mEtPasswordOld;
    protected AppCompatEditText mEtPasswordNew;
    protected AppCompatEditText mEtPasswordNewConfirm;

    protected ChangePasswordListener mChangePasswordListener;

    protected AuthenticationChangeView(final Context context) {
        mContext = context;
    }

    public AuthenticationChangeView(final Context context, final RelativeLayout authenticationChangeView) {
        this(context);

        initializeView(authenticationChangeView);
    }

    public static AuthenticationChangeView buildAuthenticationChangeView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationChangeView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationChangeView buildAuthenticationChangeView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationChangeView.buildAuthenticationChangeView(context, R.layout.system_authentication_change_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationChangeView) {
        mAuthenticationChangeView = authenticationChangeView;

        mEtPasswordOld = (AppCompatEditText) mAuthenticationChangeView.findViewById(R.id.passwordOld);
        mEtPasswordNew = (AppCompatEditText) mAuthenticationChangeView.findViewById(R.id.passwordNew);
        mEtPasswordNewConfirm = (AppCompatEditText) mAuthenticationChangeView.findViewById(R.id.passwordNewConfirm);

        AppCompatButton btnChangePassword = (AppCompatButton) mAuthenticationChangeView.findViewById(R.id.changePasswordButton);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateChangePassword();
                    if (mChangePasswordListener != null)
                        mChangePasswordListener.onChangePasswordRequest(getPasswordOld(), getPasswordNew());
                } catch (Exception ex) {
                }
            }
        });

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

    public void validateChangePassword() throws Exception {
        mEtPasswordOld.setError(null);
        mEtPasswordNew.setError(null);
        mEtPasswordNewConfirm.setError(null);

        String passwordOld = getPasswordOld();
        String passwordNew = getPasswordNew();
        String passwordNewConfirm = getPasswordNewConfirm();

        if (TextUtils.isEmpty(passwordOld)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_change_view_password_old_required);

            mEtPasswordOld.setError(invalidError);
            mEtPasswordOld.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNew)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_change_view_password_new_required);

            mEtPasswordNew.setError(invalidError);
            mEtPasswordNew.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_change_view_password_new_required);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }

        if (!passwordNew.equals(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_change_view_password_new_confirm_not_match);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationChangeView;
    }

    public void setChangePasswordListener(final ChangePasswordListener changePasswordListener) {
        mChangePasswordListener = changePasswordListener;
    }

    public interface ChangePasswordListener {
        void onChangePasswordRequest(String passwordOld, String passwordNew);
    }
}
