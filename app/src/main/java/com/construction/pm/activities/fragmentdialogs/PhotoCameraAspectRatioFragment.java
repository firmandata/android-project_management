package com.construction.pm.activities.fragmentdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.cameraview.AspectRatio;

import java.util.Set;

public class PhotoCameraAspectRatioFragment extends DialogFragment {

    public static final String PARAM_ASPECT_RATIOS = "AspectRatios";
    public static final String PARAM_ASPECT_RATIO_CURRENT = "AspectRatioCurrent";

    protected AspectRatio[] mAspectRatios;
    protected AspectRatio mAspectRatioCurrent;
    protected CameraAspectRatioAdapter mCameraAspectRatioAdapter;

    protected PhotoCameraAspectRatioListener mPhotoCameraAspectRatioListener;

    public static PhotoCameraAspectRatioFragment newInstance(Set<AspectRatio> aspectRatios, AspectRatio currentAspectRatio, final PhotoCameraAspectRatioListener photoCameraAspectRatioListener) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArray(PARAM_ASPECT_RATIOS, aspectRatios.toArray(new AspectRatio[aspectRatios.size()]));
        bundle.putParcelable(PARAM_ASPECT_RATIO_CURRENT, currentAspectRatio);

        final PhotoCameraAspectRatioFragment photoCameraAspectRatioFragment = new PhotoCameraAspectRatioFragment();
        photoCameraAspectRatioFragment.setArguments(bundle);
        photoCameraAspectRatioFragment.setPhotoCameraAspectRatioListener(photoCameraAspectRatioListener);
        return photoCameraAspectRatioFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAspectRatios = null;
        mAspectRatioCurrent = null;

        Bundle bundle = getArguments();
        if (bundle != null) {
            mAspectRatios = (AspectRatio[]) bundle.getParcelableArray(PARAM_ASPECT_RATIOS);
            if (mAspectRatios == null) {
                mAspectRatios = new AspectRatio[] { };
            }
            mAspectRatioCurrent = bundle.getParcelable(PARAM_ASPECT_RATIO_CURRENT);
        }

        mCameraAspectRatioAdapter = new CameraAspectRatioAdapter(mAspectRatios, mAspectRatioCurrent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setAdapter(mCameraAspectRatioAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                mPhotoCameraAspectRatioListener.onCameraAspectRatioSelected(mAspectRatios[position]);
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected static class CameraAspectRatioAdapter extends BaseAdapter {
        protected final AspectRatio[] mAspectRatios;
        protected final AspectRatio mAspectRatioCurrent;

        public CameraAspectRatioAdapter(final AspectRatio[] ratios, final AspectRatio current) {
            mAspectRatios = ratios;
            mAspectRatioCurrent = current;
        }

        @Override
        public int getCount() {
            if (mAspectRatios == null)
                return 0;

            return mAspectRatios.length;
        }

        @Override
        public AspectRatio getItem(int position) {
            if (mAspectRatios == null)
                return null;
            if (position >= mAspectRatios.length)
                return null;

            return mAspectRatios[position];
        }

        @Override
        public long getItemId(int position) {
            AspectRatio aspectRatio = getItem(position);
            if (aspectRatio == null)
                return 0;

            return aspectRatio.hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            CameraAspectRatioAdapter.ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new CameraAspectRatioAdapter.ViewHolder();
                holder.text = (TextView) view.findViewById(android.R.id.text1);
                view.setTag(holder);
            } else {
                holder = (CameraAspectRatioAdapter.ViewHolder) view.getTag();
            }
            AspectRatio aspectRatio = getItem(position);
            if (aspectRatio != null) {
                StringBuilder stringBuilder = new StringBuilder(aspectRatio.toString());
                if (aspectRatio.equals(mAspectRatioCurrent)) {
                    stringBuilder.append(" *");
                }
                holder.text.setText(stringBuilder);
            }
            return view;
        }

        private static class ViewHolder {
            TextView text;
        }
    }

    public void setPhotoCameraAspectRatioListener(final PhotoCameraAspectRatioListener photoCameraAspectRatioListener) {
        mPhotoCameraAspectRatioListener = photoCameraAspectRatioListener;
    }

    public interface PhotoCameraAspectRatioListener {
        void onCameraAspectRatioSelected(AspectRatio aspectRatio);
    }
}