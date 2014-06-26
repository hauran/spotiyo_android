package com.geniot.spotiyo_android.app;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    public static final String VIDEO_ID = "2Bgb-60LeK8";

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerFragment youTubePlayerFragment;


    private static final int RQS_ErrorDialog = 1;

    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaybackEventListener myPlaybackEventListener;

    String log = "";

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            serviceIntent = new Intent(this, myPlayService.class);
            initViews();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
//        Full screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        You Tube
        youTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);

        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaybackEventListener = new MyPlaybackEventListener();


//        Set up WebView
        WebView mWebView = (WebView) findViewById(R.id.activity_main_webview);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

//        mWebView.loadUrl("http://yoplay-nqitaj4wnb.elasticbeanstalk.com");
        mWebView.loadUrl("http://localhost:8080");


        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
            }
        });

    }

    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult result) {

        if (result.isUserRecoverableError()) {
            result.getErrorDialog(this, RQS_ErrorDialog).show();
        } else {
//            Toast.makeText(this,"YouTubePlayer.onInitializationFailure(): " + result.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {

        youTubePlayer = player;

        Toast.makeText(getApplicationContext(),"YouTubePlayer.onInitializationSuccess()",Toast.LENGTH_LONG).show();

        youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);

//        if (!wasRestored) {
//            player.cueVideo(VIDEO_ID);
//        }

    }

    public void loadVideo(String videoId) {
        showVideo();

        System.out.println("loadVideo: " + videoId);
        serviceIntent.putExtra("videoId", videoId);
        startService(serviceIntent);
//      stopService(serviceIntent);

//        youTubePlayer.loadVideo(videoId);
    }

    public void showVideo() {
        WebView mWebView = (WebView) findViewById(R.id.activity_main_webview);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mWebView.getLayoutParams();
        params.setMargins(0,0,0,330);
        mWebView.setLayoutParams(params);
    }

    public void hideVideo() {
        WebView mWebView = (WebView) findViewById(R.id.activity_main_webview);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mWebView.getLayoutParams();
        params.setMargins(0,0,0,0);
        mWebView.setLayoutParams(params);
    }

    private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {

        private void updateLog(String prompt){
//            log +=  "MyPlayerStateChangeListener" + "\n" +
//                    prompt + "\n\n=====";
//            System.out.print(log);
        };

        @Override
        public void onAdStarted() {
            updateLog("onAdStarted()");
        }

        @Override
        public void onError(
                com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
            updateLog("onError(): " + arg0.toString());
        }

        @Override
        public void onLoaded(String arg0) {
            updateLog("onLoaded(): " + arg0);
        }

        @Override
        public void onLoading() {
            updateLog("onLoading()");
        }

        @Override
        public void onVideoEnded() {
            updateLog("onVideoEnded()");
        }

        @Override
        public void onVideoStarted() {
            updateLog("onVideoStarted()");
        }

    }

    private final class MyPlaybackEventListener implements PlaybackEventListener {

        private void updateLog(String prompt){
//            log +=  "MyPlaybackEventListener" + "\n-" +
//                    prompt + "\n\n=====";
//            System.out.print(log);
        };

        @Override
        public void onBuffering(boolean arg0) {
            updateLog("onBuffering(): " + String.valueOf(arg0));
        }

        @Override
        public void onPaused() {
            updateLog("onPaused()");
        }

        @Override
        public void onPlaying() {
            updateLog("onPlaying()");
        }

        @Override
        public void onSeekTo(int arg0) {
            updateLog("onSeekTo(): " + String.valueOf(arg0));
        }

        @Override
        public void onStopped() {
            updateLog("onStopped()");
        }

    }

}