package com.construction.pm.libraries.widgets.mylist;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MyListView extends ListView {

    private Context mContext;

    protected int mPageCurrent;
    protected int mPageTotal;

    private RequestLoadPageListener mRequestLoadPageListener;

    public MyListView(final Context context) {
        super(context);
        mContext = context;

        setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        setClipToPadding(false);
        setChoiceMode(CHOICE_MODE_SINGLE);

        setOnScrollListener(new OnScrollListener() {
            private int preLast;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem && getPageTotal() > getPageCurrent()) {
                        if (mRequestLoadPageListener != null)
                            mRequestLoadPageListener.onRequestLoadPage(getPageCurrent() + 1);
                        preLast = lastItem;
                    }
                }
            }
        });

        MyListAdapter adapter = new MyListAdapter();
        setAdapter(adapter);
    }

    @Override
    public MyListAdapter getAdapter() {
        return (MyListAdapter) super.getAdapter();
    }

    public void setPageCurrent(final int pageCurrent) {
        mPageCurrent = pageCurrent;
    }

    public int getPageCurrent() {
        return mPageCurrent;
    }

    public void setPageTotal(final int pageTotal) {
        mPageTotal = pageTotal;
    }

    public int getPageTotal() {
        return mPageTotal;
    }

    public void setOnRequestLoadPageListener(final RequestLoadPageListener requestLoadPageListener) {
        mRequestLoadPageListener = requestLoadPageListener;
    }

    public interface RequestLoadPageListener {
        void onRequestLoadPage(int page);
    }
}