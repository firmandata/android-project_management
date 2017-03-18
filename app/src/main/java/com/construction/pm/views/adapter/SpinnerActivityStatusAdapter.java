package com.construction.pm.views.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ActivityStatusEnum;
import com.construction.pm.utils.StringUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SpinnerActivityStatusAdapter extends BaseAdapter {

    protected Context mContext;
    protected List<ActivityStatusEnum> mActivityStatusList;

    public SpinnerActivityStatusAdapter(Context context) {
        super();

        mContext = context;

        mActivityStatusList = new ArrayList<ActivityStatusEnum>();
        mActivityStatusList.add(null);
        mActivityStatusList.add(ActivityStatusEnum.IN_PROGRESS);
        mActivityStatusList.add(ActivityStatusEnum.COMPLETED);
        mActivityStatusList.add(ActivityStatusEnum.ADDENDUM);
    }

    public int getPositionByItem(ActivityStatusEnum activityStatusEnum) {
        int position = -1;
        if (activityStatusEnum != null) {
            for (int itemPosition = 0; itemPosition < getCount(); itemPosition++) {
                ActivityStatusEnum selfValue = getItem(itemPosition);
                if (selfValue != null) {
                    if (activityStatusEnum.getValue().equals(selfValue.getValue())) {
                        position = itemPosition;
                        break;
                    }
                }
            }
        }
        return position;
    }

    @Override
    public ActivityStatusEnum getItem(int position) {
        return mActivityStatusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItem view = ViewItem.buildViewItem(mContext, null);
        view.setActivityStatus(mActivityStatusList.get(position));
        return view.getView();
    }

    @Override
    public int getCount() {
        return mActivityStatusList.size();
    }

    protected static class ViewItem {

        protected Context mContext;

        protected RelativeLayout mViewItem;

        protected AppCompatTextView mActivityStatusText;

        public ViewItem(final Context context) {
            mContext = context;
        }

        public ViewItem(final Context context, final RelativeLayout itemView) {
            this(context);

            initializeView(itemView);
        }

        public static ViewItem buildViewItem(final Context context, final int layoutId, final ViewGroup viewGroup) {
            return new ViewItem(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
        }

        public static ViewItem buildViewItem(final Context context, final ViewGroup viewGroup) {
            return buildViewItem(context, R.layout.spinner_activity_status_item_view, viewGroup);
        }

        protected void initializeView(final RelativeLayout itemView) {
            mViewItem = itemView;

            mActivityStatusText = (AppCompatTextView) mViewItem.findViewById(R.id.activityStatusText);
            mActivityStatusText.setText(ViewUtil.getResourceString(mContext, R.string.project_activity_update_form_activity_status_blank));
        }

        public void setActivityStatus(final ActivityStatusEnum activityStatusEnum) {
            if (activityStatusEnum != null)
                mActivityStatusText.setText(activityStatusEnum.getValue());
        }

        public RelativeLayout getView() {
            return mViewItem;
        }
    }
}