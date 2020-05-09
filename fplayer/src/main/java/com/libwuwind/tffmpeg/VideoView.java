package com.libwuwind.tffmpeg;

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
//		setRotation(45);
//		setZOrderOnTop(true);
//		getHolder().setFormat(PixelFormat.TRANSLUCENT);
//		一般会用上面这个，但上面这个有个比较蛋疼的是画图时会盖住它上面的view；如果想让surfaceview遵从view的层级关系，不盖住上面的view的话，可以用下面这个：

//		surfaceview.setZOrderMediaOverlay(true);
//		surfaceview.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

}
