package com.construction.pm.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.pm.models.ContractModel;
import com.construction.pm.views.contract.ContractDetailView;

public class ContractDetailFragment extends Fragment {

    public static final String PARAM_CONTRACT_MODEL = "ContractModel";

    protected ContractDetailView mContractDetailView;

    public static ContractDetailFragment newInstance() {
        return newInstance(null);
    }

    public static ContractDetailFragment newInstance(final ContractModel contractModel) {
        // -- Set parameters --
        Bundle bundle = new Bundle();
        if (contractModel != null) {
            try {
                org.json.JSONObject contractModelJsonObject = contractModel.build();
                String projectModelJson = contractModelJsonObject.toString(0);
                bundle.putString(PARAM_CONTRACT_MODEL, projectModelJson);
            } catch (org.json.JSONException ex) {
            }
        }

        // -- Create ContractDetailFragment --
        ContractDetailFragment contractDetailFragment = new ContractDetailFragment();
        contractDetailFragment.setArguments(bundle);
        return contractDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContractModel contractModel = null;

        // -- Get parameters --
        Bundle bundle = getArguments();
        if (bundle != null) {
            // -- Get ContractModel parameter --
            String contractModelJson = bundle.getString(PARAM_CONTRACT_MODEL);
            if (contractModelJson != null) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(contractModelJson);
                    contractModel = ContractModel.build(jsonObject);
                } catch (org.json.JSONException ex) {
                }
            }
        }

        // -- Prepare ContractDetailView --
        mContractDetailView = ContractDetailView.buildContractDetailView(getContext(), null);
        if (contractModel != null)
            mContractDetailView.setContractModel(contractModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- Load ContractDetailView to fragment --
        return mContractDetailView.getView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setContractModel(final ContractModel contractModel) {
        if (mContractDetailView != null)
            mContractDetailView.setContractModel(contractModel);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
