package com.wuwind.shotlamb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wuwind.camera.CameraManger;

public class MainActivity extends AppCompatActivity {

    private int psw = 0b0101001;
    private int pswLen = 7;
    private int longlight = 1000;
    private int shortlight = 100;
    private int lightout = 50;
    private EditText etLongLight;
    private TextView etShortLight;
    private EditText etOutLight;
    private EditText etPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        openPermission();
    }

    private void initViews() {
        etLongLight = (EditText) findViewById(R.id.et_long_light);
        etShortLight = (TextView) findViewById(R.id.et_short_light);
        etOutLight = (EditText) findViewById(R.id.et_out_light);
        etPsw = (EditText) findViewById(R.id.et_psw);
    }

    private boolean openPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        return true;
    }

    public void open(View view) {
        if (!openPermission())
            return;
        getSetting();
        CameraManger.getInstance().open();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    int b;
                    for (int i = pswLen - 1; i >= 0; i--) {
                        b = psw >> i & 0x01;
                        Log.e("tag", b + "");
                        CameraManger.getInstance().lightOn();
                        Thread.sleep(b == 0 ? shortlight : longlight);
                        CameraManger.getInstance().lightOff();
                        Thread.sleep(lightout);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CameraManger.getInstance().close();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getSetting() {
        longlight = Integer.parseInt(etLongLight.getText().toString());
        shortlight = Integer.parseInt(etShortLight.getText().toString());
        lightout = Integer.parseInt(etOutLight.getText().toString());
        String pswStr = etPsw.getText().toString();
        pswLen = pswStr.length();
        psw = Integer.parseInt(pswStr, 2);
        Log.e("tag", "openCamera" + longlight + shortlight + lightout + pswLen + psw);
    }

    public void stop(View view) {

    }

}
