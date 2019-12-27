package com.ldm.media.activity;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.utils.FileUtil;
import com.ldm.media.utils.ThreadPoolsUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//从MP4文件中提取视频并生成新的视频文件

/**
 * 一、MediaExtractor API介绍
 * MediaExtractor的作用是把音频和视频的数据进行分离。
 * 主要API介绍：
 * setDataSource(String path)：即可以设置本地文件又可以设置网络文件
 * getTrackCount()：得到源文件通道数
 * getTrackFormat(int index)：获取指定（index）的通道格式
 * getSampleTime()：返回当前的时间戳
 * readSampleData(ByteBuffer byteBuf, int offset)：把指定通道中的数据按偏移量读取到ByteBuffer中；
 * advance()：读取下一帧数据
 * release(): 读取结束后释放资源
 * ---------------------------------------------------------
 * MediaMuxer的作用是生成音频或视频文件；还可以把音频与视频混合成一个音视频文件。
 * 相关API介绍：
 * MediaMuxer(String path, int format)：path:输出文件的名称  format:输出文件的格式；当前只支持MP4格式；
 * addTrack(MediaFormat format)：添加通道；我们更多的是使用MediaCodec.getOutpurForma()或Extractor.getTrackFormat(int index)来获取MediaFormat;也可以自己创建；
 * start()：开始合成文件
 * writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo)：把ByteBuffer中的数据写入到在构造器设置的文件中；
 * stop()：停止合成文件
 * release()：释放资源
 */
public class MediaMuxerActivity extends AppCompatActivity {
    private final String FILE_PATH = FileUtil.getSaveFile(getPackageName(), "ldm.mp4").getAbsolutePath();
    @BindView(R.id.mix_path)
    TextView mixPath;
    //多媒体提取器
    private MediaExtractor mMediaExtractor;
    //多媒体合成器
    private MediaMuxer mMediaMuxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediamuxer);
        ButterKnife.bind(this);
        mixPath.setText("文件路径：" + FILE_PATH);
    }

    @OnClick({R.id.start_muxer})
    public void onClick(View view) {
        if (view.getId() == R.id.start_muxer) {
            ThreadPoolsUtils.execute(() -> {
                try {
                    process();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean process() throws IOException {
        mMediaExtractor = new MediaExtractor();
        mMediaExtractor.setDataSource(FILE_PATH);
        int mVideoTrackIndex = -1;
        int framerate = 0;
        for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
            MediaFormat format = mMediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (!mime.startsWith("video/")) {
                continue;
            }
            framerate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
            mMediaExtractor.selectTrack(i);
            //第一个参数为：合成文件保存的路径
            mMediaMuxer = new MediaMuxer(FileUtil.getSaveFile(getPackageName(), "output.mp4").getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = mMediaMuxer.addTrack(format);
            mMediaMuxer.start();
        }

        if (mMediaMuxer == null) {
            return false;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
        int sampleSize = 0;
        while ((sampleSize = mMediaExtractor.readSampleData(buffer, 0)) > 0) {

            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs += 1000 * 1000 / framerate;
            mMediaMuxer.writeSampleData(mVideoTrackIndex, buffer, info);
            mMediaExtractor.advance();
        }
        mMediaExtractor.release();
        mMediaMuxer.stop();
        mMediaMuxer.release();
        return true;
    }
}
