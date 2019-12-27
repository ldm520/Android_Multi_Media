package com.ldm.media.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ldm.media.R;
import com.ldm.media.mediacodec.AudioCodec;
import com.ldm.media.utils.FileUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 ldm
 * @时间 2019/11/13 16:59
 * @描述 Mp3格式音频转aac格式
 */
public class Mp3ToAacActivity extends AppCompatActivity {

    @BindView(R.id.audio_change)
    Button audioChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aac);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.audio_change)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_change:
                //读取指定文件夹
                String path = FileUtil.getSavePath(getPackageName());
                File file = new File(path);
                if (file.exists() && file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File file1 = files[i];
                        String name = file1.getName();
                        if (file1.isFile() && (name.endsWith(".mp3") || name.endsWith(".wav"))) {
                            String simpleName = name.substring(0, name.indexOf("."));
                            //转成pcm文件路径
                            final String pcmPath = FileUtil.getSaveFile(getPackageName(), simpleName + ".pcm").getAbsolutePath();
                            //转成m4a文件路径
                            final String aacPath = FileUtil.getSaveFile(getPackageName(), simpleName + ".m4a").getAbsolutePath();
                            AudioCodec.getPCMFromAudio(file.getAbsolutePath() + "/" + name, pcmPath, new AudioCodec.AudioDecodeListener() {
                                @Override
                                public void decodeOver() {

                                    AudioCodec.PcmToAudio(pcmPath, aacPath, new AudioCodec.AudioDecodeListener() {
                                        @Override
                                        public void decodeOver() {
                                            audioChange.setText("音频编码完成");
                                        }

                                        @Override
                                        public void decodeFail() {

                                        }
                                    });
                                }

                                @Override
                                public void decodeFail() {

                                }
                            });
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
