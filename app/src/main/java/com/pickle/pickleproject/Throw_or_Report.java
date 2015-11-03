package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Throw_or_Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_or_report);
        Button Throw = (Button) findViewById(R.id.ThrowButton);
        Throw.setOnClickListener(new View.OnClickListener() {
            private boolean report;
            @Override
            public void onClick(View v) {
                Throw();
                report = false;
                //TOAST FOR DEBUGGING
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                toast.makeText(Throw_or_Report.this, "false", toast.LENGTH_SHORT).show();
            }
        });
        Button Report = (Button) findViewById(R.id.ReportButton);
        Report.setOnClickListener(new View.OnClickListener() {
            private boolean report;
            @Override
            public void onClick(View v) {
                Report();
                report = true;
                //TOAST FOR DEBUGGING
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                toast.makeText(Throw_or_Report.this, "true", toast.LENGTH_SHORT).show();
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
