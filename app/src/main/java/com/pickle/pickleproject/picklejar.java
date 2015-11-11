package com.pickle.pickleproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Picklejar extends AppCompatActivity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picklejar);
        new JSONTask().execute("http://private-22976-pickleapi.apiary-mock.com/trash");
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

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
    public class JSONTask extends AsyncTask<String,String,List<Trash>> {

        @Override
        protected List<Trash> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("Result");

                List<Trash> Trashlist = new ArrayList<Trash>();

                for(int i=0 ; i<parentArray.length();i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Trash trashObj = new Trash();
                    if (finalObject.getString("categories").equals("Unused Goods")){
                        trashObj.setCategories(TrashCategories.UNUSED);
                        trashObj.setDistance(finalObject.getInt("distance"));
                        trashObj.setTitle(finalObject.getString("title"));
                        if(finalObject.getString("condition").equals("Good" )){
                            trashObj.setCondition(UnusedCondition.GOOD);
                        } else if(finalObject.getString("condition").equals("Bad")){
                            trashObj.setCondition(UnusedCondition.BAD);
                        } else if (finalObject.getString("condition").equals("New")){
                            trashObj.setCondition(UnusedCondition.NEW);
                        }
                        Trashlist.add(trashObj);
                    }
                    else{
                        if(finalObject.getString("categories").equals("General Waste") ){
                            trashObj.setCategories(TrashCategories.GENERAL);
                        }
                        else if(finalObject.getString("categories").equals("Green Waste")){
                            trashObj.setCategories(TrashCategories.GREEN);
                        }
                        else if(finalObject.getString("categories").equals("Recycleable Waste") ){
                            trashObj.setCategories(TrashCategories.RECYCLED);

                        }
                        trashObj.setDesc(finalObject.getString("description"));
                        trashObj.setDistance(finalObject.getInt("distance"));
                        trashObj.setsize(finalObject.getInt("size"));
                        Trashlist.add(trashObj);
                    }
                }
                return Trashlist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trash> Result) {
            super.onPostExecute(Result);

            //Log.d("IDOBJECT",Result.get(0).getDesc());

            Trash[] trashArray = Result.toArray(new Trash[0]);
            PicklejarAdapter myAdapter=new PicklejarAdapter(Picklejar.this, R.layout.rowpicklejar, trashArray);
            ListView myList = (ListView)
                    findViewById(R.id.ListPickleJar);
            myList.setAdapter(myAdapter);

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