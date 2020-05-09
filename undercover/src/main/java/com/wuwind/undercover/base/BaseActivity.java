package com.wuwind.undercover.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.uisdk.model.IModelDelegate;
import com.wuwind.uisdk.view.IViewDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Activity基类，写activity必须继承
 * Created by Wuhf on 2016/4/1.
 * Description ：
 */
public abstract class BaseActivity<V extends IViewDelegate, M extends IModelDelegate> extends ActivityPresenter<V,M> {

    boolean isShowLoading;

    private NetReceiver netReceiver = new NetReceiver();
    private static boolean netNotifyOnce = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate();
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(this).onAppStart();
        afterOnCreate();
//        setContentView(setContentLayoutId());
        EventBus.getDefault().register(this);
        MApplication.addActivity(this);
//        initMonitor();
//        BaseApplication.startDisplyService();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
//        refreshView();
        registerReceiver(netReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    protected void onStop() {
        unregisterReceiver(netReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        MApplication.reMoveActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNotify(String msg) {
    }


    public void setShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }


    protected void beforeOnCreate() {
    }

    protected void afterOnCreate() {
    }

//    protected abstract int setContentLayoutId();

//    protected abstract void initMonitor();

//    protected abstract void refreshView();

    protected void refreshData() {
    }


    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    /**
     * 网络广播接收者
     */
    private class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_WIFI) {
                } else {
                }
            }
        }
    }


}
