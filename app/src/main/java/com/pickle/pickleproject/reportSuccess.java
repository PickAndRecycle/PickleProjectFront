package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class reportSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_success);
        Button backToHomeBut = (Button) findViewById(R.id.backToHomeButton);
        backToHomeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home();
            }
        });
    }

    public void Home(){
        Intent home = new Intent(this,Home.class);
        startActivity(home);
    }


}
