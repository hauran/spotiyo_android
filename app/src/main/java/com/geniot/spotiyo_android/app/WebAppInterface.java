package com.geniot.spotiyo_android.app;

import android.content.Context;
import android.content.Intent;
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
    public void showVideo(String videoId) {
        System.out.println("loadVideo: " + videoId);

//        ytplayer = new YouTubePlayerView.initialize();
    }


}