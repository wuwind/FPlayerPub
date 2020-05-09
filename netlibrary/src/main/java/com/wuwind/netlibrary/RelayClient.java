//package com.wuwind.netlibrary;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.util.Arrays;
//
///**
// * Created by wuhf on 2017/11/2.
// */
//
//public class RelayClient {
//
//    Socket socket;
//    InputStream inputStream;
//    OutputStream outputStream;
//    boolean isRunning;
//
//    private RelayClient(){
//
//    }
//    public static RelayClient get(){
//        return Instance.tcpClient;
//    }
//    static class Instance{
//        static RelayClient tcpClient = new RelayClient();
//    }
//
//
//    public void connectAsync(final String ip, final int port){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                connect(ip,port);
//            }
//        }).start();
//    }
//
//    public void connect(String ip, int port) {
//        if(isRunning){
//            System.out.println("is connected:"+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
//            return;
//        }
//        try {
//            socket = new Socket();
//            SocketAddress socketAddress = new InetSocketAddress(ip,port);
//            socket.connect(socketAddress,5000);
//            socket.setKeepAlive(true);
//            inputStream = socket.getInputStream();
//            outputStream = socket.getOutputStream();
//            isRunning = true;
//            System.out.println("connect:"+ip+":"+port);
//            run();
//        } catch (IOException e) {
//            e.printStackTrace();
//            close();
//        }
//    }
//
//    public static void main(String args[]){
//        RelayClient.get().connect("127.0.0.1",5000);
//        RelayClient.get().connect("127.0.0.1",5000);
//
//    }
//
//    public boolean isRunning(){
//        EventBus.getDefault().post(new RelayResponse(isRunning,"isRunning"));
//        return isRunning;
//    }
//
//    public boolean send(byte[] data) {
//        if (null == outputStream) {
//            System.out.println("outputStream is null");
//            close();
//            return false;
//        }
//        try {
//            outputStream.write(data);
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            close();
//            return false;
//        }
//        return true;
//    }
//
//    public void close() {
//        isRunning = false;
//        EventBus.getDefault().post(new RelayResponse(false,"connected"));
//        try {
//            if (inputStream != null) {
//                inputStream.close();
//                inputStream = null;
//            }
//            if (outputStream != null) {
//                outputStream.close();
//                outputStream = null;
//            }
//            if (socket != null) {
//                socket.close();
//                socket = null;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//        byte[] bytes = new byte[1024];
//        while (isRunning) {
//            try {
//                int len = inputStream.read(bytes);
//                System.out.println("recv len origin:" + len);
//                if (len > 0) {
//                    byte[] result = new byte[len];
//                    System.arraycopy(bytes, 0, result, 0, len);
//                    System.out.println("------recvsocket  origin:" + Arrays.toString(result));
//                    System.out.println("------recvsocket  origin:" + new String(result));
//                    EventBus.getDefault().post(new RelayResponse(true,new String(result)));
//                    System.out.println("recv len origin:" + len);
//                    System.out.println("recv result len:" + result.length);
//                } else {
//                    System.out.println("socket get byte <= 0  close");
//                    isRunning = false;
//                }
//            } catch (IOException e) {
//                // 连接被断开
//                isRunning = false;
//                e.printStackTrace();
//            }
//        }
//    }
//}
