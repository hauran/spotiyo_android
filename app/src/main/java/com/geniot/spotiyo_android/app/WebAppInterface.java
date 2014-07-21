package com.geniot.spotiyo_android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


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

    @JavascriptInterface
    public void play(String uri) {
        ((MainActivity)mContext).play(uri);
    }

    @JavascriptInterface
    public void speak() {
        ((MainActivity)mContext).speak();
    }

    @JavascriptInterface
    public void playerPlay() {
        ((MainActivity)mContext).playerPlay();
    }

    @JavascriptInterface
    public void playerPause() {
        ((MainActivity)mContext).playerPause();
    }

    @JavascriptInterface
    public void playerNext() {
        ((MainActivity)mContext).playerNext();
    }

    @JavascriptInterface
    public void skipForward(int skip) { ( (MainActivity)mContext).playerSkipForward(skip); }

    @JavascriptInterface
    public void skipBack(int skip) {
        ((MainActivity)mContext).playerSkipBack(skip);
    }

    @JavascriptInterface
    public void homeButton() {
        ((MainActivity)mContext).homeButton();
    }



    @JavascriptInterface
    public void spoken(String words) {
        mWebView.loadUrl("javascript:spoken(\"" + words  + "\")");
    }

    @JavascriptInterface
    public void listening() {mWebView.loadUrl("javascript:listening()");}

    @JavascriptInterface
    public void setPlaying() { mWebView.loadUrl("javascript:setPlaying()");}

    @JavascriptInterface
    public void skipNext() {
        mWebView.loadUrl("javascript:skipNext()");
    }

    @JavascriptInterface
    public void back() {
        mWebView.loadUrl("javascript:back()");
    }

}