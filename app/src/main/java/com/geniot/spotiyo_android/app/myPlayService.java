package com.geniot.spotiyo_android.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by rmai on 6/25/14.
 */
public class myPlayService extends Service {

    private String videoId;
    @Override
    public void onCreate(){
        System.out.println("!!!!!!!My play service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,  int startId){
        videoId = intent.getExtras().getString("videoId");
        System.out.println("!!!!!!!My play service started");
        
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
}
