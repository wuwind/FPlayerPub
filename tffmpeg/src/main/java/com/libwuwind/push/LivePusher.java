package com.libwuwind.push;

import android.view.SurfaceView;

import com.libwuwind.jni.NativePusher;
import com.libwuwind.push.param.AudioParam;
import com.libwuwind.push.param.VideoParam;

public class LivePusher {

    private VideoPusher videoPusher;
    private AudioPusher audioPusher;
    private boolean start;
    private NativePusher nativePusher;

    public LivePusher(SurfaceView surfaceView) {
        nativePusher = new NativePusher();
        audioPusher = new AudioPusher(new AudioParam(), nativePusher);
        videoPusher = new VideoPusher(new VideoParam(), surfaceView,nativePusher);
    }

    public void toggle(String url) {
        if(start)
            stopPush();
        else
            starPush(url);
    }

    public boolean isStart() {
        return start;
    }

    public void starPush(String url) {
        start = true;
        videoPusher.starPush();
        audioPusher.starPush();
        nativePusher.startPush(url);
    }

    public void stopPush() {
        start = false;
        videoPusher.stopPush();
        audioPusher.stopPush();
    }
}
