package com.libwuwind.tffmpeg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

public class MainActivity extends Activity {

	VideoView videoView;
	VideoView videoView2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		videoView = (VideoView) findViewById(R.id.videoView);
//		videoView2 = (VideoView) findViewById(R.id.videoView2);
//		InitUtil.init();
	}
	
	public void crop(View v){
//		VideoUtils.crop(100, 100, 100, 100);
	}

	public void btn(View v){
		String input = new File(Environment.getExternalStorageDirectory(),"/Download/05_k-近邻算法总结.mp4").getAbsolutePath();
//		String input = new File(Environment.getExternalStorageDirectory(),"/Download/屌丝男士.mov").getAbsolutePath();
//		String output = new File(Environment.getExternalStorageDirectory(),"/Download/output_512x288_yuv420p.yuv").getAbsolutePath();
		String output = new File(Environment.getExternalStorageDirectory(),"/Download/output.pcm").getAbsolutePath();
		VideoUtils.play2(input, videoView.getHolder().getSurface(), videoView2.getHolder().getSurface());
//		VideoUtils.decodeAudio(input, output);
//		new MediaPlayer().sound(input, output);
	}
}
