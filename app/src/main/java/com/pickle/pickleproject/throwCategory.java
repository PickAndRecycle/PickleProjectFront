package com.pickle.pickleproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class throwCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_category);

    ImageButton unusedButton = (ImageButton) findViewById(R.id.UnusedButton);
    ImageButton generalButton = (ImageButton) findViewById(R.id.GeneralButton);
    ImageButton recycleButton = (ImageButton) findViewById(R.id.RecycleButton);
    ImageButton greenButton = (ImageButton) findViewById(R.id.GreenButton);

        Button backButton = (Button) findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    unusedButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeUnusedButton();
        }
    });
    generalButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeGeneralButton();
        }
    });
    recycleButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeRecycleButton();
        }
    });
    greenButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeGreenButton();
        }
    });

    }

    private void changeUnusedButton(){
        Intent intent = new Intent(this, unused_goods_dialog.class);
        startActivity(intent);
    }

    private void changeGeneralButton(){
        Intent intent = new Intent(this, other_trash_dialog.class);
        startActivity(intent);
    }

    private void changeRecycleButton(){
        Intent intent = new Intent(this, other_trash_dialog.class);
        startActivity(intent);
    }

    private void changeGreenButton(){
        Intent intent = new Intent(this, other_trash_dialog.class);
        startActivity(intent);
    }

}
