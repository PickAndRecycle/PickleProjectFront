package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ThrowOrReport extends AppCompatActivity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_or_report);
        Button Throw = (Button) findViewById(R.id.ThrowButton);
        Throw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Throw();
            }
        });
        Button Report = (Button) findViewById(R.id.ReportButton);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report();
            }
        });
    }
    private void Throw(){
        Boolean report = false;
        Intent intent = new Intent(this, ThrowCategory.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("report", report.booleanValue());

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        /*
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowOrReport.this, parseInfo.toString(), boom.LENGTH_SHORT).show();
        */

        startActivity(intent);
    }
    private void Report() {
        Boolean report = true;
        Intent intent = new Intent(this, ReportSuccess.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("report", report.booleanValue());
        String pathPhoto = "sdcard/Pickle/cam_image.jpg";
        File picture = new File(pathPhoto);

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        /*
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowOrReport.this, intent.getStringExtra("description"), boom.LENGTH_SHORT).show();
        */


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/trash/";

        Trash trash = new Trash();
        //trash.setLatitude((int) intent.getDoubleExtra("latitude", 0.0) );
        //trash.setLongitude((int) intent.getDoubleExtra("longitude",0.0));
        trash.setUsername(intent.getStringExtra("username"));
        trash.setDesc(intent.getStringExtra("description"));
        trash.setReport(intent.getBooleanExtra("report", true));
        trash.setTitle("");
        trash.setCondition(UnusedCondition.UNSPECIFIED);
        Log.d("trash", trash.getDesc());


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(trash);
        Log.d("string", json);
        Map<String, String> map = new HashMap<String, String>();
        map.put("voInput", json);


        Log.d("json", json);

        final MultipartRequest multipartRequest = new MultipartRequest(url,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:", error.getMessage());
            }
        },new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("result", response);
            }
        },picture,map);

        mQueue.add(multipartRequest);

        startActivity(intent);

        /*

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


        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }


        private class TrashSerializer implements JsonSerializer<Trash> {
        @Override
        public JsonElement serialize(Trash src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id","");
            object.addProperty("categories","");
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
