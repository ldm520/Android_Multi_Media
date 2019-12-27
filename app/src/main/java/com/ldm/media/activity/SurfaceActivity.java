package com.ldm.media.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.surface.CameraGLSurfaceView;

public class SurfaceActivity  extends AppCompatActivity {

    private CameraGLSurfaceView mCameraSurfaceView;
    private Button mRecordCtrlBtn;
    private boolean mIsRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        mCameraSurfaceView = (CameraGLSurfaceView)findViewById(R.id.camera_surfaceview);
        mRecordCtrlBtn = (Button)findViewById(R.id.record_ctrl_btn);
        mRecordCtrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsRecording){
                    Log.i("MainActivity", "stop recording");
                    mCameraSurfaceView.stopRecord();
                    mRecordCtrlBtn.setText("开始录制");
                }else{
                    Log.i("MainActivity", "start recording");
                    mCameraSurfaceView.startRecord();
                    mRecordCtrlBtn.setText("停止录制");
                }
                mIsRecording = !mIsRecording;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mCameraSurfaceView.stopPreview();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
