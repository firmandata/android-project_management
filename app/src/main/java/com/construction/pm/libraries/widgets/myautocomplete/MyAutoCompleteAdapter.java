package com.construction.pm.libraries.widgets.myautocomplete;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class MyAutoCompleteAdapter<T extends MyAutoCompleteRecord> extends BaseAdapter implements Filterable {
    protected static final int MAX_RESULTS = 10;
    protected Context mContext;
    protected List<T> resultList = new ArrayList<T>();

    public MyAutoCompleteAdapter(final Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public T getItem(final int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return resultList.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return getItem(position).getView();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<T> products = onFindRecords(mContext, constraint.toString());

                    filterResults.values = products;
                    filterResults.count = products.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<T>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    protected List<T> onFindRecords(final Context context, final String keyWords) {
        return new ArrayList<T>();
    }
}