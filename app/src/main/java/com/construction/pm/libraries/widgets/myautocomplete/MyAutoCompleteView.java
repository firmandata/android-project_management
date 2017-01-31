package com.construction.pm.libraries.widgets.myautocomplete;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

public class MyAutoCompleteView extends AppCompatAutoCompleteTextView {
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;

    private MyAutoCompleteViewListener mListener;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyAutoCompleteView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public MyAutoCompleteView(final Context context) {
        super(context);

        mListener = null;
    }

    public MyAutoCompleteView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoadingIndicator(final ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setAutoCompleteDelay(final int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);

        if (mListener != null)
            mListener.onPerformFiltering();
    }

    @Override
    public void onFilterComplete(final int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }

    public void setListener(final MyAutoCompleteViewListener onPerformFiltering) {
        mListener = onPerformFiltering;
    }

    public interface MyAutoCompleteViewListener{
        void onPerformFiltering();
    }
}
