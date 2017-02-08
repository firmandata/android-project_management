package com.construction.pm.libraries.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;
    private ItemClickListener mItemClickListener;

    public RecyclerItemTouchListener(final Context context, final RecyclerView recyclerView, final ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && itemClickListener != null) {
                    itemClickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mItemClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
