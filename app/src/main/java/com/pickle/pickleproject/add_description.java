package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

public class add_description extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        String path = "sdcard/Pickle/cam_image.jpg";
        RelativeLayout trashPhoto = (RelativeLayout) findViewById(R.id.trashPhoto);
        trashPhoto.setBackground(Drawable.createFromPath(path));

        Button NextButton = (Button) findViewById(R.id.nextButton);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNext();
            }
        });

        Button HomeButton = (Button) findViewById(R.id.backButton);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBack();
            }
        });
    }
    private void changeNext(){
        Intent intent = new Intent(this, Throw_or_Report.class);
        startActivity(intent);
    }
    private void changeBack(){
        Intent intent = new Intent(this, Home.class);
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

}
