package com.ldm.media.opengl;

import android.content.Context;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
*@description 绘制正方形的GLSurfaceView
*
*@author zjf
*@date 2018/10/31 11:44
*/
public class SquareGLSurfaceView extends BaseGLSurfaceView{
    public SquareGLSurfaceView(Context context) {
        super(context);
        setRenderer(new SquareRenderer()); // 绘制正方形
    }

    class SquareRenderer implements Renderer{
        Square square;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            square = new Square();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            square.onSurfaceChanged(width,height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            square.draw();
        }
    }
}
