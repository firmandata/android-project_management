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
import com.construction.pm.models.ProjectMemberModel;
import com.construction.pm.utils.ViewUtil;

public class UserChangeProfileView {
    protected Context mContext;

    protected RelativeLayout mUserChangeProfileView;
    protected ProgressDialog mProgressDialog;

    protected ProjectMemberModel mProjectMemberModel;
    protected AppCompatEditText mEtEmail;
    protected AppCompatEditText mEtMemberName;
    protected AppCompatEditText mEtPhoneNumber;
    protected AppCompatEditText mEtDescription;

    protected UserChangeProfileListener mUserChangeProfileListener;

    protected UserChangeProfileView(final Context context) {
        mContext = context;
    }

    public UserChangeProfileView(final Context context, final RelativeLayout authenticationChangeView) {
        this(context);

        initializeView(authenticationChangeView);
    }

    public static UserChangeProfileView buildUserChangeProfileView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new UserChangeProfileView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static UserChangeProfileView buildUserChangeProfileView(final Context context, final ViewGroup viewGroup) {
        return UserChangeProfileView.buildUserChangeProfileView(context, R.layout.user_change_profile_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout authenticationChangeView) {
        mUserChangeProfileView = authenticationChangeView;

        mEtEmail = (AppCompatEditText) mUserChangeProfileView.findViewById(R.id.email);
        mEtMemberName = (AppCompatEditText) mUserChangeProfileView.findViewById(R.id.memberName);
        mEtPhoneNumber = (AppCompatEditText) mUserChangeProfileView.findViewById(R.id.phoneNumber);
        mEtDescription = (AppCompatEditText) mUserChangeProfileView.findViewById(R.id.description);

        AppCompatButton btnChangeProfile = (AppCompatButton) mUserChangeProfileView.findViewById(R.id.changeProfileButton);
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateUserChangeProfile();
                    if (mUserChangeProfileListener != null)
                        mUserChangeProfileListener.onUserChangeProfileRequest(getProjectMemberModel());
                } catch (Exception ex) {
                }
            }
        });

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setProjectMemberModel(final ProjectMemberModel projectMemberModel) {
        mProjectMemberModel = projectMemberModel;

        mEtEmail.setText(mProjectMemberModel.getEmail());
        mEtMemberName.setText(mProjectMemberModel.getMemberName());
        mEtPhoneNumber.setText(mProjectMemberModel.getPhoneNumber());
        mEtDescription.setText(mProjectMemberModel.getDescription());
    }

    public ProjectMemberModel getProjectMemberModel() {
        if (mProjectMemberModel == null)
            mProjectMemberModel = new ProjectMemberModel();

        mProjectMemberModel.setMemberName(mEtMemberName.getText().toString());
        mProjectMemberModel.setPhoneNumber(mEtPhoneNumber.getText().toString());
        mProjectMemberModel.setDescription(mEtDescription.getText().toString());

        return mProjectMemberModel;
    }

    public void validateUserChangeProfile() throws Exception {
        mEtMemberName.setError(null);
        mEtPhoneNumber.setError(null);
        mEtDescription.setError(null);

        ProjectMemberModel projectMemberModel = getProjectMemberModel();

        if (TextUtils.isEmpty(projectMemberModel.getMemberName())) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_profile_view_name_required);

            mEtMemberName.setError(invalidError);
            mEtMemberName.requestFocus();

            throw new Exception(invalidError);
        }

        if (TextUtils.isEmpty(projectMemberModel.getPhoneNumber())) {
            String invalidError = ViewUtil.getResourceString(mContext, R.string.user_change_profile_view_phone_number_required);

            mEtPhoneNumber.setError(invalidError);
            mEtPhoneNumber.requestFocus();

            throw new Exception(invalidError);
        }
    }

    public RelativeLayout getView() {
        return mUserChangeProfileView;
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
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.user_change_profile_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.user_change_profile_view_alert_title_error), errorMessage, R.drawable.cancel_2_24, null);
    }

    public void alertDialogFirstSuccess(final String successMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.user_change_profile_view_alert_title_success), successMessage, R.drawable.checked_user_24, null);
    }

    public void setUserChangeProfileListener(final UserChangeProfileListener userChangeProfileListener) {
        mUserChangeProfileListener = userChangeProfileListener;
    }

    public interface UserChangeProfileListener {
        void onUserChangeProfileRequest(ProjectMemberModel projectMemberModel);
    }
}
