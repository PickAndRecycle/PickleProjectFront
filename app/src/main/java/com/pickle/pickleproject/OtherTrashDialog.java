package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class OtherTrashDialog extends AppCompatActivity {
    private EditText sizeForm;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods_dialog);

    Button submitUnusedButton = (Button) findViewById(R.id.submitButtonUnusedGoods);
    //Button backHomeButton = (Button) findViewById(R.id.trash_notification_home);

    submitUnusedButton.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            changeSubmitButton();
        }
    });

    /*backHomeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeBackHomeButton();
        }
    });*/

    }

    private void changeSubmitButton(){
        sizeForm = (EditText) findViewById(R.id.editText3);
        Intent intent = new Intent(this, TrashNotification.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("size", Integer.parseInt(sizeForm.getText().toString()));

        //TOAST FOR DEBUGGING

        Bundle parseInfo = intent.getExtras();
        /*
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(OtherTrashDialog.this, parseInfo.toString(), boom.LENGTH_SHORT).show();
        */

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/trash/";

        Trash trash = new Trash();
        //trash.setLatitude((int) intent.getDoubleExtra("latitude",0.0) );
        //trash.setLongitude((int) intent.getDoubleExtra("longitude",0.0));
        trash.setDesc(intent.getStringExtra("description"));
        Log.d("description", trash.getDesc());
        trash.setReport(intent.getBooleanExtra("report", false));
        Log.d("categories", intent.getStringExtra("categories").toUpperCase());
        trash.setCategories(TrashCategories.valueOf(intent.getStringExtra("categories").toUpperCase()));
        trash.setTitle("");
        trash.setCondition(null);
        trash.setsize(Integer.parseInt(sizeForm.getText().toString()));


        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(trash);
        Log.d("json", json);
        try {
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("result", response.getString("result"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error:", error.getMessage());
                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(jsonRequest);
            startActivity(intent);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }



    }
    /*
    void changeBackHomeButton(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    */



    private class TrashSerializer implements JsonSerializer<Trash> {
        @Override
        public JsonElement serialize(Trash src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("categories",src.getCategories().toString());
            object.addProperty("username",src.getUsername());
            object.addProperty("status",src.getStatus());
            object.addProperty("description",src.getDesc());
            object.addProperty("distance", src.getDistance());
            object.addProperty("photo_url", src.getPhoto_url());
            object.addProperty("latitude", src.getLatitude());
            object.addProperty("longitude",src.getLongitude());
            object.addProperty("report",src.isReport());
            object.addProperty("title", src.getTitle());
            object.addProperty("trash_condition", "");
            object.addProperty("size", src.getsize());
            return object;
        }
    }
}
