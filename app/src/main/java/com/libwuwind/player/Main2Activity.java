package com.libwuwind.player;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.libwuwind.tffmpeg.VideoUtils;
import com.libwuwind.tffmpeg.VideoView;

import java.io.File;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    Button mRectBtn, mBoundBtn;
    MImageView mImageView;
    Rect originRect = new Rect();
    EditText left, right, top, bottom;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        videoView = (VideoView) findViewById(R.id.videoview);
        mRectBtn = (Button) this.findViewById(R.id.btn1);
        mImageView = (MImageView) this.findViewById(R.id.imageview);
        mRectBtn.setOnClickListener(this);
        mImageView.setImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/a.jpg");
        left = findViewById(R.id.left);
        top = findViewById(R.id.top);
        right = findViewById(R.id.right);
        bottom = findViewById(R.id.bottom);
    }

    public void btn(View v){
        String input = new File(Environment.getExternalStorageDirectory(),"/Download/05_k-近邻算法总结.mp4").getAbsolutePath();
//		String input = new File(Environment.getExternalStorageDirectory(),"/Download/屌丝男士.mov").getAbsolutePath();
//		String output = new File(Environment.getExternalStorageDirectory(),"/Download/output_512x288_yuv420p.yuv").getAbsolutePath();
        String output = new File(Environment.getExternalStorageDirectory(),"/Download/output.pcm").getAbsolutePath();
        VideoUtils.play(input, videoView.getHolder().getSurface());
//		VideoUtils.decodeAudio(input, output);
//		new MediaPlayer().sound(input, output);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        int cropLeft = Integer.parseInt(left.getText().toString());
        int cropTop = Integer.parseInt(top.getText().toString());
        int cropRight = Integer.parseInt(right.getText().toString());
        int cropBottom = Integer.parseInt(bottom.getText().toString());
        mImageView.crop(-cropLeft, -cropTop, -cropRight, -cropBottom);

    }
}
