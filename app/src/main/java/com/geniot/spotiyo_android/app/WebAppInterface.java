package com.geniot.spotiyo_android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.android.youtube.player.YouTubePlayerView;


/**
 * Created by rmai on 6/25/14.
 */
public class WebAppInterface {
    Context mContext;
    WebView mWebView;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c, WebView webview) {
        mContext = c;
        mWebView = webview;
    }

    /** Start video */
    @JavascriptInterface
    public void loadVideo(String rstp) {
        ((MainActivity)mContext).loadVideo(rstp);
    }

    @JavascriptInterface
    public void loadPlaylist(String rstps) {
        ((MainActivity)mContext).loadPlaylist(rstps);
    }

    @JavascriptInterface
    public void speak() {
        ((MainActivity)mContext).speak();
    }





    @JavascriptInterface
    public void spoken(String words) {
        mWebView.loadUrl("javascript:spoken(\"" + words  + "\")");
    }

    @JavascriptInterface
    public void listening() {
        mWebView.loadUrl("javascript:listening()");
    }
}