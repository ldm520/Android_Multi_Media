package com.ldm.media.activity;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.utils.ThreadPoolsUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 ldm
 * @时间 2019/11/13 16:26
 * @描述 本地H264视频文件解码播放
 */
public class H264ToMp4 extends AppCompatActivity implements SurfaceHolder.Callback {
    private final static String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    //测试时保证对应目录下有对应的H264格式视频文件
    private final static String H264_FILE = SD_PATH + "/ldm_test.h264";
    @BindView(R.id.mSurfaceView)
    SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaCodec mCodec;
    private boolean mFlag = false;
    private DataInputStream mInputStream;
    private boolean useSPSandPPS = false;

    /**
     * 获得可用的字节数组
     */
    public static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        if (is instanceof ByteArrayInputStream) {
            //返回可用的剩余字节
            size = is.available();
            //创建一个对应可用相应字节的字节数组
            buf = new byte[size];
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                //将读取的数据写入到字节输出流
                bos.write(buf, 0, len);
            //将这个流转换成字节数组
            buf = bos.toByteArray();
        }
        return buf;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h264);
        ButterKnife.bind(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        //获取文件输入流
        getFileInputStream();
    }

    /**
     * 获取需要解码的文件流
     */
    public void getFileInputStream() {
        try {
            File file = new File(H264_FILE);
            mInputStream = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                if (null != mInputStream) {
                    mInputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 查找帧头部的位置
     *
     * @param pattern 文件头字节数组
     * @param bytes   可用的字节数组
     * @param start   开始读取的下标
     * @param remain  可用的字节数量
     * @return
     */
    private int KMPMatch(byte[] pattern, byte[] bytes, int start, int remain) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] lsp = computeLspTable(pattern);

        int j = 0;  // Number of chars matched in pattern
        for (int i = start; i < remain; i++) {
            while (j > 0 && bytes[i] != pattern[j]) {
                // Fall back in the pattern
                j = lsp[j - 1];  // Strictly decreasing
            }
            if (bytes[i] == pattern[j]) {
                // Next char matched, increment position
                j++;
                if (j == pattern.length)
                    return i - (j - 1);
            }
        }

        return -1;
    }


    private int[] computeLspTable(byte[] pattern) {
        int[] lsp = new int[pattern.length];
        lsp[0] = 0;  // Base case
        for (int i = 1; i < pattern.length; i++) {
            // Start by assuming we're extending the previous LSP
            int j = lsp[i - 1];
            while (j > 0 && pattern[i] != pattern[j])
                j = lsp[j - 1];
            if (pattern[i] == pattern[j])
                j++;
            lsp[i] = j;
        }
        return lsp;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            //创建编码器
            mCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化编码器
        final MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mSurfaceHolder.getSurfaceFrame().width(), mSurfaceHolder.getSurfaceFrame().height());
                /*h264常见的帧头数据为：
                00 00 00 01 67    (SPS)
                00 00 00 01 68    (PPS)
                00 00 00 01 65    (IDR帧)
                00 00 00 01 61    (P帧)*/
        //获取H264文件中的pps和sps数据
        if (useSPSandPPS) {
            byte[] header_sps = {0, 0, 0, 1, 67, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
            byte[] header_pps = {0, 0, 0, 1, 68, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
            mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
            mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        }
        //设置帧率
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 40);
        mCodec.configure(mediaFormat, mSurfaceHolder.getSurface(), null, 0);
        startDecodingThread();
    }

    /**
     * 开启解码器并开启读取文件的线程
     */
    private void startDecodingThread() {
        mCodec.start();
        ThreadPoolsUtils.execute(new DecodeThread());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private class DecodeThread implements Runnable {

        @Override
        public void run() {
            //循环解码
            decodeLoop();
        }


        private void decodeLoop() {
            //获取一组输入缓存区
            ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
            //解码后的数据，包含每一个buffer的元数据信息
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            long startMs = System.currentTimeMillis();
            long timeoutUs = 10000;
            //用于检测文件头
            byte[] maker0 = new byte[]{0, 0, 0, 1};

            byte[] dummyFrame = new byte[]{0x00, 0x00, 0x01, 0x20};
            byte[] streamBuffer = null;

            try {
                //返回可用的字节数组
                streamBuffer = getBytes(mInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int bytes_cnt = 0;
            while (mFlag == false) {
                //得到可用字节数组长度
                bytes_cnt = streamBuffer.length;

                if (bytes_cnt == 0) {
                    streamBuffer = dummyFrame;
                }
                int startIndex = 0;
                //定义记录剩余字节的变量
                int remaining = bytes_cnt;
                while (true) {
                    //当剩余的字节=0或者开始的读取的字节下标大于可用的字节数时  不在继续读取
                    if (remaining == 0 || startIndex >= remaining) {
                        break;
                    }
                    //寻找帧头部
                    int nextFrameStart = KMPMatch(maker0, streamBuffer, startIndex + 2, remaining);
                    //找不到头部返回-1
                    if (nextFrameStart == -1) {
                        nextFrameStart = remaining;
                    }
                    //得到可用的缓存区
                    int inputIndex = mCodec.dequeueInputBuffer(timeoutUs);
                    //有可用缓存区
                    if (inputIndex >= 0) {
                        ByteBuffer byteBuffer = inputBuffers[inputIndex];
                        byteBuffer.clear();
                        //将可用的字节数组，传入缓冲区
                        byteBuffer.put(streamBuffer, startIndex, nextFrameStart - startIndex);
                        //把数据传递给解码器
                        mCodec.queueInputBuffer(inputIndex, 0, nextFrameStart - startIndex, 0, 0);
                        //指定下一帧的位置
                        startIndex = nextFrameStart;
                    } else {
                        continue;
                    }

                    int outputIndex = mCodec.dequeueOutputBuffer(info, timeoutUs);
                    if (outputIndex >= 0) {
                        //帧控制是不在这种情况下工作，因为没有PTS H264是可用的
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        boolean doRender = (info.size != 0);
                        //对outputbuffer的处理完后，调用这个函数把buffer重新返回给codec类。
                        mCodec.releaseOutputBuffer(outputIndex, doRender);
                    }
                }
                mFlag = true;
            }
        }
    }
}
