package com.ldm.media.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurfaceAndTextureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_texture);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_sv, R.id.btn_tv, R.id.camera_tv, R.id.media_tv})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_sv:
                intent.setClass(this, SurfaceViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tv:
                intent.setClass(this, TextureViewActivity.class);
                startActivity(intent);
                break;
            case R.id.camera_tv:
                intent.setClass(this, CameraSurfaceViewActivity.class);
                startActivity(intent);
                break;
            case R.id.media_tv:
                intent.setClass(this, MediaRecorderActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

