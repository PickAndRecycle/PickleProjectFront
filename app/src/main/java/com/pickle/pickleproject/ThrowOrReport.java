package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ThrowOrReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_or_report);
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
        Intent intent = new Intent(this, ThrowCategory.class);
        startActivity(intent);
        //TOAST FOR DEBUGGING
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        toast.makeText(ThrowOrReport.this, "false", toast.LENGTH_SHORT).show();
    }
    private void Report(){
        Intent intent = new Intent(this, ReportSuccess.class);
        startActivity(intent);
        //TOAST FOR DEBUGGING
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        toast.makeText(ThrowOrReport.this, "true", toast.LENGTH_SHORT).show();
    }

}
