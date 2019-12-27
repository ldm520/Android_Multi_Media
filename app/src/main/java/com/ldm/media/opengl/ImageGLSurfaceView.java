package com.ldm.media.opengl;

import android.content.Context;


import java.io.IOException;

/**
*@description 展示图片GLSurfaceView
*/
public class ImageGLSurfaceView extends BaseGLSurfaceView {

    public ImageGLSurfaceView(Context context) throws IOException {
        super(context);
        setRenderer(new ImageRenderer(context));
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //requestRender();
    }
}
