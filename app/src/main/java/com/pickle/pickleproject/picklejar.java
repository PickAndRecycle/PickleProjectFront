package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Picklejar extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private GestureDetector gestureDetector;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picklejar);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        //Use your machine IP (Mac/Linux ifconfig in terminal, Windows ipconfig in cmd) IP should be 192.x.x.x
        String url = "http://192.168.0.103:8080/trash/";
        //String url = "http://private-22976-pickleapi.apiary-mock.com/trash";

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        mQueue.add(jsonRequest);

    }

    private void changeHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void changeNews() {
        Intent intent = new Intent(this, Newsfeed.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);

    }
    public void onResponse(JSONObject response){
        try{
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");
            List <Trash> Trashlist = new ArrayList<Trash>();
            String username ="Yanuar";
            for (int i=0; i<parentArray.length(); i++){
                JSONObject finalObject = parentArray.getJSONObject(i);
                Trash trashObj;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TrashCategories.class,new TrashCategoriesDeserialize());
                Gson gson = gsonBuilder.create();

                if (finalObject.getString("username").equals(username)) {
                    if (finalObject.getString("status").equals(1)) {
                        trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);
                        Trashlist.add(0, trashObj);
                    } else {
                        trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);
                        Trashlist.add(trashObj);
                    }
                }


                final Trash[] trashArray = Trashlist.toArray(new Trash[0]);
                final PicklejarAdapter myAdapter=new PicklejarAdapter(Picklejar.this, R.layout.rowpicklejar, trashArray);
                final ListView myList = (ListView)
                        findViewById(R.id.ListPickleJar);
                myList.setAdapter(myAdapter);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error:", error.getMessage());
    }

    private class TrashCategoriesDeserialize implements JsonDeserializer<TrashCategories> {
        @Override
        public TrashCategories deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Unused Goods")){
                return TrashCategories.UNUSED;
            } else if (json.getAsString().equals("General Waste")){
                return TrashCategories.GENERAL;
            } else if (json.getAsString().equals("Recycleable Waste")){
                return TrashCategories.RECYCLED;
            } else if (json.getAsString().equals("Green Waste")){
                return TrashCategories.GREEN;
            } else {
                return TrashCategories.UNSPECIFIED;
            }
        }
    }





    private void onLeftSwipe() {
        changeHome();
    }

    private void onRightSwipe() {
        changeNews();
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
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Picklejar.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Picklejar.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}