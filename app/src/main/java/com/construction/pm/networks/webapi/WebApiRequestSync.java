package com.construction.pm.networks.webapi;

import android.content.Context;

import com.construction.pm.R;
import com.construction.pm.utils.ViewUtil;
import com.construction.pm.networks.webapi.persistentcookiejar.ClearableCookieJar;
import com.construction.pm.networks.webapi.persistentcookiejar.PersistentCookieJar;
import com.construction.pm.networks.webapi.persistentcookiejar.cache.SetCookieCache;
import com.construction.pm.networks.webapi.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.construction.pm.models.system.SessionLoginModel;
import com.construction.pm.models.system.SettingUserModel;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class WebApiRequestSync {

    protected Context mContext;

    protected static final int CONNECT_TIMEOUT_SEC = 10;
    protected static final int READ_TIMEOUT_SEC = 60;
    protected static final int WRITE_TIMEOUT_SEC = 60;

    protected SessionLoginModel mSessionLoginModel;
    protected SettingUserModel mSettingUserModel;

    public WebApiRequestSync(final Context context) {
        mContext = context;
    }

    public void setSettingUser(final SettingUserModel settingUserModel) {
        mSettingUserModel = settingUserModel;
    }

    public void setSession(final SessionLoginModel sessionLoginModel) {
        mSessionLoginModel = sessionLoginModel;
    }

    public void get(final String api, final WebApiParam queryParam, final WebApiResponse webApiResponse) {
        webApiResponse.onStart();

        if (mSettingUserModel == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_user_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        if (mSettingUserModel.getServerUrl() == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        // -- Initialize base parameters --
        WebApiParam newWebApiParam = queryParam;
        if (newWebApiParam == null)
            newWebApiParam = new WebApiParam();

        // -- Initialize http URL --
        HttpUrl httpUrlApi = HttpUrl.parse(mSettingUserModel.getServerUrl() + api);
        if (httpUrlApi == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_not_valid, mSettingUserModel.getServerUrl() + api)));
            webApiResponse.onFinish();
            return;
        }
        HttpUrl.Builder httpUrlBuilder = httpUrlApi.newBuilder();
        for (Map.Entry<String, String> apiParam : newWebApiParam.getParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                String apiParamValue = apiParam.getValue();
                if (apiParamValue != null)
                    httpUrlBuilder.addQueryParameter(apiParamKey, apiParamValue);
            }
        }
        HttpUrl httpUrl = httpUrlBuilder.build();

        // -- Initialize requester --
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        Request request = requestBuilder.build();

        // -- Initialize cookie --
        ClearableCookieJar clearableCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));

        // -- Initialize http client --
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.cookieJar(clearableCookieJar);
        okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Response.Builder responseBuilder = response.newBuilder();
                responseBuilder.body(new ProgressResponseBody(response.body(), new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        webApiResponse.onProgress(bytesRead, contentLength);
                    }
                }));

                return responseBuilder.build();
            }
        });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        // -- Call http --
        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            try {
                ResponseBody responseBody = response.body();
                String responseString = responseBody.string();

                org.json.JSONTokener jsonTokener = new org.json.JSONTokener(responseString);
                org.json.JSONObject jsonObject = null;
                org.json.JSONArray jsonArray = null;

                try {
                    jsonObject = (org.json.JSONObject) jsonTokener.nextValue();
                } catch (Exception ex) {
                }

                if (jsonObject == null) {
                    try {
                        jsonArray = (org.json.JSONArray) jsonTokener.nextValue();
                    } catch (Exception ex) {
                    }
                }

                if (jsonObject != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonObject);
                } else if (jsonArray != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonArray);
                } else {
                    webApiResponse.onSuccess(response.code(), response.headers(), responseString);
                }
            } catch (IOException ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            } catch (Exception ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            }
        } catch (Exception ex) {
            webApiResponse.onFailure(0, null, null, ex);
        }

        webApiResponse.onFinish();
    }

    public void post(final String api, final WebApiParam queryParam, final WebApiParam formData, final WebApiResponse webApiResponse) {
        webApiResponse.onStart();

        if (mSettingUserModel == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_user_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        if (mSettingUserModel.getServerUrl() == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        // -- Initialize base parameters --
        WebApiParam newWebApiParam = queryParam;
        if (newWebApiParam == null)
            newWebApiParam = new WebApiParam();
        WebApiParam newFormData = formData;
        if (newFormData == null)
            newFormData = new WebApiParam();

        // -- Initialize http URL --
        HttpUrl httpUrlApi = HttpUrl.parse(mSettingUserModel.getServerUrl() + api);
        if (httpUrlApi == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_not_valid, mSettingUserModel.getServerUrl() + api)));
            webApiResponse.onFinish();
            return;
        }
        HttpUrl.Builder httpUrlBuilder = httpUrlApi.newBuilder();
        for (Map.Entry<String, String> apiParam : newWebApiParam.getParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                String apiParamValue = apiParam.getValue();
                if (apiParamValue != null)
                    httpUrlBuilder.addQueryParameter(apiParamKey, apiParamValue);
            }
        }
        HttpUrl httpUrl = httpUrlBuilder.build();

        // -- Initialize post data --
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> apiParam : newFormData.getParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                String apiParamValue = apiParam.getValue();
                if (apiParamValue != null)
                    formBodyBuilder.add(apiParamKey, apiParamValue);
            }
        }
        RequestBody requestBody = formBodyBuilder.build();

        // -- Initialize requester --
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();

        // -- Initialize cookie --
        ClearableCookieJar clearableCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));

        // -- Initialize http client --
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.cookieJar(clearableCookieJar);
        okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Response.Builder responseBuilder = response.newBuilder();
                responseBuilder.body(new ProgressResponseBody(response.body(), new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        webApiResponse.onProgress(bytesRead, contentLength);
                    }
                }));

                return responseBuilder.build();
            }
        });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        // -- Call http --
        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            try {
                ResponseBody responseBody = response.body();
                String responseString = responseBody.string();

                org.json.JSONTokener jsonTokener = new org.json.JSONTokener(responseString);
                org.json.JSONObject jsonObject = null;
                org.json.JSONArray jsonArray = null;

                try {
                    jsonObject = (org.json.JSONObject) jsonTokener.nextValue();
                } catch (Exception ex) {
                }

                if (jsonObject == null) {
                    try {
                        jsonArray = (org.json.JSONArray) jsonTokener.nextValue();
                    } catch (Exception ex) {
                    }
                }

                if (jsonObject != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonObject);
                } else if (jsonArray != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonArray);
                } else {
                    webApiResponse.onSuccess(response.code(), response.headers(), responseString);
                }
            } catch (IOException ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            } catch (Exception ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            }
        } catch (Exception ex) {
            webApiResponse.onFailure(0, null, null, ex);
        }

        webApiResponse.onFinish();
    }

    public void post(final String api, final WebApiParam queryParam, final String jsonData, final WebApiResponse webApiResponse) {
        webApiResponse.onStart();

        if (mSettingUserModel == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_user_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        if (mSettingUserModel.getServerUrl() == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_config_not_set)));
            webApiResponse.onFinish();
            return;
        }

        // -- Initialize base parameters --
        WebApiParam newWebApiParam = queryParam;
        if (newWebApiParam == null)
            newWebApiParam = new WebApiParam();

        // -- Initialize http URL --
        HttpUrl httpUrlApi = HttpUrl.parse(mSettingUserModel.getServerUrl() + api);
        if (httpUrlApi == null) {
            webApiResponse.onFailure(0, null, null, new Exception(ViewUtil.getResourceString(mContext, R.string.restful_server_url_not_valid, mSettingUserModel.getServerUrl() + api)));
            webApiResponse.onFinish();
            return;
        }
        HttpUrl.Builder httpUrlBuilder = httpUrlApi.newBuilder();
        for (Map.Entry<String, String> apiParam : newWebApiParam.getParams().entrySet()) {
            String apiParamKey = apiParam.getKey();
            if (apiParamKey != null) {
                String apiParamValue = apiParam.getValue();
                if (apiParamValue != null)
                    httpUrlBuilder.addQueryParameter(apiParamKey, apiParamValue);
            }
        }
        HttpUrl httpUrl = httpUrlBuilder.build();

        // -- Initialize post data --
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);

        // -- Initialize requester --
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();

        // -- Initialize cookie --
        ClearableCookieJar clearableCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));

        // -- Initialize http client --
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClientBuilder.cookieJar(clearableCookieJar);
        okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Response.Builder responseBuilder = response.newBuilder();
                responseBuilder.body(new ProgressResponseBody(response.body(), new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        webApiResponse.onProgress(bytesRead, contentLength);
                    }
                }));

                return responseBuilder.build();
            }
        });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        // -- Call http --
        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            try {
                ResponseBody responseBody = response.body();
                String responseString = responseBody.string();

                org.json.JSONTokener jsonTokener = new org.json.JSONTokener(responseString);
                org.json.JSONObject jsonObject = null;
                org.json.JSONArray jsonArray = null;

                try {
                    jsonObject = (org.json.JSONObject) jsonTokener.nextValue();
                } catch (Exception ex) {
                }

                if (jsonObject == null) {
                    try {
                        jsonArray = (org.json.JSONArray) jsonTokener.nextValue();
                    } catch (Exception ex) {
                    }
                }

                if (jsonObject != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonObject);
                } else if (jsonArray != null) {
                    webApiResponse.onSuccess(response.code(), response.headers(), jsonArray);
                } else {
                    webApiResponse.onSuccess(response.code(), response.headers(), responseString);
                }
            } catch (IOException ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            } catch (Exception ex) {
                webApiResponse.onFailure(response.code(), response.headers(), response.message(), ex);
            }
        } catch (Exception ex) {
            webApiResponse.onFailure(0, null, null, ex);
        }

        webApiResponse.onFinish();
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(final ResponseBody responseBody, final ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(final Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
