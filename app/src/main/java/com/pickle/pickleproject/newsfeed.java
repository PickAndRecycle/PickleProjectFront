package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Newsfeed extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private GestureDetector gestureDetector;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        String url = "http://104.155.237.238:8080/article/"; //GCP
        //String url = "http://192.168.43.127:8080/article/";

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        mQueue.add(jsonRequest);

        ImageButton pickleJarButton = (ImageButton) findViewById(R.id.pickleJarButton);

        pickleJarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePickleJar();
            }
        });
    }

    private void changePickleJar(){
        Intent intent = new Intent(this, Picklejar.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);

    }

    private void onLeftSwipe() {
        changePickleJar();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error:",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");
            List<Article> ArticleList = new ArrayList<Article>();

            for(int i=0 ; i<parentArray.length();i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                Article articleObj;
                GsonBuilder gsonBuilder = new GsonBuilder();

                Gson gson = gsonBuilder.create();
                articleObj = gson.fromJson(String.valueOf(finalObject),Article.class);
                ArticleList.add(articleObj);
            }
            final Article[] newsfeedArray = ArticleList.toArray(new Article[0]);
            final NewsfeedAdapter myAdapter = new NewsfeedAdapter(this, R.layout.rownewsfeed, newsfeedArray);
            final ListView myList = (ListView) findViewById(R.id.newsfeedListview);
            myList.setAdapter(myAdapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbsLeftRight = Math.abs(e1.getY() - e2.getY());
                float diffLeftRight = e1.getX() - e2.getX();

                if (diffAbsLeftRight > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diffLeftRight > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Newsfeed.this.onLeftSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }



}
