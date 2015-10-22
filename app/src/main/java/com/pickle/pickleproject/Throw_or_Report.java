package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Throw_or_Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_or__report);
        Button Throw = (Button) findViewById(R.id.ThrowButton);
        Throw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Throw();
            }
        });
        Button Report = (Button) findViewById(R.id.ReportButton);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report();
            }
        });
    }
    private void Throw(){
        Intent intent = new Intent(this, throwCategory.class);
        startActivity(intent);
    }
    private void Report(){
        Intent intent = new Intent(this, reportSuccess.class);
        startActivity(intent);
    }

}
