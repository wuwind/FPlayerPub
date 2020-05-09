package com.libwuwind.test;

import android.view.View;
import android.widget.ImageView;

import com.libwuwind.player.R;
import com.libwuwind.test.TestView.Listener;
import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;

public class TestView extends ViewDelegate<Listener> implements View.OnClickListener {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_test;
    }

    public ImageView image;
    @Override
    public void initWidget() {
        image = get(R.id.image);
    }

    @Override
    public void onClick(View v) {

    }

    public interface Listener extends OnClick {

    }
}