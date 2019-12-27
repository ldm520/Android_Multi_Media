package com.ldm.media.surface;

public class Frame{
    public byte[] mData;//视频帧数据，nv21
    public long mTime;//时间戳
    public boolean mIsEos = false;//是否停止编码
}