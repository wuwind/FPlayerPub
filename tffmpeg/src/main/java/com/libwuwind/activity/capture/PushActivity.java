package com.libwuwind.activity.capture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.libwuwind.jni.FFmpegUtils;
import com.libwuwind.push.LivePusher;
import com.wuwind.ui.base.ActivityPresenter;

public class PushActivity extends ActivityPresenter<PushView, PushModel> {

    LivePusher livePusher;
    String outputurl = "rtmp://120.79.223.156:1935/live/jason?user=pub&psw=123";

    @Override
    protected void bindEventListener() {
        super.bindEventListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
            }
            if (null == livePusher)
                livePusher = new LivePusher(viewDelegate.getSurfaceView());
        }
        viewDelegate.setListener(new PushView.Listener() {
            @Override
            public void start() {
                livePusher.toggle(outputurl);
            }

            @Override
            public void push() {
                String inputurl = Environment.getExternalStorageDirectory().getPath() + "/Download/abc.mp4";

                FFmpegUtils.push(inputurl, outputurl);
            }
        });

        String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.h264";
//        File file = new File(s);
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.e("tag", s);
    }

}
