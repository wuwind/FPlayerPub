package com.wuwind.camera;

import android.os.Build;

import com.wuwind.shotlamb.App;

public class CameraManger implements ICameraManger {

    ICameraManger manger;

    private CameraManger() {
        manger = isLOLLIPOP() ? new Camera2(App.app) : new Camera0();
    }

    /**
     * 判断Android系统版本是否 >= LOLLIPOP(API21)
     */
    private boolean isLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static CameraManger getInstance() {
        return Instance.mCamera;
    }

    @Override
    public void open() {
        manger.open();
    }

    @Override
    public void close() {
        manger.close();
    }

    @Override
    public void lightOn() {
        manger.lightOn();
    }

    @Override
    public void lightOff() {
        manger.lightOff();
    }

    private static class Instance {
        private static CameraManger mCamera = new CameraManger();
    }
}
