package com.wuwind.netlibrary;


import com.wuwind.netlibrary.utils.LogUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by hongfengwu on 2017/10/4.
 */

public class MsgRequest {

    private String url = "ws://121.42.34.181:8000/witness/websocket/business/message.do";
    public void request(){

        OkHttpClient client0 = OkHttpClientManager.getInstance().getmOkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(client0.cookieJar()).build();
        Request request = new Request.Builder().url(url).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                LogUtil.e("onpen------");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                LogUtil.e("onMessage------");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                LogUtil.e("onMessage------");
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                LogUtil.e("onClosing------");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                LogUtil.e("onClosed------");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                LogUtil.e("onFailure------");
                t.printStackTrace();
            }
        });
        client.dispatcher().executorService().shutdown();

    }
}
