package com.wuwind.shotlamb;

import android.app.Application;

public class App extends Application {

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
    }
}
