package com.construction.pm.libraries.widgets.mylist;

import android.view.View;

public interface IMyListItem {
    void setViewId(final long id);
    long getViewId();
    void setView(final View view);
    View getView();
    void setViewSelected();
    void setViewUnSelected();
}
