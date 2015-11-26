package com.pickle.pickleproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AddDescription extends AppCompatActivity {
    public static final String PREFS_NAME = "PicklePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        String path = "sdcard/Pickle/cam_image.jpg";
        RelativeLayout trashPhoto = (RelativeLayout) findViewById(R.id.trashPhoto);
        trashPhoto.setBackground(Drawable.createFromPath(path));

        Button NextButton = (Button) findViewById(R.id.nextButton);
        NextButton.setOnClickListener(new View.OnClickListener() {

            /*
            private final static String STORETEXT="description.txt";
            */

            @Override
            public void onClick(View v) {
                changeNext();


                /*
                try {
                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
                    out.write(descForm.getText().toString()+"\n");
                    out.close();
                    Toast toast = new Toast(getApplicationContext());
                    toast.makeText(AddDescription.this, "The contents are saved in the file.", toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

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
        //Get username from preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String user = settings.getString("username", "");

        EditText descForm;
        descForm = (EditText) findViewById(R.id.editText);
        Intent intent = new Intent(this, ThrowOrReport.class);
        //To pass descForm into the next page
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("description", descForm.getText().toString());
        intent.putExtra("username", user);

        //TOAST FOR DEBUGGING

        Bundle parseInfo = intent.getExtras();
        /*
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(AddDescription.this, parseInfo.toString(), boom.LENGTH_SHORT).show();
        */

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
