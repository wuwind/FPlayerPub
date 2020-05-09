package com.libwuwind.jni;

public class NativePusher {
    public native void startPush(String url);

    public native void stopPush();

    public native void release();

    public native void setVideoOptions(int width, int height, int bitrate, int fps);

    public native void setAudioOptions(int sampleRate, int channel);

    public native void fireVideo(byte[] data);

    public native void fireAudio(byte[] data, int len);

    static {
        System.loadLibrary("myffmpeg");
    }
}
