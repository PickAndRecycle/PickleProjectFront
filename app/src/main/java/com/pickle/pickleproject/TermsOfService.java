package com.pickle.pickleproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

public class TermsOfService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebView web = (WebView) findViewById(R.id.termsofservicecontent);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());

        String filePath = "file:///android_asset/termsofservice.html";
        web.loadUrl(filePath);
    }
}