package com.wuwind.netlibrary;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.wuwind.netlibrary.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hongfengwu on 2017/2/1.
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("x-requested-with", "XMLHttpRequest")
                        .build();
                return chain.proceed(request);
            }
        });
        //cookie enabled
        builder.followRedirects(false)
                .followSslRedirects(false);
        builder.cookieJar(new CookieJar() {

            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }

        });

        mOkHttpClient = builder.build();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public void remove(){
        mInstance.mOkHttpClient.dispatcher().cancelAll();
        mInstance = null;
    }

    public OkHttpClient getmOkHttpClient(){
        return mOkHttpClient;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Object tag, Param... params) throws IOException {
        Request request = buildPostRequest(url, params, tag);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private String _post(String url, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr, null);
        return _dealResponse(request);
    }

    private String _dealResponse(Request request) {
        try {
            Response response = mOkHttpClient.newCall(request).execute();

            LogUtil.e("request tag: " + (request.tag()));
            if (response.isSuccessful()) {
                final String string = response.body().string();
                LogUtil.e("response : " + string);
                if (string.startsWith("{")) {
                    return string;
                } else {
                    return "{'success':'false','msg':'数据错误'}";
                }
            } else {
                if (response.code() == 302) {
                    final String string = response.body().string();
                    String location = response.header("Location");
                    LogUtil.e("response : " + string);
                    LogUtil.e("response url : " + location);
                    if (!string.startsWith("{"))
                        return "{'success':'false','msg':'数据错误'}";
                    StringBuilder sb = new StringBuilder();
                    String result = string.substring(0, string.lastIndexOf("}"));
                    sb.append(result).append(",'location':'").append(location).append("'}");
                    LogUtil.e("response result : " + sb.toString());
                    return sb.toString();
                }
                return "{'success':'false','msg':'服务器忙'}";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "{'success':'false','msg':'请求失败'}";
        }
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Object tag, Param... params) {
        Request request = buildPostRequest(url, params, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params, Object tag) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr, tag);
        deliveryResult(callback, request);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private String _post(String url, File file, String fileKey, Map map) throws IOException {
        Param[] params = map2Params(map);
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return _dealResponse(request);
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Map<String, String> params) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, paramsArr);
        deliveryResult(callback, request);
    }


    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    //TODO
//                    ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
//                    ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
//                    int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
//                    try {
//                        is.reset();
//                    } catch (IOException e) {
//                        response = _getAsyn(url);
//                        is = response.body().byteStream();
//                    }
//
//                    BitmapFactory.Options ops = new BitmapFactory.Options();
//                    ops.inJustDecodeBounds = false;
//                    ops.inSampleSize = inSampleSize;
//                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
//                    mDelivery.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            view.setImageBitmap(bm);
//                        }
//                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }


    //*************对外公布的方法************


    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }


    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String post(String url, Map<String, String> params) {
        return getInstance()._post(url, params);
    }

    /**
     * 同步文件
     * @param url
     * @param file
     * @param fileKey
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url,File file, String fileKey, Map<String, String> params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }


    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }


    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params, Object tag) {
        getInstance()._postAsyn(url, callback, params, tag);
    }


    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }


    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }


    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Map<String, String> params) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance()._displayImage(view, url, errorResId);
    }


    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, -1);
    }

    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    //****************************


    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                Object o = mGson.fromJson("{'success':'false','msg':'请检查网络'}", callback.mType);
                com.wuwind.netlibrary.Response res = (com.wuwind.netlibrary.Response) o;
                res.tag = call.request().tag();
                sendSuccessResultCallback(o, callback);
                sendFailedStringCallback(call.request(), e, callback);
                LogUtil.e("onFailure");
            }

            @Override
            public void onResponse(final Call call, Response response) {
                try {

//                    LogUtil.e("response tag: " + (call.request().tag()));
                    if (response.isSuccessful()) {
                        final String string = response.body().string();
                        LogUtil.e("response url:" + call.request().url());
                        LogUtil.e("response : " + string);
                        if (callback.mType == String.class) {
                            sendSuccessResultCallback(string, callback);
                        } else {
                            if (string.startsWith("{")) {
                                Object o = mGson.fromJson(string, callback.mType);
                                com.wuwind.netlibrary.Response res = (com.wuwind.netlibrary.Response) o;
                                res.tag = call.request().tag();
                                sendSuccessResultCallback(o, callback);
                            }
                        }
                    } else {
                        if (response.code() == 302) {
                            final String string = response.body().string();
                            String location = response.header("Location");
                            LogUtil.e("response : " + string);
                            LogUtil.e("response url : " + location);
                            if(!string.startsWith("{"))
                                return;
                            StringBuilder sb = new StringBuilder();
                            String result = string.substring(0, string.lastIndexOf("}"));
                            sb.append(result).append(",'location':'").append(location).append("'}");
                            LogUtil.e("response result : " + sb.toString());
                            Object o = mGson.fromJson(sb.toString(), callback.mType);
                            com.wuwind.netlibrary.Response res = (com.wuwind.netlibrary.Response) o;
                            res.tag = call.request().tag();
                            sendSuccessResultCallback(o, callback);
                            return;
                        }
                        Object o = mGson.fromJson("{'success':'false','msg':'服务器忙'}", callback.mType);
                        LogUtil.e(response.message() + response.code());
                        com.wuwind.netlibrary.Response res = (com.wuwind.netlibrary.Response) o;
                        res.tag = call.request().tag();
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .tag(tag)
                .post(requestBody)
                .build();
    }


    public static abstract class ResultCallback<T> {

        public Type mType;

        public ResultCallback(Type mType) {
            this.mType = mType;
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }


}

