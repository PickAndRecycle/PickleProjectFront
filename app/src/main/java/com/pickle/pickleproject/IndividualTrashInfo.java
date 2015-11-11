package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IndividualTrashInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_trash_info);

        Button pickButton = (Button) findViewById(R.id.pick_button);
        Button backButton = (Button) findViewById(R.id.back_button_2);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeThrowerInfo();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeThrowerInfo(){
        Intent intent = new Intent(this, ThrowerInfo.class);
        startActivity(intent);
    }
}
