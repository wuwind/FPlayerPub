package com.wuwind.undercover.activity.launcher;

import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.launcher.LauncherView.Listener;

public class LauncherView extends ViewDelegate<Listener> implements View.OnClickListener {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public void initWidget() {
        rootView.findViewById(R.id.btn_words).setOnClickListener(this);
        rootView.findViewById(R.id.btn_record).setOnClickListener(this);
        rootView.findViewById(R.id.btn_new).setOnClickListener(this);
        rootView.findViewById(R.id.btn_room).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_words:
                listener.wordClick();
                break;
            case R.id.btn_record:
                listener.recordClick();
                break;
            case R.id.btn_new:
                listener.newClick();
                break;
            case R.id.btn_room:
                listener.roomClick();
                break;
        }
    }

    public interface Listener extends OnClick {
        void wordClick();

        void recordClick();

        void newClick();

        void roomClick();
    }
}