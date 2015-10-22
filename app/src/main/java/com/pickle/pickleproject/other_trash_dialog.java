package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class other_trash_dialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trash_dialog);


    Button submitOtherButton = (Button) findViewById(R.id.submitButtonOtherWaste);

    submitOtherButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeSubmitButton();
        }
    });

    }

    private void changeSubmitButton(){
        Intent intent = new Intent(this, TrashNotification.class);
        startActivity(intent);
    }

}
