package com.wuwind.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2 implements ICameraManger {

    private static final String TAG = "Camera2";
    Context context;
    CameraCaptureSession.StateCallback stateCallback;
    private CameraManager manager = null;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession = null;
    private CaptureRequest request = null;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private String cameraId = null;
    private boolean isSupportFlashCamera2 = false;
    private boolean isOpenFlash = false;
    private boolean isOpenCamera;
    private ArrayList localArrayList;

    public Camera2(Context context) {
        this.context = context;
        this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        initCamera2();
    }

    @SuppressLint("MissingPermission")
    @Override
    public synchronized void open() {
        try {
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onOpened(CameraDevice camera) {
                    cameraOpened(camera);
                    Log.e("tag", "turnLightOnCamera2 onOpened");
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                }
            }, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void cameraOpened(CameraDevice camera) {
        cameraDevice = camera;
        isOpenCamera = true;
        stateCallback = new CameraCaptureSession.StateCallback() {
            public void onConfigured(CameraCaptureSession arg0) {
                captureSession = arg0;
            }

            public void onConfigureFailed(CameraCaptureSession arg0) {
            }
        };
        surfaceTexture = new SurfaceTexture(0, false);
        surfaceTexture.setDefaultBufferSize(1280, 720);
        surface = new Surface(surfaceTexture);
        localArrayList = new ArrayList(1);
        localArrayList.add(surface);
        try {
            cameraDevice.createCaptureSession(localArrayList, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {
        if (null != cameraDevice)
            cameraDevice.close();
    }

    @Override
    public void lightOn() {
        light(CameraMetadata.FLASH_MODE_TORCH);
    }


    @Override
    public void lightOff() {
        light(CameraMetadata.FLASH_MODE_OFF);
    }

    private void light(int mode) {
        if (null != cameraDevice && null != captureSession)
            try {
                CaptureRequest.Builder builder;
                builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.set(CaptureRequest.FLASH_MODE, mode);
                builder.addTarget(surface);
                request = builder.build();
                captureSession.capture(request, null, null);
                isOpenFlash = true;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                Log.e(TAG, "开/关灯失败1");
            }
    }

    private void initCamera2() {
        try {
            for (String _cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(_cameraId); // 过滤掉前置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
                cameraId = _cameraId; // 判断设备是否支持闪光灯
                isSupportFlashCamera2 = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
