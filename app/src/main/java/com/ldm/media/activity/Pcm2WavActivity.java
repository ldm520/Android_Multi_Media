package com.ldm.media.activity;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.Constant;
import com.ldm.media.R;
import com.ldm.media.play.PlayFactory;
import com.ldm.media.play.PlayInModeStream;
import com.ldm.media.protocol.Recordable;
import com.ldm.media.utils.FileUtil;
import com.ldm.media.utils.PcmToWavUtil;
import com.ldm.media.utils.ThreadPoolsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 ldm
 * @时间 2019/11/13 15:47
 * @描述 录音及pcm转wav
 */
public class Pcm2WavActivity extends AppCompatActivity implements Recordable {
    private final int TIMER_TASK = 0x00111;
    @BindView(R.id.record_start)
    Button recordStart;
    @BindView(R.id.record_play)
    Button recordPlay;
    private AudioRecord audioRecord;
    private boolean isRecording;
    private int count = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER_TASK:
                    if (null != recordStart) {
                        ++count;
                        recordStart.setText(count + "s");
                    }
                    mHandler.sendEmptyMessageDelayed(TIMER_TASK, 1000);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcm_wav);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.record_start, R.id.record_convert, R.id.record_play})
    public void recordClick(View v) {
        switch (v.getId()) {
            case R.id.record_start:
                String strBtnRecord = recordStart.getText().toString();
                if (strBtnRecord.equals(getString(R.string.start_record))) {
                    mHandler.sendEmptyMessageDelayed(TIMER_TASK, 1000);
                    startRecord();
                } else {
                    mHandler.removeMessages(TIMER_TASK);
                    count = 0;
                    recordStart.setText(getString(R.string.start_record));
                    stopRecord();
                }
                break;
            case R.id.record_convert:
                PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG, Constant.AUDIO_FORMAT);
                File pcmFile = FileUtil.getSaveFile(getPackageName(), "test.pcm");
                File wavFile = FileUtil.getSaveFile(getPackageName(), "ldm.wav");
                pcmToWavUtil.pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
                break;
            case R.id.record_play:
                String strBtnPlay = recordPlay.getText().toString();
                PlayFactory playFactory = new PlayFactory(this);
                PlayInModeStream playInModeStream = (PlayInModeStream) playFactory.createPlay(PlayFactory.STREAMMODE);
                //PlayInModeStatic playInModeStatic = (PlayInModeStatic) playFactory.createPlay(PlayFactory.STATICMODE);
                final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
                if (strBtnPlay.equals(getString(R.string.start_play))) {
                    recordPlay.setText(getString(R.string.stop_play));
                    playInModeStream.startPlayPcm(file);
                } else {
                    recordPlay.setText(getString(R.string.start_play));
                    playInModeStream.stopPlayPcm();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void startRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG, Constant.AUDIO_FORMAT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG,
                Constant.AUDIO_FORMAT, minBufferSize);
        final byte[] data = new byte[minBufferSize];
        final File file = FileUtil.getSaveFile(getPackageName(), "test.pcm");
        if (!file.mkdirs()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        audioRecord.startRecording();
        isRecording = true;
        ThreadPoolsUtils.execute(() -> {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (null != os) {
                while (isRecording) {
                    int read = audioRecord.read(data, 0, minBufferSize);
                    // 如果读取音频数据没有出现错误，就将数据写入到文件
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void stopRecord() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }


}
