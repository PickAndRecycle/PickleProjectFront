package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;

public class Home extends Activity {
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
        Button Swipebutton = (Button) findViewById(R.id.swiperightbtn);

        Swipebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePick();
            }
        });

        }

    private void changePick(){
        Intent intent = new Intent(this, Unused_goods.class);

        startActivity(intent);
    }

    private File getFile(){
        File folder = new File("sdcard/Pickle/media/");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,"cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/Pickle/media/cam_image.jpg";


    }
}
