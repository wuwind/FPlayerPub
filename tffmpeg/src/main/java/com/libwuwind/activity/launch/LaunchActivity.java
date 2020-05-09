package com.libwuwind.activity.launch;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.libwuwind.activity.capture.PushActivity;
import com.libwuwind.test.TestActivity;
import com.wuwind.ui.base.ActivityPresenter;

public class LaunchActivity extends ActivityPresenter<LaunchView, LaunchModel> {

    @Override
    protected void bindEventListener() {
        Intent intent;
        if (getMain().equals("pusher")) {
            intent = new Intent(this, PushActivity.class);
        } else {
//            intent = new Intent(this, RtmpPlayerActivity.class);
            intent = new Intent(this, TestActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public String getMain() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("main");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "pusher";
    }
}
