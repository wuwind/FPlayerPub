package com.libwuwind.uilibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wuhf on 2020/10/29.
 * Description ：表示选中页面的圆点
 */
public class SelectPagePoint extends View {

    private Paint bgPaint, selectPointPaint, pointPaint;
    private int mWidth = 90, mHeight = 24;
    private int padding = 15;
    private int pointHeight = 10;
    private int selectPointWidth = 22;
    private int pointMarginTop;
    private int rx = 12;

    private float lastRight;
    private int count = 2;
    private int select = 0;

    public SelectPagePoint(Context context) {
        this(context, null);
    }

    public SelectPagePoint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPagePoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#66333333"));
        selectPointPaint = new Paint();
        selectPointPaint.setColor(Color.WHITE);
        selectPointPaint.setAntiAlias(true);
        pointPaint = new Paint();
        pointPaint.setColor(Color.parseColor("#f7fafd"));
        pointPaint.setAntiAlias(true);
        pointMarginTop = (mHeight - pointHeight) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = padding * 2 + (count - 1) * pointHeight + selectPointWidth + padding * (count - 1);
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF, rx, rx, bgPaint);
        lastRight = 0;
        for (int i = 0; i < count; i++) {
            drawSelectPoint(canvas, i == select);
        }
    }

    private void drawSelectPoint(Canvas canvas, boolean isSelected) {
        lastRight += padding;
        float nextLeft = lastRight;
        if (isSelected) {
            lastRight += selectPointWidth;
            RectF rectF = new RectF(nextLeft, pointMarginTop, lastRight, pointMarginTop + pointHeight);
            canvas.drawRoundRect(rectF, rx, rx, selectPointPaint);
        } else {
            float cx = lastRight + pointHeight / 2f;
            canvas.drawCircle(cx, pointHeight / 2f + pointMarginTop, pointHeight / 2, pointPaint);
            lastRight += pointHeight;
        }
    }

    public void setSelectIndex(int index) {
        if (index != select) {
            select = index;
            invalidate();
        }
    }

}
