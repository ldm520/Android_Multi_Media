package com.ldm.media.opengl;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
*@description 摄像机下的的三角形:投影 + 相机视图
*/
public class CameraTriangle extends Triangle {
    public static final int COORDS_PER_VERTEX = 3; // 每个顶点的坐标数
    // 支持矩阵变换的顶点着色器
    public static final String vertexMatrixShaderCode =
            "attribute vec4 vPosition;\n" +
                    "uniform mat4 vMatrix;\n" +
                    "void main() {\n" +
                    "    gl_Position = vMatrix * vPosition;\n" +
                    "}";

    //定义投影
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int mMatrixHandler;//变换矩阵vMatrix成员句柄
    private int mPositionHandle;//获取顶点着色器的vPosition成员句柄
    private int mColorHandle;//获取片元着色器的vColor成员的句柄

    //设置颜色，参数为红绿蓝和透明值,目前设置的颜色为白色
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    public CameraTriangle(){
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        mProgram = createOpenGLProgram(vertexMatrixShaderCode, fragmentShaderCode);
    }

    public void onSurfaceChanged(int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void draw() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, BaseGLShape.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, BaseGLShape.VERTEX_STRIDE, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
