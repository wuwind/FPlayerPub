package com.libwuwind.player;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.libwuwind.tffmpeg.VideoUtils;
import com.libwuwind.uilibrary.dragview.DragView;
import com.libwuwind.uilibrary.dragview.MoveLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
//    IjkVideoView videoView;
    EditText left, right, top, bottom;
    private Button btn;
    private Button btn2;
    private Button btn3;
    private DragView mDragView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        left = findViewById(R.id.left);
        top = findViewById(R.id.top);
        right = findViewById(R.id.right);
        bottom = findViewById(R.id.bottom);
        final RadioGroup rg = findViewById(R.id.checkg);

        mDragView = (DragView) findViewById(R.id.dragview);

        final MImageView mImageView = new MImageView(this);
        final MoveLayout moveLayout1 = mDragView.addDragView(mImageView, 0, 100, 380, 400, false, false);

        mImageView.setImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/b.jpg");
//        final VideoView videoView = mDragView.findViewById(R.id.videoview);

        final MVideoView mVideoView = new MVideoView(this);
        final MoveLayout moveLayout = mDragView.addDragView(mVideoView, 0, 200, 380, 500, false, false);

//        videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/中国合伙人.flv");
//        videoView.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cropLeft = Integer.parseInt(left.getText().toString());
                int cropTop = Integer.parseInt(top.getText().toString());
                int cropRight = Integer.parseInt(right.getText().toString());
                int cropBottom = Integer.parseInt(bottom.getText().toString());
                if(rg.getCheckedRadioButtonId() == R.id.pic) {
                    mImageView.crop(-cropLeft, -cropTop, -cropRight, -cropBottom);
                } else {
                    VideoUtils.crop(cropLeft, cropTop, cropRight, cropBottom, 0);
                }

            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("main","getRotation:" + moveLayout.getRotation());
                if(rg.getCheckedRadioButtonId() == R.id.pic) {
                    int rotate = (int) (moveLayout1.getRotation() + 30);
                    moveLayout1.setRotation(rotate);
                } else {
                    int rotate = (int) (moveLayout.getRotation() + 30);
                    moveLayout.setRotation(rotate);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = new File(Environment.getExternalStorageDirectory(),"/Download/abc.mp4").getAbsolutePath();
                VideoUtils.play(input, mVideoView.surface);
            }
        });

    }

    private void permission() {
        if (android.os.Build.VERSION.SDK_INT >= 23)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

}
