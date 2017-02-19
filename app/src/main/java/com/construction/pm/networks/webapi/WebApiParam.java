package com.construction.pm.networks.webapi;

import android.util.Base64;

import org.json.JSONException;

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

    public void put(final String key, final Double value) {
        if (value != null)
            mHashMap.put(key, String.valueOf(value));
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

    public void add(final String key, final Double value) {
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

        protected String mFileName;
        protected String mMimeType;
        protected byte[] mFileData;

        public WebApiParamFile() {

        }

        public WebApiParamFile(final String fileName, final String mimeType, final byte[] fileData) {
            this();

            mFileName = fileName;
            mMimeType = mimeType;
            mFileData = fileData;
        }

        public void setFileName(final String fileName) {
            mFileName = fileName;
        }

        public String getFileName() {
            return mFileName;
        }

        public void setMimeType(final String mimeType) {
            mMimeType = mimeType;
        }

        public String getMimeType() {
            return mMimeType;
        }

        public void setFileData(final byte[] fileData) {
            mFileData = fileData;
        }

        public byte[] getFileData() {
            return mFileData;
        }

        public static WebApiParamFile build(final org.json.JSONObject jsonObject) throws JSONException {
            WebApiParamFile webApiParamFile = new WebApiParamFile();

            if (!jsonObject.isNull("file_name"))
                webApiParamFile.setFileName(jsonObject.getString("file_name"));
            if (!jsonObject.isNull("mime_type"))
                webApiParamFile.setMimeType(jsonObject.getString("mime_type"));
            if (!jsonObject.isNull("file_data")) {
                String base64Encode = jsonObject.getString("file_data");
                byte[] fileData = null;
                try {
                    fileData = Base64.decode(base64Encode, Base64.NO_WRAP);
                } catch (Exception ex) {
                }
                if (fileData != null)
                    webApiParamFile.setFileData(fileData);
            }

            return webApiParamFile;
        }

        public org.json.JSONObject build() throws JSONException {
            org.json.JSONObject jsonObject = new org.json.JSONObject();

            if (getFileName() != null)
                jsonObject.put("file_name", getFileName());
            if (getMimeType() != null)
                jsonObject.put("file_type", getMimeType());
            if (getFileData() != null) {
                String fileData = null;
                try {
                    fileData = Base64.encodeToString(getFileData(), Base64.NO_WRAP);
                } catch (Exception ex) {
                }
                if (fileData != null)
                    jsonObject.put("file_data", fileData);
            }

            return jsonObject;
        }
    }

    public static WebApiParam build(org.json.JSONObject jsonObject) throws org.json.JSONException {
        WebApiParam webApiParam = new WebApiParam();

        org.json.JSONArray jsonArray = jsonObject.names();
        for (int jsonArrayIdx = 0; jsonArrayIdx < jsonArray.length(); jsonArrayIdx++) {
            String name = jsonArray.getString(jsonArrayIdx);
            if (!jsonObject.isNull(name)) {
                if (name.startsWith("_WebApiParamFile_"))
                    webApiParam.put(name.substring(17), WebApiParamFile.build(jsonObject.getJSONObject(name)));
                else
                    webApiParam.put(name, jsonObject.getString(name));
            }
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
        for (Map.Entry<String, WebApiParamFile> apiParam : getFileParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                WebApiParamFile apiParamValue = apiParam.getValue();
                jsonObject.put("_WebApiParamFile_" + apiParamKey, apiParamValue);
            }
        }

        return jsonObject;
    }
}
