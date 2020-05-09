package com.wuwind.uisdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class AbstractViewDelegate implements IViewDelegate {
    protected final SparseArray<View> viewArray = new SparseArray<>();
    protected View rootView;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container) {
        int rootLayoutId = getRootLayoutId();
        rootView = inflater.inflate(rootLayoutId, container, false);
    }

    public abstract @LayoutRes
    int getRootLayoutId();

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void initWidget() {

    }

    public void setOnClickListener(OnClickListener onClickListener, int... ids) {
        if (null == ids) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(onClickListener);
        }
    }

    public <T extends View> T get(@IdRes int id) {
        return (T) bindView(id);
    }

    public <T extends View> T bindView(@IdRes int id) {
        T view = (T) viewArray.get(id);
        if (null == view) {
            view = (T) rootView.findViewById(id);
            viewArray.put(id, view);
        }
        return view;
    }

    public void toast(CharSequence msg) {
        Toast.makeText(rootView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(@StringRes int resId) {
        Toast.makeText(rootView.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public <T extends Activity> T getContext() {
        return (T) rootView.getContext();
    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) findActivity(getContext());
    }

    private Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

}
