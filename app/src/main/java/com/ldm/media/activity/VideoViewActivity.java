package com.ldm.media.activity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 ldm
 * @时间 2019/11/8 16:15
 * @描述 VideoView播放视频
 */
public class VideoViewActivity extends AppCompatActivity {
    /**
     * • VideoView是使用MediaPlayer来对视频文件进行控制的。
     * • VideoView只支持mp4、avi、3gp格式的视频，同MediaPlayer。
     * • VideoView可以播放网络视频，支持协议为：Http协议和RTSP协议两种。
     */
    @BindView(R.id.video_view)
    VideoView videoView;
    private final String videoPath="http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);
        //也可以是本地文件路径
        videoView.setVideoPath(videoPath);
        videoView.start();
        //MediaController为我们提供了一个悬浮的视频播放操作栏
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
    }
    /**
     * Android VideoView类API，其主要方法如下：
     *  setVideoPath：设置要播放的视频文件的位置
     *  start：开始或继续播放视频
     *  pause：暂停播放视频
     *  resume：将视频从头开始播放
     *  seekTo：从指定的位置开始播放视频
     *  isPlaying：判断当前是否正在播放视频
     *  getCurrentPosition：获取当前播放的位置
     *  getDuration：获取载入的视频文件的时长
     *  setVideoPath(String path)：以文件路径的方式设置VideoView播放的视频源
     *  setVideoURI(Uri uri)：以Uri的方式设置视频源，可以是网络Uri或本地Uri
     *  setMediaController(MediaController controller)：设置MediaController控制器
     *  setOnCompletionListener(MediaPlayer.onCompletionListener l)：监听播放完成的事件
     *  setOnErrorListener(MediaPlayer.OnErrorListener l)：监听播放发生错误时候的事件
     *  setOnPreparedListener(MediaPlayer.OnPreparedListener l)：监听视频装载完成的事件
     */
}
