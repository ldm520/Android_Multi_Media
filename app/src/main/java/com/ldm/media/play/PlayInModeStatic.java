package com.ldm.media.play;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Build;

import android.util.Log;


import androidx.annotation.RequiresApi;

import com.ldm.media.R;
import com.ldm.media.protocol.StartPlayable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PlayInModeStatic implements StartPlayable {
    private static final String TAG = PlayInModeStatic.class.getSimpleName();
    private byte[] audioData;
    private AudioTrack audioTrack;
    private Context context;
    public PlayInModeStatic(Context mContext){
        context = mContext;
    }
    @Override
    public void startPlayPcm(File file) {
        new  AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
            try {
                InputStream in = context.getResources().openRawResource(R.raw.ding);
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    for(int i = 0;(i = in.read()) != -1;){
                        out.write(i);
                    }
                    Log.d(TAG,"得到音频数据");
                    audioData = out.toByteArray();
                }finally {
                    in.close();
                }
            }catch (IOException e){
               Log.d(TAG,"读取失败");
            }
                return null;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(TAG,"Ctreating Track... audioData.length == " + audioData.length);
                audioTrack = new AudioTrack(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                        new AudioFormat.Builder().setSampleRate(22050)
                                .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build(),
                        audioData.length,
                        AudioTrack.MODE_STATIC,
                        AudioManager.AUDIO_SESSION_ID_GENERATE
                );
                Log.d(TAG,"Writing audio data...");
                audioTrack.write(audioData,0,audioData.length);
                Log.d(TAG,"Starting play");
                audioTrack.play();
                Log.d(TAG,"Playing");
            }
        }.execute();
    }

    @Override
    public void stopPlayPcm() {
        if (audioTrack != null){
            audioTrack.stop();
            audioTrack.release();
        }
    }
}