package com.ldm.media.protocol;

import com.ldm.media.surface.Frame;

public interface IEncoder {
    void prepare();//初始化编码器
    void input(Frame frame);//输入相机数据
    void output(boolean isEos);//输出编码后的数据
    void eos();//终止编码器
    void release();//释放编码器
}