package com.libwuwind.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MImageView extends View {
    private Bitmap bitmap;
    private Rect mTopSrcRect, mTopDestRect;

    public MImageView(Context context) {
        super(context);
    }

    public MImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImagePath(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(inputStream);//读取图片
            mTopSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mTopDestRect = new Rect(0, 0, getWidth(), getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (null == bitmap)
            return;
        mTopDestRect.right = getWidth();
        mTopDestRect.bottom = getHeight();
        canvas.save();
        canvas.drawBitmap(bitmap, mTopSrcRect, mTopDestRect, null);
        canvas.restore();
    }

    public void crop(int left, int top, int right, int bottom) {
        mTopSrcRect.left = -left;
        mTopSrcRect.top = -top;
        mTopSrcRect.right = bitmap.getWidth() + right;
        mTopSrcRect.bottom = bitmap.getHeight() + bottom;
        invalidate();
    }
}
