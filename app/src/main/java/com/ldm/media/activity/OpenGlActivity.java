package com.ldm.media.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.opengl.ImageGLSurfaceView;

import java.io.IOException;

/**
 * @作者 ldm
 * @时间 2019/11/8 11:14
 * @描述 https://www.jianshu.com/p/87abc92134dd
 */
public class OpenGlActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(new ImageGLSurfaceView(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}