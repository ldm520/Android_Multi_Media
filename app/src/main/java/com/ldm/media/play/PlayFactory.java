package com.ldm.media.play;

import android.content.Context;

import com.ldm.media.protocol.StartPlayable;

public class PlayFactory {
    private Context context;
    public static final int STREAMMODE = 1;
    public static final int STATICMODE = 2;
    public PlayFactory(Context mContext){
        context = mContext;
    }
    public StartPlayable createPlay(int type){
        switch (type){
            case STREAMMODE:
                return new PlayInModeStream(context);
            case STATICMODE:
                return new PlayInModeStatic(context);
                default:
                    return new PlayInModeStream(context);
        }
    }
}