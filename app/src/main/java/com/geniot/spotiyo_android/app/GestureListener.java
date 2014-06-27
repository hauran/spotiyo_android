package com.geniot.spotiyo_android.app;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by rmai on 6/26/14.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            System.out.print("LEFT");
            return true; // Right to left
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            System.out.print("RIGHT");
            return true; // Left to right

        }

        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            return false; // Bottom to top
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            System.out.print("DOWN");
            return true; // Top to bottom
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //always return true since all gestures always begin with onDown and<br>
        //if this returns false, the framework won't try to pick up onFling for example.
        return true;
    }
}