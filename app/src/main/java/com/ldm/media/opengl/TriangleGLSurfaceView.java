package com.ldm.media.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author zjf
 * @description 绘制各类三角形的GLSurfaceView
 * @date 2018/10/31 11:15
 */
public class TriangleGLSurfaceView extends BaseGLSurfaceView {

    public TriangleGLSurfaceView(Context context) {
        super(context);
        //setRenderer(new TriangleRenderer()); // 绘制普通三角形
        setRenderer(new CameraTriangleRenderer());//绘制摄像机下的的三角形
    }

    public TriangleGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setRenderer(new TriangleRenderer()); // 绘制普通三角形
        setRenderer(new CameraTriangleRenderer());//绘制摄像机下的的三角形
    }

    //普通三角形Triangle.java
    class TriangleRenderer implements Renderer {
        private Triangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new Triangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }

    //摄像机下的的三角形CameraTriangle.java
    class CameraTriangleRenderer implements Renderer {
        CameraTriangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new CameraTriangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            triangle.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }
}
