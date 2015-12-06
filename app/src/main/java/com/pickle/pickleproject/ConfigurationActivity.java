package com.pickle.pickleproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;
import java.util.concurrent.locks.Condition;

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

        Button aboutUsButton = (Button) findViewById(R.id.AboutUsButton);

        aboutUsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeAboutUs();
            }
        });

        final Configuration config = new Configuration();
        final Locale locale = Locale.getDefault();
        Button language = (Button) findViewById(R.id.ChangeLanguageButton);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locale.toString().toLowerCase().equals("en_us")){
                    Locale indonesia = new Locale("in");
                    Locale.setDefault(indonesia);
                    config.locale = indonesia;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                }
                else {
                    Locale english = new Locale("en_us");
                    Locale.setDefault(english);
                    config.locale = english;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);


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

    private void changeAboutUs(){
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
