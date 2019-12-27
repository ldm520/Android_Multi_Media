package com.ldm.media.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class BaseGLSurfaceView extends GLSurfaceView {
    public BaseGLSurfaceView(Context context) {
        super(context);
        initEGLContext();
    }
    public BaseGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEGLContext();
    }
    // 创建 OpenGL ES 2.0 上下文
    private void initEGLContext() {
        setEGLContextClientVersion(2);
    }
}
