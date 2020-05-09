package com.libwuwind.push;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.libwuwind.jni.NativePusher;
import com.libwuwind.push.param.VideoParam;

import java.io.IOException;

public class VideoPusher implements Pusher, SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceView surfaceView;
    private boolean isPushing;
    private byte[] buffer;
    private Camera camera;
    private VideoParam videoParam;
    private NativePusher nativePusher;

    public VideoPusher(VideoParam videoParam, SurfaceView surfaceView, NativePusher nativePusher) {
        this.videoParam = videoParam;
        this.surfaceView = surfaceView;
        this.nativePusher = nativePusher;
        buffer = new byte[videoParam.width * videoParam.height * 4];
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void starPush() {
        if (isPushing)
            return;
        isPushing = true;
        nativePusher.setVideoOptions(videoParam.width, videoParam.height, videoParam.bitrate, videoParam.fps);

        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        setcameradisplayorientation((Activity) surfaceView.getContext(), videoParam.caramID, camera);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(videoParam.width, videoParam.height);
        parameters.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
//        camera.setPreviewCallback(this);
        camera.addCallbackBuffer(buffer);
        camera.setPreviewCallbackWithBuffer(this);
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
    }

    @Override
    public void stopPush() {
        if (null != camera) {
            isPushing = false;
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        nativePusher.stopPush();
    }

    public int getdisplayrotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }
    int rotation;
    public void setcameradisplayorientation(Activity activity,
                                                   int cameraid, Camera camera) {
        // see android.hardware.camera.setcameradisplayorientation for
        // documentation.
        Camera.CameraInfo info = new  Camera.CameraInfo();
        camera.getCameraInfo(cameraid, info);
        int degrees = getdisplayrotation(activity);
        int result;
        if (info.facing ==  Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        camera.getParameters().setRotation(result);
        rotation = result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != camera)
            camera.addCallbackBuffer(buffer);
        Log.e("tag", "onPreviewFrame " + data.length);
        nativePusher.fireVideo(roatePreview(data));
    }

    private byte[] roatePreview(byte[] input) {
        int width = videoParam.width;
        int height = videoParam.height;
        int frameSize = width * height;
        int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize + 2 * qFrameSize];


        boolean swap = (rotation == 90 || rotation == 270);
        boolean yflip = (rotation == 90 || rotation == 180);
        boolean xflip = (rotation == 270 || rotation == 180);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int xo = x, yo = y;
                int w = width, h = height;
                int xi = xo, yi = yo;
                if (swap) {
                    xi = w * yo / h;
                    yi = h * xo / w;
                }
                if (yflip) {
                    yi = h - yi - 1;
                }
                if (xflip) {
                    xi = w - xi - 1;
                }
                output[w * yo + xo] = input[w * yi + xi];
                int fs = w * h;
                int qs = (fs >> 2);
                xi = (xi >> 1);
                yi = (yi >> 1);
                xo = (xo >> 1);
                yo = (yo >> 1);
                w = (w >> 1);
                h = (h >> 1);
// adjust for interleave here
                int ui = fs + (w * yi + xi) * 2;
                int uo = fs + (w * yo + xo) * 2;
// and here
                int vi = ui + 1;
                int vo = uo + 1;
                output[uo] = input[ui];
                output[vo] = input[vi];
            }
        }
        return output;
    }
}
