package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class pick_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button pick = (Button) findViewById(R.id.pickButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePick();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCancel();
            }
        });
    }

    private void changePick(){
        Intent intent = new Intent(this, Thrower_Info.class);
        startActivity(intent);
    }

    private void changeCancel(){
        Intent intent = new Intent(this, pickGeneral.class);
        startActivity(intent);
    }

}
