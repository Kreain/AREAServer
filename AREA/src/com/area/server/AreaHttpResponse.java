package com.area.server;

import org.json.JSONObject;

public class AreaHttpResponse {

    protected JSONObject requestData = null;
    private JSONObject responseData = null;
    private boolean responseCode = false;
    private String errorMessage = null;

    public AreaHttpResponse(AreaHttpRequest request) {
        if (request != null)
            requestData = request.getData();
    }

    public JSONObject getResponse() {
        JSONObject response = new JSONObject();

        response.put("responseCode", responseCode ? "SUCCESS" : "ERROR");
        if (responseData != null)
            response.put("data", responseData);
        return (response);
    }

    public void setResponseData(JSONObject rData) {
        responseData = rData;
    }

    public void setSuccessData(JSONObject rData) {
        responseCode = true;
        responseData = rData;
    }

    public void setErrorData(JSONObject rData) {
        responseCode = false;
        responseData = rData;
    }

    public void setResponseCode(boolean rCode) {
        responseCode = rCode;
    }
}
