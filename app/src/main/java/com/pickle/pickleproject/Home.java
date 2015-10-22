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
import android.widget.Button;
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
                startActivityForResult(camera_intent, CAM_REQUEST);


            }
        });


        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        }

    private void changePick(){
        Intent intent = new Intent(this, Unused_goods.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    private void changeAddDescription(){
        Intent intent = new Intent(this, add_description.class);
        startActivity(intent);

    }

    private void changeProfileActivity(){
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
        changeAddDescription();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        changePick();
    }

    private void onDownSwipe() {changeProfileActivity();}

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
                float diffAbsLeftRight = Math.abs(e1.getY() - e2.getY());
                float diffLeftRight = e1.getX() - e2.getX();

                float diffAbsUpDown = Math.abs(e1.getX() - e2.getX());
                float diffUpDown = e1.getY() - e2.getY();


                if (diffAbsLeftRight > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diffLeftRight > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Home.this.onLeftSwipe();

                    // Right swipe
                }

                if (diffAbsUpDown > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (-diffUpDown > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Home.this.onDownSwipe();

                    // Right swipe
                }

                 /*else if (-diff > SWIPE_MIN_DISTANCE
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

