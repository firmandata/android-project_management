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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        mRvNotificationList.setHasFixedSize(true);
        mRvNotificationList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvNotificationList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                NotificationModel notificationModel = mNotificationListAdapter.getNotificationModel(position);
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

    public void addNotificationModels(final NotificationModel[] notificationModels) {
        mNotificationListAdapter.addNotificationModels(notificationModels);
    }

    public void updateNotificationModel(final NotificationModel notificationModel) {
        int position = mNotificationListAdapter.getPosition(notificationModel);
        if (position >= 0) {
            mNotificationListAdapter.setNotificationModel(position, notificationModel);
        }
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

        protected List<NotificationModel> mNotificationModelList;

        public NotificationListAdapter() {
            mNotificationModelList = new ArrayList<NotificationModel>();
        }

        public NotificationListAdapter(final NotificationModel[] notificationModels) {
            this();
            mNotificationModelList = new ArrayList<NotificationModel>(Arrays.asList(notificationModels));
        }

        public void setNotificationModels(final NotificationModel[] notificationModels) {
            mNotificationModelList = new ArrayList<NotificationModel>(Arrays.asList(notificationModels));
            notifyDataSetChanged();
        }

        public void addNotificationModels(final NotificationModel[] notificationModels) {
            List<NotificationModel> newNotificationModelList = new ArrayList<NotificationModel>();
            for (NotificationModel newNotificationModel : notificationModels) {
                int position = getPosition(newNotificationModel);
                if (position >= 0) {
                    // -- replace item --
                    setNotificationModel(position, newNotificationModel);
                } else {
                    // -- new items --
                    newNotificationModelList.add(newNotificationModel);
                }
            }
            if (newNotificationModelList.size() > 0) {
                mNotificationModelList.addAll(0, newNotificationModelList);
                notifyItemRangeInserted(0, newNotificationModelList.size());
            }
        }

        public void setNotificationModel(final int position, final NotificationModel notificationModel) {
            if ((position + 1) > mNotificationModelList.size())
                return;

            mNotificationModelList.set(position, notificationModel);
            notifyItemChanged(position);
        }

        public NotificationModel getNotificationModel(final int position) {
            if ((position + 1) > mNotificationModelList.size())
                return null;

            return mNotificationModelList.get(position);
        }

        public int getPosition(final NotificationModel notificationModel) {
            if (notificationModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (NotificationModel notificationModelExist : mNotificationModelList) {
                if (notificationModelExist.equals(notificationModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchProjectNotificationId = notificationModel.getProjectNotificationId();
            if (searchProjectNotificationId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (NotificationModel notificationModelExist : mNotificationModelList) {
                Integer existProjectNotificationId = notificationModelExist.getProjectNotificationId();
                if (existProjectNotificationId != null) {
                    if (existProjectNotificationId.equals(searchProjectNotificationId)) {
                        isPositionFound = true;
                        break;
                    }
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
            if ((position + 1) > mNotificationModelList.size())
                return;

            NotificationModel notificationModel = mNotificationModelList.get(position);
            holder.setNotificationModel(notificationModel);
        }

        @Override
        public int getItemCount() {
            return mNotificationModelList.size();
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
