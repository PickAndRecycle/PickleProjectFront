package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Trash;

import android.util.LruCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class IndividualTrashInfo extends AppCompatActivity {
    private RequestQueue mRequestQueue, mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_trash_info);

        Button pickButton = (Button) findViewById(R.id.pick_button);
        Button backButton = (Button) findViewById(R.id.back_button_2);

        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        NetworkImageView trashInfo = (NetworkImageView) findViewById(R.id.trashInfoPicture);
        trashInfo.setImageUrl("http://i63.tinypic.com/312zpeu.jpg", mImageLoader);

        final Trash trash = (Trash) getIntent().getSerializableExtra("object");
        final String secureID = trash.getId();
        MontserratTextView description = (MontserratTextView) findViewById(R.id.descriptionText);
        description.setText(trash.getDesc());

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trash.setStatus(1);
                mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

                String url = "http://104.155.237.238:8080/trash/" + secureID;
                GsonBuilder gsonBuilder = new GsonBuilder();

                gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
                Gson gson = gsonBuilder.create();
                String json = gson.toJson(trash);
                Log.d("json", json);
                try {
                    final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {
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
                    changeThrowerInfo();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private class TrashSerializer implements JsonSerializer<Trash> {
        @Override
        public JsonElement serialize(Trash src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("categories",src.getCategories().toString());
            object.addProperty("username",src.getUsername());
            object.addProperty("status",src.getStatus());
            object.addProperty("description",src.getDesc());
            object.addProperty("distance",src.getDistance());
            object.addProperty("photo_url",src.getPhoto_url());
            object.addProperty("latitude",src.getLatitude());
            object.addProperty("longitude",src.getLongitude());
            object.addProperty("report",src.isReport());
            object.addProperty("title",src.getTitle());
            object.addProperty("trash_condition","");
            object.addProperty("size",src.getsize());
            return object;
        }
    }

}
