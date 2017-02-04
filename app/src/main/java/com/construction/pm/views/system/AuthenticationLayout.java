package com.construction.pm.views.system;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.fragments.AuthenticationFirstFragment;
import com.construction.pm.fragments.AuthenticationLoginFragment;

public class AuthenticationLayout {

    protected Context mContext;

    protected AppCompatActivity mActivity;
    protected Handler mActivityHandler;
    protected static final String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    protected static final String FRAGMENT_FIRST = "FRAGMENT_FIRST";

    protected RelativeLayout mAuthenticationLayout;

    protected SettingListener mSettingListener;

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

        AppCompatButton btnSetting = (AppCompatButton) mAuthenticationLayout.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSettingListener != null)
                    mSettingListener.onSettingRequest();
            }
        });
    }

    public RelativeLayout getLayout() {
        return mAuthenticationLayout;
    }

    public void setSettingListener(final SettingListener settingListener) {
        mSettingListener = settingListener;
    }

    public interface SettingListener {
        void onSettingRequest();
    }

    public void loadLayoutToActivity(final AppCompatActivity activity) {
        mActivity = activity;
        mActivity.setContentView(mAuthenticationLayout);

        mActivityHandler = new Handler();
    }

    protected void loadFragment(final Fragment fragment, final String tag) {
        if (mActivityHandler == null)
            return;

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

    public AuthenticationFirstFragment showFirstPassword() {
        AuthenticationFirstFragment authenticationFirstFragment = AuthenticationFirstFragment.newInstance();

        loadFragment(authenticationFirstFragment, FRAGMENT_FIRST);

        return authenticationFirstFragment;
    }
}
