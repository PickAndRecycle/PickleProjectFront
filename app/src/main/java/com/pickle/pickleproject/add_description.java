package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.pickle.pickleprojectmodel.Trash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

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

            private EditText descForm;
            private final static String STORETEXT="storetext.txt";

            @Override
            public void onClick(View v) {
                changeNext();
                descForm = (EditText) findViewById(R.id.editText);

//                try {
//                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
//                    out.write(descForm.getText().toString()+"\n");
//                    out.close();
//                    Toast toast = new Toast(getApplicationContext());
//                    toast.makeText(add_description.this, "The contents are saved in the file.", toast.LENGTH_SHORT).show();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                //TOAST FOR DEBUGGING
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                toast.makeText(add_description.this, descForm.getText(), toast.LENGTH_SHORT).show();
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