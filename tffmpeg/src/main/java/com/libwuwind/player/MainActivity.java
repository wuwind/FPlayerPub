package com.libwuwind.player;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.io.File;

public class MainActivity extends Activity {

    VideoView videoView;
    VideoView videoView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView2 = (VideoView) findViewById(R.id.videoView2);
//		InitUtil.init();
        checkPermission(this);
    }

    private void checkPermission(Activity activity) {
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void crop(View v){
//        VideoUtils.crop(0, 0, 0, 0, (float)(Math.PI * 30 / 180));

//        final PackageManager packageManager = getPackageManager();
//        @SuppressLint("PrivateApi")
//        Method method = packageManager.getClass().getDeclaredMethod("installPackage",
//                Uri.class, IPackageInstallObserver.class, int.class, String.class);
//        method.setAccessible(true);
//        method.invoke(packageManager, Uri.fromFile(new File(path)), new IPackageInstallObserver.Stub() {
//
        }


    public void music(View v){
        String input = new File(Environment.getExternalStorageDirectory(),"/Download/abc.avi").getAbsolutePath();
//		String input = new File(Environment.getExternalStorageDirectory(),"/Download/屌丝男士.mov").getAbsolutePath();
//		String output = new File(Environment.getExternalStorageDirectory(),"/Download/output_512x288_yuv420p.yuv").getAbsolutePath();
        String output = new File(Environment.getExternalStorageDirectory(),"/Download/output.pcm").getAbsolutePath();
//        VideoUtils.play(input, videoView.getHolder().getSurface());
//		VideoUtils.decodeAudio(input, output);
        new MediaPlayer().sound(input, output);
    }
    public void btn(View v){
//        String input = new File(Environment.getExternalStorageDirectory(),"/Download/abc.mp4").getAbsolutePath();
        String input = "rtmp://120.79.223.156:1935/live/jason?user=player&psw=123";
//		String input = new File(Environment.getExternalStorageDirectory(),"/Download/屌丝男士.mov").getAbsolutePath();
//		String output = new File(Environment.getExternalStorageDirectory(),"/Download/output_512x288_yuv420p.yuv").getAbsolutePath();
//        String output = new File(Environment.getExternalStorageDirectory(),"/Download/output.pcm").getAbsolutePath();
        VideoUtils.play(input, videoView.getHolder().getSurface());
//		VideoUtils.decodeAudio(input, output);
//		new MediaPlayer().sound(input, output);
    }
}

