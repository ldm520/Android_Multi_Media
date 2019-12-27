package com.ldm.media.protocol;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

public interface IMuxer {
    void stop();
    void release();
    void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo);
    void start();
}