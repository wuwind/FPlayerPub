package com.wuwind.undercover.activity.main;

import android.view.View;
import android.widget.TextView;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.main.MainView.Listener;

public class MainView extends ViewDelegate<Listener> implements View.OnClickListener {
    private View cvBack;
    private View cvFront;
    private TextView tvCard;
    private TextView tvWord;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        initViews(getRootView());
        showBack(0);
    }

    private void initViews(View rootView) {
        cvBack = rootView.findViewById(R.id.cv_back);
        cvFront = rootView.findViewById(R.id.cv_front);
        tvCard = rootView.findViewById(R.id.tv_card);
        tvWord = rootView.findViewById(R.id.tv_word);
        rootView.findViewById(R.id.btn_open).setOnClickListener(this);
        rootView.findViewById(R.id.btn_next).setOnClickListener(this);
        rootView.findViewById(R.id.start).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                if (null != listener) {
                    listener.open();
                }
                break;
            case R.id.btn_next:
                if (null != listener) {
                    listener.next();
                }
                break;
            case R.id.start:
                if (null != listener) {
                    listener.start();
                }
                break;
        }
    }

    public void showBack(int num) {
        cvBack.setVisibility(View.VISIBLE);
        cvFront.setVisibility(View.GONE);
        tvCard.setText(num + "号牌");
    }

    public void showFront(String word) {
        cvBack.setVisibility(View.GONE);
        cvFront.setVisibility(View.VISIBLE);
        tvWord.setText(word);
    }


    public interface Listener extends OnClick {
        void open();

        void next();

        void start();
    }
}