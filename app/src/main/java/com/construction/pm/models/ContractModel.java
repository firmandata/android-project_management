package com.construction.pm.models;

import com.construction.pm.utils.DateTimeUtil;

import org.json.JSONException;

import java.util.Calendar;

public class ContractModel {

    protected String mContractNo;
    protected String mParentContractNo;
    protected String mContractName;
    protected Double mContractAmount;
    protected String mProgramNo;
    protected String mProgramName;
    protected Double mProgramAmount;
    protected String mBudgetNo;
    protected String mBudgetName;
    protected String mVendorNo;
    protected String mVendorName;
    protected Calendar mContractDate;
    protected Calendar mContractStartDate;
    protected Calendar mContractEndDate;
    protected String mContractStatus;
    protected Integer mCreatorId;
    protected Calendar mCreateDate;
    protected Integer mLastUserId;
    protected Calendar mLastUpdate;

    public ContractModel() {

    }

    public void setContractNo(final String contractNo) {
        mContractNo = contractNo;
    }

    public String getContractNo() {
        return mContractNo;
    }

    public void setParentContractNo(final String parentContractNo) {
        mParentContractNo = parentContractNo;
    }

    public String getParentContractNo() {
        return mParentContractNo;
    }

    public void setContractName(final String contractName) {
        mContractName = contractName;
    }

    public String getContractName() {
        return mContractName;
    }

    public void setContractAmount(final Double contractAmount) {
        mContractAmount = contractAmount;
    }

    public Double getContractAmount() {
        return mContractAmount;
    }

    public void setProgramNo(final String programNo) {
        mProgramNo = programNo;
    }

    public String getProgramNo() {
        return mProgramNo;
    }

    public void setProgramName(final String programName) {
        mProgramName = programName;
    }

    public String getProgramName() {
        return mProgramName;
    }

    public void setProgramAmount(final Double programAmount) {
        mProgramAmount = programAmount;
    }

    public Double getProgramAmount() {
        return mProgramAmount;
    }

    public void setBudgetNo(final String budgetNo) {
        mBudgetNo = budgetNo;
    }

    public String getBudgetNo() {
        return mBudgetNo;
    }

    public void setBudgetName(final String budgetName) {
        mBudgetName = budgetName;
    }

    public String getBudgetName() {
        return mBudgetName;
    }

    public void setVendorNo(final String vendorNo) {
        mVendorNo = vendorNo;
    }

    public String getVendorNo() {
        return mVendorNo;
    }

    public void setVendorName(final String vendorName) {
        mVendorName = vendorName;
    }

    public String getVendorName() {
        return mVendorName;
    }

    public void setContractDate(final Calendar contractDate) {
        mContractDate = contractDate;
    }

    public Calendar getContractDate() {
        return mContractDate;
    }

    public void setContractStartDate(final Calendar contractStartDate) {
        mContractStartDate = contractStartDate;
    }

    public Calendar getContractStartDate() {
        return mContractStartDate;
    }

    public void setContractEndDate(final Calendar contractEndDate) {
        mContractEndDate = contractEndDate;
    }

    public Calendar getContractEndDate() {
        return mContractEndDate;
    }

    public void setContractStatus(final String contractStatus) {
        mContractStatus = contractStatus;
    }

    public String getContractStatus() {
        return mContractStatus;
    }

    public void setCreatorId(final Integer creatorId) {
        mCreatorId = creatorId;
    }

    public Integer getCreatorId() {
        return mCreatorId;
    }

    public void setCreateDate(final Calendar createDate) {
        mCreateDate = createDate;
    }

    public Calendar getCreateDate() {
        return mCreateDate;
    }

    public void setLastUserId(final Integer lastUserId) {
        mLastUserId = lastUserId;
    }

    public Integer getLastUserId() {
        return mLastUserId;
    }

    public void setLastUpdate(final Calendar lastUpdate) {
        mLastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return mLastUpdate;
    }

    public static ContractModel build(final org.json.JSONObject jsonObject) throws JSONException {
        ContractModel contractModel = new ContractModel();

        if (!jsonObject.isNull("contract_no"))
            contractModel.setContractNo(jsonObject.getString("contract_no"));
        if (!jsonObject.isNull("parent_contract_no"))
            contractModel.setParentContractNo(jsonObject.getString("parent_contract_no"));
        if (!jsonObject.isNull("contract_name"))
            contractModel.setContractName(jsonObject.getString("contract_name"));
        if (!jsonObject.isNull("contract_amount"))
            contractModel.setContractAmount(jsonObject.getDouble("contract_amount"));
        if (!jsonObject.isNull("program_no"))
            contractModel.setProgramNo(jsonObject.getString("program_no"));
        if (!jsonObject.isNull("program_name"))
            contractModel.setProgramName(jsonObject.getString("program_name"));
        if (!jsonObject.isNull("program_amount"))
            contractModel.setProgramAmount(jsonObject.getDouble("program_amount"));
        if (!jsonObject.isNull("budget_no"))
            contractModel.setBudgetNo(jsonObject.getString("budget_no"));
        if (!jsonObject.isNull("budget_name"))
            contractModel.setBudgetName(jsonObject.getString("budget_name"));
        if (!jsonObject.isNull("vendor_no"))
            contractModel.setVendorNo(jsonObject.getString("vendor_no"));
        if (!jsonObject.isNull("vendor_name"))
            contractModel.setVendorName(jsonObject.getString("vendor_name"));
        if (!jsonObject.isNull("contract_date"))
            contractModel.setContractDate(DateTimeUtil.FromDateString(jsonObject.getString("contract_date")));
        if (!jsonObject.isNull("contract_start_date"))
            contractModel.setContractStartDate(DateTimeUtil.FromDateString(jsonObject.getString("contract_start_date")));
        if (!jsonObject.isNull("contract_end_date"))
            contractModel.setContractEndDate(DateTimeUtil.FromDateString(jsonObject.getString("contract_end_date")));
        if (!jsonObject.isNull("contract_status"))
            contractModel.setContractStatus(jsonObject.getString("contract_status"));
        if (!jsonObject.isNull("creator_id"))
            contractModel.setCreatorId(jsonObject.getInt("creator_id"));
        if (!jsonObject.isNull("create_date"))
            contractModel.setCreateDate(DateTimeUtil.FromDateTimeString(jsonObject.getString("create_date")));
        if (!jsonObject.isNull("last_user_id"))
            contractModel.setLastUserId(jsonObject.getInt("last_user_id"));
        if (!jsonObject.isNull("last_update"))
            contractModel.setLastUpdate(DateTimeUtil.FromDateTimeString(jsonObject.getString("last_update")));

        return contractModel;
    }

    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getContractNo() != null)
            jsonObject.put("contract_no", getContractNo());
        if (getParentContractNo() != null)
            jsonObject.put("parent_contract_no", getParentContractNo());
        if (getContractName() != null)
            jsonObject.put("contract_name", getContractName());
        if (getContractAmount() != null)
            jsonObject.put("contract_amount", getContractAmount());
        if (getProgramNo() != null)
            jsonObject.put("program_no", getProgramNo());
        if (getProgramName() != null)
            jsonObject.put("program_name", getProgramName());
        if (getProgramAmount() != null)
            jsonObject.put("program_amount", getProgramAmount());
        if (getBudgetNo() != null)
            jsonObject.put("budget_no", getBudgetNo());
        if (getBudgetName() != null)
            jsonObject.put("budget_name", getBudgetName());
        if (getVendorNo() != null)
            jsonObject.put("vendor_no", getVendorNo());
        if (getVendorName() != null)
            jsonObject.put("vendor_name", getVendorName());
        if (getContractDate() != null)
            jsonObject.put("contract_date", DateTimeUtil.ToDateString(getContractDate()));
        if (getContractStartDate() != null)
            jsonObject.put("contract_start_date", DateTimeUtil.ToDateString(getContractStartDate()));
        if (getContractEndDate() != null)
            jsonObject.put("contract_end_date", DateTimeUtil.ToDateString(getContractEndDate()));
        if (getContractStatus() != null)
            jsonObject.put("contract_status", getContractStatus());
        if (getCreatorId() != null)
            jsonObject.put("creator_id", getCreatorId());
        if (getCreateDate() != null)
            jsonObject.put("create_date", DateTimeUtil.ToDateTimeString(getCreateDate()));
        if (getLastUserId() != null)
            jsonObject.put("last_user_id", getLastUserId());
        if (getLastUpdate() != null)
            jsonObject.put("last_update", DateTimeUtil.ToDateTimeString(getLastUpdate()));

        return jsonObject;
    }
}
