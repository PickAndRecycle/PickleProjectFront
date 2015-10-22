package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class add_description extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);
        Button NextButton = (Button) findViewById(R.id.nextbutton);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNext();
            }
        });

        Button HomeButton = (Button) findViewById(R.id.backbutton);
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

}
