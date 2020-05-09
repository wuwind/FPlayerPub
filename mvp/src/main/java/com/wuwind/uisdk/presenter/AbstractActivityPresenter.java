package com.wuwind.uisdk.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.wuwind.uisdk.model.IModelDelegate;
import com.wuwind.uisdk.util.ActivityManagerUtils;
import com.wuwind.uisdk.view.IViewDelegate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractActivityPresenter<V extends IViewDelegate, M extends IModelDelegate> extends FragmentActivity implements IPresenter {
    protected V viewDelegate;
    protected M modelDelegate;

    public AbstractActivityPresenter() {
        createViewAndModel();
    }

    private void createViewAndModel() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Class<V> view = (Class<V>) parameterized.getActualTypeArguments()[0];
        Class<M> model = (Class<M>) parameterized.getActualTypeArguments()[1];
        try {
            viewDelegate = view.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("create IViewDelegate error");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("create IViewDelegate error");
        }
        try {
            modelDelegate = model.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("create IModelDelegate error");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("create IModelDelegate error");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerUtils.getInstance().pushOneActivity(this);
        viewDelegate.create(getLayoutInflater(), null);
        setContentView(viewDelegate.getRootView());
        viewDelegate.initWidget();
        bindEventListener();
    }

    protected void bindEventListener() {

    }

    public void finishMyself() {
        ActivityManagerUtils.getInstance().finishActivity(getClass());
    }

    public void finishMyselfWithParent(Class<?>... classes) {
        finishMyself();
        ActivityManagerUtils.getInstance().finishActivity(classes);
    }

    public void toast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finishMyself();
    }

}
