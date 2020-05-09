package com.wuwind.camera;

import android.hardware.Camera;
import android.util.Log;

public class Camera0 implements ICameraManger {

    private static final String TAG = "Camera0";
    private Camera camera;
    private boolean isOpenCamera;

    @Override
    public synchronized void open() {
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camera = Camera.open(i);
                    camera.setPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] data, Camera camera) {
                            isOpenCamera = true;
                            Log.e("tag", "onPreviewFrame isOpenCamera");
                        }
                    });
                    camera.startPreview();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {
        if (isOpenCamera && null != camera) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isOpenCamera = false;
        }
    }

    @Override
    public void lightOn() {
        if (null != camera) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        }
    }

    @Override
    public void lightOff() {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
