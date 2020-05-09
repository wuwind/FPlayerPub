package com.wuwind.uisdk.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wuwind.uisdk.model.IModelDelegate;
import com.wuwind.uisdk.view.IViewDelegate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractFragmentPresenter<V extends IViewDelegate, M extends IModelDelegate> extends Fragment implements IPresenter {
    protected V viewDelegate;
    protected M modelDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Class<V> view = (Class<V>) parameterized.getActualTypeArguments()[0];
        Class<M> model = (Class<M>) parameterized.getActualTypeArguments()[1];
        try {
            viewDelegate = view.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("create IViewDelegate error");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("create IViewDelegate error");
        }
        try {
            modelDelegate = model.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("create IModelDelegate error");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("create IModelDelegate error");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewDelegate.create(inflater, container);
        return viewDelegate.getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDelegate.initWidget();
        bindEventListener();
        initData();
    }

    protected void bindEventListener() {

    }

    protected void initData() {

    }

    public void toast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

}
