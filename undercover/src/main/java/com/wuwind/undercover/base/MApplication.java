package com.wuwind.undercover.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.wuwind.undercover.utils.db.DBHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MApplication extends Application {

    public static Context applicationContext;
    protected static List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
        init();
    }

    private void init() {
        DBHelper.getInstance().init(applicationContext, "undercover.db");
    }

    public static void addActivity(Activity activity) {
        if (null == activities) {
            activities = new ArrayList<>();
        }
        activities.add(activity);
    }

    public static void reMoveActivity(Activity activity) {
        if (null == activities)
            return;
        activities.remove(activity);
    }
}
