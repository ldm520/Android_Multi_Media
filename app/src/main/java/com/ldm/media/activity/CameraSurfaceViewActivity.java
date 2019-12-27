package com.ldm.media.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.utils.CameraUtils;
import com.ldm.media.utils.ImageUtils;
import com.ldm.media.view.CameraSurfaceView;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 ldm
 * @时间 2019/11/14 15:14
 * @描述 利用SurfaceView拍照功能
 */
public class CameraSurfaceViewActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.camera_sf)
    CameraSurfaceView cameraSf;
    private int mOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_surface);
        ButterKnife.bind(this);
        mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraSurfaceViewActivity.this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        CameraUtils.stopPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CameraUtils.startPreview();
    }

    @OnClick({R.id.take_photo, R.id.change})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                takePicture();
                break;

            case R.id.change:
                switchCamera();
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        CameraUtils.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                CameraUtils.startPreview();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    bitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation);
                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                            + System.currentTimeMillis() + ".jpg";
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                CameraUtils.startPreview();
            }
        });
    }


    /**
     * 切换相机
     */
    private void switchCamera() {
        if (cameraSf != null) {
            CameraUtils.switchCamera(1 - CameraUtils.getCameraID(), cameraSf.getHolder());
            // 切换相机后需要重新计算旋转角度
            mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraSurfaceViewActivity.this);
        }
    }

}