package com.ldm.media.protocol;

import java.io.File;
import java.io.FileFilter;

public interface StartPlayable {
    void startPlayPcm(File Filter);

    void stopPlayPcm();
}