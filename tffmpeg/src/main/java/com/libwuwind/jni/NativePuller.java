package com.libwuwind.jni;

public class NativePuller {

    public native void startPull(String url);

    static {
        System.loadLibrary("myffmpeg");
    }
}
