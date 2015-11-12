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
import com.pickle.pickleprojectmodel.Trash;

import android.util.LruCache;

import org.json.JSONObject;

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
        trashInfo.setImageUrl("http://104.155.213.80/insantani/public/api/products/1/picture", mImageLoader);

        Trash trash = (Trash) getIntent().getSerializableExtra("object");
        final String secureID = trash.getId();
        trash.setStatus(1);


        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeThrowerInfo();

                mQueue = Volley.newRequestQueue(getApplicationContext());

                String url = "http://192.168.0.100:8080/trash/"+secureID+"/";
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", String.valueOf(error));
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("status", "1");

                        return params;
                    }

                };

                mQueue.add(putRequest);

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
