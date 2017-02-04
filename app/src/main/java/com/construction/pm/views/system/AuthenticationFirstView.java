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

public class AuthenticationFirstView {
    protected Context mContext;

    protected RelativeLayout mAuthenticationChangeView;

    protected AppCompatEditText mEtPasswordNew;
    protected AppCompatEditText mEtPasswordNewConfirm;

    protected FirstPasswordListener mFirstPasswordListener;

    protected AuthenticationFirstView(final Context context) {
        mContext = context;
    }

    public AuthenticationFirstView(final Context context, final RelativeLayout authenticationFirstView) {
        this(context);

        initializeView(authenticationFirstView);
    }

    public static AuthenticationFirstView buildAuthenticationFirstView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationFirstView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationFirstView buildAuthenticationFirstView(final Context context, final ViewGroup viewGroup) {
        return AuthenticationFirstView.buildAuthenticationFirstView(context, R.layout.system_authentication_first_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationFirstView) {
        mAuthenticationChangeView = authenticationFirstView;

        mEtPasswordNew = (AppCompatEditText) mAuthenticationChangeView.findViewById(R.id.passwordNew);
        mEtPasswordNewConfirm = (AppCompatEditText) mAuthenticationChangeView.findViewById(R.id.passwordNewConfirm);

        AppCompatButton btnFirstPassword = (AppCompatButton) mAuthenticationChangeView.findViewById(R.id.newPasswordButton);
        btnFirstPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateFirstPassword();
                    if (mFirstPasswordListener != null)
                        mFirstPasswordListener.onFirstPasswordRequest(getPasswordNew());
                } catch (Exception ex) {
                }
            }
        });

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

    public void validateFirstPassword() throws Exception {
        mEtPasswordNew.setError(null);
        mEtPasswordNewConfirm.setError(null);

        String passwordNew = getPasswordNew();
        String passwordNewConfirm = getPasswordNewConfirm();

        if (TextUtils.isEmpty(passwordNew)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_first_view_password_new_required);

            mEtPasswordNew.setError(invalidError);
            mEtPasswordNew.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_first_view_password_new_required);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }

        if (!passwordNew.equals(passwordNewConfirm)) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.system_authentication_first_view_password_new_confirm_not_match);

            mEtPasswordNewConfirm.setError(invalidError);
            mEtPasswordNewConfirm.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mAuthenticationChangeView;
    }

    public void setFirstPasswordListener(final FirstPasswordListener firstPasswordListener) {
        mFirstPasswordListener = firstPasswordListener;
    }

    public interface FirstPasswordListener {
        void onFirstPasswordRequest(String passwordNew);
    }
}
