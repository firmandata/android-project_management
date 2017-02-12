package com.construction.pm.views.notification;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.NotificationModel;
import com.construction.pm.utils.DateTimeUtil;

public class NotificationListView {
    protected Context mContext;

    protected RelativeLayout mNotificationListView;

    protected SwipeRefreshLayout mSrlNotificationList;
    protected RecyclerView mRvNotificationList;
    protected NotificationListAdapter mNotificationListAdapter;

    protected NotificationListListener mNotificationListListener;

    public NotificationListView(final Context context) {
        mContext = context;
    }

    public NotificationListView(final Context context, final RelativeLayout notificationListView) {
        this(context);

        initializeView(notificationListView);
    }

    public static NotificationListView buildNotificationListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new NotificationListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static NotificationListView buildNotificationListView(final Context context, final ViewGroup viewGroup) {
        return buildNotificationListView(context, R.layout.notification_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout notificationListView) {
        mNotificationListView = notificationListView;

        mSrlNotificationList = (SwipeRefreshLayout) mNotificationListView.findViewById(R.id.notificationListSwipeRefresh);
        mSrlNotificationList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mNotificationListListener != null)
                    mNotificationListListener.onNotificationListRequest();
            }
        });

        mRvNotificationList = (RecyclerView) mNotificationListView.findViewById(R.id.notificationList);
        mRvNotificationList.setItemAnimator(new DefaultItemAnimator());
        mRvNotificationList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvNotificationList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                NotificationModel notificationModel = mNotificationListAdapter.getItem(position);
                if (notificationModel != null) {
                    if (mNotificationListListener != null)
                        mNotificationListListener.onNotificationItemClick(notificationModel, position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvNotificationList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvNotificationList.addItemDecoration(dividerItemDecoration);

        mNotificationListAdapter = new NotificationListAdapter();
        mRvNotificationList.setAdapter(mNotificationListAdapter);

        if (mNotificationListListener != null)
            mNotificationListListener.onNotificationListRequest();
    }

    public void setNotificationModels(final NotificationModel[] notificationModels) {
        mNotificationListAdapter.setNotificationModels(notificationModels);
        mNotificationListAdapter.notifyDataSetChanged();
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlNotificationList.isRefreshing())
            mSrlNotificationList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlNotificationList.isRefreshing())
            mSrlNotificationList.setRefreshing(false);
    }

    public void setNotificationModelRead(final NotificationModel notificationModel) {
        notificationModel.setRead(true);

        int position = mNotificationListAdapter.getItemPosition(notificationModel);
        if (position >= 0)
            mNotificationListAdapter.notifyItemChanged(position);
    }

    public RelativeLayout getView() {
        return mNotificationListView;
    }

    public void setNotificationListListener(final NotificationListListener notificationListListener) {
        mNotificationListListener = notificationListListener;
    }

    public interface NotificationListListener {
        void onNotificationListRequest();
        void onNotificationItemClick(NotificationModel notificationModel, int position);
    }

    protected class NotificationListAdapter extends RecyclerView.Adapter<NotificationListViewHolder> {

        protected NotificationModel[] mNotificationModels;

        public NotificationListAdapter() {

        }

        public NotificationListAdapter(final NotificationModel[] notificationModels) {
            this();
            mNotificationModels = notificationModels;
        }

        public void setNotificationModels(final NotificationModel[] notificationModels) {
            mNotificationModels = notificationModels;

            notifyDataSetChanged();
        }

        public NotificationModel getItem(final int position) {
            if (mNotificationModels == null)
                return null;
            if ((position + 1) > mNotificationModels.length)
                return null;

            return mNotificationModels[position];
        }

        public int getItemPosition(final NotificationModel notificationModel) {
            if (mNotificationModels == null)
                return -1;

            boolean isPositionFound = false;
            int position = 0;
            for (NotificationModel notificationModelExist : mNotificationModels) {
                if (notificationModelExist.equals(notificationModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            return -1;
        }

        @Override
        public NotificationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item_view, parent, false);
            return new NotificationListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NotificationListViewHolder holder, int position) {
            if (mNotificationModels == null)
                return;
            if ((position + 1) > mNotificationModels.length)
                return;

            NotificationModel notificationModel = mNotificationModels[position];
            holder.setNotificationModel(notificationModel);
        }

        @Override
        public int getItemCount() {
            if (mNotificationModels == null)
                return 0;

            return mNotificationModels.length;
        }
    }

    protected class NotificationListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtNotificationDate;
        protected AppCompatTextView mEtNotificationMessage;

        public NotificationListViewHolder(View view) {
            super(view);

            mEtNotificationDate = (AppCompatTextView) view.findViewById(R.id.notificationDate);
            mEtNotificationMessage = (AppCompatTextView) view.findViewById(R.id.notificationMessage);
        }

        public void setNotificationModel(final NotificationModel notificationModel) {
            mEtNotificationDate.setText(DateTimeUtil.ToDateTimeDisplayString(notificationModel.getNotificationDate()));
            mEtNotificationMessage.setText(notificationModel.getNotificationMessage());
            if (notificationModel.isRead())
                setNotificationRead();
            else
                setNotificationUnRead();
        }

        public void setNotificationRead() {
            mEtNotificationDate.setTypeface(null, Typeface.NORMAL);
            mEtNotificationMessage.setTypeface(null, Typeface.NORMAL);
        }

        public void setNotificationUnRead() {
            mEtNotificationDate.setTypeface(null, Typeface.BOLD);
            mEtNotificationMessage.setTypeface(null, Typeface.BOLD);
        }
    }
}
