package com.construction.pm.models.network;

import com.construction.pm.models.ReportRequestModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportRequestResponseModel extends SimpleResponseModel {
    protected List<ReportRequestModel> mReportRequestModelList;

    public ReportRequestResponseModel() {
        super();
        mReportRequestModelList = new ArrayList<ReportRequestModel>();
    }

    public ReportRequestResponseModel(final Integer code, final String message, final ReportRequestModel[] reportRequestModels) {
        super(code, message);
        mReportRequestModelList = new ArrayList<ReportRequestModel>(Arrays.asList(reportRequestModels));
    }

    public void setReportRequestModels(final ReportRequestModel[] reportRequestModels) {
        mReportRequestModelList = new ArrayList<ReportRequestModel>(Arrays.asList(reportRequestModels));
    }

    public void addReportRequestModel(final ReportRequestModel reportRequestModel) {
        mReportRequestModelList.add(reportRequestModel);
    }

    public ReportRequestModel[] getReportRequestModels() {
        ReportRequestModel[] reportRequestModels = new ReportRequestModel[mReportRequestModelList.size()];
        mReportRequestModelList.toArray(reportRequestModels);
        return reportRequestModels;
    }

    public static ReportRequestResponseModel build(org.json.JSONObject jsonObject) throws JSONException {
        ReportRequestResponseModel reportRequestResponseModel = new ReportRequestResponseModel();

        if (!jsonObject.isNull("responseCode"))
            reportRequestResponseModel.setCode(jsonObject.getInt("responseCode"));
        if (!jsonObject.isNull("responseMessage"))
            reportRequestResponseModel.setMessage(jsonObject.getString("responseMessage"));
        if (!jsonObject.isNull("result")) {
            org.json.JSONObject jsonResultObject = jsonObject.getJSONObject("result");
            if (!jsonResultObject.isNull("reportRequest")) {
                org.json.JSONArray jsonReportRequestModels = jsonResultObject.getJSONArray("reportRequest");
                for (int reportRequestIdx = 0; reportRequestIdx < jsonReportRequestModels.length(); reportRequestIdx++) {
                    ReportRequestModel reportRequestModel = ReportRequestModel.build(jsonReportRequestModels.getJSONObject(reportRequestIdx));
                    reportRequestResponseModel.addReportRequestModel(reportRequestModel);
                }
            }
        }

        return reportRequestResponseModel;
    }

    @Override
    public org.json.JSONObject build() throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        if (getCode() != null)
            jsonObject.put("responseCode", getCode());
        if (getMessage() != null)
            jsonObject.put("responseMessage", getMessage());
        if (getReportRequestModels() != null) {
            org.json.JSONArray jsonReportRequestModels = new org.json.JSONArray();
            ReportRequestModel[] reportRequestModels = getReportRequestModels();
            for (ReportRequestModel reportRequestModel : reportRequestModels) {
                jsonReportRequestModels.put(reportRequestModel.build());
            }

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("reportRequest", jsonReportRequestModels);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
