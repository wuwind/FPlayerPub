package com.libwuwind.activity.capture;

import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.libwuwind.activity.capture.PushView.Listener;
import com.libwuwind.player.R;
import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;

public class PushView extends ViewDelegate<Listener> implements View.OnClickListener {

    private SurfaceView surface;
    private Button start;
    private Button push;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_push;
    }

    @Override
    public void initWidget() {
        surface = get(R.id.surface);
        start = get(R.id.start);
        push = get(R.id.push);
        start.setOnClickListener(this);
        push.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                listener.start();
                break;
            case R.id.push:
                listener.push();
                break;
        }

    }

    public SurfaceView getSurfaceView() {
        return surface;
    }

    public interface Listener extends OnClick {
        void start();

        void push();
    }
}
