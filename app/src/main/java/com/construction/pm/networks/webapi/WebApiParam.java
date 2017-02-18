package com.construction.pm.networks.webapi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WebApiParam {
    protected HashMap<String, String> mHashMap;
    protected HashMap<String, WebApiParamFile> mHashMapFile;

    public WebApiParam() {
        mHashMap = new HashMap<String, String>();
        mHashMapFile = new HashMap<String, WebApiParamFile>();
    }

    public void put(final String key, final String value) {
        mHashMap.put(key, value);
    }

    public void put(final String key, final int value) {
        mHashMap.put(key, String.valueOf(value));
    }

    public void put(final String key, final Integer value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value));
        else
            mHashMap.put(key, null);
    }

    public void put(final String key, final long value) {
        mHashMap.put(key, String.valueOf(value));
    }

    public void put(final String key, final Long value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value));
        else
            mHashMap.put(key, null);
    }

    public void put(final String key, final boolean value) {
        mHashMap.put(key, String.valueOf(value ? 1 : 0));
    }

    public void put(final String key, final Boolean value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value ? 1 : 0));
        else
            mHashMap.put(key, null);
    }

    public void put(final String key, final WebApiParamFile webApiParamFile) {
        mHashMapFile.put(key, webApiParamFile);
    }

    public void add(final String key, final String value) {
        put(key, value);
    }

    public void add(final String key, final int value) {
        put(key, value);
    }

    public void add(final String key, final Integer value) {
        put(key, value);
    }

    public void add(final String key, final long value) {
        put(key, value);
    }

    public void add(final String key, final Long value) {
        put(key, value);
    }

    public void add(final String key, final boolean value) {
        put(key, value);
    }

    public void add(final String key, final Boolean value) {
        put(key, value);
    }

    public void add(final String key, final WebApiParamFile webApiParamFile) {
        put(key, webApiParamFile);
    }

    public HashMap<String, String> getParams() {
        return mHashMap;
    }

    public HashMap<String, WebApiParamFile> getFileParams() {
        return mHashMapFile;
    }

    public boolean has(final String key) {
        return (mHashMap.containsKey(key) || mHashMapFile.containsKey(key));
    }

    public void remove(final String key) {
        mHashMap.remove(key);
        mHashMapFile.remove(key);
    }

    public void clear() {
        mHashMap.clear();
        mHashMapFile.clear();
    }

    public static class WebApiParamFile {

        protected String mMimeType;
        protected File mFile;

        public WebApiParamFile(final String mimeType, final File file) {
            mMimeType = mimeType;
            mFile = file;
        }

        public String getMimeType() {
            return mMimeType;
        }

        public File getFile() {
            return mFile;
        }
    }

    public static WebApiParam build(org.json.JSONObject jsonObject) throws org.json.JSONException {
        WebApiParam webApiParam = new WebApiParam();

        org.json.JSONArray jsonArray = jsonObject.names();
        for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
            String name = jsonArray.getString(jsonArrayIdx);
            if (!jsonObject.isNull(name))
                webApiParam.put(name, jsonObject.getString(name));
        }

        return webApiParam;
    }

    public org.json.JSONObject build() throws org.json.JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        for (Map.Entry<String, String> apiParam : getParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                String apiParamValue = apiParam.getValue();
                jsonObject.put(apiParamKey, apiParamValue);
            }
        }

        return jsonObject;
    }
}
