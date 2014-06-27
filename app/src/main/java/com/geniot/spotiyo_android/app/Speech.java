package com.geniot.spotiyo_android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmai on 6/26/14.
 */
public class Speech {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    Context mContext;

    Speech(Context c) {
        mContext = c;
    }

    private Activity atv;
    public Speech(Activity atv) {
        this.atv=atv;
    }

    protected void initSpeech() {
        checkVoiceRecognition();
    }

    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = atv.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(atv, "Voice recognizer not present",Toast.LENGTH_SHORT).show();
        }
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());

        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        int noOfMatches = 1;
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        atv.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    void showToastMessage(String message){
        Toast.makeText(atv, message, Toast.LENGTH_SHORT).show();
    }
}
