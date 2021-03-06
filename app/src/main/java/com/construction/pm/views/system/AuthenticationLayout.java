package com.construction.pm.views.system;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.activities.fragments.AuthenticationForgetPasswordFragment;
import com.construction.pm.activities.fragments.AuthenticationLoginFirstFragment;
import com.construction.pm.activities.fragments.AuthenticationLoginFragment;

public class AuthenticationLayout {

    protected Context mContext;

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected String mFragmentTagSelected;
    protected static final String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    protected static final String FRAGMENT_LOGIN_FIRST = "FRAGMENT_LOGIN_FIRST";
    protected static final String FRAGMENT_FORGET_PASSWORD = "FORGET_PASSWORD";

    protected RelativeLayout mAuthenticationLayout;

    protected AuthenticationLayout(final Context context) {
        mContext = context;
    }

    public AuthenticationLayout(final Context context, final RelativeLayout authenticationLayout) {
        this(context);

        initializeView(authenticationLayout);
    }

    public static AuthenticationLayout buildAuthenticationLayout(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new AuthenticationLayout(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static AuthenticationLayout buildAuthenticationLayout(final Context context, final ViewGroup viewGroup) {
        return buildAuthenticationLayout(context, R.layout.system_authentication_layout, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationLayout) {
        mAuthenticationLayout = authenticationLayout;
    }

    public RelativeLayout getLayout() {
        return mAuthenticationLayout;
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mActivity = activity;
        mActivity.setContentView(mAuthenticationLayout);

        mActivityHandler = new Handler();
    }

    public boolean isLoginFragmentShow() {
        if (mFragmentTagSelected != null)
            return mFragmentTagSelected.equals(FRAGMENT_LOGIN);
        return false;
    }

    public boolean isLoginFirstFragmentShow() {
        if (mFragmentTagSelected != null)
            return mFragmentTagSelected.equals(FRAGMENT_LOGIN_FIRST);
        return false;
    }

    protected void loadFragment(final Fragment fragment, final String tag) {
        if (mActivityHandler == null)
            return;
        if (mFragmentTagSelected != null) {
            if (mFragmentTagSelected.equals(tag))
                return;
        }

        mFragmentTagSelected = tag;

        mActivityHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mActivity == null)
                    return;

                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentBodyHolder, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

    public AuthenticationLoginFragment showLogin() {
        AuthenticationLoginFragment authenticationLoginFragment = AuthenticationLoginFragment.newInstance();

        loadFragment(authenticationLoginFragment, FRAGMENT_LOGIN);

        return authenticationLoginFragment;
    }

    public AuthenticationLoginFirstFragment showLoginFirst() {
        AuthenticationLoginFirstFragment authenticationLoginFirstFragment = AuthenticationLoginFirstFragment.newInstance();

        loadFragment(authenticationLoginFirstFragment, FRAGMENT_LOGIN_FIRST);

        return authenticationLoginFirstFragment;
    }

    public AuthenticationForgetPasswordFragment showForgetPassword() {
        AuthenticationForgetPasswordFragment authenticationForgetPasswordFragment = AuthenticationForgetPasswordFragment.newInstance();

        loadFragment(authenticationForgetPasswordFragment, FRAGMENT_FORGET_PASSWORD);

        return authenticationForgetPasswordFragment;
    }
}
