package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MyProfileActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageButton configurationButton = (ImageButton) findViewById(R.id.configurationButton);

        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeConfiguration();
            }
        });

        gestureDetector = new GestureDetector(new SwipeGestureDetector());
    }

    private void changeConfiguration(){
        Intent intent = new Intent(this,ConfigurationActivity.class);
        startActivity(intent);
    }

    private void changeHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onUpSwipe(){changeHome();}

    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                //float diffAbsLeftRight = Math.abs(e1.getY() - e2.getY());
                //float diffLeftRight = e1.getX() - e2.getX();

                float diffAbsUpDown = Math.abs(e1.getX() - e2.getX());
                float diffUpDown = e1.getY() - e2.getY();

                if (diffAbsUpDown > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diffUpDown > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    MyProfileActivity.this.onUpSwipe();

                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}
