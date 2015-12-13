package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 12/10/2015.
 */
public class RegistrationSuccess  extends AppCompatActivity {
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
        Button shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setVisibility(View.INVISIBLE);

        ImageView image = (ImageView) findViewById(R.id.reportImage);
        image.setImageResource(R.mipmap.circlecheck);
        image.getLayoutParams().height = 250;
        image.getLayoutParams().width =250;
        image.setMaxWidth(100);
        TextView textView = (TextView) findViewById(R.id.successReportText);
        textView.setText(R.string.registrationSuccess);
    }
    public void Home(){
        Intent home = new Intent(this,Home.class);
        startActivity(home);
    }
}