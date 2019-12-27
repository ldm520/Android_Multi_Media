package com.ldm.media;

import android.content.Context;
import android.media.AudioFormat;
import android.os.Environment;

import java.io.File;

/**
 * @作者 ldm
 * @时间 2019/11/8 9:27
 * @描述 常量类
 */
public class Constant {
    /**
     * 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
     */
    public static final int SAMPLE_RATE_INHZ = 16000;

    /**
     * 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
     */
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    /**
     * 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
     */
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int VIDEO_WIDTH = 720;//视频宽度
    public static final int VIDEO_HEIGHT = 1280;//高度

    /**
     * 获取媒体文件的路径,不同的音频文件放在不同的文件夹中
     *
     * @param context
     * @param path     文件所在路径
     * @param fileName 文件名
     * @return
     */
    public static String getPath(Context context, String path, String fileName) {
        String p = getBaseFolder(context) + path;//获取媒体文件的目录
        File file = new File(p);
        if (!file.exists() && !file.mkdirs()) {
            return getBaseFolder(context) + fileName;
        }
        return p + fileName;
    }

    /**
     * 获取工作目录，如果没有的话创建工作目录
     *
     * @param context
     * @return
     */
    public static String getBaseFolder(Context context) {
        String baseFolder = Environment.getExternalStorageDirectory() + "/CodecFile/";
        File file = new File(baseFolder);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            if (!b) {
                baseFolder = context.getExternalFilesDir(null).getAbsolutePath() + "/";
            }
        }
        return baseFolder;
    }

    /**
     * @param path
     * @return
     */
    public static String getPath(Context context, String path) {
        String p = getBaseFolder(context) + path;
        File file = new File(p);
        if (!file.exists() && !file.mkdirs()) {
            return getBaseFolder(context);
        }
        return p;
    }
}
