package com.geniot.spotiyo_android.app;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rmai on 6/25/14.
 */
public class myPlayService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

    private String rstp;
    private List<String> rstps;
    int count = 0;
    MediaPlayer mediaPlayer = new MediaPlayer();

    private void playTrack() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(rstps.get(count));
            mediaPlayer.prepareAsync();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(){
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags,  int startId){
        rstp = intent.getExtras().getString("rstp");
        rstps = Arrays.asList(rstp.split("\\s*,\\s*"));
        count = 0;
        playTrack();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
      super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        count++;
        playTrack();
    }
}
