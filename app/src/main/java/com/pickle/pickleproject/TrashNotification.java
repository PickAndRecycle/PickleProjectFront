package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Trash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class TrashNotification extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_notification);
        MontserratButton Back_Home = (MontserratButton) findViewById(R.id.Back_Home);
        Back_Home.setOnClickListener(new View.OnClickListener() {
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


        Bundle parseInfo = getIntent().getExtras();


    }

    private void changeShare(){
        Intent sendIntent = new Intent();
        String successShare = "I have successfully throw a trash. Throw your trash only at Pickle!";
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,successShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public void Home(){
        Intent home = new Intent(this,Home.class);
        finish();
        startActivity(home);
    }

    public void onBackPressed(){
        Home();
    }

}



