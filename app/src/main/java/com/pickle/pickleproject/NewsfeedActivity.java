package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Newsfeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedActivity extends AppCompatActivity  implements Response.ErrorListener, Response.Listener<JSONObject> {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
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
        try {
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");
            List<com.pickle.pickleprojectmodel.Newsfeed> newsfeedList = new ArrayList<com.pickle.pickleprojectmodel.Newsfeed>();
            for(int i=0 ; i<parentArray.length();i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);

                com.pickle.pickleprojectmodel.Newsfeed newsfeedObj;
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                newsfeedObj = gson.fromJson(String.valueOf(finalObject), com.pickle.pickleprojectmodel.Newsfeed.class);
                newsfeedList.add(newsfeedObj);
            }
            Newsfeed[] newsfeedsArray = newsfeedList.toArray(new com.pickle.pickleprojectmodel.Newsfeed[0]);
            NewsfeedAdapter myAdapter=new NewsfeedAdapter(NewsfeedActivity.this, R.layout.rownewsfeed, newsfeedsArray);
            ListView myList = (ListView)
                    findViewById(R.id.newsfeedListview);
            myList.setAdapter(myAdapter);


        } catch (JSONException e) {
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
                    NewsfeedActivity.this.onLeftSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }



}
