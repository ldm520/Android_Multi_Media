package com.ldm.media.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 ldm
 * @时间 2019/11/8 16:15
 * @描述 MediaPlayer播放视频
 * android9以上版本在AndroidManifest.xml中配置 android:networkSecurityConfig="@xml/network_security_config"
 */
public class MediaPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private final String url = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4";
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.stop)
    TextView stop;
    private SurfaceHolder surfaceHolder;
    /**
     * 1， MediaPlayer是使用Surface进行视频内容的展示。
     * 2， MediaPlayer只支持mp4、avi、3gp格式的视频文件。
     * 3， MediaPlayer也可以播放网络视频，支持协议为：Http和RTSP协议两种。
     */
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        ButterKnife.bind(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        play();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void play() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频路径，可以是本地文件路径
            Uri uri = Uri.parse(url);
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            // 把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            // 播放
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.start, R.id.stop})
    public void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                play();
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
        }
    }

    /**
     * MediaPlayer 的提供的方法如下：
     *   •   void setDataSource(String path) ：通过一个具体的路径来设置MediaPlayer的数据源，path可以是本地的一个路径，也可以是一个网络路径
     *   •   void setDataSource(Context context, Uri uri)： 通过给定的Uri来设置MediaPlayer的数据源，这里的Uri可以是网络路径或是一个ContentProvider的Uri。
     *   •   void setDataSource(MediaDataSource dataSource) ：通过提供的MediaDataSource来设置数据源
     *   •   void setDataSource(FileDescriptor fd)： 通过文件描述符FileDescriptor来设置数据源
     *   •   int getCurrentPosition() ：获取当前播放的位置
     *   •   int getAudioSessionId() ：返回音频的session ID
     *   •   int getDuration() ：得到文件的时间
     *   •   TrackInfo[] getTrackInfo() ：返回一个track信息的数组
     *   •   boolean isLooping ()： 是否循环播放
     *   •   boolean isPlaying()： 是否正在播放
     *   •   void pause () ：暂停
     *   •   void start () ：开始
     *   •   void stop () ： 停止
     *   •   void prepare()： 同步的方式装载流媒体文件。
     *   •   void prepareAsync()： 异步的方式装载流媒体文件。
     *   •   void reset()： 重置MediaPlayer至未初始化状态。
     *   •   void release ()： 回收流媒体资源。
     *   •   void seekTo(int msec)： 指定播放的位置（以毫秒为单位的时间）
     *   •   void setAudioStreamType(int streamtype) ：指定流媒体类型
     *   •   void setLooping(boolean looping) ：设置是否单曲循环
     *   •   void setNextMediaPlayer(MediaPlayer next) ： 当前这个MediaPlayer播放完毕后，MediaPlayer next开始播放
     *   •   void setWakeMode(Context context, int mode)：设置CPU唤醒的状态。
     *   •   setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) ：网络流媒体的缓冲变化时回调
     *   •   setOnCompletionListener(MediaPlayer.OnCompletionListener listener) ：网络流媒体播放结束时回调
     *   •   setOnErrorListener(MediaPlayer.OnErrorListener listener) ：发生错误时回调
     *   •   setOnPreparedListener(MediaPlayer.OnPreparedListener listener)：当装载流媒体完毕的时候回调。
     */
}
