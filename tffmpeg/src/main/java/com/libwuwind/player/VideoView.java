package com.libwuwind.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoView extends SurfaceView {

	public VideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	

	public VideoView(Context context) {
		super(context);
		init();
	}

	private void init() {
		SurfaceHolder holder = getHolder();
		holder.setFormat(PixelFormat.RGBA_8888);
	}

}
