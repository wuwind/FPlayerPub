package com.libwuwind.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.wuwind.ui.base.ActivityPresenter;

public class TestActivity extends ActivityPresenter<TestView, TestModel> {

    @Override
    protected void bindEventListener() {
        String path = Environment.getExternalStorageDirectory()+"/Download/1.jpg";
        bitmapFactory(path);
//        viewDelegate.image.setImageBitmap(BitmapFactory.decodeFile(path));
//        Glide.with(this).load(path).into(viewDelegate.image);
    }

    public void bitmapFactory( String imagePath){
        // 配置压缩的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        ////inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = caculateSampleSize(options,800,1280);
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        viewDelegate.image.setImageBitmap(bm);
    }

    /**
     * 计算出所需要压缩的大小
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }
}
