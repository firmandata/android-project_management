package com.construction.pm.models.network;

import com.construction.pm.models.ReportRequestModel;

import org.json.JSONException;

public class ReportRequestResponseModel extends SimpleResponseModel {
    protected ReportRequestModel mReportRequestModel;

    public ReportRequestResponseModel() {
        super();
    }

    public ReportRequestResponseModel(final Integer code, final String message, final ReportRequestModel reportRequestModel) {
        super(code, message);
        mReportRequestModel = reportRequestModel;
    }

    public void setReportRequestModel(final ReportRequestModel reportRequestModel) {
        mReportRequestModel = reportRequestModel;
    }

    public ReportRequestModel getReportRequestModel() {
        return mReportRequestModel;
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
                    reportRequestResponseModel.setReportRequestModel(reportRequestModel);
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
        if (getReportRequestModel() != null) {
            org.json.JSONArray jsonReportRequestModels = new org.json.JSONArray();
            jsonReportRequestModels.put(getReportRequestModel().build());

            org.json.JSONObject jsonResultObject = new org.json.JSONObject();
            jsonResultObject.put("reportRequest", jsonReportRequestModels);

            jsonObject.put("result", jsonResultObject);
        }

        return jsonObject;
    }
}
