package com.libwuwind.tffmpeg;

import android.view.Surface;

public class VideoUtils {

	public native static void decode(String input,String output);
	public native static void decodeAudio(String input,String output);
	
	public native static void play(String input, Surface surface);
	public native static void play2(String input, Surface surface, Surface surface2);
	public native static void init();
	public native static void crop(int left, int top, int right, int bottom, float roate);
	
	static{
		System.loadLibrary("avutil-54");
		System.loadLibrary("swresample-1");
		System.loadLibrary("avcodec-56");
		System.loadLibrary("avformat-56");
		System.loadLibrary("swscale-3");
		System.loadLibrary("postproc-53");
		System.loadLibrary("avfilter-5");
		System.loadLibrary("avdevice-56");
		System.loadLibrary("myffmpeg");
	}
}
