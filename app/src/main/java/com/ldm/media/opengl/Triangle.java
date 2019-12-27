package com.ldm.media.opengl;

import android.opengl.GLES20;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
*@description 普通三角形
*
*@author zjf
*@date 2018/10/31 11:19
*/
public class Triangle extends BaseGLShape{
    // 简单的顶点着色器
    public static final String vertexShaderCode =
            "attribute vec4 vPosition;\n" +
                    " void main() {\n" +
                    "     gl_Position   = vPosition;\n" +
                    " }";

    // 简单的片元着色器
    public static final String fragmentShaderCode =
            " precision mediump float;\n" +
                    " uniform vec4 vColor;\n" +
                    " void main() {\n" +
                    "     gl_FragColor = vColor;\n" +
                    " }";

    // 定义三角形的坐标
    public static float triangleCoords[] = {
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // 定义三角形的颜色——白色
    public static float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    // 顶点个数
    public static final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    FloatBuffer vertexBuffer;

    int mProgram;

    public Triangle() {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 申请底色空间
        //为存放形状的坐标，初始化顶点字节缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(Triangle.triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(Triangle.triangleCoords);
        vertexBuffer.position(0);

        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    public void draw() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的vPosition成员句柄
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,VERTEX_STRIDE , vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, Triangle.color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, Triangle.vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
