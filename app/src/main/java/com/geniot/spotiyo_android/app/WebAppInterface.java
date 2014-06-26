package com.geniot.spotiyo_android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.google.android.youtube.player.YouTubePlayerView;


/**
 * Created by rmai on 6/25/14.
 */
public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Start video from web app */
    @JavascriptInterface
    public void loadVideo(final String videoId) {
        ((MainActivity)mContext).runOnUiThread(new Runnable(){
            public void run(){
                ((MainActivity)mContext).loadVideo(videoId);
            }

        });
    }
}