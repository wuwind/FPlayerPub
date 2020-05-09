package com.wuwind.ui.base;

import com.wuwind.uisdk.view.AbstractViewDelegate;

public abstract class ViewDelegate<T extends OnClick> extends AbstractViewDelegate {

    protected T listener;

    public void setListener(T listener) {
        this.listener = listener;
    }
}
