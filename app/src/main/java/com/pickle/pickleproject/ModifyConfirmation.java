package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Trash;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;

public class ModifyConfirmation extends AppCompatActivity {
    private RequestQueue mRequestQueue,aQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_confirmation);

        final Trash trash = (Trash) getIntent().getSerializableExtra("object");
        final Button modifyButton = (Button) findViewById(R.id.modify);
        Button backButton = (Button) findViewById(R.id.backToModify);
        if (trash == null) {
            Toast.makeText(getApplicationContext(), "Null Object", Toast.LENGTH_SHORT).show();
        }
        if (trash.getStatus() ==1){
            modifyButton.setText(R.string.done);
        }
        final String secureID = trash.getId();
        MontserratTextView description = (MontserratTextView) findViewById(R.id.descModify);
        description.setText(trash.getDesc());

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
                        JSONObject parentObject = response;
                        Log.d("json2:", response.getString("result"));
                        JSONObject finalObject = parentObject.getJSONObject("result");
                        NetworkImageView trashInfo = (NetworkImageView) findViewById(R.id.ImageModify);
                        trashInfo.setImageUrl(finalObject.getString("photo_url"), mImageLoader);
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
            mRequestQueue.add(jsonRequest);

            NetworkImageView trashInfo = (NetworkImageView) findViewById(R.id.ImageModify);
            trashInfo.setImageUrl("http://i63.tinypic.com/312zpeu.jpg", mImageLoader);

            modifyButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if (trash.getStatus() ==1){
                            changeStatus(trash);
                        }
                        else if (trash.getCategories().toString().equals("Unused Goods")) {
                            ModifyUnused(trash);
                        } else {
                            ModifyOther(trash);
                        }

                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "No trash found!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goBack();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void changeStatus(Trash trash) {
        trash.setStatus(2);
        GsonBuilder gsonBuilder = new GsonBuilder();

        aQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "http://104.155.237.238:8080/trash/" + trash.getId();

        gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(trash);
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
            aQueue.add(jsonRequest);
            goBack();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ModifyUnused(Trash trash){
        Intent intent = new Intent(this,ModifyRequestUnused.class);
        intent.putExtra("object",  trash);
        startActivity(intent);
    }
    private void ModifyOther(Trash trash){
        Intent intent = new Intent(this,ModifyRequestOther.class);
        intent.putExtra("object", trash);
        startActivity(intent);
    }

    private void goBack(){
        finish();
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
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


