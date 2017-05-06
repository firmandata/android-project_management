package com.construction.pm.views.report_request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.construction.pm.activities.fragmentdialogs.ReportRequestDetailDialogFragment;
import com.construction.pm.activities.fragmentdialogs.ReportRequestFormDialogFragment;
import com.construction.pm.libraries.widgets.RecyclerItemTouchListener;
import com.construction.pm.models.ReportRequestModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportRequestListView implements
        ReportRequestDetailDialogFragment.ReportRequestDetailListener,
        ReportRequestFormDialogFragment.ReportRequestFormListener{
    protected Context mContext;

    protected FragmentManager mFragmentManager;
    protected static final String FRAGMENT_TAG_REQUEST_REPORT_DETAIL_DIALOG = "FRAGMENT_REQUEST_REPORT_DETAIL_DIALOG";
    protected static final String FRAGMENT_TAG_REQUEST_REPORT_FORM_DIALOG = "FRAGMENT_REQUEST_REPORT_FORM_DIALOG";
    protected ReportRequestFormDialogFragment mReportRequestFormDialogFragment;
    protected ReportRequestDetailDialogFragment mReportRequestDetailDialogFragment;

    protected RelativeLayout mReportRequestListView;

    protected SwipeRefreshLayout mSrlReportRequestList;
    protected RecyclerView mRvReportRequestList;
    protected ReportRequestListAdapter mReportRequestAdapter;

    protected ProgressDialog mProgressDialog;

    protected ReportRequestListListener mReportRequestListListener;

    public ReportRequestListView(final Context context) {
        mContext = context;
    }

    public ReportRequestListView(final Context context, final RelativeLayout reportRequestListView) {
        this(context);

        initializeView(reportRequestListView);
    }

    public static ReportRequestListView buildReportRequestListView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ReportRequestListView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ReportRequestListView buildReportRequestListView(final Context context, final ViewGroup viewGroup) {
        return buildReportRequestListView(context, R.layout.report_request_list_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout reportRequestListView) {
        mReportRequestListView = reportRequestListView;

        mSrlReportRequestList = (SwipeRefreshLayout) mReportRequestListView.findViewById(R.id.reportRequestListSwipeRefresh);
        mSrlReportRequestList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mReportRequestListListener != null)
                    mReportRequestListListener.onReportRequestListRequest();
            }
        });

        mRvReportRequestList = (RecyclerView) mReportRequestListView.findViewById(R.id.reportRequestList);
        mRvReportRequestList.setItemAnimator(new DefaultItemAnimator());
        mRvReportRequestList.setHasFixedSize(true);
        mRvReportRequestList.addOnItemTouchListener(new RecyclerItemTouchListener(mContext, mRvReportRequestList, new RecyclerItemTouchListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ReportRequestModel reportRequestModel = mReportRequestAdapter.getReportRequestModel(position);
                if (reportRequestModel != null) {
                    if (mReportRequestListListener != null)
                        mReportRequestListListener.onReportRequestItemClick(reportRequestModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvReportRequestList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRvReportRequestList.addItemDecoration(dividerItemDecoration);

        mReportRequestAdapter = new ReportRequestListAdapter();
        mRvReportRequestList.setAdapter(mReportRequestAdapter);

        if (mReportRequestListListener != null)
            mReportRequestListListener.onReportRequestListRequest();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setReportRequestModels(final ReportRequestModel[] reportRequestModels) {
        mReportRequestAdapter.setReportRequestModels(reportRequestModels);
    }

    public void addReportRequestModel(final ReportRequestModel reportRequestModel) {
        mReportRequestAdapter.addReportRequestModels(new ReportRequestModel[] { reportRequestModel });
    }

    public ReportRequestModel[] getReportRequestModels() {
        return mReportRequestAdapter.getReportRequestModels();
    }

    public void startRefreshAnimation() {
        // -- Start refresh animation --
        if (!mSrlReportRequestList.isRefreshing())
            mSrlReportRequestList.setRefreshing(true);
    }

    public void stopRefreshAnimation() {
        // -- Stop refresh animation --
        if (mSrlReportRequestList.isRefreshing())
            mSrlReportRequestList.setRefreshing(false);
    }

    public RelativeLayout getView() {
        return mReportRequestListView;
    }

    public void loadLayoutToFragment(final Fragment fragment) {
        mFragmentManager = fragment.getChildFragmentManager();
    }

    public void showReportRequestAdd() {
        mReportRequestFormDialogFragment = ReportRequestFormDialogFragment.newInstance(this);
        if (mFragmentManager != null)
            mReportRequestFormDialogFragment.show(mFragmentManager, FRAGMENT_TAG_REQUEST_REPORT_FORM_DIALOG);
    }

    @Override
    public void onReportRequestFormProjectListProgress(String progressMessage) {
        // -- Show progress dialog --
        progressDialogShow(progressMessage);
    }

    @Override
    public void onReportRequestFormProjectListFinish(String errorMessage) {
        // -- Hide progress dialog --
        progressDialogDismiss();

        if (errorMessage != null) {
            // -- Show message dialog --
            alertDialogErrorShow(errorMessage);
        }
    }

    @Override
    public void onReportRequestFormSendInvalid(String invalidMessage) {
        // -- Show message dialog --
        alertDialogErrorShow(invalidMessage);
    }

    @Override
    public void onReportRequestFormSendProgress(String progressMessage) {
        // -- Show progress dialog --
        progressDialogShow(progressMessage);
    }

    @Override
    public void onReportRequestFormSent(ReportRequestModel reportRequestModel, String message) {
        // -- Hide progress dialog --
        progressDialogDismiss();

        if (reportRequestModel != null) {
            // -- Update ReportRequestModel list --
            addReportRequestModel(reportRequestModel);

            // -- Close fragment dialog --
            mReportRequestFormDialogFragment.dismiss();
        } else {
            if (message != null) {
                // -- Show message dialog --
                alertDialogErrorShow(message);
            }
        }
    }

    public void showReportRequestDetail(final ReportRequestModel reportRequestModel) {
        mReportRequestDetailDialogFragment = ReportRequestDetailDialogFragment.newInstance(reportRequestModel, this);
        if (mFragmentManager != null)
            mReportRequestDetailDialogFragment.show(mFragmentManager, FRAGMENT_TAG_REQUEST_REPORT_DETAIL_DIALOG);
    }

    @Override
    public void onReportRequestDetailResendProgress(String progressMessage) {
        // -- Show progress dialog --
        progressDialogShow(progressMessage);
    }

    @Override
    public void onReportRequestDetailResent(ReportRequestModel reportRequestModel, String message) {
        // -- Hide progress dialog --
        progressDialogDismiss();

        if (reportRequestModel != null) {
            // -- Update ReportRequestModel list --
            addReportRequestModel(reportRequestModel);

            // -- Close fragment dialog --
            mReportRequestDetailDialogFragment.dismiss();
        } else {
            if (message != null) {
                // -- Show message dialog --
                alertDialogErrorShow(message);
            }
        }
    }

    public void progressDialogShow(final String progressMessage) {
        mProgressDialog.setMessage(progressMessage);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void progressDialogDismiss() {
        mProgressDialog.setMessage(null);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void alertDialogShow(final String alertTitle, final String alertMessage, final int iconId, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setIcon(iconId);
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setPositiveButton(ViewUtil.getResourceString(mContext, R.string.report_request_list_view_alert_button), onClickListener);
        alertDialog.show();
    }

    public void alertDialogErrorShow(final String errorMessage) {
        alertDialogShow(ViewUtil.getResourceString(mContext, R.string.report_request_list_view_alert_title_error), errorMessage, R.drawable.ic_error_dark_24, null);
    }

    public void setReportRequestListListener(final ReportRequestListListener reportRequestListListener) {
        mReportRequestListListener = reportRequestListListener;
    }

    public interface ReportRequestListListener {
        void onReportRequestListRequest();
        void onReportRequestItemClick(ReportRequestModel reportRequestModel);
    }

    protected class ReportRequestListAdapter extends RecyclerView.Adapter<ReportRequestListViewHolder> {

        protected List<ReportRequestModel> mReportRequestModelList;

        public ReportRequestListAdapter() {
            mReportRequestModelList = new ArrayList<ReportRequestModel>();
        }

        public ReportRequestListAdapter(final ReportRequestModel[] reportRequestModels) {
            this();
            mReportRequestModelList = new ArrayList<ReportRequestModel>(Arrays.asList(reportRequestModels));
        }

        public void setReportRequestModels(final ReportRequestModel[] reportRequestModels) {
            if (reportRequestModels != null)
                mReportRequestModelList = new ArrayList<ReportRequestModel>(Arrays.asList(reportRequestModels));
            else
                mReportRequestModelList = new ArrayList<ReportRequestModel>();
            notifyDataSetChanged();
        }

        public void addReportRequestModels(final ReportRequestModel[] reportRequestModels) {
            List<ReportRequestModel> newReportRequestModelList = new ArrayList<ReportRequestModel>();
            for (ReportRequestModel newReportRequestModel : reportRequestModels) {
                int position = getPosition(newReportRequestModel);
                if (position >= 0) {
                    // -- replace item --
                    setReportRequestModel(position, newReportRequestModel);
                } else {
                    // -- new items --
                    newReportRequestModelList.add(newReportRequestModel);
                }
            }
            if (newReportRequestModelList.size() > 0) {
                mReportRequestModelList.addAll(0, newReportRequestModelList);
                notifyItemRangeInserted(0, newReportRequestModelList.size());
            }
        }

        public void setReportRequestModel(final int position, final ReportRequestModel reportRequestModel) {
            if ((position + 1) > mReportRequestModelList.size())
                return;

            mReportRequestModelList.set(position, reportRequestModel);
            notifyItemChanged(position);
        }

        public ReportRequestModel[] getReportRequestModels() {
            ReportRequestModel[] reportRequestModels = new ReportRequestModel[mReportRequestModelList.size()];
            mReportRequestModelList.toArray(reportRequestModels);
            return reportRequestModels;
        }

        public ReportRequestModel getReportRequestModel(final int position) {
            if ((position + 1) > mReportRequestModelList.size())
                return null;

            return mReportRequestModelList.get(position);
        }

        public int getPosition(final ReportRequestModel reportRequestModel) {
            if (reportRequestModel == null)
                return -1;

            boolean isPositionFound;
            int position;

            // -- Search by object --
            isPositionFound = false;
            position = 0;
            for (ReportRequestModel reportRequestModelExist : mReportRequestModelList) {
                if (reportRequestModelExist.equals(reportRequestModel)) {
                    isPositionFound = true;
                    break;
                }
                position++;
            }

            if (isPositionFound)
                return position;

            // -- Search by id --
            Integer searchReportRequestId = reportRequestModel.getReportRequestId();
            if (searchReportRequestId == null)
                return -1;

            isPositionFound = false;
            position = 0;
            for (ReportRequestModel reportRequestModelExist : mReportRequestModelList) {
                Integer existReportRequestModelId = reportRequestModelExist.getReportRequestId();
                if (existReportRequestModelId != null) {
                    if (existReportRequestModelId.equals(searchReportRequestId)) {
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
        public ReportRequestListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_request_list_item_view, parent, false);
            return new ReportRequestListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ReportRequestListViewHolder holder, int position) {
            if ((position + 1) > mReportRequestModelList.size())
                return;

            ReportRequestModel reportRequestModel = mReportRequestModelList.get(position);
            holder.setReportRequestModel(reportRequestModel);
        }

        @Override
        public int getItemCount() {
            return mReportRequestModelList.size();
        }
    }

    protected class ReportRequestListViewHolder extends RecyclerView.ViewHolder {

        protected AppCompatTextView mEtRequestDate;
        protected AppCompatTextView mEtProjectName;
        protected AppCompatTextView mEtRequestStatus;
        protected AppCompatTextView mEtComment;

        public ReportRequestListViewHolder(View view) {
            super(view);

            mEtRequestDate = (AppCompatTextView) view.findViewById(R.id.requestDate);
            mEtProjectName = (AppCompatTextView) view.findViewById(R.id.projectName);
            mEtRequestStatus = (AppCompatTextView) view.findViewById(R.id.requestStatus);
            mEtComment = (AppCompatTextView) view.findViewById(R.id.comment);
        }

        public void setReportRequestModel(final ReportRequestModel reportRequestModel) {
            mEtRequestDate.setText(DateTimeUtil.ToDateTimeDisplayString(reportRequestModel.getRequestDate()));
            mEtProjectName.setText(reportRequestModel.getProjectName());
            mEtRequestStatus.setText(reportRequestModel.getRequestStatus());
            mEtComment.setText(reportRequestModel.getComment());
        }
    }
}
