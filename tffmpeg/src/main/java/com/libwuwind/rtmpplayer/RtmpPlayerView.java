package com.libwuwind.rtmpplayer;

import android.view.View;

import com.libwuwind.jni.NativePuller;
import com.libwuwind.player.R;
import com.libwuwind.rtmpplayer.RtmpPlayerView.Listener;
import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;

public class RtmpPlayerView extends ViewDelegate<Listener> implements View.OnClickListener {

    String outputurl = "rtmp://120.79.223.156:1935/live/jason?user=player&psw=123";

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_rtmp_player;
    }

    @Override
    public void initWidget() {
        get(R.id.play).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NativePuller nativePuller = new NativePuller();
        nativePuller.startPull(outputurl);
    }

    public interface Listener extends OnClick {

    }
}