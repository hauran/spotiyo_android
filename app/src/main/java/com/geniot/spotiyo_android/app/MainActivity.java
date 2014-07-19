package com.geniot.spotiyo_android.app;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import java.util.ArrayList;
import java.util.List;
import android.view.KeyEvent;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;


public class MainActivity extends Activity implements PlayerNotificationCallback, ConnectionStateCallback {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    WebAppInterface webAppInterface;
    public static int LONG_PRESS_TIME = 500;
    private Player mPlayer;
    WebView mWebView;

    @Override
    public void onBackPressed()
    {
        webAppInterface.back();
    }

    public boolean homeButton()
    {
      moveTaskToBack(true);
      return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initViews();
            initSpeech();
            SpotifyAuthentication.openAuthWindow(Keys.SPOTIFY_CLIENT_ID, "token", Keys.SPOTIFY_REDIRECT_URI,
                    new String[]{"user-read-private", "streaming"}, null, this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    mPlayer.addConnectionStateCallback(MainActivity.this);
                    mPlayer.addPlayerNotificationCallback(MainActivity.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
    }

    @Override
    public void onLoggedIn() {
        System.out.println("MainActivity: User logged in");
    }


    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onNewCredentials(String s) {
        System.out.println("MainActivity: User credentials blob received");
    }

    @Override
    public void onConnectionMessage(String message) {
        System.out.println("MainActivity: Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType) {
        System.out.println("MainActivity: Playback event received: " + eventType.name());

//        PLAY
//        TRACK_CHANGED
//        SKIP_NEXT
        if(eventType.name().equals("PLAY")) {
            webAppInterface.setPlaying();
        }
        else if(eventType.name().equals("SKIP_NEXT")) {
            webAppInterface.skipNext();
        }
//        else if(eventType.name().equals("TRACK_CHANGED")){
//            webAppInterface.setPlayingText();
//        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    private void initViews() {
//        Full screen
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Set up WebView
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webAppInterface = new WebAppInterface(this, mWebView);
        mWebView.addJavascriptInterface(webAppInterface, "Android");
        mWebView.setLongClickable(true);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                speak();
                return true;
            }
        });

        mWebView.loadUrl(Keys.WEB_VIEW_URL);

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
            }
        });

    }

    public void play(String uri){
        mPlayer.play(uri);
    }

    public void playerPlay(){
        mPlayer.resume();
    }

    public void playerPause(){ mPlayer.pause();}


    public void playerNext(){mPlayer.skipToNext();}

    public void playerSkipForward(int skip){
        for(int i=0; i<skip; i++){
            mPlayer.skipToNext();
        }
    }

    public void playerSkipBack(int skip){
        for(int i=0; i<skip; i++) {
            mPlayer.skipToPrevious();
        }
    }

    /*************************************/
    /************* SPEECH ****************/
    /*************************************/

    public void initSpeech() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(this, "Voice recognizer not present",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, ((Object) this).getClass().getPackage().getName()).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, ((Object) this).getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "A song, artist or playlist");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        webAppInterface.listening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showToastMessage("requestCode " +  requestCode);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
            showToastMessage("here " +  resultCode);
            //If Voice recognition is successful then it returns RESULT_OK
            if(resultCode == RESULT_OK) {
                showToastMessage("RESULT_OK");
                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                System.out.println(textMatchList.get(0));
                if (!textMatchList.isEmpty()) {
                    spoken(textMatchList.get(0));
                }
                //Result code for various error.
            }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
                showToastMessage("Audio Error");
            }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
                showToastMessage("Client Error");
            }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
                showToastMessage("Network Error");
            }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
                showToastMessage("No Match");
            }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
                showToastMessage("Server Error");
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * Helper method to show the toast message
     **/
    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void spoken (String words){
        webAppInterface.spoken(words);
    }
}