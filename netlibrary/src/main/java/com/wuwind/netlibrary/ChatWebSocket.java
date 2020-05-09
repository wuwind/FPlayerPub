//package com.wuwind.netlibrary;
//
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.wuwind.netlibrary.utils.LogUtil;
//
//import org.json.JSONObject;
//
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLSession;
//
//import okhttp3.OkHttpClient;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;
//
///**
// * Created by hongfengwu on 2017/10/5.
// */
//
//public final class ChatWebSocket extends WebSocketListener {
//    private WebSocket _WebSocket = null;
//    private JSONObject _JsonObject = null;
//
//    private String url = UrlConst.SOCKET_URL + "/websocket/business/message.do";
//    private static ChatWebSocket mChatWebSocket = null;
//
//    Gson gson;
//
//    @Override
//    public void onOpen(WebSocket webSocket, Response response) {
//        _WebSocket = webSocket;
//        LogUtil.e("ChatWebSocket onOpen");
//        if (null == gson) {
//            gson = new Gson();
//        }
//    }
//
//    @Override
//    public void onMessage(WebSocket webSocket, String text) {
//        LogUtil.e("ChatWebSocket MESSAGE:" + text);
//        ChatWebResponse chatWebResponse = gson.fromJson(text, ChatWebResponse.class);
//        EventBus.getDefault().post(chatWebResponse);
////        try {
////            _JsonObject = new JSONObject(text);
////        }catch (JSONException e){
////            e.printStackTrace();
////        }
//
//    }
//
//    @Override
//    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        LogUtil.e("ChatWebSocket MESSAGE: " + bytes.hex());
//    }
//
//    @Override
//    public void onClosing(WebSocket webSocket, int code, String reason) {
//        webSocket.close(1000, null);
//        mChatWebSocket = null;
//        LogUtil.e("ChatWebSocket onClosing:" + code + reason);
//    }
//
//    @Override
//    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        t.printStackTrace();
//        mChatWebSocket = null;
//        LogUtil.e("ChatWebSocket onFailure");
//    }
//
//    /**
//     * 初始化WebSocket服务器
//     */
//    private void run() {
//        OkHttpClient client;// = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
//        client = OkHttpClientManager.getInstance().getmOkHttpClient();
////        client = new OkHttpClient.Builder().cookieJar(client.cookieJar()).readTimeout(0, TimeUnit.MILLISECONDS).build();
//
//
//        client = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
//                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
//                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
//                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
//                .cookieJar(client.cookieJar())//Cookies持久化
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
//                .build();
//
//        Request request = new Request.Builder().url(url).build();
//        client.newWebSocket(request, this);
//        client.dispatcher().executorService().shutdown();
//    }
//
//    /**
//     * @param s
//     * @return
//     */
//    public boolean sendMessage(String s) {
//        return _WebSocket.send(s);
//    }
//
//    public void closeWebSocket() {
//        mChatWebSocket = null;
//        _WebSocket.close(1000, "主动关闭");
//        Log.e("ChatWebSocket close", "关闭成功");
//    }
//
//    /**
//     * 获取全局的ChatWebSocket类
//     *
//     * @return ChatWebSocket
//     */
//    public static ChatWebSocket getChartWebSocket() {
//        if (mChatWebSocket == null) {
//            mChatWebSocket = new ChatWebSocket();
//            mChatWebSocket.run();
//        }
//        return mChatWebSocket;
//    }
//
//}