package com.pickle.pickleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Button TermsOfServiceButton = (Button) findViewById(R.id.TermsOfServiceButton);

        TermsOfServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTermsOfService();
            }
        });

        Button PrivacyPolicyButton = (Button) findViewById(R.id.PrivacyPolicyButton);

        PrivacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePrivacyPolicy();
            }
        });

        Button backToMyProfileButton = (Button) findViewById(R.id.BackToMyProfileButton);

        backToMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });
    }

    private void changeTermsOfService(){
        Intent intent = new Intent(this, TermsOfService.class);
        startActivity(intent);
    }

    private void changePrivacyPolicy(){
        Intent intent = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    private void changeProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
