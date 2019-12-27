package com.ldm.media.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ldm.media.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//https://www.cnblogs.com/renhui/p/7452572.html
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_ROR_APP = 0x00111;
    @BindView(R.id.pcm_wva)
    TextView pcmWva;
    @BindView(R.id.camera2)
    TextView camera2;
    @BindView(R.id.media)
    TextView media;
    @BindView(R.id.mp3_aac)
    TextView mp3Aac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT > 22) {
            permissionForM();
        }
    }

    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_ROR_APP);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_ROR_APP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.media_player, R.id.videoview_player, R.id.pcm_wva, R.id.camera2, R.id.media, R.id.mp3_aac,
            R.id.local_h, R.id.local_yuv, R.id.surface,R.id.opengl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.media_player:
                startActivity(new Intent(MainActivity.this, MediaPlayerActivity.class));
                break;
            case R.id.videoview_player:
                startActivity(new Intent(MainActivity.this, VideoViewActivity.class));
                break;
            case R.id.pcm_wva:
                startActivity(new Intent(MainActivity.this, Pcm2WavActivity.class));
                break;
            case R.id.camera2:
                startActivity(new Intent(MainActivity.this, SurfaceAndTextureActivity.class));
                break;
            case R.id.media:
                startActivity(new Intent(MainActivity.this, MediaMuxerActivity.class));
                break;
            case R.id.mp3_aac:
                startActivity(new Intent(MainActivity.this, Mp3ToAacActivity.class));
                break;
            case R.id.local_h:
                startActivity(new Intent(MainActivity.this, H264ToMp4.class));
                break;
            case R.id.local_yuv:
                startActivity(new Intent(MainActivity.this, EncodeYUVToH264Activity.class));
                break;
            case R.id.surface:
                startActivity(new Intent(MainActivity.this, SurfaceActivity.class));
                break;
            case R.id.opengl:
                startActivity(new Intent(MainActivity.this, OpenGlActivity.class));
                break;
        }
    }
}
