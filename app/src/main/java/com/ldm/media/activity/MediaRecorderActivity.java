package com.ldm.media.activity;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.utils.FileUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 ldm
 * @时间 2019/11/14 16:19
 * @描述 通过SurfaceView进行录像
 */
public class MediaRecorderActivity extends AppCompatActivity {

    private final int TIME_TASK = 0x00222;
    @BindView(R.id.media_sf)
    SurfaceView mediaSf;
    @BindView(R.id.video_bt)
    Button videoBt;
    @BindView(R.id.stop)
    Button stop;
    private MediaRecorder mediaRecorder;
    private int count;
    private boolean isStart;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIME_TASK) {
                if (null != videoBt) {
                    videoBt.setText(++count + "s");
                }
                mHandler.sendEmptyMessageDelayed(TIME_TASK, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_surface);
        ButterKnife.bind(this);
        //实例化媒体录制器
        mediaRecorder = new MediaRecorder();
    }

    @OnClick({R.id.video_bt, R.id.stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_bt:
                start();
                mHandler.sendEmptyMessageDelayed(TIME_TASK, 1000);
                break;
            case R.id.stop:
                mHandler.removeCallbacksAndMessages(null);
                count = 0;
                stop();
                videoBt.setText("录像");
                break;
        }
    }


    @SuppressWarnings("deprecation")
    public void start() {
        Camera camera;
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (camera != null) {
            camera.setDisplayOrientation(90);
            camera.unlock();
            mediaRecorder.setCamera(camera);
        }

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置保存路径
        mediaRecorder.setOutputFile(FileUtil.getSaveFile(getPackageName(), System.currentTimeMillis() + ".mp4"));
        mediaRecorder.setOrientationHint(0);
        mediaRecorder.setPreviewDisplay(mediaSf.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

    }
}
