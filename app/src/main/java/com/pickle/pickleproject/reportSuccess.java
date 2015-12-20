package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ReportSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_success);
        MontserratButton backToHomeBut = (MontserratButton) findViewById(R.id.backToHomeButton);
        backToHomeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home();
            }
        });
        MontserratButton shareButton = (MontserratButton) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeShare();
            }
        });
    }

    private void changeShare(){
        Intent sendIntent = new Intent();
        String successShare = "I have successfully created a trash report. Report the trash condition of your environment only at Pickle!";
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, successShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void Home(){
        Intent home = new Intent(this,Home.class);
        startActivity(home);
    }


}
