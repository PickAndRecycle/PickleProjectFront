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
import android.widget.Button;
import java.io.File;
import android.content.Context;


public class Home extends Activity   {
    int CAM_REQUEST =1;
    ImageButton Camera;


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

        /*
        Button Swipebutton;
        Swipebutton = (Button) findViewById(R.id.swiperightbtn);

        Swipebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePick();
            }
        });
        */

            view.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeLeft() {
                    // Whatever
                }
            });


        }

    private void changePick(){
        Intent intent = new Intent(this, Unused_goods.class);

        startActivity(intent);
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




}

