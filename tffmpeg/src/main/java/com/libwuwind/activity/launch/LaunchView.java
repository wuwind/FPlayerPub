package com.libwuwind.activity.launch;

import android.view.View;

import com.libwuwind.activity.launch.LaunchView.Listener;
import com.libwuwind.player.R;
import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;

public class LaunchView extends ViewDelegate<Listener> implements View.OnClickListener {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void onClick(View v) {

    }

    public interface Listener extends OnClick {

    }
}