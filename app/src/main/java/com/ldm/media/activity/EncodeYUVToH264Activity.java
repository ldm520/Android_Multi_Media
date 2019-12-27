package com.ldm.media.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ldm.media.R;
import com.ldm.media.mediacodec.h264.Camera2Preview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EncodeYUVToH264Activity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    @BindView(R.id.camera_preview)
    FrameLayout cameraPreview;
    @BindView(R.id.record_btn)
    Button recordBtn;
    private Camera2Preview camera2Preview;
    private Button mRecordBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_yuv);
        ButterKnife.bind(this);
        mRecordBtn = (Button) findViewById(R.id.record_btn);
        requestPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else {
            initCameraView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCameraView() {
        camera2Preview = new Camera2Preview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(camera2Preview);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();
        if (camera2Preview != null) {
            camera2Preview.onPause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (camera2Preview != null) {
            camera2Preview.onResume();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCameraView();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void toggleVideo(View view) {
        if (camera2Preview.toggleVideo()) {
            mRecordBtn.setText("停止录制视频");
        } else {
            mRecordBtn.setText("开始录制视频");
        }
    }
}
