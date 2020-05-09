package com.wuwind.uisdk.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface IViewDelegate {
    void create(LayoutInflater inflater, ViewGroup container);

    View getRootView();

    void initWidget();
}
