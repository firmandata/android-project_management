package com.construction.pm.activities.fragmentdialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class PermissionConfirmationDialogFragment extends DialogFragment {
    private static final String PARAM_MESSAGE = "Message";
    private static final String PARAM_PERMISSIONS = "Permissions";
    private static final String PARAM_REQUEST_CODE = "RequestCode";
    private static final String PARAM_NOT_GRANTED_MESSAGE = "NotGrantedMessage";

    public static PermissionConfirmationDialogFragment newInstance(@StringRes int message, String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
        Bundle args = new Bundle();
        args.putInt(PARAM_MESSAGE, message);
        args.putStringArray(PARAM_PERMISSIONS, permissions);
        args.putInt(PARAM_REQUEST_CODE, requestCode);
        args.putInt(PARAM_NOT_GRANTED_MESSAGE, notGrantedMessage);

        PermissionConfirmationDialogFragment fragment = new PermissionConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        return new AlertDialog.Builder(getActivity())
                .setMessage(args.getInt(PARAM_MESSAGE))
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] permissions = args.getStringArray(PARAM_PERMISSIONS);
                                if (permissions == null) {
                                    throw new IllegalArgumentException();
                                }
                                ActivityCompat.requestPermissions(getActivity(),
                                        permissions, args.getInt(PARAM_REQUEST_CODE));
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),
                                        args.getInt(PARAM_NOT_GRANTED_MESSAGE),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .create();
    }
}
