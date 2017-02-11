package com.construction.pm.views.contract;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;
import com.construction.pm.models.ContractModel;
import com.construction.pm.utils.DateTimeUtil;
import com.construction.pm.utils.StringUtil;

public class ContractDetailView {
    protected Context mContext;

    protected RelativeLayout mContractDetailView;
    protected AppCompatTextView mTvContractNo;
    protected AppCompatTextView mTvParentContractNo;
    protected AppCompatTextView mTvContractName;
    protected AppCompatTextView mTvContractAmount;
    protected AppCompatTextView mTvProgramNo;
    protected AppCompatTextView mTvProgramName;
    protected AppCompatTextView mTvProgramAmount;
    protected AppCompatTextView mTvBudgetNo;
    protected AppCompatTextView mTvBudgetName;
    protected AppCompatTextView mTvVendorNo;
    protected AppCompatTextView mTvVendorName;
    protected AppCompatTextView mTvContractDate;
    protected AppCompatTextView mTvContractStartDate;
    protected AppCompatTextView mTvContractEndDate;
    protected AppCompatTextView mTvContractStatus;

    public ContractDetailView(final Context context) {
        mContext = context;
    }

    public ContractDetailView(final Context context, final RelativeLayout contractDetailView) {
        this(context);

        initializeView(contractDetailView);
    }

    public static ContractDetailView buildContractDetailView(final Context context, final int layoutId, final ViewGroup viewGroup) {
        return new ContractDetailView(context, (RelativeLayout) LayoutInflater.from(context).inflate(layoutId, viewGroup));
    }

    public static ContractDetailView buildContractDetailView(final Context context, final ViewGroup viewGroup) {
        return buildContractDetailView(context, R.layout.contract_detail_view, viewGroup);
    }

    protected void initializeView(final RelativeLayout contractDetailView) {
        mContractDetailView = contractDetailView;

        mTvContractNo = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractNo);
        mTvParentContractNo = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvParentContractNo);
        mTvContractName = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractName);
        mTvContractAmount = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractAmount);
        mTvProgramNo = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvProgramNo);
        mTvProgramName = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvProgramName);
        mTvProgramAmount = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvProgramAmount);
        mTvBudgetNo = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvBudgetNo);
        mTvBudgetName = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvBudgetName);
        mTvVendorNo = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvVendorNo);
        mTvVendorName = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvVendorName);
        mTvContractDate = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractDate);
        mTvContractStartDate = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractStartDate);
        mTvContractEndDate = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractEndDate);
        mTvContractStatus = (AppCompatTextView) mContractDetailView.findViewById(R.id.tvContractStatus);
    }

    public RelativeLayout getView() {
        return mContractDetailView;
    }

    public void setContractModel(final ContractModel contractModel) {
        if (contractModel == null)
            return;

        mTvContractNo.setText(contractModel.getContractNo());
        mTvParentContractNo.setText(contractModel.getParentContractNo());
        mTvContractName.setText(contractModel.getContractName());
        mTvContractAmount.setText(StringUtil.numberFormat(contractModel.getContractAmount()));
        mTvProgramNo.setText(contractModel.getProgramNo());
        mTvProgramName.setText(contractModel.getProgramName());
        mTvProgramAmount.setText(StringUtil.numberFormat(contractModel.getProgramAmount()));
        mTvBudgetNo.setText(contractModel.getBudgetNo());
        mTvBudgetName.setText(contractModel.getBudgetName());
        mTvVendorNo.setText(contractModel.getVendorNo());
        mTvVendorName.setText(contractModel.getVendorName());
        mTvContractDate.setText(DateTimeUtil.ToDateDisplayString(contractModel.getContractDate()));
        mTvContractStartDate.setText(DateTimeUtil.ToDateDisplayString(contractModel.getContractStartDate()));
        mTvContractEndDate.setText(DateTimeUtil.ToDateDisplayString(contractModel.getContractEndDate()));
        mTvContractStatus.setText(contractModel.getContractStatus());
    }
}
