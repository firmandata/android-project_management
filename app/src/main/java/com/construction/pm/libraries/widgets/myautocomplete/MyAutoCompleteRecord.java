package com.construction.pm.libraries.widgets.myautocomplete;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.construction.pm.utils.ViewUtil;

public class MyAutoCompleteRecord {

    private long mId;

    protected Context mContext;

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

    public MyAutoCompleteRecord(final Context context) {
        mContext = context;

        this.setPadding(
                ViewUtil.getPxFromDp(mContext, 8),
                ViewUtil.getPxFromDp(mContext, 5),
                ViewUtil.getPxFromDp(mContext, 8),
                ViewUtil.getPxFromDp(mContext, 5)
        );
    }

    public void setId(final long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public LinearLayout getView(){
        LinearLayout llSelf = new LinearLayout(mContext);
        llSelf.setOrientation(LinearLayout.HORIZONTAL);
        llSelf.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        llSelf.addView(getRecord());

        return llSelf;
    }

    public RelativeLayout getRecord() {
        RelativeLayout rlSelf = new RelativeLayout(mContext);

        return rlSelf;
    }

    public void setPadding(final int left, final int top, final int right, final int bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    public MyAutoCompleteIModel getData(){
        return null;
    }
}
