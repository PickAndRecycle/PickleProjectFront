package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import java.io.File;


public class Home extends Activity   {
    int CAM_REQUEST =1;
    ImageButton Camera;
    private GestureDetector gestureDetector;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Camera = (ImageButton) findViewById(R.id.Camera_Button);
        Camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);


            }
        });


        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        }

    private void changePick(){
        Intent intent = new Intent(this, Unused_goods.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_out,R.anim.push_left_in);
    }

    private File getFile(){
        File folder = new File("sdcard/Pickle");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,"cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/Pickle/cam_image.jpg";
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        changePick();
    }

    /*private void onRightSwipe() {
        // Do something
    }*/

    // Private class for gestures
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
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Home.this.onLeftSwipe();

                    // Right swipe
                } /*else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    YourActivity.this.onRightSwipe();
                }*/
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }


}

