package com.construction.pm.libraries.widgets.mylist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyListAdapter extends BaseAdapter {

    protected List<Object> mItemList;
    protected List<Object> mItemSelectedList;

    public MyListAdapter() {
        mItemList = new ArrayList<Object>();
        mItemSelectedList = new ArrayList<Object>();
    }

    public MyListAdapter(final IMyListItem[] items) {
        this();
        setItems(items);
    }


    // -----------------------
    // -- Item Manipulation --
    // -----------------------

    public void setItems(final IMyListItem[] items) {
        mItemList.clear();
        mItemList.addAll(Arrays.asList(items));

        notifyDataSetChanged();
    }

    public void addItem(IMyListItem item) {
        IMyListItem oldItem = getItemIdExists(item);
        if (oldItem == null) {
            mItemList.add(item);
            notifyDataSetChanged();
        } else {
            oldItem.setView(item.getView());
        }
    }

    public void addItems(final IMyListItem[] items) {
        boolean isFoundAddItem = false;
        for (IMyListItem item : items) {
            IMyListItem oldItem = getItemIdExists(item);
            if (oldItem == null) {
                mItemList.add(item);
                isFoundAddItem = true;
            } else {
                oldItem.setView(item.getView());
            }
        }
        if (isFoundAddItem)
            notifyDataSetChanged();
    }

    public void removeItem(IMyListItem item) {
        try {
            mItemList.remove(item);
        } catch (Exception ex) {
        }

        removeItemSelected(item);
        notifyDataSetChanged();
    }

    public void removeItems(final IMyListItem[] items) {
        boolean isFoundItem = false;
        for (IMyListItem item : items) {
            try {
                mItemList.remove(item);
                isFoundItem = true;
            } catch (Exception ex) {
            }
        }
        if (isFoundItem)
            notifyDataSetChanged();
    }

    public void clearItems() {
        mItemList.clear();
        mItemSelectedList.clear();
        notifyDataSetChanged();
    }


    // ---------------
    // -- Item Info --
    // ---------------

    public IMyListItem getItemIdExists(IMyListItem item) {
        IMyListItem oldExistingItem = null;
        if (item.getViewId() > 0) {
            for (Object existingItemObject : mItemList) {
                IMyListItem existingItem = (IMyListItem) existingItemObject;
                if (existingItem.getViewId() == item.getViewId() && existingItem.getViewId() > 0)
                {
                    oldExistingItem = existingItem;
                    break;
                }
            }
        }
        return oldExistingItem;
    }

    public int getItemPosition(IMyListItem item) {
        return mItemList.indexOf(item);
    }

    public Object[] getItems() {
        Object[] items = new Object[mItemList.size()];
        mItemList.toArray(items);
        return items;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(final int position) {
        if (mItemList.size() - 1 >= position)
            return mItemList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(final int position) {
        Object record = getItem(position);
        if (record != null) {
            return ((IMyListItem) record).getViewId();
        } else
            return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        if (position < 0)
            return null;

        if (mItemList.size() - 1 >= position) {
            IMyListItem item = (IMyListItem) mItemList.get(position);

            // -- Set view selected or not selected --
            int selectedIndex = mItemSelectedList.indexOf(item);
            if (selectedIndex >= 0)
                item.setViewSelected();
            else
                item.setViewUnSelected();

            return item.getView();
        }
        else
            return null;
    }


    // -------------------
    // -- Item Selector --
    // -------------------

    public void setItemSelectedAll() {
        for (Object record : mItemList) {
            addItemSelected((IMyListItem) record);
        }
    }

    public void addItemSelected(final int position) {
        if (mItemList.size() - 1 >= position) {
            addItemSelected((IMyListItem) mItemList.get(position));
        }
    }

    public void addItemSelected(IMyListItem item) {
        item.setViewSelected();
        mItemSelectedList.add(item);
    }

    public void removeItemSelectedAll() {
        for (Object record : mItemSelectedList) {
            removeItemSelected((IMyListItem) record);
        }
    }

    public void removeItemSelected(final int position) {
        if (mItemList.size() - 1 >= position) {
            removeItemSelected((IMyListItem) mItemList.get(position));
        }
    }

    public void removeItemSelected(IMyListItem item) {
        item.setViewUnSelected();
        try {
            mItemSelectedList.remove(item);
        } catch (Exception ex) {
        }
    }
}