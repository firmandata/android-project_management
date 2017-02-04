package com.construction.pm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.views.system.AuthenticationFirstView;

public class AuthenticationFirstFragment extends Fragment implements AuthenticationFirstView.FirstPasswordListener {

    protected AuthenticationFirstView mAuthenticationFirstView;

    protected AuthenticationFirstFragmentListener mAuthenticationFirstFragmentListener;

    public static AuthenticationFirstFragment newInstance() {
        return new AuthenticationFirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthenticationFirstView = AuthenticationFirstView.buildAuthenticationFirstView(getContext(), null);
        mAuthenticationFirstView.setFirstPasswordListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mAuthenticationFirstView.getView();
    }

    @Override
    public void onFirstPasswordRequest(String passwordNew) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAuthenticationFirstFragmentListener(final AuthenticationFirstFragmentListener authenticationFirstFragmentListener) {
        mAuthenticationFirstFragmentListener = authenticationFirstFragmentListener;
    }

    public interface AuthenticationFirstFragmentListener {
        void onFirstPassword();
    }
}