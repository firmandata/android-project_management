package com.construction.pm.views.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.construction.pm.R;

public class ContractDetailView {
    protected Context mContext;

    protected RelativeLayout mContractDetailView;

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
    }

    public RelativeLayout getView() {
        return mContractDetailView;
    }
}
