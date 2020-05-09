package com.wuwind.netlibrary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wuhf on 2017/11/2.
 */

public class TcpServer {

    List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args){
        new TcpServer().startServer(5000);
    }

    public TcpServer() {

    }

    public void startServer(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            Socket accept = server.accept();
            getClient(accept);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getClient(Socket socket) {
        sockets.add(socket);
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(new byte[]{1,1});
            byte[] bytes = new byte[1024];
            while (true){
                try {
                    int len = inputStream.read(bytes);
                    System.out.println("recv len origin:" + len);
                    if (len > 0) {
                        byte[] result = new byte[len];
                        System.arraycopy(bytes, 0, result, 0, len);
                        System.out.println("------recvsocket  origin:" + Arrays.toString(result));
                        System.out.println("recv len origin:" + len);
                        System.out.println("recv result len:" + result.length);
                    } else {
                        System.out.println("socket get byte <= 0  close");
                    }
                } catch (IOException e) {
                    // 连接被断开
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
